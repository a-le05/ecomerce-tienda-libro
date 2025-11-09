import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const routes = [
  { path: '/', component: () => import('../views/Home.vue') },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  { path: '/carrito', component: () => import('../views/Carrito.vue'), meta: { requiresAuth: true } },
  { path: '/admin', component: () => import('../views/AdminDashboard.vue'), meta: { requiresAuth: true, role: 'ADMINISTRADOR' } },
  {
    path: '/mis-compras',
    component: () => import('../views/MisCompras.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/admin/libros',
    component: () => import('../views/AdminLibros.vue'),
    meta: { requiresAuth: true, role: 'ADMINISTRADOR' }
  },
  // Rutas de resultado de pago de Mercado Pago
  {
    path: '/pago/success',
    component: () => import('../views/PagoSuccess.vue')
  },
  {
    path: '/pago/failure',
    component: () => import('../views/PagoFailure.vue')
  },
  {
    path: '/pago/pending',
    component: () => import('../views/PagoPending.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
  } else if (to.meta.role && authStore.user?.rol !== to.meta.role) {
    next('/')
  } else {
    next()
  }
})

export default router