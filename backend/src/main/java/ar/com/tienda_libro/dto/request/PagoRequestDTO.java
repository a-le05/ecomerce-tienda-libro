package ar.com.tienda_libro.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoRequestDTO { //Este se usa cuando el cliente envía datos
    @NotNull(message = "El monto del pago es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

    @NotBlank(message = "El método del pago es obligatorio")
    private String metodo;

    @NotBlank(message = "El estado del pago es obligatorio")
    private String estado;

    @NotNull(message = "Debe asociar un pedido al pago")
    private Long idpedido;
}
