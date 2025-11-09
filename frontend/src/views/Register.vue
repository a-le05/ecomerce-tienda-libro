<template>
  <div style="max-width: 600px; margin: 50px auto; padding: 40px; background-color: #fdfaf6; border-radius: 15px; box-shadow: 0 4px 15px rgba(0,0,0,0.1); font-family: 'Poppins', sans-serif;">
    
    <h1 style="text-align: center; color: #5c4033; font-size: 2em; margin-bottom: 25px;">
      👤 Crear Cuenta
    </h1>

    <form @submit.prevent="handleRegister" style="display: flex; flex-direction: column; gap: 15px;">

      <!-- Datos personales -->
      <h3 style="color: #5c4033; margin-bottom: 5px;">Datos personales</h3>
      <input v-model="form.nombre" type="text" placeholder="Nombre completo" required class="input-estilo" />
      <input v-model="form.email" type="email" placeholder="Correo electrónico" required class="input-estilo" />
      <input v-model="form.contrasena" type="password" placeholder="Contraseña" required class="input-estilo" />
      <input v-model="form.numeroTelefonico" type="tel" placeholder="Número de teléfono" required class="input-estilo" />

      <!-- Dirección -->
      <h3 style="color: #5c4033; margin-top: 20px; margin-bottom: 5px;">Dirección</h3>
      <input v-model="form.direccion.calle" type="text" placeholder="Calle" required class="input-estilo" />
      <input v-model="form.direccion.ciudad" type="text" placeholder="Ciudad" required class="input-estilo" />
      <input v-model="form.direccion.estado" type="text" placeholder="Estado / Provincia" required class="input-estilo" />
      <input v-model="form.direccion.codigoPostal" type="text" placeholder="Código Postal" required class="input-estilo" />
      <input v-model="form.direccion.pais" type="text" placeholder="País" required class="input-estilo" />

      <!-- Botón -->
      <button
        type="submit"
        style="margin-top: 20px; padding: 14px; background-color: #81c995; color: white; border: none; border-radius: 10px; font-size: 1.1em; font-weight: bold; cursor: pointer; transition: 0.3s;"
        @mouseover="hover = true"
        @mouseleave="hover = false"
        :style="{ transform: hover ? 'scale(1.03)' : 'scale(1)' }"
      >
        ✨ Registrarse
      </button>
    </form>

    <!-- Mensajes -->
    <p v-if="error" style="margin-top: 15px; color: #c0392b; text-align: center;">{{ error }}</p>
    <p v-if="success" style="margin-top: 15px; color: #27ae60; text-align: center;">{{ success }}</p>

    <!-- Enlace a login -->
    <p style="text-align: center; margin-top: 25px; color: #5c4033;">
      ¿Ya tienes cuenta?
      <router-link to="/login" style="color: #4e6c50; font-weight: bold; text-decoration: none;">Inicia sesión</router-link>
    </p>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const error = ref('')
const success = ref('')
const hover = ref(false)

const form = ref({
  nombre: '',
  email: '',
  contrasena: '',
  numeroTelefonico: '',
  rol: 'USUARIO',
  direccion: {
    calle: '',
    ciudad: '',
    estado: '',
    codigoPostal: '',
    pais: ''
  }
})

async function handleRegister() {
  try {
    error.value = ''
    success.value = ''

    const { data } = await api.post('/auth/register', form.value)

    authStore.token = data.token
    authStore.user = data.usuario
    localStorage.setItem('token', data.token)
    localStorage.setItem('user', JSON.stringify(data.usuario))

    success.value = '✅ Registro exitoso. Redirigiendo...'
    setTimeout(() => router.push('/'), 1200)
  } catch (err) {
    error.value = 'Error al registrarse: ' + (err.response?.data?.message || err.message)
  }
}
</script>

<style scoped>
.input-estilo {
  padding: 12px 15px;
  border-radius: 10px;
  border: 2px solid #e6ccb2;
  background-color: #fffdf8;
  font-size: 1em;
  color: #5c4033;
  transition: border 0.3s, box-shadow 0.3s;
}
.input-estilo:focus {
  border-color: #81c995;
  box-shadow: 0 0 5px rgba(129, 201, 149, 0.5);
  outline: none;
}
</style>
