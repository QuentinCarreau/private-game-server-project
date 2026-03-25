import api, { setAuthHeader } from './api';

export const authService = {
  async login(username, password) {
    // In HTTP Basic, we just try to call an endpoint
    setAuthHeader(username, password);
    try {
      await api.get('/users'); // Simple test call
      return true;
    } catch (error) {
      setAuthHeader(null);
      throw error;
    }
  },
  logout() {
    setAuthHeader(null);
  },
  isAuthenticated() {
    return !!localStorage.getItem('user');
  }
};
