package ar.com.tienda_libro.config;

import ar.com.tienda_libro.entity.Categoria;
import ar.com.tienda_libro.entity.Direccion;
import ar.com.tienda_libro.entity.Libro;
import ar.com.tienda_libro.entity.Usuario;
import ar.com.tienda_libro.enums.UsuarioRol;
import ar.com.tienda_libro.repository.CategoriaRepository;
import ar.com.tienda_libro.repository.LibroRepository;
import ar.com.tienda_libro.repository.UsuarioRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository; // <-- agregar
    private final PasswordEncoder passwordEncoder;     // <-- para cifrar la contraseña
    private final ObjectMapper objectMapper;

    @Override
    public void run(String... args) throws Exception {
        // Solo cargar datos si la base de datos está vacía
        if (libroRepository.count() == 0 && categoriaRepository.count() == 0) {
            log.info("La base de datos está vacía. Cargando datos iniciales...");
            loadInitialData();
        } else {
            log.info("La base de datos ya contiene datos. Omitiendo carga inicial.");
        }
    }

    private void loadInitialData() {
        try {
            ClassPathResource resource = new ClassPathResource("data/libros-iniciales.json");
            JsonNode rootNode = objectMapper.readTree(resource.getInputStream());

            // Lista para almacenar las categorías en orden (índice = idCategoria - 1)
            Map<Integer, Categoria> categoriasMap = new HashMap<>();

            // Primero, cargar las categorías
            JsonNode categoriasNode = rootNode.get("categorias");
            if (categoriasNode != null && categoriasNode.isArray()) {
                int index = 1;
                for (JsonNode categoriaNode : categoriasNode) {
                    String nombreCategoria = categoriaNode.get("nombre").asText();

                    // Verificar si la categoría ya existe
                    Categoria categoria = categoriaRepository.findByNombre(nombreCategoria)
                            .orElseGet(() -> {
                                Categoria nuevaCategoria = new Categoria();
                                nuevaCategoria.setNombre(nombreCategoria);
                                return categoriaRepository.save(nuevaCategoria);
                            });

                    categoriasMap.put(index, categoria);
                    log.info("Categoría {} cargada/verificada: {}", index, nombreCategoria);
                    index++;
                }
            }

            // Luego, cargar los libros
            JsonNode librosNode = rootNode.get("libros");
            if (librosNode != null && librosNode.isArray()) {
                for (JsonNode libroNode : librosNode) {
                    String titulo = libroNode.get("titulo").asText();

                    // Verificar si el libro ya existe
                    if (libroRepository.findByTitulo(titulo).isEmpty()) {
                        Libro libro = new Libro();
                        libro.setTitulo(titulo);
                        libro.setAutor(libroNode.get("autor").asText());
                        libro.setDescripcion(libroNode.get("descripcion").asText());
                        libro.setImagenUrl(libroNode.get("imagenUrl").asText());
                        libro.setPrecio(new BigDecimal(libroNode.get("precio").asInt()));
                        libro.setStock(libroNode.get("stock").asInt());

                        // Asignar la categoría usando idCategoria
                        int idCategoria = libroNode.get("idCategoria").asInt();
                        Categoria categoria = categoriasMap.get(idCategoria);
                        if (categoria != null) {
                            libro.setCategoria(categoria);
                            libroRepository.save(libro);
                            log.info("Libro cargado: {} - Categoría: {} - Stock: {}",
                                    titulo, categoria.getNombre(), libro.getStock());
                        } else {
                            log.warn("No se encontró la categoría con ID {} para el libro: {}", idCategoria, titulo);
                        }
                    }
                }
            }

            // --- Usuarios ---
            JsonNode usuariosNode = rootNode.get("usuarios");
            if (usuariosNode != null && usuariosNode.isArray()) {
                for (JsonNode usuarioNode : usuariosNode) {
                    String email = usuarioNode.get("email").asText();
                    if (usuarioRepository.findByEmail(email).isEmpty()) {
                        Usuario usuario = new Usuario();
                        usuario.setNombre(usuarioNode.get("nombre").asText());
                        usuario.setEmail(email);
                        // Cifrar contraseña
                        usuario.setContrasena(passwordEncoder.encode(usuarioNode.get("contrasena").asText()));
                        usuario.setNumeroTelefono(usuarioNode.get("numeroTelefonico").asText());
                        String rolStr = usuarioNode.get("rol").asText();
                        usuario.setRol(UsuarioRol.valueOf(rolStr));

                        // Dirección
                        JsonNode dir = usuarioNode.get("direccion");
                        Direccion direccion = new Direccion();
                        direccion.setCalle(dir.get("calle").asText());
                        direccion.setCiudad(dir.get("ciudad").asText());
                        direccion.setEstado(dir.get("estado").asText());
                        direccion.setCodigoPostal(dir.get("codigoPostal").asText());
                        direccion.setPais(dir.get("pais").asText());
                        usuario.setDireccion(direccion);

                        usuarioRepository.save(usuario);
                        log.info("Usuario cargado: {} - Rol: {}", usuario.getNombre(), usuario.getRol());
                    }
                }
            }

            log.info("Datos iniciales cargados exitosamente.");
            log.info("Total categorías: {}", categoriaRepository.count());
            log.info("Total libros: {}", libroRepository.count());

        } catch (IOException e) {
            log.error("Error al cargar datos iniciales desde JSON", e);
        }
    }
}
