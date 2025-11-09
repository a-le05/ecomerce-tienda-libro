# 📚 Tienda de Libros - E-commerce

> Sistema completo de comercio electrónico para venta de libros, desarrollado con Spring Boot y Vue.js

## 🚀 Características

- 🔐 **Autenticación y Autorización** con JWT y Spring Security
- 📖 **Gestión de Libros** (CRUD completo)
- 🏷️ **Categorías** organizadas (20 categorías predefinidas)
- 🛒 **Carrito de Compras** funcional
- 💳 **Integración con Mercado Pago** para pagos
- 👥 **Sistema de Usuarios** con roles (ADMIN, USER)
- 📊 **Panel de Administración** para gestión de inventario
- 🔍 **Búsqueda y Filtrado** de libros por categorías
- 📱 **API REST** bien documentada con Swagger
- 🎨 **Frontend Moderno** con Vue 3 y Vite

## 🛠️ Tecnologías

### Backend
- **Java 21**
- **Spring Boot 3.5.7**
- **Spring Security** (autenticación JWT)
- **Spring Data JPA** (persistencia)
- **PostgreSQL** (base de datos)
- **Maven** (gestión de dependencias)
- **Lombok** (reducir boilerplate)
- **Swagger/OpenAPI** (documentación API)

### Frontend
- **Vue.js 3.5.13**
- **Vue Router 4.5.0** (navegación)
- **Pinia 2.3.0** (gestión de estado)
- **Axios 1.7.9** (peticiones HTTP)
- **Vite 6.0.6** (build tool)

### Integración de Pagos
- **Mercado Pago SDK**

## 📋 Requisitos Previos

- Java 21 o superior
- Node.js 18+ y npm
- PostgreSQL 14+
- Maven 3.8+

## ⚙️ Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/tu-usuario/tienda-libro.git
cd tienda-libro
```

### 2. Configurar la Base de Datos

Crear una base de datos en PostgreSQL:

```sql
CREATE DATABASE tienda_libro;
```

Configurar las credenciales en `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/tienda_libro
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 3. Configurar Variables de Entorno

Editar `src/main/resources/application.properties` y configurar:

```properties
# JWT Secret (cambiar en producción)
jwt.secret=tu_secreto_jwt_muy_seguro

# Mercado Pago
mercadopago.access.token=tu_token_de_mercadopago
```

### 4. Instalar Dependencias del Backend

```bash
mvn clean install
```

### 5. Instalar Dependencias del Frontend

```bash
cd frontend
npm install
```

## 🚀 Ejecución

### Backend

```bash
mvn spring-boot:run
```

El backend estará disponible en: `http://localhost:8080`

### Frontend

```bash
cd frontend
npm run dev
```

El frontend estará disponible en: `http://localhost:5173`

## 📊 Datos Iniciales

Al iniciar la aplicación por primera vez, se cargan automáticamente:
- **20 categorías** predefinidas
- **60 libros** de ejemplo con imágenes

Los datos se cargan desde `src/main/resources/data/libros-iniciales.json`

## 📚 Documentación de la API

Una vez iniciado el backend, acceder a la documentación Swagger en:

```
http://localhost:8080/swagger-ui.html
```

### Principales Endpoints

#### Autenticación
- `POST /auth/login` - Iniciar sesión
- `POST /auth/register` - Registrar usuario

#### Libros
- `GET /api/libros` - Listar todos los libros
- `GET /api/libros/{id}` - Obtener libro por ID
- `POST /api/libros` - Crear libro (ADMIN)
- `PUT /api/libros/{id}` - Actualizar libro (ADMIN)
- `DELETE /api/libros/{id}` - Eliminar libro (ADMIN)
- `GET /api/libros/buscar?texto={query}` - Buscar libros

#### Categorías
- `GET /api/categorias` - Listar categorías
- `GET /api/categorias/{id}` - Obtener categoría por ID
- `GET /api/categorias/{id}/libros` - Libros por categoría
- `POST /api/categorias` - Crear categoría (ADMIN)
- `PUT /api/categorias/{id}` - Actualizar categoría (ADMIN)
- `DELETE /api/categorias/{id}` - Eliminar categoría (ADMIN)

#### Carrito
- `GET /api/carrito` - Ver carrito del usuario
- `POST /api/carrito/agregar` - Agregar libro al carrito
- `PUT /api/carrito/actualizar/{itemId}` - Actualizar cantidad
- `DELETE /api/carrito/eliminar/{itemId}` - Eliminar item
- `POST /api/carrito/checkout` - Finalizar compra

#### Pagos
- `POST /api/pagos/crear-preferencia` - Crear preferencia de pago en Mercado Pago
- `POST /api/pagos/webhook` - Webhook de Mercado Pago

## 🗂️ Estructura del Proyecto

```
tienda-libro/
├── src/
│   ├── main/
│   │   ├── java/ar/com/tienda_libro/
│   │   │   ├── config/          # Configuraciones (Security, CORS, DataLoader)
│   │   │   ├── controller/      # Controladores REST
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # Entidades JPA (Libro, Categoria, Usuario, etc.)
│   │   │   ├── repository/      # Repositorios JPA
│   │   │   ├── security/        # Seguridad (JWT, UserDetails)
│   │   │   └── service/         # Lógica de negocio
│   │   └── resources/
│   │       ├── data/            # Datos iniciales (JSON)
│   │       └── application.properties
│   └── test/                    # Tests unitarios
├── frontend/
│   ├── src/
│   │   ├── components/          # Componentes Vue
│   │   ├── views/               # Vistas/Páginas
│   │   ├── router/              # Configuración de rutas
│   │   ├── stores/              # Stores de Pinia
│   │   └── services/            # Servicios API (Axios)
│   ├── public/                  # Recursos estáticos
│   └── package.json
├── pom.xml                      # Dependencias Maven
└── README.md
```

## 🔒 Seguridad

- Autenticación basada en **JWT** (JSON Web Tokens)
- Contraseñas encriptadas con **BCrypt**
- Roles de usuario: `ADMIN` y `USER`
- CORS configurado para desarrollo

⚠️ **Importante**: Cambiar las siguientes variables en producción:
- `jwt.secret` en `application.properties`
- Credenciales de base de datos
- Token de Mercado Pago

## 🧪 Testing

Ejecutar tests:

```bash
mvn test
```

## 📦 Build para Producción

### Backend

```bash
mvn clean package
java -jar target/tienda_libro-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
cd frontend
npm run build
```

Los archivos de producción se generan en `frontend/dist/`

## 🗃️ Base de Datos

### Diagrama de Entidades

```
Usuario (1) ----< (N) Carrito (N) >---- (1) Libro
                          |
                          v
                    CarritoItem

Libro (N) >---- (1) Categoria

Usuario (1) ----< (N) Pedido
Pedido (1) ----< (N) DetallePedido >---- (1) Libro
```

### Tablas Principales

- `usuarios` - Usuarios del sistema
- `libros` - Catálogo de libros
- `categorias` - Categorías de libros
- `carritos` - Carritos de compra
- `carrito_items` - Items del carrito
- `pedidos` - Pedidos realizados
- `detalle_pedidos` - Detalles de cada pedido

## 🎨 Categorías Disponibles

1. Ficción
2. No Ficción
3. Ciencia y Tecnología
4. Historia
5. Biografía
6. Autoayuda
7. Negocios
8. Programación
9. Infantil
10. Juvenil
11. Romance
12. Misterio
13. Fantasía
14. Terror
15. Ciencia Ficción
16. Poesía
17. Arte
18. Cocina
19. Viajes
20. Religión

## 🤝 Contribuir

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

## 📝 Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

## 👤 Autor

**Tu Nombre**

- GitHub: [@tu-usuario](https://github.com/tu-usuario)
- LinkedIn: [Tu Perfil](https://linkedin.com/in/tu-perfil)

## 🙏 Agradecimientos

- Spring Boot Team
- Vue.js Team
- Mercado Pago Developers
- Comunidad de desarrolladores

---

⭐ Si te gustó este proyecto, dale una estrella en GitHub!
