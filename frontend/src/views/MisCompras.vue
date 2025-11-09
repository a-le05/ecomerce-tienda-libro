<template>
  <div
    style="max-width: 800px; margin: 40px auto; background: #fffaf5; padding: 30px; border-radius: 16px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); font-family: 'Segoe UI', sans-serif; color: #3a2d1b;"
  >
    <h1 style="text-align:center; color:#5e3c1b; font-size:2em; margin-bottom:20px;">
      🛍️ Mis Compras
    </h1>

    <div v-if="pedidos.length === 0" style="text-align:center; padding:20px; color:#8b7a60;">
      <p>No tienes compras todavía 🌿</p>
    </div>

    <div
      v-for="pedido in pedidos"
      :key="pedido.idPedido"
      style="background:#fff; border:2px solid #e8d5b5; padding:20px; margin:20px 0; border-radius:12px; box-shadow:0 2px 6px rgba(0,0,0,0.05); transition:transform 0.2s;"
      @mouseover="hover = pedido.idPedido"
      @mouseleave="hover = null"
      :style="{ transform: hover === pedido.idPedido ? 'scale(1.01)' : 'scale(1)' }"
    >
      <h3 style="margin-bottom:8px; color:#4d3319;">Pedido #{{ pedido.idPedido }}</h3>
      <p><strong>📅 Fecha:</strong> {{ formatearFecha(pedido.creadoEn) }}</p>
      <p><strong>💰 Total:</strong> ${{ pedido.precioTotal }}</p>

      <h4 style="margin-top:15px; color:#6a4a2d;">📚 Detalle de libros:</h4>

      <div
        v-for="item in pedido.libros"
        :key="item.idLibroPedido"
        style="margin-left:20px; padding:10px 0; border-bottom:1px dashed #d6c6a8;"
      >
        <p style="margin:0;">
          <span style="font-weight:600;">{{ item.tituloLibro }}</span><br>
          Cantidad: {{ item.cantidad }} |
          Subtotal: ${{ item.precioTotal }} |
          Estado:
          <span
            :style="{
              color: getEstadoColor(item.estadoPedido),
              fontWeight: 'bold'
            }"
          >
            {{ item.estadoPedido }}
          </span>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()
const pedidos = ref([])
const hover = ref(null)

onMounted(async () => {
  try {
    const { data } = await api.get('/pedidos/mis-pedidos')
    pedidos.value = data.sort((a, b) => new Date(b.creadoEn) - new Date(a.creadoEn))
  } catch (err) {
    console.error('Error al cargar pedidos:', err)
  }
})

function formatearFecha(fechaISO) {
  if (!fechaISO) return 'N/A'
  const fecha = new Date(fechaISO)
  return fecha.toLocaleDateString('es-AR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

function getEstadoColor(estado) {
  const colores = {
    'PENDIENTE': '#d08a00',
    'EN_PREPARACION': '#1E90FF',
    'ENVIADO': '#2e8b57',
    'ENTREGADO': '#4b9b28',
    'CANCELADO': '#b23a48'
  }
  return colores[estado] || '#000000'
}
</script>
