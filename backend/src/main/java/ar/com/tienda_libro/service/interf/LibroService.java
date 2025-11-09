package ar.com.tienda_libro.service.interf;

import ar.com.tienda_libro.dto.request.LibroRequestDTO;
import ar.com.tienda_libro.dto.response.LibroResponseDTO;

import java.util.List;

public interface LibroService {

    // Crear un libro (con URL de imagen)
    LibroResponseDTO crearLibro(LibroRequestDTO libroRequest);

    // Obtener un libro por ID
    LibroResponseDTO obtenerLibroPorId(Long id);

    // Listar todos los libros
    List<LibroResponseDTO> listarLibros();

    // Actualizar un libro existente (con URL de imagen)
    LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libroRequest);

    // Eliminar un libro
    void eliminarLibro(Long id);

    // Buscar libros por texto en título o autor
    List<LibroResponseDTO> buscarPorTexto(String texto);

    // Obtener libros por categoría
    List<LibroResponseDTO> obtenerPorCategoria(Long idCategoria);

    // Crear múltiples libros (carga masiva con URLs)
    List<LibroResponseDTO> crearLibrosLote(List<LibroRequestDTO> librosRequest);
}
