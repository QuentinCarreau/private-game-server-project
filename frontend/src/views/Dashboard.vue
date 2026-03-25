<template>
  <div class="dashboard-root">
    <AppSidebar @logout="handleLogout" />

    <main class="content-area">
      <DashboardHeader @deploy="showModal = true" />

      <section class="servers-section">
        <div class="section-header">
          <h2>Vos instances actives ({{ servers.length }})</h2>
        </div>

        <!-- État de chargement -->
        <div v-if="loadingData" class="loading-state">
          <div class="spinner"></div>
          <p>Chargement des serveurs...</p>
        </div>

        <!-- Erreur -->
        <div v-else-if="fetchError" class="error-state">
          <p>⚠️ {{ fetchError }}</p>
          <button @click="loadData" class="btn-retry">Réessayer</button>
        </div>

        <!-- Grille de serveurs -->
        <div v-else class="cards-grid">
          <ServerCard 
            v-for="server in servers" 
            :key="server.id" 
            :server="server" 
            @launch="openLaunchModal(server)"
          />
          
          <div v-if="servers.length === 0" class="empty-state" @click="showModal = true">
            <div class="empty-icon">🖥️</div>
            <p>Aucun serveur actif.</p>
            <span>Cliquez pour en déployer un.</span>
          </div>
        </div>
      </section>
    </main>

    <CreateServerModal 
      v-if="showModal" 
      :games="games"
      :loading="loading"
      @close="closeModal"
      @launch="confirmLaunch"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/auth.service';
import { serverService } from '../services/server.service';
import api from '../services/api';

// Components
import AppSidebar from '../components/AppSidebar.vue';
import DashboardHeader from '../components/DashboardHeader.vue';
import ServerCard from '../components/ServerCard.vue';
import CreateServerModal from '../components/CreateServerModal.vue';

const servers = ref([]);
const games = ref([]);
const showModal = ref(false);
const selectedServer = ref(null);
const loading = ref(false);
const loadingData = ref(false);
const fetchError = ref(null);
const router = useRouter();

const loadData = async () => {
  loadingData.value = true;
  fetchError.value = null;
  try {
    servers.value = await serverService.getAllServers();
    const response = await api.get('/games');
    games.value = response.data;
  } catch (error) {
    console.error("Erreur de chargement des données:", error);
    if (error.response?.status === 401 || error.response?.status === 403) {
      // Session expirée, rediriger vers le login
      authService.logout();
      router.push('/login');
    } else {
      fetchError.value = "Impossible de contacter le serveur. Vérifiez que le backend est démarré.";
    }
  } finally {
    loadingData.value = false;
  }
};

onMounted(loadData);

const openLaunchModal = (server) => {
  selectedServer.value = server;
  showModal.value = true;
};

const closeModal = () => {
  showModal.value = false;
  selectedServer.value = null;
};

const confirmLaunch = async (formData) => {
  loading.value = true;
  try {
    const id = selectedServer.value?.id || (servers.value[0]?.id);
    await serverService.launchServer(id, formData.gameId, formData.maxPlayers);
    closeModal();
    loadData();
  } catch (err) {
    alert("Erreur lors du lancement");
  } finally {
    loading.value = false;
  }
};

const handleLogout = () => {
  authService.logout();
  router.push('/login');
};
</script>

<style scoped>
.dashboard-root {
  display: flex;
  min-height: 100vh;
}

.content-area {
  flex-grow: 1;
  padding: 3rem 4rem;
  background: var(--bg-main);
}

.servers-section { display: flex; flex-direction: column; gap: 2rem; }

h2 { margin: 0; font-size: 1.25rem; color: var(--text-secondary); }

.cards-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(340px, 1fr));
  gap: 2rem;
}

/* Loading */
.loading-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 250px;
  gap: 1.5rem;
  color: var(--text-muted);
}

.loading-state .spinner {
  width: 36px;
  height: 36px;
  border: 3px solid rgba(255, 255, 255, 0.05);
  border-top-color: var(--accent-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

/* Error */
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
  height: 250px;
  justify-content: center;
  color: var(--accent-danger);
  font-weight: 600;
}

.btn-retry {
  background: transparent;
  border: 1px solid var(--accent-danger);
  color: var(--accent-danger);
  padding: 0.5rem 1.5rem;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--transition);
}

.btn-retry:hover { background: rgba(239, 68, 68, 0.1); }

/* Empty state */
.empty-state {
  grid-column: 1 / -1;
  border: 2px dashed rgba(255, 255, 255, 0.1);
  border-radius: var(--radius-lg);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 250px;
  gap: 0.75rem;
  color: var(--text-secondary);
  background: rgba(255, 255, 255, 0.02);
  cursor: pointer;
  transition: var(--transition);
}

.empty-state:hover {
  background: rgba(255, 255, 255, 0.04);
  border-color: var(--accent-primary);
}

.empty-icon { font-size: 2.5rem; }
.empty-state p { font-size: 1rem; font-weight: 600; margin: 0; }
.empty-state span { font-size: 0.85rem; color: var(--text-muted); }

@media (max-width: 1100px) {
  .dashboard-root { flex-direction: column; }
  .content-area { padding: 2rem 1.5rem; }
}
</style>
