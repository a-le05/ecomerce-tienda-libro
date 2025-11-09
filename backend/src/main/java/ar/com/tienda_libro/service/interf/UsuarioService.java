package ar.com.tienda_libro.service.interf;

import ar.com.tienda_libro.dto.request.InicioSesionRequestDTO;
import ar.com.tienda_libro.dto.request.UsuarioRequestDTO;
import ar.com.tienda_libro.dto.response.InicioSesionResponseDTO;
import ar.com.tienda_libro.dto.response.UsuarioResponseDTO;

import java.util.List;

public interface UsuarioService {

    // Registrar nuevo usuario
    InicioSesionResponseDTO registrarUsuario(UsuarioRequestDTO registroRequest);

    // Iniciar sesión
    InicioSesionResponseDTO iniciarSesion(InicioSesionRequestDTO loginRequest);

    // Obtener todos los usuarios (solo ADMIN)
    List<UsuarioResponseDTO> obtenerTodos();

    // Obtener el usuario actualmente logueado (opcional)
    UsuarioResponseDTO obtenerUsuarioActual();
}
