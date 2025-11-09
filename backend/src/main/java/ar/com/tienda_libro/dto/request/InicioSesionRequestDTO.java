package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InicioSesionRequestDTO {

    @Email(message = "Debe ser un correo electronico válido")
    @NotBlank(message = "El mail es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contraseña;
}
