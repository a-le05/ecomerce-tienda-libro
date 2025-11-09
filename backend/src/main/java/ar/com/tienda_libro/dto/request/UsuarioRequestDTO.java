package ar.com.tienda_libro.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioRequestDTO { // Se usa cuando el cliente envia datos( crear, modificar usuario)

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Debe ser un correo electronico válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La constraseña es obligatoria")
    private String contrasena;

    @NotBlank(message = "El número de telefono es obligatorio")
    private String numeroTelefonico;

    @NotNull(message = "La direccion es obligatoria")
    @Valid // Para validar los campos internos del objeto DireccionRequestDTO
    private DireccionRequestDTO direccion;

    @NotBlank(message = "El rol del usuario es obligatorio")
    private String rol; // lo manejamos como String, y luego lo convertimos a UsuarioRol en el mapper
}
