<template>
  <div class="login-viewport">
    <div class="login-card glass">
      <div class="logo">GamePulse</div>
      <h1>Bon retour</h1>
      <p>Connectez-vous pour piloter vos serveurs.</p>
      
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label for="username">Utilisateur</label>
          <input id="username" v-model="username" type="text" required placeholder="Votre pseudo" />
        </div>
        <div class="form-group">
          <label for="password">Mot de passe</label>
          <input id="password" v-model="password" type="password" required placeholder="••••••••" />
        </div>
        
        <button type="submit" :disabled="loading" class="btn-login">
          {{ loading ? 'Authentification...' : 'Se Connecter' }}
        </button>
        
        <p v-if="error" class="error-msg">{{ error }}</p>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { authService } from '../services/auth.service';

const username = ref('quentin');
const password = ref('password');
const error = ref('');
const loading = ref(false);
const router = useRouter();

const handleLogin = async () => {
  loading.value = true;
  error.value = '';
  try {
    await authService.login(username.value, password.value);
    router.push('/Dashboard');
  } catch (err) {
    error.value = 'Identifiants invalides ou serveur indisponible.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-viewport {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: var(--bg-main);
}

.login-card {
  width: 100%;
  max-width: 400px;
  padding: 3rem;
  border-radius: var(--radius-lg);
  text-align: center;
  background: var(--bg-sidebar);
  border: 1px solid var(--border-light);
}

.logo {
  font-size: 1.5rem;
  font-weight: 900;
  color: var(--accent-primary);
  margin-bottom: 2rem;
}

h1 { font-size: 1.75rem; font-weight: 800; margin-bottom: 0.5rem; }

p { color: var(--text-secondary); margin-bottom: 2rem; }

.form-group { text-align: left; margin-bottom: 1.5rem; }

label {
  display: block;
  font-size: 0.75rem;
  font-weight: 700;
  color: var(--text-muted);
  margin-bottom: 0.5rem;
  text-transform: uppercase;
}

input {
  width: 100%;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid var(--border-light);
  padding: 0.8rem 1rem;
  border-radius: var(--radius-md);
  color: #fff;
  transition: var(--transition);
}

input:focus { outline: none; border-color: var(--accent-primary); }

.btn-login {
  width: 100%;
  padding: 1rem;
  background: var(--accent-primary);
  border: none;
  border-radius: var(--radius-md);
  color: #000;
  font-weight: 800;
  cursor: pointer;
  transition: var(--transition);
}

.btn-login:hover { filter: brightness(1.1); }

.error-msg {
  color: var(--accent-danger);
  margin-top: 1.5rem;
  font-size: 0.85rem;
  font-weight: 600;
}
</style>
