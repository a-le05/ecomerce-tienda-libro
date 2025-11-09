package ar.com.tienda_libro.service.interf;

import ar.com.tienda_libro.dto.request.PedidoRequestDTO;
import ar.com.tienda_libro.dto.response.PedidoResponseDTO;
import ar.com.tienda_libro.enums.EstadoPedido;

import java.util.List;

public interface PedidoService {

    // Crear un nuevo pedido
    PedidoResponseDTO crearPedido(PedidoRequestDTO pedidoRequest);

    // Obtener un pedido por ID
    PedidoResponseDTO obtenerPedidoPorId(Long id);

    // Listar todos los pedidos (solo ADMIN)
    List<PedidoResponseDTO> listarTodosPedidos();

    // Listar pedidos de un usuario específico
    List<PedidoResponseDTO> listarPedidosPorUsuario(Long idUsuario);

    // Obtener historial de pedidos del usuario autenticado
    List<PedidoResponseDTO> obtenerMisPedidos();

    // Actualizar estado de un pedido (ADMIN)
    PedidoResponseDTO actualizarEstadoPedido(Long idPedido, Long idLibroPedido, EstadoPedido nuevoEstado);

    // Cancelar un pedido
    PedidoResponseDTO cancelarPedido(Long idPedido);

    // Eliminar un pedido (solo ADMIN)
    void eliminarPedido(Long id);
}
