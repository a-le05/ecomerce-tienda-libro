<template>
  <nav
    style="
      background: #f2e9dc;
      padding: 12px 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      gap: 14px;
      border-bottom: 3px solid #d2b48c;
      box-shadow: 0 3px 6px rgba(0,0,0,0.1);
      font-family: 'Trebuchet MS', sans-serif;
    "
  >
    <!-- Enlace principal -->
    <router-link
      to="/"
      style="
        color: #4c3b2b;
        text-decoration: none;
        font-weight: bold;
        background: #fdf7f0;
        padding: 8px 14px;
        border-radius: 10px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        transition: all 0.2s ease-in-out;
      "
      @mouseover="hoverColor($event)"
      @mouseleave="resetColor($event)"
    >
      🏠 Home
    </router-link>

    <router-link
      to="/carrito"
      style="
        color: #4c3b2b;
        text-decoration: none;
        font-weight: bold;
        background: #fdf7f0;
        padding: 8px 14px;
        border-radius: 10px;
        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        transition: all 0.2s ease-in-out;
      "
      @mouseover="hoverColor($event)"
      @mouseleave="resetColor($event)"
    >
      🛒 Carrito ({{ carritoStore.cantidadTotal }})
    </router-link>

    <span v-if="!authStore.isAuthenticated">
      <router-link
        to="/login"
        style="
          color: #4c3b2b;
          text-decoration: none;
          font-weight: bold;
          background: #fdf7f0;
          padding: 8px 14px;
          border-radius: 10px;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
          transition: all 0.2s ease-in-out;
        "
        @mouseover="hoverColor($event)"
        @mouseleave="resetColor($event)"
      >
        🔑 Login
      </router-link>
    </span>

    <span v-else style="display: flex; gap: 10px;">
      <router-link
        to="/mis-compras"
        style="
          color: #4c3b2b;
          text-decoration: none;
          font-weight: bold;
          background: #fdf7f0;
          padding: 8px 14px;
          border-radius: 10px;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
          transition: all 0.2s ease-in-out;
        "
        @mouseover="hoverColor($event)"
        @mouseleave="resetColor($event)"
      >
        📦 Mis Compras
      </router-link>

      <router-link
        v-if="authStore.isAdmin"
        to="/admin"
        style="
          color: #4c3b2b;
          text-decoration: none;
          font-weight: bold;
          background: #fdf7f0;
          padding: 8px 14px;
          border-radius: 10px;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
          transition: all 0.2s ease-in-out;
        "
        @mouseover="hoverColor($event)"
        @mouseleave="resetColor($event)"
      >
        🧙‍♀️ Admin
      </router-link>

      <button
        @click="authStore.logout()"
        style="
          background: #8fb996;
          border: none;
          color: #fff;
          font-weight: bold;
          padding: 8px 14px;
          border-radius: 10px;
          cursor: pointer;
          box-shadow: 0 2px 4px rgba(0,0,0,0.1);
          transition: all 0.2s ease-in-out;
        "
        @mouseover="($event.target.style.background = '#7ba883')"
        @mouseleave="($event.target.style.background = '#8fb996')"
      >
        🚪 Salir
      </button>
    </span>
  </nav>
</template>

<script setup>
import { useAuthStore } from '../stores/auth'
import { useCarritoStore } from '../stores/carrito'

const authStore = useAuthStore()
const carritoStore = useCarritoStore()

// Hover helpers
const hoverColor = (e) => {
  e.target.style.background = '#d5c3aa'
}
const resetColor = (e) => {
  e.target.style.background = '#fdf7f0'
}
</script>
