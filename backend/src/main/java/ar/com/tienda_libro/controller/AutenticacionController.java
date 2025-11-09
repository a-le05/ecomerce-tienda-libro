package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.request.InicioSesionRequestDTO;
import ar.com.tienda_libro.dto.request.UsuarioRequestDTO;
import ar.com.tienda_libro.dto.response.InicioSesionResponseDTO;
import ar.com.tienda_libro.service.interf.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AutenticacionController {

    private final UsuarioService usuarioService;

    public AutenticacionController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<InicioSesionResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRequestDTO request) {
        InicioSesionResponseDTO response = usuarioService.registrarUsuario(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<InicioSesionResponseDTO> iniciarSesion(
            @Valid @RequestBody InicioSesionRequestDTO request) {
        InicioSesionResponseDTO response = usuarioService.iniciarSesion(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("✅ Servidor activo y endpoint accesible");
    }
}
