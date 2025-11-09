package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.Pago;
import ar.com.tienda_libro.enums.EstadoPago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // Buscar pago por el ID del pedido asociado
    Optional<Pago> findByPedidoIdPedido(Long idPedido);

    // Verificar si ya existe un pago para un pedido
    boolean existsByPedidoIdPedido(Long idPedido);

    // Métodos para Mercado Pago

    // Buscar pago por ID de pago de Mercado Pago
    Optional<Pago> findByPaymentId(Long paymentId);

    // Buscar pago por ID de preferencia
    Optional<Pago> findByPreferenceId(String preferenceId);

    // Buscar pago por referencia externa (ej: ID del pedido)
    Optional<Pago> findByExternalReference(String externalReference);

    // Buscar pagos por estado
    List<Pago> findByEstado(EstadoPago estado);

    // Buscar pagos por email del pagador
    List<Pago> findByPayerEmail(String payerEmail);
}
