package ar.com.tienda_libro.service.impl;

import ar.com.tienda_libro.dto.response.MercadoPagoPreferenceResponseDTO;
import ar.com.tienda_libro.entity.LibroPedido;
import ar.com.tienda_libro.entity.Pago;
import ar.com.tienda_libro.entity.Pedido;
import ar.com.tienda_libro.enums.EstadoPago;
import ar.com.tienda_libro.enums.EstadoPedido;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.repository.LibroPedidoRepository;
import ar.com.tienda_libro.repository.PagoRepository;
import ar.com.tienda_libro.repository.PedidoRepository;
import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.payment.PaymentClient;
import com.mercadopago.client.preference.PreferenceBackUrlsRequest;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.payment.Payment;
import com.mercadopago.resources.preference.Preference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para la integración con Mercado Pago
 * Gestiona la creación de preferencias de pago y procesamiento de webhooks
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MercadoPagoService {

    private final PedidoRepository pedidoRepository;
    private final PagoRepository pagoRepository;
    private final LibroPedidoRepository libroPedidoRepository;
    private final ar.com.tienda_libro.mapper.PagoMapper pagoMapper;

    @Value("${mercadopago.access-token}")
    private String accessToken;

    @Value("${mercadopago.url.success:http://localhost:3000/pago/success}")
    private String successUrl;

    @Value("${mercadopago.url.failure:http://localhost:3000/pago/failure}")
    private String failureUrl;

    @Value("${mercadopago.url.pending:http://localhost:3000/pago/pending}")
    private String pendingUrl;

    @Value("${mercadopago.webhook.url}")
    private String webhookUrl;

    /**
     * Crea una preferencia de pago en Mercado Pago para un pedido
     */
    public MercadoPagoPreferenceResponseDTO crearPreferenciaPago(Long idPedido) {
        log.info("Iniciando creación de preferencia de pago para pedido ID: {}", idPedido);

        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        log.info("Pedido encontrado - Total: {}, Libros: {}",
            pedido.getPrecioTotal(), pedido.getListaLibros().size());

        // Verificar que el pedido no tenga ya un pago
        if (pagoRepository.existsByPedidoIdPedido(idPedido)) {
            throw new IllegalArgumentException("El pedido ya tiene un pago asociado");
        }

        try {
            // Configurar el access token de Mercado Pago
            log.info("Configurando Access Token de Mercado Pago (primeros 20 caracteres): {}",
                accessToken != null ? accessToken.substring(0, Math.min(20, accessToken.length())) : "NULL");
            MercadoPagoConfig.setAccessToken(accessToken);

            // Crear items de la preferencia
            List<PreferenceItemRequest> items = new ArrayList<>();

            for (LibroPedido libroPedido : pedido.getListaLibros()) {
                // Validar que el precio sea mayor a 0
                if (libroPedido.getLibro().getPrecio() == null ||
                    libroPedido.getLibro().getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException(
                        "El libro '" + libroPedido.getLibro().getTitulo() + "' tiene un precio inválido"
                    );
                }

                log.info("Agregando item: {} - Cantidad: {}, Precio: {}",
                    libroPedido.getLibro().getTitulo(),
                    libroPedido.getCantidad(),
                    libroPedido.getLibro().getPrecio());

                PreferenceItemRequest item = PreferenceItemRequest.builder()
                        .title(libroPedido.getLibro().getTitulo())
                        .description("Autor: " + libroPedido.getLibro().getAutor())
                        .quantity(libroPedido.getCantidad())
                        .unitPrice(libroPedido.getLibro().getPrecio())
                        .currencyId("ARS") // Argentina Pesos
                        .build();
                items.add(item);
            }

            log.info("Total de items creados: {}", items.size());

            // Configurar URLs de retorno
            log.info("Configurando URLs de retorno - Success: {}, Failure: {}, Pending: {}",
                successUrl, failureUrl, pendingUrl);

            PreferenceBackUrlsRequest backUrls = PreferenceBackUrlsRequest.builder()
                    .success(successUrl)
                    .failure(failureUrl)
                    .pending(pendingUrl)
                    .build();

            // IMPORTANTE: Configurar la URL de notificación para webhooks
            log.info("Configurando URL de webhook: {}", webhookUrl);

            // Crear la preferencia
            PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                    .items(items)
                    .backUrls(backUrls)
                    .notificationUrl(webhookUrl) // URL donde MP enviará las notificaciones de pago
                    // .autoReturn("approved") // Removido: causa error si las URLs no están bien configuradas
                    .externalReference(String.valueOf(idPedido)) // Referencia al pedido
                    .statementDescriptor("TIENDA_LIBROS") // Nombre que aparece en el resumen de tarjeta
                    .build();

            log.info("Enviando preferencia a Mercado Pago...");

            // Crear cliente y enviar la preferencia a MP
            PreferenceClient client = new PreferenceClient();
            Preference preference = client.create(preferenceRequest);

            log.info("Preferencia creada exitosamente - ID: {}", preference.getId());

            // Crear registro de pago en la base de datos
            Pago pago = new Pago();
            pago.setPedido(pedido);
            pago.setMonto(pedido.getPrecioTotal());
            pago.setEstado(EstadoPago.PENDIENTE);
            pago.setPreferenceId(preference.getId());
            pago.setExternalReference(String.valueOf(idPedido));
            pago.setPayerEmail(pedido.getUsuario().getEmail());
            pagoRepository.save(pago);

            // Preparar respuesta
            MercadoPagoPreferenceResponseDTO response = new MercadoPagoPreferenceResponseDTO();
            response.setPreferenceId(preference.getId());
            response.setInitPoint(preference.getInitPoint());
            response.setSandboxInitPoint(preference.getSandboxInitPoint());

            return response;

        } catch (MPApiException e) {
            // Error específico de la API de Mercado Pago
            log.error("Error de API de Mercado Pago - Status: {}, Message: {}",
                e.getStatusCode(), e.getMessage(), e);

            // Imprimir detalles completos del error
            log.error("API Error Content: {}", e.getApiResponse() != null ? e.getApiResponse().getContent() : "No content");

            String errorDetails = "Sin detalles";
            if (e.getApiResponse() != null && e.getApiResponse().getContent() != null) {
                errorDetails = e.getApiResponse().getContent();
            }

            String errorMessage = String.format(
                "Error de API de Mercado Pago - Status: %d, Message: %s, Details: %s",
                e.getStatusCode(),
                e.getMessage(),
                errorDetails
            );
            throw new RuntimeException(errorMessage, e);
        } catch (MPException e) {
            // Error general del SDK de Mercado Pago
            log.error("Error del SDK de Mercado Pago: {}", e.getMessage(), e);
            throw new RuntimeException("Error al crear preferencia de pago en Mercado Pago: " + e.getMessage(), e);
        } catch (Exception e) {
            // Cualquier otro error
            log.error("Error inesperado al crear preferencia: {}", e.getMessage(), e);
            throw new RuntimeException("Error inesperado al crear preferencia: " + e.getMessage(), e);
        }
    }

    /**
     * Procesa las notificaciones de webhook de Mercado Pago
     */
    public void procesarWebhook(Long paymentId, String status, String externalReference) {
        log.info("Procesando webhook - PaymentId: {}, Status: {}, ExternalRef: {}",
            paymentId, status, externalReference);

        // Buscar el pago por externalReference (ID del pedido)
        Pago pago = pagoRepository.findByExternalReference(externalReference)
                .orElseThrow(() -> new NotFoundException(
                    "Pago con external reference " + externalReference + " no encontrado"
                ));

        log.info("Pago encontrado - ID: {}, Estado actual: {}", pago.getIdPago(), pago.getEstado());

        // Actualizar estado del pago según la notificación de MP
        EstadoPago nuevoEstado = mapearEstadoMercadoPago(status);
        pago.setEstado(nuevoEstado);
        pago.setPaymentId(paymentId);
        pago.setActualizadoEn(LocalDateTime.now());
        pagoRepository.save(pago);

        log.info("Estado de pago actualizado a: {}", nuevoEstado);

        // Si el pago fue aprobado, actualizar el estado del pedido
        if (nuevoEstado == EstadoPago.APROBADO) {
            log.info("Pago aprobado - Actualizando estado del pedido a CONFIRMADO");
            actualizarEstadoPedidoPagado(pago.getPedido());
        } else if (nuevoEstado == EstadoPago.RECHAZADO || nuevoEstado == EstadoPago.CANCELADO) {
            log.info("Pago rechazado/cancelado - Cancelando pedido");
            cancelarPedido(pago.getPedido());
        }
    }

    /**
     * Mapea el estado de Mercado Pago a nuestro enum EstadoPago
     */
    private EstadoPago mapearEstadoMercadoPago(String estadoMP) {
        return switch (estadoMP.toLowerCase()) {
            case "approved" -> EstadoPago.APROBADO;
            case "pending", "in_process" -> EstadoPago.PENDIENTE;
            case "authorized" -> EstadoPago.AUTORIZADO;
            case "in_mediation" -> EstadoPago.EN_MEDIACION;
            case "rejected" -> EstadoPago.RECHAZADO;
            case "cancelled" -> EstadoPago.CANCELADO;
            case "refunded" -> EstadoPago.REEMBOLSADO;
            case "charged_back" -> EstadoPago.CONTRACARGO;
            default -> EstadoPago.PENDIENTE;
        };
    }

    /**
     * Actualiza el estado del pedido cuando el pago es aprobado
     */
    private void actualizarEstadoPedidoPagado(Pedido pedido) {
        // Cambiar todos los LibroPedido a CONFIRMADO
        for (LibroPedido libroPedido : pedido.getListaLibros()) {
            if (libroPedido.getEstadoPedido() == EstadoPedido.PENDIENTE) {
                libroPedido.setEstadoPedido(EstadoPedido.CONFIRMADO);
            }
        }
        pedidoRepository.save(pedido);
    }

    /**
     * Cancela el pedido cuando el pago es rechazado o cancelado
     */
    private void cancelarPedido(Pedido pedido) {
        for (LibroPedido libroPedido : pedido.getListaLibros()) {
            if (libroPedido.getEstadoPedido() == EstadoPedido.PENDIENTE ||
                libroPedido.getEstadoPedido() == EstadoPedido.CONFIRMADO) {
                libroPedido.setEstadoPedido(EstadoPedido.CANCELADO);
            }
        }
        pedidoRepository.save(pedido);
    }

    /**
     * Procesa un webhook consultando el pago directamente desde MercadoPago
     * Similar al flujo de Node.js: recibe paymentId, consulta MP, actualiza BD
     */
    public void procesarWebhookPorPaymentId(Long paymentId) {
        log.info("========================================");
        log.info("PROCESANDO WEBHOOK - Payment ID: {}", paymentId);
        log.info("========================================");

        try {
            // Configurar access token
            MercadoPagoConfig.setAccessToken(accessToken);
            log.info("Access token configurado");

            // Consultar el pago en MercadoPago (igual que en Node.js con payment.get())
            PaymentClient paymentClient = new PaymentClient();
            log.info("Consultando pago en MercadoPago API...");
            Payment payment = paymentClient.get(paymentId);

            log.info("✓ Pago obtenido de MP");
            log.info("  - Payment ID: {}", payment.getId());
            log.info("  - Status: {}", payment.getStatus());
            log.info("  - External Reference: {}", payment.getExternalReference());
            log.info("  - Payment Method: {}", payment.getPaymentMethodId());
            log.info("  - Payment Type: {}", payment.getPaymentTypeId());

            // Validar que tenga external_reference
            String externalReference = payment.getExternalReference();
            if (externalReference == null || externalReference.isEmpty()) {
                log.error("✗ PAGO SIN EXTERNAL_REFERENCE - No se puede asociar con pedido");
                log.error("  Payment ID: {}", paymentId);
                return; // No arrojar excepción, solo registrar y continuar
            }

            log.info("Buscando pago en BD con external_reference: {}", externalReference);

            // Buscar el pago en nuestra BD por external reference (orderId)
            Pago pago = pagoRepository.findByExternalReference(externalReference)
                    .orElse(null);

            if (pago == null) {
                log.error("✗ PAGO NO ENCONTRADO EN BD");
                log.error("  External Reference buscado: {}", externalReference);
                log.error("  Payment ID de MP: {}", paymentId);

                // Listar todos los pagos pendientes para debug
                log.info("Pagos PENDIENTES en BD:");
                pagoRepository.findAll().forEach(p -> {
                    if (p.getEstado() == EstadoPago.PENDIENTE) {
                        log.info("  - ID: {}, ExternalRef: '{}', PreferenceId: {}",
                            p.getIdPago(), p.getExternalReference(), p.getPreferenceId());
                    }
                });

                return; // No arrojar excepción, solo registrar
            }

            log.info("✓ Pago encontrado en BD");
            log.info("  - ID Pago: {}", pago.getIdPago());
            log.info("  - Estado actual: {}", pago.getEstado());
            log.info("  - ID Pedido: {}", pago.getPedido().getIdPedido());

            // Mapear estado de MercadoPago a nuestro enum
            EstadoPago nuevoEstado = mapearEstadoMercadoPago(payment.getStatus());
            log.info("Estado mapeado: {} -> {}", payment.getStatus(), nuevoEstado);

            // Actualizar el pago con la información de MercadoPago
            pago.setEstado(nuevoEstado);
            pago.setPaymentId(payment.getId());
            pago.setMetodoPago(payment.getPaymentMethodId());
            pago.setPaymentType(payment.getPaymentTypeId());
            pago.setActualizadoEn(LocalDateTime.now());

            log.info("Guardando pago actualizado...");
            pagoRepository.save(pago);
            log.info("✓ Pago guardado en BD con estado: {}", nuevoEstado);

            // Actualizar estado del pedido según el resultado
            if (nuevoEstado == EstadoPago.APROBADO) {
                log.info(">>> PAGO APROBADO - Actualizando pedido a CONFIRMADO");
                actualizarEstadoPedidoPagado(pago.getPedido());
                log.info("✓ Pedido confirmado exitosamente");
            } else if (nuevoEstado == EstadoPago.RECHAZADO || nuevoEstado == EstadoPago.CANCELADO) {
                log.info(">>> PAGO RECHAZADO/CANCELADO - Cancelando pedido");
                cancelarPedido(pago.getPedido());
                log.info("✓ Pedido cancelado");
            } else {
                log.info("Estado del pago: {} - No se modifica el pedido", nuevoEstado);
            }

            log.info("========================================");
            log.info("✓✓✓ WEBHOOK PROCESADO EXITOSAMENTE");
            log.info("========================================");

        } catch (MPApiException e) {
            log.error("========================================");
            log.error("✗✗✗ ERROR DE API DE MERCADOPAGO");
            log.error("========================================");
            log.error("Status Code: {}", e.getStatusCode());
            log.error("Message: {}", e.getMessage());
            log.error("API Response: {}", e.getApiResponse() != null ? e.getApiResponse().getContent() : "N/A");
            log.error("========================================");
            // NO arrojar excepción - devolver 200 OK siempre
        } catch (MPException e) {
            log.error("========================================");
            log.error("✗✗✗ ERROR DEL SDK DE MERCADOPAGO");
            log.error("========================================");
            log.error("Message: {}", e.getMessage(), e);
            log.error("========================================");
            // NO arrojar excepción - devolver 200 OK siempre
        } catch (Exception e) {
            log.error("========================================");
            log.error("✗✗✗ ERROR INESPERADO AL PROCESAR WEBHOOK");
            log.error("========================================");
            log.error("Tipo: {}", e.getClass().getName());
            log.error("Message: {}", e.getMessage(), e);
            log.error("========================================");
            // NO arrojar excepción - devolver 200 OK siempre
        }
    }

    /**
     * Consulta el estado de un pago en MercadoPago usando el ID del pedido
     * Busca todos los pagos con ese external_reference en MercadoPago y actualiza la BD
     */
    public ar.com.tienda_libro.dto.response.PagoResponseDTO consultarYActualizarPagoPorPedido(Long idPedido) {
        log.info("Consultando estado de pago para pedido ID: {}", idPedido);

        try {
            // Configurar access token
            MercadoPagoConfig.setAccessToken(accessToken);

            // Buscar el pago en nuestra BD por pedido
            Pago pago = pagoRepository.findByPedidoIdPedido(idPedido)
                    .orElseThrow(() -> new NotFoundException(
                        "No existe un pago asociado al pedido " + idPedido
                    ));

            log.info("Pago encontrado en BD - ID: {}, Estado actual: {}, PaymentId: {}, PreferenceId: {}",
                pago.getIdPago(), pago.getEstado(), pago.getPaymentId(), pago.getPreferenceId());

            // Si ya tenemos paymentId, consultar ese pago directamente
            if (pago.getPaymentId() != null) {
                log.info("Ya existe paymentId, consultando: {}", pago.getPaymentId());
                procesarWebhookPorPaymentId(pago.getPaymentId());
            } else {
                log.warn("No existe paymentId asociado. El usuario aún no completó el pago en MercadoPago.");
                log.info("PreferenceId: {} - El usuario debe acceder a la URL de pago", pago.getPreferenceId());
            }

            // Recargar el pago actualizado
            pago = pagoRepository.findById(pago.getIdPago())
                    .orElseThrow(() -> new NotFoundException("Pago no encontrado"));

            // Convertir a DTO y retornar
            return pagoMapper.toResponseDTO(pago);

        } catch (Exception e) {
            log.error("Error al consultar pago por pedido: {}", e.getMessage(), e);
            throw new RuntimeException("Error al consultar pago: " + e.getMessage(), e);
        }
    }
}
