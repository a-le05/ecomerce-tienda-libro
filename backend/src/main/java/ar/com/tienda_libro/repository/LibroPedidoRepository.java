package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.LibroPedido;
import ar.com.tienda_libro.enums.EstadoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroPedidoRepository extends JpaRepository<LibroPedido, Long> {

    // Buscar todos los libros pedidos por un usuario
    List<LibroPedido> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar todos los libros de un pedido específico
    List<LibroPedido> findByPedidoIdPedido(Long idPedido);

    // Buscar por estado del pedido
    List<LibroPedido> findByEstadoPedido(EstadoPedido estadoPedido);

    // Buscar libros pedidos por usuario y estado
    List<LibroPedido> findByUsuarioIdUsuarioAndEstadoPedido(Long idUsuario, EstadoPedido estadoPedido);
}
