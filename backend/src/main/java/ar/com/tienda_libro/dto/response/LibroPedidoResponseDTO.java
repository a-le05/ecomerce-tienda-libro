package ar.com.tienda_libro.dto.response;

import ar.com.tienda_libro.enums.EstadoPedido;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroPedidoResponseDTO { //Usado cuando la API devuelve datos.

    private Long idLibroPedido;
    private int cantidad;
    private EstadoPedido estadoPedido;

    //Informacion relacionada
    private Long idLibro;
    private String tituloLibro;
    private BigDecimal precioTotal;

    private Long idUsuario;
    private String nombreUsuario;
}
