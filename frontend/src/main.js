import { renderLoginPage } from './pages/login.js'
import { renderRegisterPage } from './pages/register.js'
import { renderCustomerDashboard } from './pages/customer-dashboard.js'
import { renderAdminDashboard } from './pages/admin-dashboard.js'
import { renderDeliveryAgentDashboard } from './pages/delivery-agent-dashboard.js'

const app = document.getElementById('app')

class SmartCourierApp {
  constructor() {
    this.currentPage = null
    this.token = localStorage.getItem('token')
    this.userRole = localStorage.getItem('userRole')
    this.userId = localStorage.getItem('userId')
  }

  init() {
    if (this.token && this.userRole) {
      this.renderApp()
    } else {
      this.showLogin()
    }
  }

  renderApp() {
    app.innerHTML = `
      <nav class="navbar">
        <div class="navbar-brand">Smart Courier</div>
        <ul class="nav-links">
          <li><a href="#" class="nav-link" data-page="dashboard">Dashboard</a></li>
          <li><a href="#" class="nav-link" data-page="track">Track Package</a></li>
          <li><a href="#" class="nav-link" data-page="profile">Profile</a></li>
          <li><a href="#" class="nav-link" data-logout>Logout</a></li>
        </ul>
      </nav>
      <div id="pages-container"></div>
    `

    // Setup navigation
    document.querySelectorAll('[data-page]').forEach(link => {
      link.addEventListener('click', (e) => {
        e.preventDefault()
        const page = e.target.dataset.page
        this.navigateTo(page)
      })
    })

    document.querySelector('[data-logout]').addEventListener('click', (e) => {
      e.preventDefault()
      this.logout()
    })

    // Show initial page
    this.navigateTo('dashboard')
  }

  navigateTo(page) {
    const container = document.getElementById('pages-container')
    const token = localStorage.getItem('token')
    const userId = localStorage.getItem('userId')

    // Update active nav
    document.querySelectorAll('[data-page]').forEach(link => {
      link.classList.remove('active')
    })
    document.querySelector(`[data-page="${page}"]`)?.classList.add('active')

    switch (page) {
      case 'dashboard':
        if (this.userRole === 'ADMIN') {
          renderAdminDashboard(container, token, userId)
        } else if (this.userRole === 'DELIVERY_AGENT') {
          renderDeliveryAgentDashboard(container, token, userId)
        } else {
          renderCustomerDashboard(container, token, userId)
        }
        break
      case 'track':
        this.renderTrackingPage(container)
        break
      case 'profile':
        this.renderProfilePage(container)
        break
    }
  }

  renderTrackingPage(container) {
    container.innerHTML = `
      <div class="container">
        <div class="form-section">
          <h2 class="form-title">Track Your Package</h2>
          <div class="form-group">
            <label>Tracking Number</label>
            <input type="text" id="trackingInput" placeholder="Enter tracking number (e.g., TRK1234567890)" />
          </div>
          <button class="btn btn-primary" id="trackBtn">Track Package</button>
          <div id="trackingResult"></div>
        </div>
      </div>
    `

    document.getElementById('trackBtn').addEventListener('click', () => {
      const tracking = document.getElementById('trackingInput').value
      if (!tracking) {
        alert('Please enter a tracking number')
        return
      }
      this.trackPackage(tracking)
    })
  }

  async trackPackage(trackingNumber) {
    try {
      const response = await fetch(
        `http://localhost:8080/api/tracking/${trackingNumber}`,
        {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
          }
        }
      )
      const data = await response.json()
      const resultDiv = document.getElementById('trackingResult')
      
      if (response.ok) {
        resultDiv.innerHTML = `
          <div class="alert alert-success" style="margin-top: 1rem;">
            <div class="tracking-card">
              <div class="tracking-number">TRK: ${trackingNumber}</div>
              <div class="tracking-status">
                <div class="status-indicator"></div>
                <div>Status: <strong>${data.status || 'Unknown'}</strong></div>
              </div>
              <p><strong>Location:</strong> ${data.location || 'In transit'}</p>
              <p><strong>Last Update:</strong> ${new Date().toLocaleString()}</p>
            </div>
          </div>
        `
      } else {
        resultDiv.innerHTML = `
          <div class="alert alert-error" style="margin-top: 1rem;">
            Tracking number not found or invalid
          </div>
        `
      }
    } catch (error) {
      document.getElementById('trackingResult').innerHTML = `
        <div class="alert alert-error" style="margin-top: 1rem;">
          Error: ${error.message}
        </div>
      `
    }
  }

  renderProfilePage(container) {
    container.innerHTML = `
      <div class="container">
        <div class="form-section">
          <h2 class="form-title">Profile</h2>
          <p><strong>User ID:</strong> ${localStorage.getItem('userId')}</p>
          <p><strong>Role:</strong> ${localStorage.getItem('userRole')}</p>
          <p><strong>Username:</strong> ${localStorage.getItem('username')}</p>
        </div>
      </div>
    `
  }

  showLogin() {
    renderLoginPage(app, (token, userId, role) => {
      localStorage.setItem('token', token)
      localStorage.setItem('userId', userId)
      localStorage.setItem('userRole', role)
      this.token = token
      this.userId = userId
      this.userRole = role
      this.renderApp()
    }, () => this.showRegister())
  }

  showRegister() {
    renderRegisterPage(app, () => this.showLogin())
  }

  logout() {
    localStorage.removeItem('token')
    localStorage.removeItem('userId')
    localStorage.removeItem('userRole')
    localStorage.removeItem('username')
    this.token = null
    this.userId = null
    this.userRole = null
    this.showLogin()
  }
}

// Initialize app
const app_instance = new SmartCourierApp()
app_instance.init()
