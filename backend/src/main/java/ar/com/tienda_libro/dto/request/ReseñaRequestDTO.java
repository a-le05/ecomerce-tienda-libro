package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseñaRequestDTO { //Se usa cuando el cliente envía datos

    @NotBlank(message = "El contenido de la reseña es obligatorio")
    private String contenido;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 1, message = "La calificacion minima es 1")
    @Max(value = 5, message = "La calificacion maxima es 5")
    private Integer calificacion;

    @NotNull(message = "Debe especificar el libro reseñado")
    private Long idLibro;

    @NotNull(message = "Debe especificar el usuario que hace la reseña")
    private Long idUsuario;
}
