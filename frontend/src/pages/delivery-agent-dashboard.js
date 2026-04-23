export function renderDeliveryAgentDashboard(container, token, userId) {
  container.innerHTML = `
    <div class="container">
      <!-- Agent Stats -->
      <div class="dashboard-grid">
        <div class="stats-card">
          <h3>Today's Deliveries</h3>
          <div class="value" id="todayCount">0</div>
        </div>
        <div class="stats-card">
          <h3>Completed</h3>
          <div class="value" id="completedCount">0</div>
        </div>
        <div class="stats-card">
          <h3>In Progress</h3>
          <div class="value" id="inProgressCount">0</div>
        </div>
        <div class="stats-card">
          <h3>Pending</h3>
          <div class="value" id="pendingCount">0</div>
        </div>
      </div>

      <!-- Assigned Deliveries -->
      <div class="form-section">
        <h2 class="form-title">My Assigned Deliveries</h2>
        <div id="deliveriesContainer" class="loading">
          <div class="spinner"></div>
        </div>
      </div>

      <!-- Update Delivery Status -->
      <div class="form-section">
        <h2 class="form-title">Update Delivery Status</h2>
        <div class="form-group">
          <label>Select Delivery</label>
          <select id="deliverySelect">
            <option value="">Loading...</option>
          </select>
        </div>
        <div class="form-group">
          <label>New Status</label>
          <select id="statusSelect">
            <option value="">Select status</option>
            <option value="PENDING">Pending</option>
            <option value="IN_TRANSIT">In Transit</option>
            <option value="DELIVERED">Delivered</option>
            <option value="FAILED">Failed</option>
          </select>
        </div>
        <div class="form-group">
          <label>Current Location</label>
          <input type="text" id="location" placeholder="Enter current location" />
        </div>
        <button class="btn btn-primary" id="updateBtn">Update Status</button>
        <div id="updateMessage"></div>
      </div>
    </div>
  `

  loadDeliveries()

  async function loadDeliveries() {
    try {
      const response = await fetch('http://localhost:8090/api/deliveries/my', {
        headers: {
          'Authorization': `Bearer ${token}`,
          'X-User-Id': userId
        }
      })

      if (response.ok) {
        const deliveries = await response.json()
        const container = document.getElementById('deliveriesContainer')
        const select = document.getElementById('deliverySelect')

        if (Array.isArray(deliveries) && deliveries.length > 0) {
          // Update stats
          const completed = deliveries.filter(d => d.status === 'DELIVERED').length
          const inProgress = deliveries.filter(d => d.status === 'IN_TRANSIT').length
          const pending = deliveries.filter(d => d.status === 'PENDING').length

          document.getElementById('todayCount').textContent = deliveries.length
          document.getElementById('completedCount').textContent = completed
          document.getElementById('inProgressCount').textContent = inProgress
          document.getElementById('pendingCount').textContent = pending

          // Render table
          container.innerHTML = `
            <div class="table-container">
              <table>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Service Type</th>
                    <th>Status</th>
                    <th>From</th>
                    <th>To</th>
                    <th>Weight</th>
                    <th>Priority</th>
                  </tr>
                </thead>
                <tbody>
                  ${deliveries.map(d => `
                    <tr>
                      <td>${d.id}</td>
                      <td>${d.serviceType}</td>
                      <td><span class="badge ${getStatusBadgeClass(d.status)}">${d.status}</span></td>
                      <td>${d.addresses?.find(a => a.type === 'SENDER')?.city || 'N/A'}</td>
                      <td>${d.addresses?.find(a => a.type === 'RECEIVER')?.city || 'N/A'}</td>
                      <td>${d.parcel?.weight || 'N/A'} kg</td>
                      <td>${d.parcel?.fragile ? '🔴 Fragile' : '✓ Normal'}</td>
                    </tr>
                  `).join('')}
                </tbody>
              </table>
            </div>
          `

          // Populate delivery select
          select.innerHTML = `
            <option value="">Select a delivery</option>
            ${deliveries.map(d => `
              <option value="${d.id}">Delivery #${d.id} - ${d.addresses?.find(a => a.type === 'SENDER')?.city} to ${d.addresses?.find(a => a.type === 'RECEIVER')?.city}</option>
            `).join('')}
          `
        } else {
          container.innerHTML = '<div class="empty-state"><h3>No Deliveries Assigned</h3><p>You have no deliveries assigned for today</p></div>'
          select.innerHTML = '<option value="">No deliveries available</option>'
        }
      }
    } catch (error) {
      document.getElementById('deliveriesContainer').innerHTML = '<div class="alert alert-error">Failed to load deliveries</div>'
    }
  }

  // Handle status update
  document.getElementById('updateBtn').addEventListener('click', async () => {
    const deliveryId = document.getElementById('deliverySelect').value
    const status = document.getElementById('statusSelect').value
    const location = document.getElementById('location').value

    if (!deliveryId || !status) {
      alert('Please select a delivery and new status')
      return
    }

    try {
      const trackingData = {
        trackingNumber: `TRK${deliveryId}${Date.now()}`,
        status: status,
        location: location || 'In transit'
      }

      const response = await fetch('http://localhost:8090/api/tracking', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
          'X-User-Id': userId
        },
        body: JSON.stringify(trackingData)
      })

      if (response.ok) {
        const messageDiv = document.getElementById('updateMessage')
        messageDiv.innerHTML = '<div class="alert alert-success">Delivery status updated successfully!</div>'
        document.getElementById('statusSelect').value = ''
        document.getElementById('location').value = ''
        setTimeout(() => messageDiv.innerHTML = '', 3000)
      } else {
        alert('Failed to update status')
      }
    } catch (error) {
      alert('Error: ' + error.message)
    }
  })
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
