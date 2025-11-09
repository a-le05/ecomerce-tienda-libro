<template>
  <div
    style="
      width: 220px;
      background: #f6f1e7;
      border-right: 3px solid #d2b48c;
      padding: 20px;
      min-height: 100vh;
      box-shadow: 3px 0 8px rgba(0, 0, 0, 0.1);
      font-family: 'Trebuchet MS', sans-serif;
    "
  >
    <h3
      style="
        margin-bottom: 20px;
        color: #4c3b2b;
        text-align: center;
        font-weight: bold;
        font-size: 1.2em;
      "
    >
      ✨ Categorías ✨
    </h3>

    <!-- Opción de todas las categorías -->
    <div
      @click="$emit('seleccionar', null)"
      :style="{
        padding: '10px',
        marginBottom: '8px',
        cursor: 'pointer',
        textAlign: 'center',
        backgroundColor: categoriaSeleccionada === null ? '#8fb996' : '#fdf7f0',
        color: categoriaSeleccionada === null ? '#fff' : '#4c3b2b',
        borderRadius: '12px',
        fontWeight: categoriaSeleccionada === null ? 'bold' : 'normal',
        boxShadow: categoriaSeleccionada === null ? '0 0 6px #a1c79b' : '0 0 3px rgba(0,0,0,0.1)',
        transition: 'all 0.2s ease-in-out'
      }"
    >
      📚 Todas las categorías
    </div>

    <!-- Lista de categorías -->
    <div
      v-for="categoria in categorias"
      :key="categoria.idCategoria"
      @click="$emit('seleccionar', categoria.idCategoria)"
      :style="{
        padding: '10px',
        marginBottom: '8px',
        cursor: 'pointer',
        textAlign: 'center',
        backgroundColor: categoriaSeleccionada === categoria.idCategoria ? '#8fb996' : '#fdf7f0',
        color: categoriaSeleccionada === categoria.idCategoria ? '#fff' : '#4c3b2b',
        borderRadius: '12px',
        fontWeight: categoriaSeleccionada === categoria.idCategoria ? 'bold' : 'normal',
        boxShadow: categoriaSeleccionada === categoria.idCategoria ? '0 0 6px #a1c79b' : '0 0 3px rgba(0,0,0,0.1)',
        transition: 'all 0.2s ease-in-out'
      }"
      @mouseover="hover = categoria.idCategoria"
      @mouseleave="hover = null"
    >
      {{ categoria.nombre }}
    </div>
  </div>
</template>

<script setup>
defineProps({
  categorias: Array,
  categoriaSeleccionada: Number
})
defineEmits(['seleccionar'])
</script>

