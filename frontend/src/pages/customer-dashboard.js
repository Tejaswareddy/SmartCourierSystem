export function renderCustomerDashboard(container, token, userId) {
  container.innerHTML = `
    <div class="container">
      <div id="message"></div>
      
      <!-- Send Parcel Form -->
      <div class="form-section">
        <h2 class="form-title">Send a Parcel</h2>
        <form id="sendParcelForm">
          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
            <div class="form-group">
              <label>Service Type</label>
              <select id="serviceType" required>
                <option value="">Select service</option>
                <option value="standard">Standard (5-7 days)</option>
                <option value="express">Express (2-3 days)</option>
                <option value="overnight">Overnight</option>
              </select>
            </div>
            <div class="form-group">
              <label>Total Amount</label>
              <input type="number" id="totalAmount" placeholder="0.00" step="0.01" required />
            </div>
          </div>

          <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 1rem;">
            <div class="form-group">
              <label>Weight (kg)</label>
              <input type="number" id="weight" placeholder="0.00" step="0.01" required />
            </div>
            <div class="form-group">
              <label>Dimensions (LxWxH)</label>
              <input type="text" id="dimensions" placeholder="10x10x10" />
            </div>
          </div>

          <div class="form-group">
            <label>
              <input type="checkbox" id="fragile" />
              Fragile Package
            </label>
          </div>

          <div class="form-group">
            <label>Sender Name</label>
            <input type="text" id="senderName" required />
          </div>

          <div class="form-group">
            <label>Sender City</label>
            <input type="text" id="senderCity" required />
          </div>

          <div class="form-group">
            <label>Receiver Name</label>
            <input type="text" id="receiverName" required />
          </div>

          <div class="form-group">
            <label>Receiver City</label>
            <input type="text" id="receiverCity" required />
          </div>

          <button type="submit" class="btn btn-primary">Send Parcel</button>
        </form>
      </div>

      <!-- My Deliveries -->
      <div class="form-section">
        <h2 class="form-title">My Deliveries</h2>
        <div id="deliveriesContainer" class="loading">
          <div class="spinner"></div>
        </div>
      </div>
    </div>
  `

  // Handle form submission
  document.getElementById('sendParcelForm').addEventListener('submit', async (e) => {
    e.preventDefault()

    const delivery = {
      userId: parseInt(userId),
      serviceType: document.getElementById('serviceType').value,
      status: 'PENDING',
      totalAmount: parseFloat(document.getElementById('totalAmount').value),
      parcel: {
        weight: parseFloat(document.getElementById('weight').value),
        dimensions: document.getElementById('dimensions').value,
        fragile: document.getElementById('fragile').checked
      },
      addresses: [
        {
          type: 'SENDER',
          name: document.getElementById('senderName').value,
          city: document.getElementById('senderCity').value
        },
        {
          type: 'RECEIVER',
          name: document.getElementById('receiverName').value,
          city: document.getElementById('receiverCity').value
        }
      ]
    }

    try {
      const response = await fetch('http://localhost:8090/api/deliveries', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`,
          'X-User-Id': userId
        },
        body: JSON.stringify(delivery)
      })

      if (response.ok) {
        const messageDiv = document.getElementById('message')
        messageDiv.innerHTML = '<div class="alert alert-success">Parcel sent successfully!</div>'
        document.getElementById('sendParcelForm').reset()
        loadDeliveries()
        setTimeout(() => messageDiv.innerHTML = '', 5000)
      } else {
        alert('Failed to send parcel')
      }
    } catch (error) {
      alert('Error: ' + error.message)
    }
  })

  // Load deliveries
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

        if (Array.isArray(deliveries) && deliveries.length > 0) {
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
                    <th>Amount</th>
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
                      <td>$${d.totalAmount?.toFixed(2) || '0.00'}</td>
                    </tr>
                  `).join('')}
                </tbody>
              </table>
            </div>
          `
        } else {
          container.innerHTML = '<div class="empty-state"><h3>No deliveries yet</h3><p>Send your first parcel to get started</p></div>'
        }
      }
    } catch (error) {
      document.getElementById('deliveriesContainer').innerHTML = '<div class="alert alert-error">Failed to load deliveries</div>'
    }
  }

  loadDeliveries()
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
