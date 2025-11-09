package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO { //Se usa cuando la API devuelve datos

    private Long idUsuario;
    private String nombre;
    private String email;
    private String numeroTelefonico;
    private String rol;
    private DireccionResponseDTO direccion;
}
