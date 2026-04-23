export function renderRegisterPage(container, onRegisterSuccess) {
  container.innerHTML = `
    <div class="auth-container">
      <h1 style="text-align: center; margin-bottom: 2rem; color: #667eea;">Create Account</h1>
      <form class="auth-form" id="registerForm">
        <div class="form-group">
          <label for="username">Username</label>
          <input type="text" id="username" name="username" required />
        </div>
        <div class="form-group">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" required />
        </div>
        <div class="form-group">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" required />
        </div>
        <div class="form-group">
          <label for="role">User Type</label>
          <select id="role" name="role" required>
            <option value="">Select your role</option>
            <option value="USER">Customer</option>
            <option value="DELIVERY_AGENT">Delivery Agent</option>
          </select>
        </div>
        <button type="submit" class="btn btn-primary">Register</button>
        <div id="error" class="alert alert-error" style="display: none;"></div>
        <div id="success" class="alert alert-success" style="display: none;"></div>
      </form>
      <div class="auth-link">
        Already have an account? <a href="#" id="loginLink">Login here</a>
      </div>
    </div>
  `

  const form = document.getElementById('registerForm')
  const loginLink = document.getElementById('loginLink')
  const errorDiv = document.getElementById('error')
  const successDiv = document.getElementById('success')

  form.addEventListener('submit', async (e) => {
    e.preventDefault()
    const username = document.getElementById('username').value
    const email = document.getElementById('email').value
    const password = document.getElementById('password').value
    const role = document.getElementById('role').value

    try {
      const response = await fetch('http://localhost:8090/api/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          username,
          email,
          password,
          roles: [{ name: role }]
        })
      })

      if (response.ok) {
        successDiv.style.display = 'block'
        successDiv.textContent = 'Registration successful! Redirecting to login...'
        setTimeout(() => {
          onRegisterSuccess()
        }, 2000)
      } else {
        errorDiv.style.display = 'block'
        errorDiv.textContent = 'Registration failed. Try again.'
      }
    } catch (error) {
      errorDiv.style.display = 'block'
      errorDiv.textContent = 'Registration error: ' + error.message
    }
  })

  loginLink.addEventListener('click', (e) => {
    e.preventDefault()
    onRegisterSuccess()
  })
}
