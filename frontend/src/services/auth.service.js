import api, { setAuthHeader } from './api';

export const authService = {
  async login(username, password) {
    // In HTTP Basic, we just try to call an endpoint
    setAuthHeader(username, password);
    try {
      const response = await api.get('/users/me'); // Get user profile and role
      const user = response.data;
      
      // Update localStorage with full user data + token
      const token = btoa(`${username}:${password}`);
      localStorage.setItem('user', JSON.stringify({ ...user, token }));
      
      return user;
    } catch (error) {
      setAuthHeader(null);
      localStorage.removeItem('user');
      throw error;
    }
  },
  logout() {
    setAuthHeader(null);
    localStorage.removeItem('user');
  },
  isAuthenticated() {
    return !!localStorage.getItem('user');
  }
};
