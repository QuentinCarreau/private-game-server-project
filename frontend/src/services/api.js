import axios from 'axios';

const api = axios.create({
  baseURL: '/api'
});

// For HTTP Basic Auth, we'll set the header if credentials are present
export const setAuthHeader = (username, password) => {
  if (username && password) {
    const token = btoa(`${username}:${password}`);
    api.defaults.headers.common['Authorization'] = `Basic ${token}`;
    localStorage.setItem('user', JSON.stringify({ username, token }));
  } else {
    delete api.defaults.headers.common['Authorization'];
    localStorage.removeItem('user');
  }
};

// Initialize from localStorage
const storedUser = localStorage.getItem('user');
if (storedUser) {
  const { token } = JSON.parse(storedUser);
  api.defaults.headers.common['Authorization'] = `Basic ${token}`;
}

export default api;
