<template>
  <aside class="sidebar">
    <div class="logo">GamePulse</div>
    <nav>
      <a href="#" class="active">Surveillance</a>
      <a href="#">Instances</a>
      <a href="#">Réglages</a>
    </nav>
    <div class="sidebar-bottom">
      <button @click="$emit('logout')" class="btn-logout">Quitter</button>
    </div>
  </aside>
</template>

<script setup>
defineEmits(['logout']);
</script>

<style scoped>
.sidebar {
  width: 260px;
  background: var(--bg-sidebar);
  border-right: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  padding: 2rem 1.5rem;
  flex-shrink: 0;
  transition: var(--transition);
}

.logo {
  font-size: 1.5rem;
  font-weight: 900;
  color: var(--accent-primary);
  margin-bottom: 3rem;
  letter-spacing: -0.5px;
}

nav { display: flex; flex-direction: column; gap: 0.5rem; flex-grow: 1; }

nav a {
  padding: 0.75rem 1rem;
  border-radius: var(--radius-md);
  color: var(--text-secondary);
  text-decoration: none;
  font-weight: 600;
  transition: var(--transition);
}

nav a:hover, nav a.active { background: rgba(255, 255, 255, 0.05); color: var(--text-primary); }

.sidebar-bottom {
  margin-top: auto;
  padding-top: 2rem;
}

.btn-logout {
  width: 100%;
  padding: 0.75rem;
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-muted);
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--transition);
}

.btn-logout:hover {
  background: rgba(239, 68, 68, 0.1);
  color: var(--accent-danger);
  border-color: var(--accent-danger);
}

@media (max-width: 1100px) {
  .sidebar { width: 100%; height: auto; flex-direction: row; justify-content: space-between; padding: 1rem 2rem; border-right: none; border-bottom: 1px solid var(--border-light); }
  .logo { margin-bottom: 0; }
  nav { display: none; }
  .sidebar-bottom { margin-top: 0; padding-top: 0; }
  .btn-logout { width: auto; padding: 0.5rem 1rem; }
}
</style>
