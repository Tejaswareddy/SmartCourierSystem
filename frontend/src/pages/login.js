export function renderLoginPage(container, onLoginSuccess, onRegisterClick) {
  container.innerHTML = `
    <div class="auth-container">
      <h1 style="text-align: center; margin-bottom: 2rem; color: #667eea;">Smart Courier</h1>
      <form class="auth-form" id="loginForm">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" name="username" required />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" required />
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
        <div id="error" class="alert alert-error" style="display: none;"></div>
      </form>
      <div class="auth-link">
        Don't have an account? <a href="#" id="registerLink">Register here</a>
      </div>
    </div>
  `

  const form = document.getElementById('loginForm')
  const registerLink = document.getElementById('registerLink')
  const errorDiv = document.getElementById('error')

  form.addEventListener('submit', async (e) => {
    e.preventDefault()
    const username = document.getElementById('username').value
    const password = document.getElementById('password').value

    try {
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username, password })
      })

      if (response.ok) {
        const token = await response.text()
        // For demo, assume user ID is 1 and role is USER
        // In production, you'd want to fetch this from a user endpoint
        localStorage.setItem('username', username)
        onLoginSuccess(token, '1', 'USER')
      } else {
        errorDiv.style.display = 'block'
        errorDiv.textContent = 'Invalid credentials'
      }
    } catch (error) {
      errorDiv.style.display = 'block'
      errorDiv.textContent = 'Login failed: ' + error.message
    }
  })

  registerLink.addEventListener('click', (e) => {
    e.preventDefault()
    onRegisterClick()
  })
}
