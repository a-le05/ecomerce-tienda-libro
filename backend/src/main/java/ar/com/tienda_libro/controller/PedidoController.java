package ar.com.tienda_libro.controller;

import ar.com.tienda_libro.dto.request.PedidoRequestDTO;
import ar.com.tienda_libro.dto.response.PedidoResponseDTO;
import ar.com.tienda_libro.enums.EstadoPedido;
import ar.com.tienda_libro.service.interf.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    // ================= CREAR PEDIDO =================
    @PostMapping("/crear")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> crearPedido(@RequestBody PedidoRequestDTO pedidoRequest) {
        PedidoResponseDTO pedidoCreado = pedidoService.crearPedido(pedidoRequest);
        return ResponseEntity.ok(pedidoCreado);
    }

    // ================= OBTENER PEDIDO POR ID =================
    @GetMapping("/obtener/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    // ================= LISTAR TODOS LOS PEDIDOS (SOLO ADMIN) =================
    @GetMapping("/listar")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PedidoResponseDTO>> listarTodosPedidos() {
        return ResponseEntity.ok(pedidoService.listarTodosPedidos());
    }

    // ================= LISTAR PEDIDOS POR USUARIO (ADMIN) =================
    @GetMapping("/usuario/{idUsuario}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<PedidoResponseDTO>> listarPedidosPorUsuario(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorUsuario(idUsuario));
    }

    // ================= OBTENER MIS PEDIDOS (USUARIO AUTENTICADO) =================
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerMisPedidos() {
        return ResponseEntity.ok(pedidoService.obtenerMisPedidos());
    }

    // ================= ACTUALIZAR ESTADO DE UN PEDIDO (ADMIN) =================
    @PutMapping("/actualizar-estado/{idPedido}/libro-pedido/{idLibroPedido}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<PedidoResponseDTO> actualizarEstadoPedido(
            @PathVariable Long idPedido,
            @PathVariable Long idLibroPedido,
            @RequestParam EstadoPedido nuevoEstado) {
        PedidoResponseDTO pedidoActualizado = pedidoService.actualizarEstadoPedido(idPedido, idLibroPedido, nuevoEstado);
        return ResponseEntity.ok(pedidoActualizado);
    }

    // ================= CANCELAR PEDIDO =================
    @PutMapping("/cancelar/{idPedido}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'USUARIO')")
    public ResponseEntity<PedidoResponseDTO> cancelarPedido(@PathVariable Long idPedido) {
        PedidoResponseDTO pedidoCancelado = pedidoService.cancelarPedido(idPedido);
        return ResponseEntity.ok(pedidoCancelado);
    }

    // ================= ELIMINAR PEDIDO (SOLO ADMIN) =================
    @DeleteMapping("/eliminar/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<String> eliminarPedido(@PathVariable Long id) {
        pedidoService.eliminarPedido(id);
        return ResponseEntity.ok("Pedido eliminado correctamente");
    }
}
