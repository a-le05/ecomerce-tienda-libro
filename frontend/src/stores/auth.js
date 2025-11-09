import { defineStore } from 'pinia'
import api from '../services/api'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    token: localStorage.getItem('token')
  }),
  getters: {
    isAuthenticated: (state) => !!state.token,
    isAdmin: (state) => state.user?.rol === 'ADMINISTRADOR'
  },
  actions: {
    async login(email, password) {
      const { data } = await api.post('/auth/login', { email, contraseña: password })
      this.token = data.token
      this.user = data.usuario
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(data.usuario))
      return data.usuario
    },
    logout() {
      this.user = null
      this.token = null
      localStorage.removeItem('token')
      localStorage.removeItem('user')
    }
  }
})