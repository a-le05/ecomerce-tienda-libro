package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.request.LibroRequestDTO;
import ar.com.tienda_libro.dto.response.LibroResponseDTO;
import ar.com.tienda_libro.service.interf.LibroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
@RequiredArgsConstructor
public class LibroController {

    private final LibroService libroService;

    // ================= CREAR LIBRO (con URL de imagen) =================
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<LibroResponseDTO> crearLibro(@RequestBody LibroRequestDTO libroRequest) {
        LibroResponseDTO libroCreado = libroService.crearLibro(libroRequest);
        return ResponseEntity.ok(libroCreado);
    }

    // ================= CREAR MÚLTIPLES LIBROS (carga masiva) =================
    @PostMapping("/crear-lote")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<LibroResponseDTO>> crearLibrosLote(@RequestBody List<LibroRequestDTO> librosRequest) {
        List<LibroResponseDTO> librosCreados = libroService.crearLibrosLote(librosRequest);
        return ResponseEntity.ok(librosCreados);
    }

    // ================= ACTUALIZAR LIBRO =================
    @PutMapping("/actualizar/{idLibro}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<LibroResponseDTO> actualizarLibro(
            @PathVariable Long idLibro,
            @RequestBody LibroRequestDTO libroRequest
    ) {
        LibroResponseDTO libroActualizado = libroService.actualizarLibro(idLibro, libroRequest);
        return ResponseEntity.ok(libroActualizado);
    }

    // ================= ELIMINAR LIBRO =================
    @DeleteMapping("/eliminar/{idLibro}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> eliminarLibro(@PathVariable Long idLibro){
        libroService.eliminarLibro(idLibro);
        return ResponseEntity.ok("Libro eliminado correctamente");
    }

    // ================= OBTENER LIBRO POR ID =================
    @GetMapping("/obtener/{idLibro}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    public ResponseEntity<LibroResponseDTO> obtenerLibroPorId(@PathVariable Long idLibro){
        return ResponseEntity.ok(libroService.obtenerLibroPorId(idLibro));
    }

    // ================= LISTAR TODOS LOS LIBROS (PÚBLICO - Catálogo) =================
    @GetMapping("/listar")
    public ResponseEntity<List<LibroResponseDTO>> listarLibros(){
        return ResponseEntity.ok(libroService.listarLibros());
    }

    // ================= OBTENER POR CATEGORÍA =================
    @GetMapping("/categoria/{idCategoria}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    public ResponseEntity<List<LibroResponseDTO>> obtenerLibrosPorCategoria(@PathVariable Long idCategoria){
        return ResponseEntity.ok(libroService.obtenerPorCategoria(idCategoria));
    }

    // ================= BUSCAR LIBRO POR TEXTO =================
    @GetMapping("/buscar")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','USUARIO')")
    public ResponseEntity<List<LibroResponseDTO>> buscarLibros(@RequestParam String texto){
        return ResponseEntity.ok(libroService.buscarPorTexto(texto));
    }
}
