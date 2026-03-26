<template>
  <header class="main-header">
    <div class="header-titles">
      <h1>Surveillance du parc</h1>
      <p>Dernière mise à jour : à l'instant</p>
    </div>
    
    <div class="header-actions">
      <ResourceMonitor />
      <button v-if="isAdmin" @click="router.push('/createGameServer')" class="btn-primary">
        + Déployer un serveur
      </button>
    </div>
  </header>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ResourceMonitor from './ResourceMonitor.vue';

const router = useRouter();
const isAdmin = ref(false);

onMounted(() => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    const user = JSON.parse(userStr);
    isAdmin.value = user.role === 'ADMIN';
  }
});
</script>

<style scoped>
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 4rem;
  flex-wrap: wrap;
  gap: 2rem;
}

h1 { margin: 0; font-size: 2.25rem; font-weight: 800; letter-spacing: -1px; }

.header-titles p { color: var(--text-muted); margin-top: 0.5rem; }

.header-actions { display: flex; align-items: center; gap: 2rem; }

.btn-primary {
  background: var(--accent-primary);
  color: #000;
  border: none;
  padding: 0.8rem 1.75rem;
  border-radius: var(--radius-md);
  font-weight: 800;
  cursor: pointer;
  transition: var(--transition);
}

.btn-primary:hover { filter: brightness(1.1); transform: translateY(-1px); }

@media (max-width: 1100px) {
  .main-header { flex-direction: column; align-items: flex-start; gap: 2rem; margin-bottom: 2rem; }
  .header-actions { width: 100%; justify-content: space-between; gap: 1rem; }
}
</style>
