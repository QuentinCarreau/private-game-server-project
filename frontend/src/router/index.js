import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import Dashboard from '../views/Dashboard.vue';
import CreateGameServer from '../views/CreateGameServer.vue';
import { authService } from '../services/auth.service';

/**
 * Routes de l'application
 */
const routes = [
  {
    path: '/login',
    component: Login,
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    redirect: '/Dashboard'
  },
  {
    path: '/Dashboard',
    component: Dashboard,
    meta: { requiresAuth: true }
  },
  {
    path: '/createGameServer',
    component: CreateGameServer,
    meta: { requiresAuth: true }
  },
  // Catch-all route for undefined paths
  {
    path: '/:pathMatch(.*)*',
    redirect: '/Dashboard'
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Global Navigation Guard
router.beforeEach((to, from, next) => {
  const isAuthenticated = authService.isAuthenticated();

  // Redirect to login if route requires auth and we are not authenticated
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login');
  }
  // Redirect to dashboard if trying to access login while already authenticated
  else if (to.path === '/login' && isAuthenticated) {
    next('/Dashboard');
  }
  // Allow access
  else {
    next();
  }
});

export default router;
