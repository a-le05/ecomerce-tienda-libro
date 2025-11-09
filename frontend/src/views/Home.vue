<template>
  <div class="home-container">
    <!-- Sidebar -->
    <CategoriaSidebar
      :categorias="categorias"
      :categoriaSeleccionada="categoriaSeleccionada"
      @seleccionar="seleccionarCategoria"
    />

    <!-- Contenido principal -->
    <div class="home-content">
      <h1 class="titulo">🌿 Catálogo de Libros</h1>
      <p v-if="categoriaSeleccionada" class="categoria-subtitulo">
        Mostrando: <strong>{{ categoriaNombre }}</strong>
      </p>

      <!-- Sin libros -->
      <div v-if="librosFiltrados.length === 0" class="sin-libros">
        <p>No hay libros en esta categoría...</p>
        <span>🍃</span>
      </div>

      <!-- Grid de libros -->
      <div class="libros-grid">
        <div
          v-for="libro in librosFiltrados"
          :key="libro.idLibro"
          class="libro-card"
          @mouseover="hover = libro.idLibro"
          @mouseleave="hover = null"
          :class="{ hover: hover === libro.idLibro }"
        >
          <!-- Imagen -->
          <img
            v-if="libro.imagenUrl"
            :src="libro.imagenUrl"
            :alt="libro.titulo"
            @error="manejarErrorImagen"
            class="libro-imagen"
          />
          <div v-else class="sin-imagen">📚 Sin imagen</div>

          <!-- Info -->
          <h3 class="libro-titulo">{{ libro.titulo }}</h3>
          <p class="libro-autor">{{ libro.autor }}</p>
          <p class="libro-precio">${{ libro.precio }}</p>

          <!-- Botón -->
          <button
            @click="agregarAlCarrito(libro)"
            class="btn-agregar"
            @mouseover="botonHover = libro.idLibro"
            @mouseleave="botonHover = null"
            :class="{ hover: botonHover === libro.idLibro }"
          >
            🛒 Agregar al Carrito
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import api from '../services/api'
import { useCarritoStore } from '../stores/carrito'
import CategoriaSidebar from '../components/CategoriaSidebar.vue'

const libros = ref([])
const categorias = ref([])
const categoriaSeleccionada = ref(null)
const carritoStore = useCarritoStore()
const hover = ref(null)
const botonHover = ref(null)

const librosFiltrados = computed(() => {
  if (categoriaSeleccionada.value === null) return libros.value
  return libros.value.filter(libro => libro.categoria?.idCategoria === categoriaSeleccionada.value)
})

const categoriaNombre = computed(() => {
  const categoria = categorias.value.find(c => c.idCategoria === categoriaSeleccionada.value)
  return categoria?.nombre || ''
})

onMounted(async () => {
  try {
    const [librosRes, categoriasRes] = await Promise.all([
      api.get('/libros/listar'),
      api.get('/categorias/listar')
    ])
    libros.value = librosRes.data
    categorias.value = categoriasRes.data
  } catch (err) {
    console.error('Error al cargar datos:', err)
  }
})

function seleccionarCategoria(idCategoria) {
  categoriaSeleccionada.value = idCategoria
}

async function agregarAlCarrito(libro) {
  try {
    const { data: libroActual } = await api.get(`/libros/obtener/${libro.idLibro}`)
    const stockDisponible = libroActual.stock ?? 999
    const item = carritoStore.items.find(i => i.idLibro === libro.idLibro)
    const cantidadActual = item ? item.cantidad : 0

    if (cantidadActual >= stockDisponible) {
      alert(`No hay más stock disponible (${stockDisponible})`)
      return
    }

    carritoStore.agregar(libro)
    alert('🌼 Libro agregado al carrito')
  } catch (err) {
    console.error('Error al agregar al carrito:', err)
    alert('Error al verificar stock')
  }
}

function manejarErrorImagen(event) {
  event.target.style.display = 'none'
  event.target.nextElementSibling.style.display = 'flex'
}
</script>

<style>
.home-container {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #f2e9dc 0%, #fdf7f0 100%);
  font-family: 'Segoe UI', sans-serif;
  color: #3f2e1f;
  animation: fadeIn 0.6s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

.home-content {
  flex: 1;
  padding: 40px;
  background: #fffaf3;
  border-radius: 20px;
  margin: 40px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
  animation: scaleIn 0.6s ease-out;
}

@keyframes scaleIn {
  from { transform: scale(0.95); opacity: 0; }
  to { transform: scale(1); opacity: 1; }
}

.titulo {
  font-size: 2.2em;
  margin-bottom: 10px;
  color: #4c3b2b;
  text-align: center;
}

.categoria-subtitulo {
  color: #7b6a55;
  text-align: center;
  margin-bottom: 25px;
  font-style: italic;
}

/* Sin libros */
.sin-libros {
  margin-top: 40px;
  text-align: center;
  font-size: 1.2em;
  color: #7b6a55;
}

/* Grid */
.libros-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 25px;
}

/* Card */
.libro-card {
  background: #f8f2ea;
  border-radius: 14px;
  padding: 18px;
  box-shadow: 0 4px 10px rgba(0,0,0,0.05);
  transition: all 0.3s ease;
  text-align: center;

  display: flex;               /* 👈 convierte la card en contenedor flexible */
  flex-direction: column; 
  align-items: center;     /* apila elementos en columna */
  justify-content: flex-start; /* separa imagen/texto arriba y botón abajo */
  height: auto;                /* permite igualar alturas */
  min-height: 360px;
  box-sizing: border-box; 
}

.libro-card.hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 14px rgba(0, 0, 0, 0.1);
  background: #fff5ec;
}

/* Imagen */
.libro-imagen {
  width: 100%;
  height: 200px;
  object-fit: contain;
  border-radius: 12px;
  margin-bottom: 12px;

}

.sin-imagen {
  width: 100%;
  height: 200px;
  background-color: #fdf7f0;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 12px;
  color: #7b6a55;
  font-size: 1.1em;
  margin-bottom: 12px;
}

/* Texto */
.libro-titulo {
  font-size: 1.1em;
  color: #4c3b2b;
  margin: 6px 0 4px;
  min-height: 2.4em;
}

.libro-autor {
  color: #7b6a55;
  font-size: 0.9em;
  margin-bottom: 5px;
  font-style: italic;
  min-height: 1.8em;
}

.libro-precio {
  font-size: 1.1em;
  font-weight: bold;
  color: #7ba883;
  margin: 10px 0;
}

/* Botón */
.btn-agregar {
  margin-top: 1rem;
  width: 100%;
  padding: 12px;
  background: #8fb996;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-size: 1em;
  font-weight: 600;
  transition: all 0.3s;
  box-shadow: 0 4px 10px rgba(143,185,150,0.4);
}

.btn-agregar.hover {
  background: #7ba883;
  transform: scale(1.03);
  box-shadow: 0 6px 14px rgba(123,168,131,0.4);
}

</style>
