import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import { authService } from '../services/auth.service';

const routes = [
  { path: '/login', component: Login },
  // redirect the root path to /Dashboard
  { path: '/', redirect: '/Dashboard' },
  {
    path: '/Dashboard',
    component: Dashboard,
    beforeEnter: (to, from, next) => {
      if (!authService.isAuthenticated()) {
        next('/login');
      } else {
        next();
      }
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
