package ar.com.tienda_libro.service.impl;

import ar.com.tienda_libro.dto.request.CategoriaRequestDTO;
import ar.com.tienda_libro.dto.response.CategoriaResponseDTO;
import ar.com.tienda_libro.entity.Categoria;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.mapper.CategoriaMapper;
import ar.com.tienda_libro.repository.CategoriaRepository;
import ar.com.tienda_libro.service.interf.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoriaServiceImpl implements CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final CategoriaMapper categoriaMapper;

    @Override
    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO categoriaRequest) {
        // Verificar si ya existe una categoría con ese nombre
        if (categoriaRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaRequest.getNombre());
        }

        // Convertir DTO a entidad
        Categoria categoria = categoriaMapper.toEntity(categoriaRequest);

        // Guardar categoría
        categoria = categoriaRepository.save(categoria);

        return categoriaMapper.toResponseDTO(categoria);
    }

    @Override
    public CategoriaResponseDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));
        return categoriaMapper.toResponseDTO(categoria);
    }

    @Override
    public List<CategoriaResponseDTO> listarCategorias() {
        return categoriaRepository.findAll()
                .stream()
                .map(categoriaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO categoriaRequest) {
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));

        // Verificar si el nuevo nombre ya existe (y no es la misma categoría)
        if (!categoriaExistente.getNombre().equals(categoriaRequest.getNombre()) &&
            categoriaRepository.existsByNombre(categoriaRequest.getNombre())) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + categoriaRequest.getNombre());
        }

        // Actualizar datos de la categoría
        categoriaMapper.updateEntity(categoriaExistente, categoriaRequest);

        categoriaExistente = categoriaRepository.save(categoriaExistente);

        return categoriaMapper.toResponseDTO(categoriaExistente);
    }

    @Override
    public void eliminarCategoria(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoría con ID " + id + " no encontrada"));

        categoriaRepository.delete(categoria);
    }
}
