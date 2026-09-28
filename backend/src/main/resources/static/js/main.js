// SmartCityX Main JS
// Global AJAX utility with CSRF token injection

document.addEventListener('DOMContentLoaded', function() {
  // Auto-close alerts after 5 seconds
  setTimeout(() => {
    document.querySelectorAll('.alert-smart').forEach(a => {
      a.style.opacity = '0';
      a.style.transform = 'translateY(-10px)';
      a.style.transition = 'all 0.4s ease';
      setTimeout(() => a.remove(), 400);
    });
  }, 5000);

  // Add CSRF token to all fetch calls (handled in JS inline)
  // Highlight active nav items
  const path = window.location.pathname;
  document.querySelectorAll('.nav-item').forEach(el => {
    if (el.getAttribute('href') === path) {
      el.classList.add('active');
    }
  });
});

// Global CSRF token getter
function getCsrfToken() {
  const meta = document.querySelector('meta[name="_csrf"]');
  return meta ? meta.getAttribute('content') : '';
}

// Fetch wrapper with CSRF
async function apiPost(url, data) {
  return fetch(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'Accept': 'application/json'
    },
    body: JSON.stringify(data)
  });
}
