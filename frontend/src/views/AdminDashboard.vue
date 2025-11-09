<template>
  <div
    style="
      background: #fdf7f0;
      min-height: 100vh;
      padding: 40px;
      font-family: 'Trebuchet MS', sans-serif;
      color: #4c3b2b;
    "
  >
    <h1
      style="
        text-align: center;
        color: #3e2f24;
        background: #f2e3c6;
        padding: 14px 0;
        border-radius: 12px;
        border: 3px solid #d2b48c;
        box-shadow: 0 3px 6px rgba(0,0,0,0.1);
        margin-bottom: 30px;
      "
    >
      🧙‍♀️ Panel de Administrador
    </h1>

    <div style="text-align: center; margin-bottom: 30px;">
      <button
        @click="$router.push('/admin/libros')"
        style="
          background: #8fb996;
          color: white;
          border: none;
          font-weight: bold;
          padding: 10px 20px;
          border-radius: 10px;
          cursor: pointer;
          box-shadow: 0 3px 6px rgba(0,0,0,0.2);
          transition: all 0.2s ease-in-out;
        "
        @mouseover="($event.target.style.background = '#7ba883')"
        @mouseleave="($event.target.style.background = '#8fb996')"
      >
        📚 Gestionar Libros
      </button>
    </div>

    <h2
      style="
        color: #4c3b2b;
        border-bottom: 2px solid #d2b48c;
        display: inline-block;
        margin-bottom: 15px;
      "
    >
      📦 Todos los Pedidos
    </h2>

    <div
      v-if="pedidos.length === 0"
      style="
        text-align: center;
        color: #6b5641;
        font-style: italic;
        margin-top: 20px;
      "
    >
      No hay pedidos todavía...
    </div>

    <div
      v-for="pedido in pedidos"
      :key="pedido.idPedido"
      style="
        background: #fffaf3;
        border: 2px solid #d2b48c;
        padding: 15px 20px;
        margin: 15px 0;
        border-radius: 10px;
        box-shadow: 0 3px 6px rgba(0,0,0,0.1);
        transition: transform 0.2s ease-in-out;
      "
      @mouseover="($event.target.style.transform = 'scale(1.02)')"
      @mouseleave="($event.target.style.transform = 'scale(1)')"
    >
      <h3 style="margin-bottom: 8px;">📜 Pedido #{{ pedido.idPedido }}</h3>
      <p><strong>👤 Usuario:</strong> {{ pedido.usuario?.nombre }} ({{ pedido.usuario?.email }})</p>
      <p><strong>📅 Fecha:</strong> {{ new Date(pedido.fechaPedido).toLocaleDateString() }}</p>
      <p><strong>💰 Total:</strong> ${{ pedido.precioTotal }}</p>

      <div
        v-for="item in pedido.listaLibros"
        :key="item.idLibroPedido"
        style="
          background: #f2e9dc;
          padding: 8px 10px;
          border-radius: 6px;
          margin: 5px 0;
        "
      >
        📗 <strong>{{ item.libro.titulo }}</strong> ×{{ item.cantidad }}
        <span style="float: right;">🪄 Estado: <em>{{ item.estadoPedido }}</em></span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'

const pedidos = ref([])

onMounted(async () => {
  try {
    const { data } = await api.get('/pedidos/listar')
    pedidos.value = data
  } catch (err) {
    console.error('Error al cargar pedidos:', err)
  }
})
</script>


