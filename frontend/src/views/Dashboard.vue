<template>
  <div class="dashboard-container">
    <header class="glass">
      <div class="logo">GameLauncher</div>
      <button @click="handleLogout" class="btn-secondary">Déconnexion</button>
    </header>

    <main>
      <section class="server-grid">
        <ServerCard 
          v-for="server in servers" 
          :key="server.id" 
          :server="server" 
          @launch="openLaunchModal(server)"
        />
      </section>
    </main>

    <div v-if="showModal" class="modal-overlay">
      <div class="modal glass">
        <h2>Lancer un serveur</h2>
        <p>Simulation pour : {{ selectedServer.name }}</p>
        
        <div class="input-group">
          <label>Choisir le jeu</label>
          <select v-model="launchData.gameId">
            <option v-for="game in games" :key="game.id" :value="game.id">
              {{ game.name }}
            </option>
          </select>
        </div>

        <div class="input-group">
          <label>Nombre de joueurs max</label>
          <input type="number" v-model="launchData.maxPlayers" min="1" max="100" />
        </div>

        <div class="modal-actions">
          <button @click="showModal = false" class="btn-outline">Annuler</button>
          <button @click="confirmLaunch" class="btn-primary" :disabled="loading">
            Lancer la simulation
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/auth.service';
import { serverService } from '../services/server.service';
import ServerCard from '../components/ServerCard.vue';
import axios from 'axios';

const servers = ref([]);
const games = ref([]);
const showModal = ref(false);
const selectedServer = ref(null);
const loading = ref(false);
const router = useRouter();

const launchData = ref({
  gameId: null,
  maxPlayers: 10
});

const loadData = async () => {
  try {
    servers.value = await serverService.getAllServers();
    const response = await axios.get('/api/games'); // Assuming I'll add this endpoint
    games.value = response.data;
  } catch (error) {
    console.error('Erreur de chargement', error);
  }
};

onMounted(loadData);

const openLaunchModal = (server) => {
  selectedServer.value = server;
  showModal.value = true;
};

const confirmLaunch = async () => {
  loading.value = true;
  try {
    await serverService.launchServer(
      selectedServer.value.id, 
      launchData.value.gameId, 
      launchData.value.maxPlayers
    );
    showModal.value = false;
    await loadData();
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
.dashboard-container {
  min-height: 100vh;
  background: #0f172a;
  color: #fff;
  padding: 2rem;
}

header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 2rem;
  margin-bottom: 2rem;
}

.logo {
  font-size: 1.5rem;
  font-weight: 800;
  color: #4ecca3;
}

.server-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 1.5rem;
}

.glass {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(12px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 1rem;
}

.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal {
  padding: 2rem;
  width: 100%;
  max-width: 500px;
}

.input-group {
  margin-bottom: 1.5rem;
}

select, input {
  width: 100%;
  padding: 0.8rem;
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 0.5rem;
  color: #fff;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
}

.btn-primary {
  padding: 0.8rem 1.5rem;
  background: #4ecca3;
  border: none;
  border-radius: 0.5rem;
  color: #0f172a;
  font-weight: 700;
  cursor: pointer;
}

.btn-secondary {
  padding: 0.5rem 1rem;
  background: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 0.5rem;
  color: #fff;
  cursor: pointer;
}

.btn-outline {
  padding: 0.8rem 1.5rem;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: #fff;
  border-radius: 0.5rem;
  cursor: pointer;
}
</style>
