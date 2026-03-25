<template>
  <div class="server-card glass" :class="server.status.toLowerCase()">
    <div class="card-header">
      <h3>{{ server.name }}</h3>
      <span class="badge">{{ server.status }}</span>
    </div>
    <div class="card-body">
      <p v-if="server.game">{{ server.game.name }}</p>
      <p v-else class="placeholder">Aucun jeu actif</p>
      
      <div v-if="server.status === 'ONLINE'" class="details">
        <div class="ip-address">
          <span>IP:</span>
          <code>{{ server.ipAddress }}</code>
        </div>
        <div class="players">
          <span>Joueurs:</span>
          <span>{{ server.currentPlayers }} / {{ server.maxPlayers }}</span>
        </div>
      </div>
    </div>
    <div class="card-footer">
      <button v-if="server.status === 'OFFLINE'" @click="$emit('launch')" class="btn-launch">
        Démarrer
      </button>
      <button v-else class="btn-stop" disabled>
        Arrêter (Simulation)
      </button>
    </div>
  </div>
</template>

<script setup>
defineProps({
  server: Object
});
defineEmits(['launch']);
</script>

<style scoped>
.server-card {
  padding: 1.5rem;
  transition: transform 0.3s ease;
}

.server-card:hover {
  transform: translateY(-5px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
}

.badge {
  font-size: 0.7rem;
  padding: 0.2rem 0.6rem;
  border-radius: 1rem;
  background: #334155;
}

.online .badge {
  background: #059669;
}

.offline .badge {
  background: #475569;
}

.ip-address {
  margin-top: 1rem;
  padding: 0.5rem;
  background: rgba(0, 0, 0, 0.2);
  border-radius: 0.5rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

code {
  color: #4ecca3;
}

.btn-launch {
  width: 100%;
  padding: 0.8rem;
  background: #4ecca3;
  border: none;
  border-radius: 0.5rem;
  color: #0f172a;
  font-weight: 700;
  cursor: pointer;
}

.btn-stop {
  width: 100%;
  padding: 0.8rem;
  background: rgba(239, 68, 68, 0.2);
  border: 1px solid rgba(239, 68, 68, 0.5);
  border-radius: 0.5rem;
  color: #ef4444;
}
</style>
