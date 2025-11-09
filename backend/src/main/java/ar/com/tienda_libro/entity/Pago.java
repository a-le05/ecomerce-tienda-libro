package ar.com.tienda_libro.entity;

import ar.com.tienda_libro.enums.EstadoPago;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pago")
    private Long idPago;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "metodo_pago", length = 50)
    private String metodoPago;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", length = 30, nullable = false)
    private EstadoPago estado;

    // Campos específicos de Mercado Pago
    @Column(name = "payment_id", unique = true)
    private Long paymentId; // ID del pago en Mercado Pago

    @Column(name = "preference_id", length = 100)
    private String preferenceId; // ID de la preferencia creada en MP

    @Column(name = "merchant_order_id")
    private Long merchantOrderId; // ID de la orden del comerciante en MP

    @Column(name = "external_reference", length = 100)
    private String externalReference; // Referencia externa (ej: ID del pedido)

    @Column(name = "payment_type", length = 50)
    private String paymentType; // Tipo de pago (credit_card, debit_card, ticket, etc.)

    @Column(name = "installments")
    private Integer installments; // Número de cuotas

    @Column(name = "payer_email", length = 100)
    private String payerEmail; // Email del pagador

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @CreationTimestamp
    @Column(name = "creado_en", updatable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    // equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pago pago = (Pago) o;
        return Objects.equals(idPago, pago.idPago) &&
                Objects.equals(paymentId, pago.paymentId);
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(idPago, paymentId);
    }

    // canEqual()
    public boolean canEqual(Object other) {
        return other instanceof Pago;
    }
}
