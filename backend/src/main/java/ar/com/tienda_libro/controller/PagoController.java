package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.response.MercadoPagoPreferenceResponseDTO;
import ar.com.tienda_libro.dto.response.PagoResponseDTO;
import ar.com.tienda_libro.mapper.PagoMapper;
import ar.com.tienda_libro.repository.PagoRepository;
import ar.com.tienda_libro.service.impl.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador para gestionar pagos
 */
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final MercadoPagoService mercadoPagoService;
    private final PagoRepository pagoRepository;
    private final PagoMapper pagoMapper;

    /**
     * Crea una preferencia de pago en Mercado Pago para un pedido
     * Devuelve la URL a la que debe ser redirigido el usuario para pagar
     */
    @PostMapping("/crear-preferencia/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<MercadoPagoPreferenceResponseDTO> crearPreferenciaPago(@PathVariable Long idPedido) {
        MercadoPagoPreferenceResponseDTO preferencia = mercadoPagoService.crearPreferenciaPago(idPedido);
        return ResponseEntity.ok(preferencia);
    }

    /**
     * Obtiene el estado de un pago por ID de pedido
     */
    @GetMapping("/pedido/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorPedido(@PathVariable Long idPedido) {
        return pagoRepository.findByPedidoIdPedido(idPedido)
                .map(pagoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un pago por su ID
     */
    @GetMapping("/{idPago}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(@PathVariable Long idPago) {
        return pagoRepository.findById(idPago)
                .map(pagoMapper::toResponseDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Procesa el resultado del pago cuando el usuario es redirigido desde MercadoPago
     * El frontend debe llamar a este endpoint con el payment_id que recibe en la URL
     *
     * Endpoint: POST /api/pagos/procesar-resultado/{paymentId}
     */
    @PostMapping("/procesar-resultado/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PagoResponseDTO> procesarResultadoPago(@PathVariable Long paymentId) {
        try {
            // Procesar el webhook manualmente con el payment_id
            mercadoPagoService.procesarWebhookPorPaymentId(paymentId);

            // Buscar el pago actualizado para devolverlo
            return pagoRepository.findByPaymentId(paymentId)
                    .map(pagoMapper::toResponseDTO)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Versión pública del endpoint de procesar resultado (sin autenticación)
     * Para usar desde la página de retorno de MercadoPago
     */
    @PostMapping("/public/procesar-resultado/{paymentId}")
    public ResponseEntity<String> procesarResultadoPagoPublico(@PathVariable Long paymentId) {
        try {
            // Procesar el webhook manualmente con el payment_id
            mercadoPagoService.procesarWebhookPorPaymentId(paymentId);

            return ResponseEntity.ok("Pago procesado correctamente");

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    /**
     * Endpoint para consultar manualmente el estado de un pago en MercadoPago por external_reference
     * Útil para diagnosticar problemas con webhooks
     */
    @GetMapping("/public/consultar-por-pedido/{idPedido}")
    public ResponseEntity<?> consultarPagoPorPedido(@PathVariable Long idPedido) {
        try {
            PagoResponseDTO pagoActualizado = mercadoPagoService.consultarYActualizarPagoPorPedido(idPedido);
            return ResponseEntity.ok(pagoActualizado);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }
}
