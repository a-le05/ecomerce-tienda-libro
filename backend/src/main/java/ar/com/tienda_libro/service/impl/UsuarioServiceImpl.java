package ar.com.tienda_libro.service.impl;

import ar.com.tienda_libro.dto.request.InicioSesionRequestDTO;
import ar.com.tienda_libro.dto.request.UsuarioRequestDTO;
import ar.com.tienda_libro.dto.response.InicioSesionResponseDTO;
import ar.com.tienda_libro.dto.response.UsuarioResponseDTO;
import ar.com.tienda_libro.entity.Usuario;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.mapper.UsuarioMapper;
import ar.com.tienda_libro.repository.UsuarioRepository;
import ar.com.tienda_libro.security.AuthUser;
import ar.com.tienda_libro.security.JwtUtils;
import ar.com.tienda_libro.service.interf.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;

    @Override
    public InicioSesionResponseDTO registrarUsuario(UsuarioRequestDTO registroRequest) {
        // Convertimos DTO a entidad
        Usuario usuario = usuarioMapper.toEntity(registroRequest);

        // Encriptamos la contraseña antes de guardar
        usuario.setContrasena(passwordEncoder.encode(usuario.getContrasena()));

        // Guardamos en base de datos
        usuarioRepository.save(usuario);

        // Generamos token JWT
        String token = jwtUtils.generateToken(usuario);

        // Convertir usuario a DTO
        UsuarioResponseDTO usuarioDTO = usuarioMapper.toResponseDto(usuario);

        return new InicioSesionResponseDTO(token, "Usuario registrado correctamente", usuarioDTO);
    }

    @Override
    public InicioSesionResponseDTO iniciarSesion(InicioSesionRequestDTO loginRequest) {
        // Buscamos usuario por email
        Usuario usuario = usuarioRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Validamos contraseña
        if (!passwordEncoder.matches(loginRequest.getContraseña(), usuario.getContrasena())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Generamos nuevo token
        String token = jwtUtils.generateToken(usuario);

        // Convertir usuario a DTO
        UsuarioResponseDTO usuarioDTO = usuarioMapper.toResponseDto(usuario);

        return new InicioSesionResponseDTO(token, "Inicio de sesión exitoso", usuarioDTO);
    }

    @PreAuthorize("hasRole('ADMINISTRADOR')") // Solo permite que usuarios con rol ADMINISTRADOR ejecuten este métod
    @Override
    public List<UsuarioResponseDTO> obtenerTodos() {
        List<Usuario> usuarios = usuarioRepository.findAll();

        // Convertimos cada entidad a DTO usando tu UsuarioMapper
        return usuarios.stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    @Override
    public UsuarioResponseDTO obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new NotFoundException("Usuario no autenticado");
        }

        // Obtenemos el principal (nuestro AuthUser)
        Object principal = authentication.getPrincipal();
        String email;

        if (principal instanceof AuthUser authUser) {
            email = authUser.getUsername(); // devuelve el email
        } else if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            throw new RuntimeException("Tipo de usuario desconocido en SecurityContext");
        }

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return usuarioMapper.toResponseDto(usuario);
    }
}
