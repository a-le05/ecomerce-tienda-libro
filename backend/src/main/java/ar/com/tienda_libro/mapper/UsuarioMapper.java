package ar.com.tienda_libro.mapper;
import ar.com.tienda_libro.dto.request.UsuarioRequestDTO;
import ar.com.tienda_libro.dto.response.DireccionResponseDTO;
import ar.com.tienda_libro.dto.response.UsuarioResponseDTO;
import ar.com.tienda_libro.entity.Direccion;
import ar.com.tienda_libro.entity.Usuario;
import ar.com.tienda_libro.enums.UsuarioRol;
import ar.com.tienda_libro.exceptions.InvalidCredentialsException;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper { //se encarga de convertir entre entidades y DTOs

    // Convierte UsuarioRequestDTO → Usuario (para crear o actualizar)
    public Usuario toEntity(UsuarioRequestDTO dto) {
        if (dto == null) return null; //evitamos errores si alguien envía un DTO vacío

        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena()); // Aquí después podrías encriptar con Spring Security
        usuario.setNumeroTelefono(dto.getNumeroTelefonico());

        // Manejo seguro del enum
        if (dto.getRol() != null) {
            try {
                usuario.setRol(UsuarioRol.valueOf(dto.getRol().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidCredentialsException("El rol '" + dto.getRol() + "' no es válido.");
            }
        }

        // Mapeo de la dirección
        if (dto.getDireccion() != null) {
            Direccion direccion = new Direccion();
            direccion.setCalle(dto.getDireccion().getCalle());
            direccion.setCiudad(dto.getDireccion().getCiudad());
            direccion.setEstado(dto.getDireccion().getEstado());
            direccion.setCodigoPostal(dto.getDireccion().getCodigoPostal());
            direccion.setPais(dto.getDireccion().getPais());
            direccion.setUsuario(usuario); // vincula dirección al usuario
            usuario.setDireccion(direccion);
        }

        return usuario;
    }

    // Convierte Usuario → UsuarioResponseDTO (para devolver al cliente)
    public UsuarioResponseDTO toResponseDto(Usuario usuario) {
        if (usuario == null) return null;

        // Mapea campos simples:
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(usuario.getIdUsuario());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setNumeroTelefonico(usuario.getNumeroTelefono());
        dto.setRol(usuario.getRol() != null ? usuario.getRol().name() : null);

        if (usuario.getDireccion() != null) {
            DireccionResponseDTO direccionDto = new DireccionResponseDTO();
            direccionDto.setCalle(usuario.getDireccion().getCalle());
            direccionDto.setCiudad(usuario.getDireccion().getCiudad());
            direccionDto.setEstado(usuario.getDireccion().getEstado());
            direccionDto.setCodigoPostal(usuario.getDireccion().getCodigoPostal());
            direccionDto.setPais(usuario.getDireccion().getPais());
            dto.setDireccion(direccionDto);
        }

        return dto;
    }
}
