<template>
  <div class="create-page">
    <AppSidebar @logout="handleLogout" />

    <main class="create-content">
      <!-- Header -->
      <div class="page-header">
        <button class="btn-back" @click="router.push('/Dashboard')">← Retour</button>
        <div>
          <h1>Déployer un serveur</h1>
          <p>Suivez les étapes pour configurer votre instance.</p>
        </div>
      </div>

      <!-- Indicateur d'étapes -->
      <div class="steps-indicator">
        <div class="step" :class="{ active: step >= 1, done: step > 1 }">
          <span class="step-num">1</span>
          <span class="step-label">Choisir un jeu</span>
        </div>
        <div class="step-line" :class="{ done: step > 1 }"></div>
        <div class="step" :class="{ active: step >= 2 }">
          <span class="step-num">2</span>
          <span class="step-label">Configurer</span>
        </div>
      </div>

      <!-- ÉTAPE 1 : Sélection du jeu -->
      <section v-if="step === 1" class="step-section">
        <h2>Quel jeu voulez-vous héberger ?</h2>

        <div v-if="loadingGames" class="loading-state">
          <div class="spinner"></div><p>Chargement des jeux...</p>
        </div>

        <div v-else class="game-grid">
          <div
            v-for="game in games"
            :key="game.id"
            class="game-card"
            :class="{ selected: selectedGame?.id === game.id }"
            @click="selectGame(game)"
          >
            <div class="game-icon">🎮</div>
            <div class="game-name">{{ game.name }}</div>
            <div class="game-meta">
              <span>👥 {{ game.maxPlayers }} j. max</span>
              <span>🖥 {{ game.cpuUsage }} CPU</span>
              <span>🧠 {{ game.ramUsage }} Go RAM</span>
            </div>
          </div>
        </div>

        <!-- Description du jeu sélectionné -->
        <div v-if="selectedGame" class="game-info-banner">
          <div class="game-info-text">
            <strong>{{ selectedGame.name }}</strong>
            <p>{{ selectedGame.info }}</p>
          </div>
          <div class="game-resources">
            <div class="resource-item">
              <label>CPU requis</label>
              <span>{{ selectedGame.cpuUsage }} cœur(s)</span>
            </div>
            <div class="resource-item">
              <label>RAM requise</label>
              <span>{{ selectedGame.ramUsage }} Go</span>
            </div>
            <div class="resource-item">
              <label>Stockage</label>
              <span>{{ selectedGame.storageUsage }} Go</span>
            </div>
          </div>
        </div>

        <div class="step-actions">
          <button class="btn-next" :disabled="!selectedGame" @click="step = 2">
            Suivant →
          </button>
        </div>
      </section>

      <!-- ÉTAPE 2 : Configuration -->
      <section v-if="step === 2" class="step-section">
        <h2>Configurer votre serveur <em>{{ selectedGame?.name }}</em></h2>

        <form @submit.prevent="handleSubmit" class="config-form">
          <div class="form-group">
            <label>Nom du serveur</label>
            <input
              v-model="formData.serverName"
              type="text"
              placeholder="Ex: Le Serveur de Quentin"
              required
            />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label>Nombre de joueurs max</label>
              <input
                v-model.number="formData.maxPlayers"
                type="number"
                :min="1"
                :max="selectedGame?.maxPlayers"
              />
              <span class="field-hint">Maximum autorisé : {{ selectedGame?.maxPlayers }}</span>
            </div>

            <div class="form-group">
              <label>Confidentialité</label>
              <div class="privacy-toggle">
                <button
                  type="button"
                  class="privacy-btn"
                  :class="{ active: formData.privacy === 'PUBLIC' }"
                  @click="formData.privacy = 'PUBLIC'"
                >
                  🌍 Public
                </button>
                <button
                  type="button"
                  class="privacy-btn"
                  :class="{ active: formData.privacy === 'PRIVATE' }"
                  @click="formData.privacy = 'PRIVATE'"
                >
                  🔒 Privé
                </button>
              </div>
            </div>
          </div>

          <div class="form-group">
            <label>Mot de passe</label>
            <div class="password-field">
              <input
                v-model="formData.password"
                :type="showPass ? 'text' : 'password'"
                :placeholder="formData.privacy === 'PUBLIC' ? 'Optionnel pour un serveur public' : 'Requis pour un serveur privé'"
                :required="formData.privacy === 'PRIVATE'"
              />
              <button type="button" class="btn-eye" @click="showPass = !showPass">
                {{ showPass ? '🙈' : '👁' }}
              </button>
            </div>
          </div>

          <div class="step-actions">
            <button type="button" class="btn-back-step" @click="step = 1">← Retour</button>
            <button type="submit" class="btn-deploy" :disabled="loading">
              <span v-if="loading" class="spinner-sm"></span>
              {{ loading ? 'Déploiement...' : '🚀 Déployer le serveur' }}
            </button>
          </div>
        </form>
      </section>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/auth.service';
import { serverService } from '../services/server.service';
import AppSidebar from '../components/AppSidebar.vue';

const router = useRouter();
const step = ref(1);
const games = ref([]);
const selectedGame = ref(null);
const loadingGames = ref(false);
const loading = ref(false);
const showPass = ref(false);

const formData = ref({
  serverName: '',
  maxPlayers: 10,
  privacy: 'PUBLIC',
  password: '',
});

onMounted(async () => {
  loadingGames.value = true;
  try {
    games.value = await serverService.getAllGamesTemplate();
  } catch (e) {
    console.error("Impossible de charger les jeux", e);
  } finally {
    loadingGames.value = false;
  }
});

const selectGame = (game) => {
  selectedGame.value = game;
  // Pré-remplir le nombre de joueurs max par défaut
  formData.value.maxPlayers = game.maxPlayers;
};

const handleSubmit = async () => {
  loading.value = true;
  try {
    await serverService.createGameServer({
      gameId: selectedGame.value.id,
      maxPlayers: formData.value.maxPlayers,
      serverName: formData.value.serverName,
      privacy: formData.value.privacy,
      password: formData.value.password || null,
    });
    router.push('/Dashboard');
  } catch (err) {
    console.error("Erreur lors de la création", err);
    alert("Erreur lors de la création du serveur. Vérifiez votre connexion.");
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
.create-page {
  display: flex;
  min-height: 100vh;
}

.create-content {
  flex-grow: 1;
  padding: 3rem 4rem;
  background: var(--bg-main);
  max-width: 900px;
}

/* Header */
.page-header {
  display: flex;
  align-items: flex-start;
  gap: 2rem;
  margin-bottom: 3rem;
}

.btn-back {
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-muted);
  padding: 0.5rem 1rem;
  border-radius: var(--radius-md);
  cursor: pointer;
  white-space: nowrap;
  margin-top: 0.4rem;
  transition: var(--transition);
}
.btn-back:hover { color: var(--text-primary); border-color: rgba(255,255,255,0.2); }

h1 { margin: 0; font-size: 2rem; font-weight: 800; }
.page-header p { color: var(--text-muted); margin-top: 0.25rem; }

/* Steps */
.steps-indicator {
  display: flex;
  align-items: center;
  gap: 0;
  margin-bottom: 3rem;
}

.step {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: var(--text-muted);
  transition: var(--transition);
}

.step.active { color: var(--text-primary); }
.step.done { color: var(--accent-primary); }

.step-num {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 2px solid currentColor;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 800;
  font-size: 0.85rem;
  flex-shrink: 0;
}

.step.done .step-num { background: var(--accent-primary); border-color: var(--accent-primary); color: #000; }
.step.active .step-num { border-color: var(--text-primary); }

.step-label { font-weight: 600; font-size: 0.9rem; }

.step-line {
  flex-grow: 1;
  height: 2px;
  background: var(--border-light);
  margin: 0 1.5rem;
  transition: background 0.3s;
}
.step-line.done { background: var(--accent-primary); }

/* Game Grid */
h2 { margin: 0 0 2rem; font-size: 1.4rem; font-weight: 700; }
h2 em { color: var(--accent-primary); font-style: normal; }

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 1rem;
  margin-bottom: 2rem;
}

.game-card {
  background: rgba(255,255,255,0.02);
  border: 2px solid var(--border-light);
  border-radius: var(--radius-lg);
  padding: 1.5rem;
  cursor: pointer;
  transition: var(--transition);
  text-align: center;
}

.game-card:hover { border-color: rgba(255,255,255,0.15); background: rgba(255,255,255,0.04); }
.game-card.selected { border-color: var(--accent-primary); background: rgba(16,185,129,0.08); }

.game-icon { font-size: 2rem; margin-bottom: 0.75rem; }
.game-name { font-weight: 700; font-size: 1rem; margin-bottom: 0.75rem; }
.game-meta { display: flex; flex-direction: column; gap: 0.25rem; font-size: 0.75rem; color: var(--text-muted); }

/* Info banner */
.game-info-banner {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 2rem;
  background: rgba(16,185,129,0.06);
  border: 1px solid rgba(16,185,129,0.2);
  border-radius: var(--radius-lg);
  padding: 1.5rem 2rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.game-info-text strong { font-size: 1rem; display: block; margin-bottom: 0.25rem; }
.game-info-text p { color: var(--text-secondary); font-size: 0.9rem; margin: 0; }

.game-resources { display: flex; gap: 2rem; flex-shrink: 0; }
.resource-item { display: flex; flex-direction: column; gap: 0.25rem; text-align: center; }
.resource-item label { font-size: 0.65rem; font-weight: 700; color: var(--text-muted); text-transform: uppercase; }
.resource-item span { font-size: 0.9rem; font-weight: 700; color: var(--accent-primary); }

/* Config form */
.config-form { display: flex; flex-direction: column; gap: 1.5rem; }

.form-group { display: flex; flex-direction: column; gap: 0.5rem; }
label {
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--text-secondary);
  text-transform: uppercase;
}

input, select {
  background: rgba(0,0,0,0.2);
  border: 1px solid var(--border-light);
  padding: 0.8rem 1rem;
  border-radius: var(--radius-md);
  color: #fff;
  transition: var(--transition);
  width: 100%;
}
input:focus { outline: none; border-color: var(--accent-primary); }

.field-hint { font-size: 0.75rem; color: var(--text-muted); }

.form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; }

/* Privacy toggle */
.privacy-toggle { display: flex; gap: 0.5rem; }
.privacy-btn {
  flex: 1;
  padding: 0.75rem;
  border: 1px solid var(--border-light);
  background: transparent;
  color: var(--text-secondary);
  border-radius: var(--radius-md);
  cursor: pointer;
  font-weight: 600;
  transition: var(--transition);
}
.privacy-btn.active { border-color: var(--accent-primary); background: rgba(16,185,129,0.1); color: var(--accent-primary); }

/* Password */
.password-field { position: relative; }
.password-field input { padding-right: 3rem; }
.btn-eye {
  position: absolute;
  right: 0.75rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  cursor: pointer;
  font-size: 1.1rem;
}

/* Actions */
.step-actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 1rem;
  padding-top: 2rem;
  border-top: 1px solid var(--border-light);
}

.btn-next, .btn-deploy {
  background: var(--accent-primary);
  color: #000;
  border: none;
  padding: 1rem 2.5rem;
  border-radius: var(--radius-md);
  font-weight: 800;
  cursor: pointer;
  transition: var(--transition);
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-left: auto;
}
.btn-next:disabled, .btn-deploy:disabled { opacity: 0.4; cursor: not-allowed; }
.btn-next:not(:disabled):hover, .btn-deploy:not(:disabled):hover { filter: brightness(1.1); transform: translateY(-1px); }

.btn-back-step {
  background: transparent;
  border: 1px solid var(--border-light);
  color: var(--text-secondary);
  padding: 1rem 1.5rem;
  border-radius: var(--radius-md);
  cursor: pointer;
  transition: var(--transition);
}
.btn-back-step:hover { color: var(--text-primary); }

/* Loading */
.loading-state { display: flex; align-items: center; gap: 1rem; color: var(--text-muted); padding: 3rem 0; }
.spinner {
  width: 28px; height: 28px;
  border: 2px solid rgba(255,255,255,0.05);
  border-top-color: var(--accent-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
.spinner-sm {
  width: 16px; height: 16px;
  border: 2px solid rgba(0,0,0,0.2);
  border-top-color: #000;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}
@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 1100px) {
  .create-page { flex-direction: column; }
  .create-content { padding: 2rem 1.5rem; }
  .form-row { grid-template-columns: 1fr; }
  .game-info-banner { flex-direction: column; align-items: flex-start; }
}
</style>
