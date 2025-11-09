package ar.com.tienda_libro.mapper;

import ar.com.tienda_libro.dto.response.LibroPedidoResponseDTO;
import ar.com.tienda_libro.dto.response.PedidoResponseDTO;
import ar.com.tienda_libro.dto.response.UsuarioResponseDTO;
import ar.com.tienda_libro.entity.LibroPedido;
import ar.com.tienda_libro.entity.Pedido;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    // Convierte Pedido entity a PedidoResponseDTO
    public PedidoResponseDTO toResponseDTO(Pedido pedido) {
        if (pedido == null) return null;

        PedidoResponseDTO dto = new PedidoResponseDTO();
        dto.setIdPedido(pedido.getIdPedido());
        dto.setPrecioTotal(pedido.getPrecioTotal());
        dto.setCreadoEn(pedido.getCreadoEn());

        // Mapear usuario (información básica)
        if (pedido.getUsuario() != null) {
            UsuarioResponseDTO usuarioDTO = new UsuarioResponseDTO();
            usuarioDTO.setIdUsuario(pedido.getUsuario().getIdUsuario());
            usuarioDTO.setNombre(pedido.getUsuario().getNombre());
            usuarioDTO.setEmail(pedido.getUsuario().getEmail());
            usuarioDTO.setNumeroTelefonico(pedido.getUsuario().getNumeroTelefono());
            dto.setUsuario(usuarioDTO);
        }

        // Mapear lista de libros del pedido
        if (pedido.getListaLibros() != null) {
            List<LibroPedidoResponseDTO> librosDTO = pedido.getListaLibros()
                    .stream()
                    .map(this::toLibroPedidoResponseDTO)
                    .collect(Collectors.toList());
            dto.setLibros(librosDTO);
        }

        return dto;
    }

    // Convierte LibroPedido entity a LibroPedidoResponseDTO
    public LibroPedidoResponseDTO toLibroPedidoResponseDTO(LibroPedido libroPedido) {
        if (libroPedido == null) return null;

        LibroPedidoResponseDTO dto = new LibroPedidoResponseDTO();
        dto.setIdLibroPedido(libroPedido.getIdLibroPedido());
        dto.setCantidad(libroPedido.getCantidad());
        dto.setEstadoPedido(libroPedido.getEstadoPedido());

        // Información del libro
        if (libroPedido.getLibro() != null) {
            dto.setIdLibro(libroPedido.getLibro().getIdLibro());
            dto.setTituloLibro(libroPedido.getLibro().getTitulo());
            // Calcular precio total (precio unitario * cantidad)
            dto.setPrecioTotal(libroPedido.getLibro().getPrecio()
                    .multiply(java.math.BigDecimal.valueOf(libroPedido.getCantidad())));
        }

        // Información del usuario
        if (libroPedido.getUsuario() != null) {
            dto.setIdUsuario(libroPedido.getUsuario().getIdUsuario());
            dto.setNombreUsuario(libroPedido.getUsuario().getNombre());
        }

        return dto;
    }
}
