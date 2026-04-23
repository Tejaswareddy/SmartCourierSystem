import axios from 'axios'

const API_BASE_URL = 'http://localhost:8080'

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json'
  }
})

// Add token to requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  const userId = localStorage.getItem('userId')
  
  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }
  if (userId) {
    config.headers['X-User-Id'] = userId
  }
  
  return config
})

// Handle responses
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('token')
      localStorage.removeItem('userId')
      localStorage.removeItem('userRole')
      window.location.reload()
    }
    return Promise.reject(error)
  }
)

// Auth API
export const authAPI = {
  register: (username, email, password) =>
    api.post('/api/auth/register', {
      username,
      email,
      password,
      roles: [{ name: 'USER' }]
    }),
  
  login: (username, password) =>
    api.post('/api/auth/login', { username, password })
}

// Delivery API
export const deliveryAPI = {
  createDelivery: (data) =>
    api.post('/api/deliveries', data),
  
  getAllDeliveries: () =>
    api.get('/api/deliveries'),
  
  getMyDeliveries: () =>
    api.get('/api/deliveries/my'),
  
  getDeliveryById: (id) =>
    api.get(`/api/deliveries/${id}`)
}

// Tracking API
export const trackingAPI = {
  createTracking: (data) =>
    api.post('/api/tracking', data),
  
  getTracking: (trackingNumber) =>
    api.get(`/api/tracking/${trackingNumber}`)
}

// Admin API
export const adminAPI = {
  getDashboard: () =>
    api.get('/api/admin/dashboard'),
  
  getAllDeliveries: () =>
    api.get('/api/admin/deliveries')
}
