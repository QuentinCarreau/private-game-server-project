<template>
  <div class="modal-overlay" @click.self="$emit('close')">
    <div class="modal-content">
      <div class="modal-header">
        <h2>Modifier le Serveur</h2>
        <button class="btn-close" @click="$emit('close')">×</button>
      </div>

      <div class="modal-body">
        <form @submit.prevent="handleSave" class="config-form">
          <div class="form-group">
            <label for="serverName">Nom du serveur</label>
            <input 
              id="serverName" 
              v-model="formData.serverName" 
              type="text" 
              placeholder="Ex: Mon super serveur" 
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="maxPlayers">Joueurs Max</label>
              <input 
                id="maxPlayers" 
                v-model.number="formData.maxPlayers" 
                type="number" 
                min="1" 
                :max="server.game?.maxPlayers || 100" 
                required
              />
            </div>

            <div class="form-group">
              <label for="privacy">Confidentialité</label>
              <select id="privacy" v-model="formData.privacy">
                <option value="PUBLIC">Public</option>
                <option value="PRIVATE">Privé</option>
              </select>
            </div>
          </div>

          <div class="form-group" v-if="formData.privacy === 'PRIVATE'">
            <label for="password">Nouveau mot de passe (laisser vide pour ne pas changer)</label>
            <input 
              id="password" 
              v-model="formData.password" 
              type="password" 
              placeholder="••••••••" 
            />
          </div>

          <div class="modal-actions">
            <button type="button" class="btn-cancel" @click="$emit('close')" :disabled="loading">
              Annuler
            </button>
            <button type="submit" class="btn-confirm" :disabled="loading">
              {{ loading ? 'Enregistrement...' : 'Enregistrer' }}
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const props = defineProps({
  server: {
    type: Object,
    required: true
  },
  loading: Boolean
});

const emit = defineEmits(['close', 'save']);

const formData = ref({
  serverName: props.server.name || '',
  maxPlayers: props.server.maxPlayers || 10,
  privacy: props.server.privacy || 'PUBLIC',
  password: '' // empty by default
});

const handleSave = () => {
  emit('save', formData.value);
};
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(15, 23, 42, 0.8);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.modal-content {
  background: var(--bg-card);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  width: 90%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1.5rem;
  border-bottom: 1px solid var(--border-light);
}

.modal-header h2 {
  margin: 0;
  font-size: 1.25rem;
  color: var(--text-primary);
}

.btn-close {
  background: transparent;
  border: none;
  color: var(--text-muted);
  font-size: 1.5rem;
  cursor: pointer;
}

.btn-close:hover { color: white; }

.modal-body {
  padding: 1.5rem;
}

.config-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  font-size: 0.85rem;
  font-weight: 600;
  color: var(--text-secondary);
}

input, select {
  padding: 0.75rem;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  color: white;
  font-size: 0.95rem;
}

input:focus, select:focus {
  outline: none;
  border-color: var(--accent-primary);
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

button {
  padding: 0.75rem 1.5rem;
  border-radius: var(--radius-md);
  font-weight: 600;
  cursor: pointer;
  transition: var(--transition);
}

.btn-cancel {
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-secondary);
}

.btn-cancel:hover { background: rgba(255, 255, 255, 0.05); }

.btn-confirm {
  background: var(--accent-primary);
  border: none;
  color: #000;
}

.btn-confirm:hover { filter: brightness(1.1); }
.btn-confirm:disabled { opacity: 0.5; cursor: not-allowed; }
</style>
