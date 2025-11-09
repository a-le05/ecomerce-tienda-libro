package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    // Buscar todos los pedidos de un usuario
    List<Pedido> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar pedidos por precio mayor a cierto monto
    List<Pedido> findByPrecioTotalGreaterThan(BigDecimal monto);

    // Buscar pedidos por precio menor a cierto monto
    List<Pedido> findByPrecioTotalLessThan(BigDecimal monto);
}
