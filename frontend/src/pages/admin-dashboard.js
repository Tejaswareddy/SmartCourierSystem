export function renderAdminDashboard(container, token, userId) {
  container.innerHTML = `
    <div class="container">
      <!-- Dashboard Stats -->
      <div class="dashboard-grid" id="statsContainer">
        <div class="stats-card">
          <h3>Total Deliveries</h3>
          <div class="value" id="totalDeliveries">-</div>
        </div>
        <div class="stats-card">
          <h3>Pending</h3>
          <div class="value" id="pendingCount">-</div>
        </div>
        <div class="stats-card">
          <h3>In Transit</h3>
          <div class="value" id="inTransitCount">-</div>
        </div>
        <div class="stats-card">
          <h3>Delivered</h3>
          <div class="value" id="deliveredCount">-</div>
        </div>
      </div>

      <!-- All Deliveries Table -->
      <div class="form-section">
        <h2 class="form-title">All Deliveries</h2>
        <div id="deliveriesContainer" class="loading">
          <div class="spinner"></div>
        </div>
      </div>
    </div>
  `

  loadDashboard()

  async function loadDashboard() {
    try {
      // Load admin dashboard stats
      const dashResponse = await fetch('http://localhost:8080/api/admin/dashboard', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (dashResponse.ok) {
        const dashData = await dashResponse.json()
        document.getElementById('totalDeliveries').textContent = dashData.totalDeliveries || 0
        document.getElementById('pendingCount').textContent = dashData.pending || 0
        document.getElementById('inTransitCount').textContent = dashData.inTransit || 0
        document.getElementById('deliveredCount').textContent = dashData.delivered || 0
      }

      // Load all deliveries
      const delResponse = await fetch('http://localhost:8080/api/admin/deliveries', {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      })

      if (delResponse.ok) {
        const deliveries = await delResponse.json()
        const container = document.getElementById('deliveriesContainer')

        if (Array.isArray(deliveries) && deliveries.length > 0) {
          container.innerHTML = `
            <div class="table-container">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>User ID</th>
                    <th>Service Type</th>
                    <th>Status</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Amount</th>
                    <th>Weight (kg)</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  ${deliveries.map(d => `
                    <tr>
                      <td>${d.id}</td>
                      <td>${d.userId}</td>
                      <td>${d.serviceType}</td>
                      <td><span class="badge ${getStatusBadgeClass(d.status)}">${d.status}</span></td>
                      <td>${d.addresses?.find(a => a.type === 'SENDER')?.city || 'N/A'}</td>
                      <td>${d.addresses?.find(a => a.type === 'RECEIVER')?.city || 'N/A'}</td>
                      <td>$${d.totalAmount?.toFixed(2) || '0.00'}</td>
                      <td>${d.parcel?.weight || 'N/A'}</td>
                      <td>
                        <button class="btn btn-secondary" style="padding: 0.5rem; font-size: 0.875rem;" onclick="alert('Edit delivery: ${d.id}')">Edit</button>
                      </td>
                    </tr>
                  `).join('')}
                </tbody>
              </table>
            </div>
          `
        } else {
          container.innerHTML = '<div class="empty-state"><h3>No deliveries</h3><p>No delivery records found</p></div>'
        }
      }
    } catch (error) {
      document.getElementById('deliveriesContainer').innerHTML = '<div class="alert alert-error">Failed to load deliveries: ' + error.message + '</div>'
    }
  }
}

function getStatusBadgeClass(status) {
  const statusMap = {
    'PENDING': 'badge-warning',
    'IN_TRANSIT': 'badge-info',
    'DELIVERED': 'badge-success',
    'FAILED': 'badge-danger'
  }
  return statusMap[status] || 'badge-info'
}
