import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import { authService } from '../services/auth.service';

const routes = [
  { path: '/login', component: Login },
  { 
    path: '/', 
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
