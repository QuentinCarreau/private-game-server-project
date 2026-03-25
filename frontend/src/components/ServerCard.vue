<template>
  <div class="server-card">
    <div class="header">
      <div class="title-group">
        <h3>{{ server.name }}</h3>
        <span class="status-badge" :class="server.status.toLowerCase()">
          {{ server.status }}
        </span>
      </div>
      <button class="btn-more" @click="expanded = !expanded" :title="expanded ? 'Réduire' : 'Détails'">
        {{ expanded ? '−' : '＋' }}
      </button>
    </div>

    <div class="info-row">
      <div class="info-item">
        <label>Jeu</label>
        <span class="value">{{ server.game?.name || 'Libre' }}</span>
      </div>
      <div class="info-item players" v-if="server.status === 'ONLINE'">
        <label>Joueurs</label>
        <span class="value">{{ server.currentPlayers }} / {{ server.maxPlayers }}</span>
      </div>
    </div>

    <div v-if="expanded" class="expanded-details">
      <div class="detail">
        <label>Adresse IP</label>
        <div class="copyable">
          <code>{{ server.ipAddress || 'Indisponible' }}</code>
          <button @click="copy(server.ipAddress)">Copier</button>
        </div>
      </div>
      <div class="detail" v-if="server.password">
        <label>Mot de passe</label>
        <div class="password-field">
          <code>{{ showPass ? server.password : '••••••••' }}</code>
          <button @click="showPass = !showPass">{{ showPass ? 'Masquer' : 'Voir' }}</button>
        </div>
      </div>
    </div>

    <div class="footer">
      <button v-if="server.status === 'OFFLINE'" @click="$emit('launch')" class="btn-start">
        Démarrer le serveur
      </button>
      <button v-else-if="server.status === 'ONLINE'" class="btn-stop" disabled>
        En cours d'exécution
      </button>
      <div v-else class="loading-state">Initialisation...</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({ server: Object });
defineEmits(['launch']);

const expanded = ref(false);
const showPass = ref(false);

const copy = (text) => {
  navigator.clipboard.writeText(text);
};
</script>

<style scoped>
.server-card {
  background: var(--bg-card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: 1.5rem;
  display: flex;
  flex-direction: column;
  gap: 1.25rem;
  transition: var(--transition);
}

.server-card:hover {
  border-color: rgba(255, 255, 255, 0.1);
  background: rgba(30, 41, 59, 0.6);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.title-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

h3 { margin: 0; font-size: 1.25rem; font-weight: 700; }

.status-badge {
  font-size: 0.7rem;
  font-weight: 800;
  padding: 0.2rem 0.6rem;
  border-radius: 2rem;
  text-transform: uppercase;
  width: fit-content;
}

.status-badge.online { background: rgba(16, 185, 129, 0.1); color: #10b981; }
.status-badge.offline { background: rgba(148, 163, 184, 0.1); color: #94a3b8; }
.status-badge.starting { background: rgba(245, 158, 11, 0.1); color: #f59e0b; }

.btn-more {
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-muted);
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
}

.info-row {
  display: flex;
  gap: 2rem;
}

.info-item { display: flex; flex-direction: column; gap: 0.25rem; }

label { font-size: 0.7rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; }

.value { font-size: 0.95rem; font-weight: 600; color: var(--text-primary); }

.expanded-details {
  padding-top: 1rem;
  border-top: 1px solid var(--border-light);
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.detail { display: flex; flex-direction: column; gap: 0.4rem; }

.copyable, .password-field {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: rgba(0, 0, 0, 0.2);
  padding: 0.5rem 0.75rem;
  border-radius: var(--radius-md);
}

code { font-family: monospace; font-size: 0.85rem; color: var(--accent-secondary); }

.copyable button, .password-field button {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 0.75rem;
  cursor: pointer;
  font-weight: 600;
}

.btn-start {
  width: 100%;
  padding: 0.75rem;
  background: var(--accent-primary);
  color: #000;
  border: none;
  border-radius: var(--radius-md);
  font-weight: 800;
  cursor: pointer;
  transition: var(--transition);
}

.btn-start:hover { filter: brightness(1.1); }

.btn-stop {
  width: 100%;
  padding: 0.75rem;
  background: transparent;
  border: 1px solid var(--accent-danger);
  color: var(--accent-danger);
  border-radius: var(--radius-md);
  opacity: 0.6;
}

.loading-state {
  text-align: center;
  font-size: 0.85rem;
  color: var(--text-muted);
}
</style>
