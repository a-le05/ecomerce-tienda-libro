package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PedidoRequestDTO { //Usado cuando el cliente crea un pedido.

    @NotNull(message = "Debe indicar el ID del usuario que realiza el pedido")
    private Long idUsuario;

    @NotNull(message = "El pedido debe contener al menos un libro")
    private List<LibroPedidoRequestDTO> libros;
}
