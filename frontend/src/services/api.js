import axios from 'axios'

const baseURL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

const api = axios.create({
  baseURL: baseURL + '/api'
})

// Interceptor de request: agrega el token si existe
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers.Authorization = 'Bearer ' + token
  }
  return config
})

// Interceptor de response: maneja errores 401 (token inválido/expirado)
api.interceptors.response.use(
  response => response,
  error => {
    // Si es error 401 y hay token guardado, significa que el token es inválido
    if (error.response?.status === 401) {
      const token = localStorage.getItem('token')
      if (token) {
        // Limpiar localStorage
        localStorage.removeItem('token')
        localStorage.removeItem('user')

        // Si el endpoint es público, reintentar sin token
        const publicEndpoints = ['/libros/listar', '/categorias/listar', '/auth/login', '/auth/register']
        const isPublicEndpoint = publicEndpoints.some(endpoint =>
          error.config.url.includes(endpoint)
        )

        if (isPublicEndpoint) {
          // Reintentar sin el header de autorización
          delete error.config.headers.Authorization
          return axios.request(error.config)
        }
      }
    }

    return Promise.reject(error)
  }
)

export default api