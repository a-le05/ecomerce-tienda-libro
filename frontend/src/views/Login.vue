<template>
  <div
    style="min-height: 100vh; display: flex; align-items: center; justify-content: center;
           background: linear-gradient(180deg, #f9f4ec 0%, #f0e4d4 100%);
           font-family: 'Quicksand', sans-serif; color: #4a3b32;"
  >
    <div
      style="background-color: #fffaf5; border: 1px solid #e2d6c7; border-radius: 15px;
             padding: 40px; width: 100%; max-width: 400px; box-shadow: 0 6px 12px rgba(0,0,0,0.08); text-align: center;"
    >
      <h1 style="font-size: 2em; margin-bottom: 20px; color: #5b4033;">🔮 Iniciar Sesión</h1>

      <form @submit.prevent="handleLogin" style="display: flex; flex-direction: column; gap: 15px;">
        <input
          v-model="email"
          type="email"
          placeholder="Correo electrónico"
          required
          style="padding: 12px; border: 1px solid #d3c4b7; border-radius: 8px;
                 font-size: 1em; outline: none; transition: 0.3s;
                 background-color: #fffdf9;"
          @focus="inputFocus = 'email'"
          @blur="inputFocus = null"
          :style="inputFocus === 'email' ? 'border-color:#a88463; box-shadow:0 0 6px rgba(168,132,99,0.3);' : ''"
        />

        <input
          v-model="password"
          type="password"
          placeholder="Contraseña"
          required
          style="padding: 12px; border: 1px solid #d3c4b7; border-radius: 8px;
                 font-size: 1em; outline: none; transition: 0.3s;
                 background-color: #fffdf9;"
          @focus="inputFocus = 'password'"
          @blur="inputFocus = null"
          :style="inputFocus === 'password' ? 'border-color:#a88463; box-shadow:0 0 6px rgba(168,132,99,0.3);' : ''"
        />

        <button
          type="submit"
          style="padding: 12px; background: linear-gradient(135deg, #6dbb7a, #4c8c5d);
                 color: white; border: none; border-radius: 10px; font-size: 1.1em;
                 cursor: pointer; transition: 0.3s; font-weight: bold;"
          @mouseover="hover = true"
          @mouseleave="hover = false"
          :style="hover ? 'filter: brightness(1.1); transform: scale(1.03);' : ''"
        >
          🌿 Entrar
        </button>
      </form>

      <!-- Error -->
      <p v-if="error" style="color: #d9534f; margin-top: 15px; font-weight: bold;">{{ error }}</p>

      <!-- Registro -->
      <p style="margin-top: 25px; color: #6c5c50;">
        ¿No tienes cuenta?
        <router-link
          to="/register"
          style="color: #4c8c5d; text-decoration: none; font-weight: bold; transition: 0.2s;"
          @mouseover="linkHover = true"
          @mouseleave="linkHover = false"
          :style="linkHover ? 'text-decoration: underline;' : ''"
        >
          🌸 Regístrate aquí
        </router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const email = ref('')
const password = ref('')
const error = ref('')
const hover = ref(false)
const inputFocus = ref(null)
const linkHover = ref(false)

async function handleLogin() {
  try {
    const user = await authStore.login(email.value, password.value)
    if (user.rol === 'ADMINISTRADOR') router.push('/admin')
    else router.push('/')
  } catch (err) {
    error.value = '❌ Error al iniciar sesión. Verifica tus credenciales.'
  }
}
</script>
