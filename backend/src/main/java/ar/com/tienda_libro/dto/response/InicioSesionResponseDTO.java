package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InicioSesionResponseDTO {

    private String token;
    private String mensaje;
    private UsuarioResponseDTO usuario;
}
