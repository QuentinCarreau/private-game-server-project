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
      <div class="info-item" v-if="server.status === 'ONLINE'">
        <label>Ressources</label>
        <span class="value-mini">CPU: {{ server.currentCpuUsage }} / {{ server.game?.cpuUsage }}</span>
        <span class="value-mini">RAM: {{ server.currentRamUsage }} / {{ server.game?.ramUsage }} Go</span>
        <span class="value-mini">Stockage: {{ server.currentDiskUsage }} / {{ server.game?.storageUsage }} Go</span>
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
      <div class="admin-actions" v-if="isAdmin && server.status === 'OFFLINE'">
        <button class="btn-outline" @click="$emit('edit', server)">Paramètres du serveur</button>
        <button class="btn-danger" @click="confirmDelete">Détruire l'instance</button>
      </div>
    </div>

    <div class="footer" v-if="isAdmin">
      <button v-if="server.status === 'OFFLINE'" @click="launchServer" class="btn-start" :disabled="isLaunching">
        {{ isLaunching ? 'Lancement...' : 'Démarrer le serveur' }}
      </button>
      <div v-else-if="server.status === 'ONLINE'" class="footer-online">
        <span class="running-label">⚡ En cours d'exécution</span>
        <button class="btn-stop-action" @click="confirmStop" :disabled="isStopping">
          {{ isStopping ? 'Arrêt...' : 'Arrêter' }}
        </button>
      </div>
      <div v-else class="loading-state">Initialisation...</div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { serverService } from '../services/server.service';

const props = defineProps({ server: Object });
const emit = defineEmits(['edit', 'delete']);

const expanded = ref(false);
const showPass = ref(false);
const isLaunching = ref(false);
const isStopping = ref(false);
const isAdmin = ref(false);

onMounted(() => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    const user = JSON.parse(userStr);
    isAdmin.value = user.role === 'ADMIN';
  }
});

/**
 * Lance un serveur
 * @param {number} serverId - ID du serveur
 */
const launchServer = async () => {
  isLaunching.value = true;
  try {
    const updatedServer = await serverService.launchServer(props.server.id);
    // Met à jour l'objet local pour afficher les nouvelles informations (IP, mdp, statut ONLINE)
    Object.assign(props.server, updatedServer);
    // Développer automatiquement pour montrer l'IP
    expanded.value = true;
  } catch (error) {
    console.error("Erreur lors du lancement:", error);
    alert("Impossible de démarrer le serveur.");
  } finally {
    isLaunching.value = false;
  }
};

/**
 * Confirme l'arrêt d'un serveur
 * @param {number} serverId - ID du serveur
 */
const confirmStop = async () => {
  const confirmed = confirm(
    `⚠️ Arrêter le serveur "${props.server.name}" ?\n\n` +
    `Les joueurs encore connectés seront déconnectés de force.\n` +
    `Des données non sauvegardées pourraient être perdues.\n\n` +
    `Voulez-vous vraiment continuer ?`
  );
  if (!confirmed) return;

  isStopping.value = true;
  try {
    const updatedServer = await serverService.stopServer(props.server.id);
    Object.assign(props.server, updatedServer);
    expanded.value = false;
  } catch (error) {
    console.error('Erreur lors de l\'arrêt:', error);
    alert(
      `Impossible d'arrêter le serveur "${props.server.name}" pour le moment.\n\n` +
      `Si le problème persiste, veuillez contacter le support.`
    );
  } finally {
    isStopping.value = false;
  }
};

/**
 * Copie un texte dans le presse-papiers
 * @param {string} text - Texte à copier
 */
const copy = (text) => {
  navigator.clipboard.writeText(text);
};

/**
 * Confirme la suppression d'un serveur
 * @param {number} serverId - ID du serveur
 */
const confirmDelete = () => {
  if (confirm(`Êtes-vous sûr de vouloir supprimer définitivement le serveur "${props.server.name}" ?`)) {
    emit('delete', props.server);
  }
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
.value-mini { font-size: 0.8rem; font-weight: 500; color: var(--text-secondary); }

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

.admin-actions {
  display: flex;
  gap: 1rem;
  margin-top: 0.5rem;
  padding-top: 1rem;
  border-top: 1px dashed var(--border-light);
}

.btn-outline {
  flex: 1;
  padding: 0.5rem;
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-secondary);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: var(--transition);
}

.btn-outline:hover { background: rgba(255,255,255,0.05); }

.btn-danger {
  flex: 1;
  padding: 0.5rem;
  background: transparent;
  border: 1px dashed var(--accent-danger);
  color: var(--accent-danger);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: var(--transition);
}

.btn-danger:hover { background: rgba(239, 68, 68, 0.1); }

.btn-start:hover { filter: brightness(1.1); }

.footer-online {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 0.5rem 0.75rem;
  background: rgba(16, 185, 129, 0.05);
  border: 1px solid rgba(16, 185, 129, 0.2);
  border-radius: var(--radius-md);
}

.running-label {
  font-size: 0.85rem;
  font-weight: 600;
  color: #10b981;
}

.btn-stop-action {
  padding: 0.4rem 1rem;
  background: transparent;
  border: 1px solid var(--accent-danger);
  color: var(--accent-danger);
  border-radius: var(--radius-sm);
  font-size: 0.8rem;
  font-weight: 700;
  cursor: pointer;
  transition: var(--transition);
}

.btn-stop-action:hover:not(:disabled) { background: rgba(239, 68, 68, 0.1); }
.btn-stop-action:disabled { opacity: 0.5; cursor: not-allowed; }

.loading-state {
  text-align: center;
  font-size: 0.85rem;
  color: var(--text-muted);
}
</style>
