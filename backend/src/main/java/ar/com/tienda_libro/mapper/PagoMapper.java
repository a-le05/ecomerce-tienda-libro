package ar.com.tienda_libro.mapper;

import ar.com.tienda_libro.dto.response.PagoResponseDTO;
import ar.com.tienda_libro.entity.Pago;
import org.springframework.stereotype.Component;

@Component
public class PagoMapper {

    // Convierte Pago entity a PagoResponseDTO
    public PagoResponseDTO toResponseDTO(Pago pago) {
        if (pago == null) return null;

        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setIdPago(pago.getIdPago());
        dto.setMonto(pago.getMonto());
        dto.setMetodoPago(pago.getMetodoPago());
        dto.setEstado(pago.getEstado());

        // Datos de Mercado Pago
        dto.setPaymentId(pago.getPaymentId());
        dto.setPreferenceId(pago.getPreferenceId());
        dto.setExternalReference(pago.getExternalReference());
        dto.setPaymentType(pago.getPaymentType());
        dto.setInstallments(pago.getInstallments());
        dto.setPayerEmail(pago.getPayerEmail());

        // Datos del pedido asociado
        if (pago.getPedido() != null) {
            dto.setIdPedido(pago.getPedido().getIdPedido());
            dto.setPrecioTotalPedido(pago.getPedido().getPrecioTotal());
        }

        dto.setCreadoEn(pago.getCreadoEn());
        dto.setActualizadoEn(pago.getActualizadoEn());

        return dto;
    }
}
