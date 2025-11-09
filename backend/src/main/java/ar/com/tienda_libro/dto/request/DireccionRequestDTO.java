package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionRequestDTO { //Se usa cuando el cliente envia datos(ej: crear, modificar usuario con direccion)

    @NotBlank(message = "La cllase es olbigatoria")
    private String calle;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotBlank(message = "El estado es obligatorio")
    private String codigoPostal;

    @NotBlank(message = "El pais es obligatorio")
    private String pais;
}
