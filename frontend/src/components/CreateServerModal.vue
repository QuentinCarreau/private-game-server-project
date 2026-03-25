<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-card">
      <div class="modal-header">
        <h2>Déployer un nouveau serveur</h2>
        <button class="btn-close" @click="$emit('close')">✕</button>
      </div>

      <form @submit.prevent="handleSubmit">
        <div class="form-section">
          <label>Choisissez un jeu</label>
          <div class="game-grid">
            <div 
              v-for="game in games" 
              :key="game.id" 
              class="game-card"
              :class="{ active: formData.gameId === game.id }"
              @click="formData.gameId = game.id"
            >
              <div class="game-name">{{ game.name }}</div>
            </div>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>Joueurs maximum</label>
            <input type="number" v-model="formData.maxPlayers" min="1" max="100" />
          </div>
          <div class="form-group">
            <label>Confidentialité</label>
            <select v-model="formData.isPublic">
              <option :value="true">Serveur Public</option>
              <option :value="false">Serveur Privé</option>
            </select>
          </div>
        </div>

        <div class="modal-footer">
          <button type="button" class="btn-cancel" @click="$emit('close')">Annuler</button>
          <button type="submit" class="btn-confirm" :disabled="loading || !formData.gameId">
            {{ loading ? 'Lancement...' : 'Créer l\'instance' }}
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({ games: Array, loading: Boolean });
const emit = defineEmits(['close', 'launch']);

const formData = ref({
  gameId: null,
  maxPlayers: 10,
  isPublic: false
});

const handleSubmit = () => {
  emit('launch', formData.value);
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.75);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1.5rem;
}

.modal-card {
  background: var(--bg-sidebar);
  width: 100%;
  max-width: 500px;
  border-radius: var(--radius-lg);
  padding: 2.5rem;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.5);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

h2 { margin: 0; font-size: 1.5rem; font-weight: 800; }

.btn-close {
  background: transparent;
  border: none;
  font-size: 1.5rem;
  color: var(--text-muted);
  cursor: pointer;
}

.form-section { margin-bottom: 2rem; }

label {
  display: block;
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--text-secondary);
  text-transform: uppercase;
  margin-bottom: 0.75rem;
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 0.75rem;
}

.game-card {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid var(--border-light);
  padding: 1rem;
  border-radius: var(--radius-md);
  text-align: center;
  cursor: pointer;
  transition: var(--transition);
}

.game-card:hover { background: rgba(255, 255, 255, 0.06); }
.game-card.active { border-color: var(--accent-primary); background: rgba(16, 185, 129, 0.1); }

.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; margin-bottom: 2rem; }

input, select {
  width: 100%;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid var(--border-light);
  padding: 0.75rem 1rem;
  border-radius: var(--radius-md);
  color: #fff;
  transition: var(--transition);
}

input:focus, select:focus { outline: none; border-color: var(--accent-secondary); }

.modal-footer { display: flex; gap: 1rem; }

.btn-confirm {
  flex-grow: 1;
  background: var(--accent-primary);
  color: #000;
  border: none;
  padding: 1rem;
  border-radius: var(--radius-md);
  font-weight: 800;
  cursor: pointer;
}

.btn-cancel {
  padding: 1rem 1.5rem;
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-primary);
  border-radius: var(--radius-md);
  cursor: pointer;
}
</style>
