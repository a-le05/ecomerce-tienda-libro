package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.response.UsuarioResponseDTO;
import ar.com.tienda_libro.service.interf.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // Endpoint para obtener todos los usuarios (solo ADMINISTRADOR)
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('ADMINISTRADOR')") // solo ADMINISTRADOR puede acceder
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos() {
        List<UsuarioResponseDTO> usuarios = usuarioService.obtenerTodos();
        return ResponseEntity.ok(usuarios);
    }

    // Endpoint para obtener la info del usuario logueado
    @GetMapping("/mi-info")
    public ResponseEntity<UsuarioResponseDTO> obtenerUsuarioActual() {
        UsuarioResponseDTO usuario = usuarioService.obtenerUsuarioActual();
        return ResponseEntity.ok(usuario);
    }
}
