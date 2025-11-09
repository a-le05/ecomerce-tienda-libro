package ar.com.tienda_libro.dto.request;

import ar.com.tienda_libro.enums.EstadoPedido;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroPedidoRequestDTO {

    @NotNull(message = "Debe indicar el ID del libro")
    private Long idLibro;

    @NotNull(message = "Debe indicar el ID del usuario")
    private Long idUsuario;

    @Min(value = 1, message = "La cantidad minima es 1")
    private int cantidad;

    private EstadoPedido estadoPedido;
}
