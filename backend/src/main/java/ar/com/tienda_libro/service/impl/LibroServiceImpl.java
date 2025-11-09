package ar.com.tienda_libro.service.impl;

import ar.com.tienda_libro.dto.request.LibroRequestDTO;
import ar.com.tienda_libro.dto.response.LibroResponseDTO;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.mapper.LibroMapper;
import ar.com.tienda_libro.repository.CategoriaRepository;
import ar.com.tienda_libro.repository.LibroRepository;
import ar.com.tienda_libro.service.interf.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class LibroServiceImpl implements LibroService {

    private final LibroRepository libroRepository;
    private final CategoriaRepository categoriaRepository;
    private final LibroMapper libroMapper;

    @Override
    public LibroResponseDTO crearLibro(LibroRequestDTO libroRequest) {
        // Buscar categoría
        var categoria = categoriaRepository.findById(libroRequest.getIdCategoria())
                .orElseThrow(() -> new NotFoundException(
                        "Categoría con ID " + libroRequest.getIdCategoria() + " no encontrada"
                ));

        // Convertir DTO a entidad
        var libro = libroMapper.toEntity(libroRequest, categoria);

        // Usar URL de imagen del DTO o imagen por defecto
        if (libroRequest.getImagenUrl() != null && !libroRequest.getImagenUrl().isEmpty()) {
            libro.setImagenUrl(libroRequest.getImagenUrl());
        } else {
            libro.setImagenUrl("/uploads/libros/default-book.png");
        }

        // Guardar libro
        libro = libroRepository.save(libro);

        return libroMapper.toResponseDTO(libro);
    }

    @Override
    public LibroResponseDTO obtenerLibroPorId(Long id) {
        var libro = libroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Libro con ID " + id + " no encontrado"));
        return libroMapper.toResponseDTO(libro);
    }

    @Override
    public List<LibroResponseDTO> listarLibros() {
        return libroRepository.findAll()
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LibroResponseDTO actualizarLibro(Long id, LibroRequestDTO libroRequest) {
        var libroExistente = libroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Libro con ID " + id + " no encontrado"));

        var categoria = categoriaRepository.findById(libroRequest.getIdCategoria())
                .orElseThrow(() -> new NotFoundException(
                        "Categoría con ID " + libroRequest.getIdCategoria() + " no encontrada"
                ));

        // Actualizar datos del libro
        libroMapper.updateEntity(libroExistente, libroRequest, categoria);

        // Actualizar URL de imagen si se proporciona
        if (libroRequest.getImagenUrl() != null && !libroRequest.getImagenUrl().isEmpty()) {
            libroExistente.setImagenUrl(libroRequest.getImagenUrl());
        }

        libroExistente = libroRepository.save(libroExistente);

        return libroMapper.toResponseDTO(libroExistente);
    }

    @Override
    public void eliminarLibro(Long id) {
        var libro = libroRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Libro con ID " + id + " no encontrado"));

        // Solo eliminar el registro de la base de datos
        // La imagen es una URL externa, no necesita ser eliminada
        // Si hay conflicto de integridad referencial, el GlobalExceptionHandler lo maneja
        libroRepository.delete(libro);
    }

    @Override
    public List<LibroResponseDTO> buscarPorTexto(String texto) {
        return libroRepository.buscarPorTexto(texto)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroResponseDTO> obtenerPorCategoria(Long idCategoria) {
        return libroRepository.findByCategoriaIdCategoria(idCategoria)
                .stream()
                .map(libroMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<LibroResponseDTO> crearLibrosLote(List<LibroRequestDTO> librosRequest) {
        return librosRequest.stream()
                .map(this::crearLibro)
                .collect(Collectors.toList());
    }
}
