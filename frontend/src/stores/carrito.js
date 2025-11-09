import { defineStore } from 'pinia'

export const useCarritoStore = defineStore('carrito', {
  state: () => ({
    items: JSON.parse(localStorage.getItem('carrito') || '[]')
  }),
  getters: {
    total: (state) => state.items.reduce((sum, item) => sum + (item.precio * item.cantidad), 0),
    cantidadTotal: (state) => state.items.reduce((sum, item) => sum + item.cantidad, 0)
  },
  actions: {
    agregar(libro, cantidad = 1) {
      const existente = this.items.find(i => i.idLibro === libro.idLibro)
      if (existente) {
        existente.cantidad += cantidad
      } else {
        // Solo guardar campos necesarios, NO el stock
        this.items.push({
          idLibro: libro.idLibro,
          titulo: libro.titulo,
          autor: libro.autor,
          precio: libro.precio,
          imagenUrl: libro.imagenUrl,
          cantidad
        })
      }
      localStorage.setItem('carrito', JSON.stringify(this.items))
    },
    incrementar(idLibro) {
      const item = this.items.find(i => i.idLibro === idLibro)
      if (item) {
        item.cantidad++
        localStorage.setItem('carrito', JSON.stringify(this.items))
      }
    },
    decrementar(idLibro) {
      const item = this.items.find(i => i.idLibro === idLibro)
      if (item && item.cantidad > 1) {
        item.cantidad--
        localStorage.setItem('carrito', JSON.stringify(this.items))
      }
    },
    eliminar(idLibro) {
      this.items = this.items.filter(i => i.idLibro !== idLibro)
      localStorage.setItem('carrito', JSON.stringify(this.items))
    },
    limpiar() {
      this.items = []
      localStorage.removeItem('carrito')
    }
  }
})