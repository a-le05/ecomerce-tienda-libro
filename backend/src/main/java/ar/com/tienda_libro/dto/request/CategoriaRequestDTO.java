package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaRequestDTO { //Se usa cuando el cliente eniva datos

    @NotBlank(message = "El nombre de la categoria es obligatorio")
    private String nombre;
}
