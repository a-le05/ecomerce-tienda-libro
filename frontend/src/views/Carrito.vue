<template>
  <div class="carrito-container">
    <!-- Encabezado -->
    <div class="carrito-header">
      <h1>🛒 Tu Carrito</h1>
      <button
        v-if="carritoStore.items.length > 0"
        @click="vaciarCarrito"
        class="btn-vaciar"
        @mouseover="hoverBotonVaciar = true"
        @mouseleave="hoverBotonVaciar = false"
        :class="{ hover: hoverBotonVaciar }"
      >
        🗑️ Vaciar
      </button>
    </div>

    <!-- Carrito vacío -->
    <div v-if="carritoStore.items.length === 0" class="carrito-vacio">
      <p>Parece que tu cesta está vacía...</p>
      <button @click="$router.push('/')" class="btn-ir-catalogo">🌿 Ir al Catálogo</button>
    </div>

    <!-- Items -->
    <div v-else>
      <div v-for="item in carritoStore.items" :key="item.idLibro" class="carrito-item">
        <!-- Imagen -->
        <img
          v-if="item.imagenUrl"
          :src="item.imagenUrl"
          :alt="item.titulo"
          class="item-imagen"
        />
        <div v-else class="sin-imagen">📖</div>

        <!-- Info -->
        <div class="item-info">
          <h3>{{ item.titulo }}</h3>
          <p class="autor">{{ item.autor }}</p>
          <p class="precio">${{ item.precio }}</p>
        </div>

        <!-- Controles -->
        <div class="item-controles">
          <button @click="decrementar(item)" :disabled="item.cantidad <= 1">−</button>
          <span>{{ item.cantidad }}</span>
          <button
            @click="incrementar(item)"
            :disabled="verificandoStock[item.idLibro] || item.cantidad >= (stockDisponible[item.idLibro] || 999)"
          >+</button>
        </div>

        <!-- Subtotal -->
        <div class="item-subtotal">
          <p>Subtotal:</p>
          <p>${{ (item.precio * item.cantidad).toFixed(2) }}</p>
        </div>

        <!-- Eliminar -->
        <button @click="eliminarItem(item.idLibro)" class="btn-eliminar">🗑️</button>
      </div>

      <!-- Resumen -->
      <div class="carrito-resumen">
        <div class="resumen-linea">
          <span>Cantidad de items:</span>
          <strong>{{ carritoStore.cantidadTotal }}</strong>
        </div>
        <div class="resumen-linea total">
          <span>Total:</span>
          <strong>${{ carritoStore.total.toFixed(2) }}</strong>
        </div>
        <button
          @click="crearPedido"
          class="btn-pagar"
          @mouseover="hoverBotonPagar = true"
          @mouseleave="hoverBotonPagar = false"
          :class="{ hover: hoverBotonPagar }"
        >
          💳 Proceder al Pago
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.carrito-container {
  max-width: 950px;
  margin: 40px auto;
  padding: 30px;
  background: #fffaf3;
  border-radius: 18px;
  box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
  font-family: 'Segoe UI', sans-serif;
  color: #4c3b2b;
  animation: fadeIn 0.6s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(-10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Encabezado */
.carrito-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
}

.carrito-header h1 {
  font-size: 2em;
  color: #5c4033;
}

/* Carrito vacío */
.carrito-vacio {
  text-align: center;
  padding: 70px 20px;
  background: #fdf7f0;
  border-radius: 14px;
  border: 2px dashed #e6ccb2;
  color: #8b6b61;
  font-size: 1.2em;
}

.btn-ir-catalogo {
  margin-top: 20px;
  padding: 12px 24px;
  background: #8fb996;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: 600;
  transition: 0.3s;
}
.btn-ir-catalogo:hover { background: #7ba883; transform: scale(1.03); }

/* Items */
.carrito-item {
  display: flex;
  align-items: center;
  gap: 16px;
  background: #f8f2ea;
  border: 2px solid #f1e1c6;
  border-radius: 14px;
  padding: 14px 18px;
  margin-bottom: 16px;
  box-shadow: 0 3px 8px rgba(0, 0, 0, 0.05);
}

.item-imagen {
  width: 80px;
  height: 110px;
  border-radius: 10px;
  object-fit: cover;
  border: 2px solid #f1e1c6;
}

.sin-imagen {
  width: 80px;
  height: 110px;
  background: #f0ebe1;
  border-radius: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2em;
}

.item-info {
  flex: 1;
}

.item-info h3 {
  margin: 0 0 6px;
  color: #4c3b2b;
  font-size: 1.1em;
}
.item-info .autor {
  margin: 0;
  color: #7b6a55;
  font-style: italic;
  font-size: 0.9em;
}
.item-info .precio {
  margin-top: 5px;
  color: #7ba883;
  font-weight: bold;
}

.item-controles {
  display: flex;
  align-items: center;
  gap: 8px;
}

.item-controles button {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  border: none;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}

.item-controles button:first-child {
  background: #e6ccb2;
  color: #5c4033;
}
.item-controles button:last-child {
  background: #8fb996;
  color: white;
}
.item-controles button:hover {
  transform: scale(1.1);
}

.item-subtotal {
  text-align: right;
  min-width: 100px;
}
.item-subtotal p:first-child {
  color: #7b6a55;
  font-size: 0.9em;
}
.item-subtotal p:last-child {
  font-weight: bold;
  color: #4e6c50;
}

/* Botones */
.btn-eliminar {
  padding: 8px 12px;
  background: #f77f6e;
  color: white;
  border: none;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
  transition: 0.3s;
}
.btn-eliminar:hover {
  background: #e86b5a;
  transform: scale(1.05);
}

.btn-vaciar {
  background: #f77f6e;
  color: white;
  border: none;
  border-radius: 10px;
  padding: 10px 20px;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}
.btn-vaciar.hover {
  transform: scale(1.05);
  background: #e86b5a;
}

/* Resumen */
.carrito-resumen {
  margin-top: 30px;
  padding: 22px;
  background: #fff8f2;
  border-radius: 14px;
  border: 2px solid #f1e1c6;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.resumen-linea {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
  font-size: 1.1em;
  color: #4c3b2b;
}
.resumen-linea.total {
  border-top: 2px dashed #e6ccb2;
  padding-top: 10px;
  font-size: 1.3em;
  font-weight: bold;
  color: #4e6c50;
}

.btn-pagar {
  width: 100%;
  margin-top: 20px;
  padding: 14px;
  background: #8fb996;
  color: white;
  border: none;
  border-radius: 12px;
  font-size: 1.1em;
  font-weight: bold;
  cursor: pointer;
  transition: 0.3s;
}
.btn-pagar.hover {
  transform: scale(1.03);
  background: #7ba883;
}
</style>


<script setup>
import { ref, onMounted } from 'vue'
import { useCarritoStore } from '../stores/carrito'
import { useAuthStore } from '../stores/auth'
import api from '../services/api'

const carritoStore = useCarritoStore()
const authStore = useAuthStore()
const stockDisponible = ref({})
const verificandoStock = ref({})
const hoverBotonVaciar = ref(false)
const hoverBotonPagar = ref(false)

onMounted(async () => {
  await verificarStockDeTodos()
})

async function verificarStockDeTodos() {
  for (const item of carritoStore.items) {
    await verificarStock(item.idLibro)
  }
}

async function verificarStock(idLibro) {
  try {
    verificandoStock.value[idLibro] = true
    const { data: libro } = await api.get(`/libros/obtener/${idLibro}`)
    stockDisponible.value[idLibro] = libro.stock ?? 999
  } catch (err) {
    console.error('Error al verificar stock:', err)
    stockDisponible.value[idLibro] = 999
  } finally {
    verificandoStock.value[idLibro] = false
  }
}

async function incrementar(item) {
  await verificarStock(item.idLibro)
  const stockActual = stockDisponible.value[item.idLibro] ?? 999
  if (item.cantidad >= stockActual) {
    alert(`No hay más stock disponible. Stock actual: ${stockActual}`)
    return
  }
  carritoStore.incrementar(item.idLibro)
}

function decrementar(item) {
  if (item.cantidad > 1) carritoStore.decrementar(item.idLibro)
}

function eliminarItem(idLibro) {
  if (confirm('¿Eliminar este libro del carrito?')) carritoStore.eliminar(idLibro)
}

function vaciarCarrito() {
  if (confirm('¿Vaciar todo el carrito?')) carritoStore.limpiar()
}

async function crearPedido() {
  try {
    if (!authStore.user || !authStore.user.idUsuario) {
      alert('Debes iniciar sesión para continuar')
      return
    }

    for (const item of carritoStore.items) {
      await verificarStock(item.idLibro)
      const stockActual = stockDisponible.value[item.idLibro] ?? 999
      if (item.cantidad > stockActual) {
        alert(`Stock insuficiente para "${item.titulo}". Disponible: ${stockActual}`)
        return
      }
    }

    const libros = carritoStore.items.map(i => ({ idLibro: i.idLibro, cantidad: i.cantidad }))
    const { data: pedido } = await api.post('/pedidos/crear', {
      idUsuario: authStore.user.idUsuario,
      libros
    })

    const { data: pago } = await api.post(`/pagos/crear-preferencia/${pedido.idPedido}`)
    carritoStore.limpiar()
    window.location.href = pago.initPoint
  } catch (err) {
    alert('Error: ' + (err.response?.data?.message || err.message))
  }
}
</script>
