package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.service.impl.MercadoPagoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controlador para recibir webhooks de Mercado Pago
 * IMPORTANTE: Este endpoint NO debe tener autenticación (@PreAuthorize)
 * porque las notificaciones vienen desde los servidores de Mercado Pago
 */
@RestController
@RequestMapping("/api/webhooks/mercadopago")
@RequiredArgsConstructor
@Slf4j
public class MercadoPagoWebhookController {

    private final MercadoPagoService mercadoPagoService;

    /**
     * Endpoint que recibe las notificaciones de Mercado Pago
     *
     * Mercado Pago envía diferentes tipos de notificaciones:
     * - payment: Cuando cambia el estado de un pago
     * - merchant_order: Cuando cambia el estado de una orden
     */
    @PostMapping("/notificacion")
    public ResponseEntity<Map<String, Object>> recibirNotificacion(
            @RequestBody(required = false) Map<String, Object> body,
            @RequestHeader(required = false) Map<String, String> headers) {

        try {
            log.info("========================================");
            log.info("WEBHOOK RECIBIDO DE MERCADO PAGO");
            log.info("========================================");
            log.info("Headers: {}", headers);
            log.info("Body completo: {}", body);
            log.info("========================================");

            // MercadoPago envía: { "type": "payment", "data": { "id": "123456" } }
            if (body == null || !body.containsKey("type")) {
                log.warn("Webhook sin type en el body");
                return ResponseEntity.ok(Map.of("received", true));
            }

            String type = (String) body.get("type");

            // Procesar solo eventos de tipo "payment"
            if ("payment".equals(type)) {
                // Extraer el payment ID del objeto data
                @SuppressWarnings("unchecked")
                Map<String, Object> data = (Map<String, Object>) body.get("data");

                if (data == null || !data.containsKey("id")) {
                    log.error("Webhook de payment sin data.id");
                    return ResponseEntity.ok(Map.of("received", true, "error", "No data.id"));
                }

                String paymentIdStr = data.get("id").toString();
                Long paymentId = Long.parseLong(paymentIdStr);

                log.info("Procesando payment ID: {}", paymentId);

                // Consultar el pago en MercadoPago para obtener toda la información
                mercadoPagoService.procesarWebhookPorPaymentId(paymentId);

                log.info("Webhook procesado exitosamente: {}", paymentId);
                return ResponseEntity.ok(Map.of("received", true));

            } else {
                log.info("Tipo de notificación no procesado: {}", type);
                return ResponseEntity.ok(Map.of("received", true));
            }

        } catch (Exception e) {
            log.error("Error al procesar webhook de Mercado Pago", e);
            // IMPORTANTE: Siempre devolver 200 OK para que MP no reintente
            return ResponseEntity.ok(Map.of("received", true, "error", e.getMessage()));
        }
    }

    /**
     * Endpoint de prueba para verificar que el webhook está funcionando
     */
    @GetMapping("/test")
    public ResponseEntity<String> testWebhook() {
        return ResponseEntity.ok("Webhook endpoint is working!");
    }
}
