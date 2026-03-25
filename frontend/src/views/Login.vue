<template>
  <div class="login-container">
    <div class="login-card glass">
      <h1>Connexion</h1>
      <p>Accédez à votre console de gestion de serveurs.</p>
      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <label for="username">Utilisateur</label>
          <input id="username" v-model="username" type="text" required placeholder="quentin" />
        </div>
        <div class="input-group">
          <label for="password">Mot de passe</label>
          <input id="password" v-model="password" type="password" required placeholder="••••••••" />
        </div>
        <button type="submit" :disabled="loading" class="btn-primary">
          <span v-if="loading">Connexion...</span>
          <span v-else>Se connecter</span>
        </button>
        <p v-if="error" class="error">{{ error }}</p>
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
  loading.ref = true;
  error.value = '';
  try {
    await authService.login(username.value, password.value);
    router.push('/');
  } catch (err) {
    error.value = 'Identifiants invalides ou serveur hors ligne.';
  } finally {
    loading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: radial-gradient(circle at top right, #1a1a2e, #16213e);
}

.login-card {
  padding: 2.5rem;
  width: 100%;
  max-width: 400px;
  text-align: center;
}

.glass {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 1.5rem;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.37);
}

h1 {
  margin-bottom: 0.5rem;
  font-weight: 700;
  color: #fff;
}

p {
  color: #a0a0a0;
  margin-bottom: 2rem;
}

.input-group {
  text-align: left;
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #ccc;
  font-size: 0.9rem;
}

input {
  width: 100%;
  padding: 0.8rem 1rem;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 0.8rem;
  color: #fff;
  transition: all 0.3s ease;
}

input:focus {
  outline: none;
  border-color: #4ecca3;
  background: rgba(255, 255, 255, 0.1);
}

.btn-primary {
  width: 100%;
  padding: 1rem;
  background: #4ecca3;
  border: none;
  border-radius: 0.8rem;
  color: #1a1a2e;
  font-weight: 700;
  cursor: pointer;
  transition: transform 0.2s ease, background 0.3s ease;
}

.btn-primary:hover {
  background: #45b393;
  transform: translateY(-2px);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error {
  color: #ff4d4d;
  margin-top: 1rem;
  font-size: 0.9rem;
}
</style>
