package ar.com.tienda_libro.security;

import ar.com.tienda_libro.entity.Usuario;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService { // Se conecta con tu UsuarioRepository

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario con email " + email + " no encontrado"));
        return AuthUser.builder()
                .usuario(usuario)
                .build();
    }
}
