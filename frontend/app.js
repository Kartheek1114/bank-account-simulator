// app.js - Enhanced Logic with Auth Wall, History, and Calculator

document.addEventListener('DOMContentLoaded', () => {
  // DOM Elements
  const nav = document.getElementById('main-nav');
  const navLinks = document.querySelectorAll('.nav-link');
  const sections = document.querySelectorAll('.section');
  const userInfoPanel = document.getElementById('user-info-panel');
  const userNameDisplay = document.getElementById('user-name-display');
  const authForm = document.getElementById('auth-form');
  const logoutBtn = document.getElementById('logout-btn');

  // Navigation Handling
  function showSection(id) {
    sections.forEach(sec => {
      if (sec.id === id) {
        sec.classList.add('active');
      } else {
        sec.classList.remove('active');
      }
    });

    navLinks.forEach(link => {
      if (link.getAttribute('data-section') === id) {
        link.classList.add('active-nav');
      } else {
        link.classList.remove('active-nav');
      }
    });
  }

  navLinks.forEach(link => {
    link.addEventListener('click', e => {
      e.preventDefault();
      showSection(link.getAttribute('data-section'));
    });
  });

  // Data Models
  let account = JSON.parse(localStorage.getItem('neonBankAccount')) || {
      name: null,
      email: null,
      balance: 0,
      history: [],
      accountNumber: null,
      ifsc: null
    };

  const fdBanks = [
    { name: 'HDFC Bank', fdRate: 7.1, loanRate: 9.5 },
    { name: 'State Bank of India', fdRate: 6.8, loanRate: 9.0 },
    { name: 'ICICI Bank', fdRate: 7.0, loanRate: 9.2 },
    { name: 'Axis Bank', fdRate: 7.2, loanRate: 9.3 },
    { name: 'Kotak Mahindra', fdRate: 7.25, loanRate: 9.4 }
  ];

  // Auth Logic
  function checkAuth() {
    console.log('Checking auth, account:', account);
    if (account.name && account.email) {
      // Logged in
      nav.style.display = 'flex';
      userInfoPanel.style.display = 'block';
      userNameDisplay.textContent = account.name;
      // Generate account details if missing
      if (!account.accountNumber) {
        account.accountNumber = 'AC' + Math.floor(1000000000 + Math.random() * 9000000000);
      }
      if (!account.ifsc) {
        account.ifsc = 'IFSC' + Math.floor(1000 + Math.random() * 9000);
      }
      // Update UI fields
      document.getElementById('dash-acno').textContent = account.accountNumber;
      document.getElementById('dash-ifsc').textContent = account.ifsc;
      document.getElementById('dash-name').textContent = account.name;
      document.getElementById('dash-email').textContent = account.email;
      showSection('balance');
      updateBalanceUI();
      renderHistory();
      renderFDBanks(0, 0); // Initial FD render
    } else {
      // Not logged in
      nav.style.display = 'none';
      userInfoPanel.style.display = 'none';
      showSection('auth');
    }
  }

  if (authForm) {
    authForm.addEventListener('submit', e => {
      e.preventDefault();
      const name = document.getElementById('auth-name').value.trim();
      const email = document.getElementById('auth-email').value.trim();
      
      if (name && email) {
        console.log('Auth form submitted, name:', name, 'email:', email);
        // Save and refresh UI
        account.name = name;
        account.email = email;
        account.balance = 0;
        account.history = [];
        // Generate account details
        account.accountNumber = 'AC' + Math.floor(1000000000 + Math.random() * 9000000000);
        account.ifsc = 'IFSC' + Math.floor(1000 + Math.random() * 9000);
        saveAccount();
        console.log('Account object after creation:', account);
        checkAuth();
      }
    });
  }

  if (logoutBtn) {
    logoutBtn.addEventListener('click', () => {
      account.name = null;
      account.email = null;
      account.balance = 0;
      account.history = [];
      saveAccount();
      checkAuth();
      authForm.reset();
    });
  }

  // Core Functions
  function saveAccount() {
    localStorage.setItem('neonBankAccount', JSON.stringify(account));
  }

  function addTransaction(type, amount) {
    const transaction = {
      id: Date.now(),
      type: type, // 'deposit' or 'withdraw'
      amount: amount,
      date: new Date().toLocaleString()
    };
    account.history.unshift(transaction); // Add to beginning
    if(account.history.length > 50) account.history.pop(); // Keep last 50
    saveAccount();
    updateBalanceUI();
    renderHistory();
  }

  function updateBalanceUI() {
    const balanceDisplay = document.getElementById('balance-display');
    if (balanceDisplay) balanceDisplay.textContent = `$${account.balance.toFixed(2)}`;
  }

  function renderHistory() {
    const list = document.getElementById('history-list');
    if (!list) return;
    
    list.innerHTML = '';
    
    if (account.history.length === 0) {
      list.innerHTML = '<li style="color:#64748b; padding:15px; text-align:center;">No recent transactions.</li>';
      return;
    }

    account.history.forEach(tx => {
      const li = document.createElement('li');
      li.className = 'history-item';
      const symbol = tx.type === 'deposit' ? '+' : '-';
      li.innerHTML = `
        <div>
          <div class="history-type" style="text-transform: capitalize;">${tx.type}</div>
          <div class="history-date">${tx.date}</div>
        </div>
        <div class="history-amount ${tx.type}">${symbol}$${tx.amount.toFixed(2)}</div>
      `;
      list.appendChild(li);
    });
  }

  // Deposit Logic
  const depositForm = document.getElementById('deposit-form');
  if (depositForm) {
    depositForm.addEventListener('submit', e => {
      e.preventDefault();
      const input = document.getElementById('deposit-amount');
      const amount = parseFloat(input.value);
      if (amount > 0) {
        account.balance += amount;
        addTransaction('deposit', amount);
        input.value = '';
        showSection('balance');
      }
    });
  }

  // Withdraw Logic
  const withdrawForm = document.getElementById('withdraw-form');
  if (withdrawForm) {
    withdrawForm.addEventListener('submit', e => {
      e.preventDefault();
      const input = document.getElementById('withdraw-amount');
      const amount = parseFloat(input.value);
      
      if (amount > 0) {
        if (amount > account.balance) {
          alert('Insufficient funds!');
          return;
        }
        account.balance -= amount;
        addTransaction('withdraw', amount);
        input.value = '';
        showSection('balance');
      }
    });
  }

  // FD Calculator Logic
  function renderFDBanks(principal, years) {
    const tbody = document.getElementById('fd-tbody');
    if (!tbody) return;
    tbody.innerHTML = '';

    fdBanks.forEach(bank => {
      let maturityStr = '—';
      if (principal > 0 && years > 0) {
        // Simple compound interest formula: A = P(1 + r/100)^t
        const amount = principal * Math.pow((1 + bank.fdRate / 100), years);
        maturityStr = `$${amount.toFixed(2)}`;
      }

        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td><strong>${bank.name}</strong></td>
          <td style="color:var(--primary);">${bank.fdRate.toFixed(2)}%</td>
          <td style="color:#ffb400;">${bank.loanRate.toFixed(2)}%</td>
          <td style="font-weight:600;">${maturityStr}</td>
        `;
        tbody.appendChild(tr);
    });
  }

  // Using click listener on the button instead of form submit to prevent defaults easily
  const fdCalcBtn = document.getElementById('fd-calc-btn');
  if (fdCalcBtn) {
    fdCalcBtn.addEventListener('click', () => {
      const principalInput = document.getElementById('fd-principal');
      const yearsInput = document.getElementById('fd-years');
      
      const principal = parseFloat(principalInput.value);
      const years = parseFloat(yearsInput.value);
      
      if (principal > 0 && years > 0) {
        renderFDBanks(principal, years);
        
        // Show result box with best bank
        const resultBox = document.getElementById('fd-calc-result');
        const bestBank = fdBanks.reduce((prev, current) => (prev.fdRate > current.fdRate) ? prev : current);
        const bestAmount = principal * Math.pow((1 + bestBank.fdRate / 100), years);
        
        resultBox.style.display = 'block';
        resultBox.innerHTML = `<strong style="color:#fff;">Top Pick:</strong> ${bestBank.name} offers the highest rate. Your $${principal} will grow to <strong style="color:#fff;">$${bestAmount.toFixed(2)}</strong> in ${years} years.`;
      } else {
        alert("Please enter a valid Principal and Duration.");
      }
    });
  }

    // Loan Calculator Logic
    function renderLoanBanks(principal, years) {
      const tbody = document.getElementById('loan-tbody');
      if (!tbody) return;
      tbody.innerHTML = '';
      const months = years * 12;
      fdBanks.forEach(bank => {
        const r = bank.loanRate / 12 / 100;
        const emi = principal * r * Math.pow(1 + r, months) / (Math.pow(1 + r, months) - 1);
        const totalInterest = emi * months - principal;
        const tr = document.createElement('tr');
        tr.innerHTML = `
          <td><strong>${bank.name}</strong></td>
          <td style="color:#ffb400;">${bank.loanRate.toFixed(2)}%</td>
          <td style="font-weight:600;">₹${emi.toFixed(2)}</td>
          <td style="color:#f87171;">₹${totalInterest.toFixed(2)}</td>
        `;
        tbody.appendChild(tr);
      });
    }

    const loanCalcBtn = document.getElementById('loan-calc-btn');
    if (loanCalcBtn) {
      loanCalcBtn.addEventListener('click', () => {
        const principalInput = document.getElementById('loan-amount');
        const yearsInput = document.getElementById('loan-years');
        const principal = parseFloat(principalInput.value);
        const years = parseFloat(yearsInput.value);
        if (principal > 0 && years > 0) {
          renderLoanBanks(principal, years);
          const resultBox = document.getElementById('loan-calc-result');
          resultBox.style.display = 'block';
          resultBox.innerHTML = `<strong style="color:#fff;">Loan calculations displayed below.</strong>`;
        } else {
          alert('Please enter valid loan amount and tenure.');
        }
      });
    }
window.switchTo = function(sectionId){ showSection(sectionId); };

});
