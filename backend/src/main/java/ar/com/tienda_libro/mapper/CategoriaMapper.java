package ar.com.tienda_libro.mapper;

import ar.com.tienda_libro.dto.request.CategoriaRequestDTO;
import ar.com.tienda_libro.dto.response.CategoriaResponseDTO;
import ar.com.tienda_libro.entity.Categoria;
import org.springframework.stereotype.Component;

@Component
public class CategoriaMapper {

    // Convierte de entidad a DTO de respuesta
    public CategoriaResponseDTO toResponseDTO(Categoria categoria) {
        if (categoria == null) return null;

        CategoriaResponseDTO dto = new CategoriaResponseDTO();
        dto.setIdCategoria(categoria.getIdCategoria());
        dto.setNombre(categoria.getNombre());
        dto.setCreadoEn(categoria.getCreadoEn());

        return dto;
    }

    // Convierte de DTO de request a entidad
    public Categoria toEntity(CategoriaRequestDTO dto) {
        if (dto == null) return null;

        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());

        return categoria;
    }

    // Actualiza una entidad existente con datos del DTO
    public void updateEntity(Categoria categoria, CategoriaRequestDTO dto) {
        if (dto == null || categoria == null) return;

        categoria.setNombre(dto.getNombre());
    }
}
