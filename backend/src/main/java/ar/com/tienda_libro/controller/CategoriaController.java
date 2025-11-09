package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.request.CategoriaRequestDTO;
import ar.com.tienda_libro.dto.response.CategoriaResponseDTO;
import ar.com.tienda_libro.service.interf.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // ================= CREAR CATEGORÍA =================
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@RequestBody CategoriaRequestDTO request) {
        CategoriaResponseDTO categoriaCreada = categoriaService.crearCategoria(request);
        return ResponseEntity.ok(categoriaCreada);
    }

    // ================= LISTAR TODAS LAS CATEGORÍAS =================
    @GetMapping("/listar")
    public ResponseEntity<List<CategoriaResponseDTO>> listarCategorias() {
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    // ================= OBTENER CATEGORÍA POR ID =================
    @GetMapping("/obtener/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerCategoriaPorId(id));
    }

    // ================= ACTUALIZAR CATEGORÍA =================
    @PutMapping("/actualizar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaRequestDTO request) {
        CategoriaResponseDTO categoriaActualizada = categoriaService.actualizarCategoria(id, request);
        return ResponseEntity.ok(categoriaActualizada);
    }

    // ================= ELIMINAR CATEGORÍA =================
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> eliminarCategoria(@PathVariable Long id) {
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.ok("Categoría eliminada correctamente");
    }
}
