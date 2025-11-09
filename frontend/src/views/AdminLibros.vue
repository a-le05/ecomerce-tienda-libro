<template>
  <div
    style="
      background: #fcf5e8;
      min-height: 100vh;
      padding: 40px;
      font-family: 'Trebuchet MS', sans-serif;
      color: #3e2f24;
    "
  >
    <h1
      style="
        text-align: center;
        background: #f2e3c6;
        border: 3px solid #d2b48c;
        border-radius: 12px;
        padding: 14px;
        box-shadow: 0 3px 6px rgba(0,0,0,0.1);
        margin-bottom: 30px;
      "
    >
      📖 Gestión de Libros
    </h1>

    <div style="text-align: center; margin-bottom: 20px;">
      <button
        @click="mostrarForm = true"
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
        ✨ Crear Libro
      </button>
    </div>

    <!-- FORMULARIO -->
    <form
      v-if="mostrarForm"
      @submit.prevent="crearLibro"
      style="
        background: #fffaf3;
        border: 2px solid #d2b48c;
        padding: 20px;
        border-radius: 10px;
        max-width: 500px;
        margin: 0 auto 30px;
        box-shadow: 0 4px 10px rgba(0,0,0,0.1);
      "
    >
      <h3 style="text-align: center; margin-bottom: 15px;">📜 Nuevo Libro</h3>

      <input
        v-model="form.titulo"
        placeholder="Título"
        required
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />
      <input
        v-model="form.autor"
        placeholder="Autor"
        required
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />
      <textarea
        v-model="form.descripcion"
        placeholder="Descripción"
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      ></textarea>
      <input
        v-model="form.precio"
        type="number"
        step="0.01"
        placeholder="Precio"
        required
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />
      <input
        v-model.number="form.stock"
        type="number"
        placeholder="Stock"
        required
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />
      <input
        v-model.number="form.idCategoria"
        type="number"
        placeholder="ID Categoría"
        required
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />
      <input
        v-model="form.imagenUrl"
        placeholder="URL Imagen"
        style="display: block; width: 100%; margin: 8px 0; padding: 8px; border-radius: 6px; border: 1px solid #d2b48c;"
      />

      <button
        type="submit"
        style="
          background: #b39375;
          color: white;
          border: none;
          padding: 10px 20px;
          border-radius: 8px;
          cursor: pointer;
          width: 100%;
          margin-top: 10px;
          font-weight: bold;
        "
        @mouseover="($event.target.style.background = '#9e8267')"
        @mouseleave="($event.target.style.background = '#b39375')"
      >
        💾 Guardar
      </button>
    </form>

    <!-- LISTA DE LIBROS -->
    <div
      v-for="libro in libros"
      :key="libro.idLibro"
      style="
        background: #fffaf3;
        border: 2px solid #d2b48c;
        border-radius: 10px;
        padding: 15px;
        margin: 10px auto;
        max-width: 600px;
        box-shadow: 0 3px 6px rgba(0,0,0,0.1);
      "
    >
      <p style="margin: 0; font-size: 16px;">
        <strong>📗 {{ libro.titulo }}</strong> — {{ libro.autor }}<br />
        <span style="color: #6b5641;">
          💰 Precio: ${{ libro.precio }} | 📦 Stock: {{ libro.stock ?? 0 }} unidades
        </span>
      </p>

      <button
        @click="eliminar(libro.idLibro)"
        style="
          background: #d46a6a;
          color: white;
          border: none;
          border-radius: 8px;
          padding: 6px 12px;
          cursor: pointer;
          margin-top: 8px;
        "
        @mouseover="($event.target.style.background = '#b15555')"
        @mouseleave="($event.target.style.background = '#d46a6a')"
      >
        🗑️ Eliminar
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../services/api'

const libros = ref([])
const mostrarForm = ref(false)
const form = ref({
  titulo: '',
  autor: '',
  descripcion: '',
  precio: 0,
  stock: 0,
  idCategoria: null,
  imagenUrl: ''
})

onMounted(async () => {
  try {
    const { data } = await api.get('/libros/listar')
    libros.value = data
  } catch (err) {
    console.error('Error al cargar libros:', err)
  }
})

async function crearLibro() {
  try {
    await api.post('/libros/crear', form.value)
    mostrarForm.value = false
    form.value = {
      titulo: '',
      autor: '',
      descripcion: '',
      precio: 0,
      stock: 0,
      idCategoria: null,
      imagenUrl: ''
    }
    const { data } = await api.get('/libros/listar')
    libros.value = data
    alert('Libro creado correctamente 📚')
  } catch (err) {
    alert('Error al crear libro: ' + err.message)
  }
}

async function eliminar(id) {
  try {
    await api.delete(`/libros/eliminar/${id}`)
    libros.value = libros.value.filter(l => l.idLibro !== id)
    alert('Libro eliminado 🪄')
  } catch (err) {
    const errorMsg = err.response?.data?.message || err.message
    alert('Error al eliminar libro: ' + errorMsg)
  }
}
</script>
