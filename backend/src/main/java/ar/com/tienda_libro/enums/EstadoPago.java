package ar.com.tienda_libro.enums;

public enum EstadoPago {
    // Estados de Mercado Pago
    PENDIENTE,        // Pago pendiente (pending)
    APROBADO,         // Pago aprobado (approved)
    AUTORIZADO,       // Pago autorizado pero no capturado (authorized)
    EN_PROCESO,       // Pago en proceso (in_process)
    EN_MEDIACION,     // Pago en mediación (in_mediation)
    RECHAZADO,        // Pago rechazado (rejected)
    CANCELADO,        // Pago cancelado (cancelled)
    REEMBOLSADO,      // Pago reembolsado (refunded)
    CONTRACARGO       // Contracargo (charged_back)
}
