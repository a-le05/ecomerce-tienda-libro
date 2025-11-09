package ar.com.tienda_libro.mapper;

import ar.com.tienda_libro.dto.request.LibroRequestDTO;
import ar.com.tienda_libro.dto.response.CategoriaResponseDTO;
import ar.com.tienda_libro.dto.response.LibroResponseDTO;
import ar.com.tienda_libro.entity.Categoria;
import ar.com.tienda_libro.entity.Libro;
import org.springframework.stereotype.Component;

@Component
public class LibroMapper {

    // Convierte de entidad a DTO de respuesta
    public LibroResponseDTO toResponseDTO(Libro libro) {
        if (libro == null) return null;

        LibroResponseDTO dto = new LibroResponseDTO();
        dto.setIdLibro(libro.getIdLibro());
        dto.setTitulo(libro.getTitulo());
        dto.setAutor(libro.getAutor());
        dto.setDescripcion(libro.getDescripcion());
        dto.setImagenUrl(libro.getImagenUrl());
        dto.setPrecio(libro.getPrecio());
        dto.setStock(libro.getStock());
        // Convertimos LocalDateTime a BigDecimal no tiene sentido, debería ser LocalDateTime
        dto.setCreadoEn(libro.getCreadoEn());

        if (libro.getCategoria() != null) {
            CategoriaResponseDTO categoriaDTO = new CategoriaResponseDTO();
            categoriaDTO.setIdCategoria(libro.getCategoria().getIdCategoria());
            categoriaDTO.setNombre(libro.getCategoria().getNombre());
            dto.setCategoria(categoriaDTO);
        }

        return dto;
    }

    // Convierte de DTO de request a entidad
    public Libro toEntity(LibroRequestDTO dto, Categoria categoria) {
        if (dto == null) return null;

        Libro libro = new Libro();
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setDescripcion(dto.getDescripcion());
        libro.setPrecio(dto.getPrecio());
        libro.setStock(dto.getStock());
        libro.setCategoria(categoria); // Recibimos la categoría ya cargada desde la base de datos

        return libro;
    }

    // Actualiza una entidad existente con datos del DTO
    public void updateEntity(Libro libro, LibroRequestDTO dto, Categoria categoria) {
        libro.setTitulo(dto.getTitulo());
        libro.setAutor(dto.getAutor());
        libro.setDescripcion(dto.getDescripcion());
        libro.setPrecio(dto.getPrecio());
        libro.setStock(dto.getStock());
        libro.setCategoria(categoria);
    }
}

