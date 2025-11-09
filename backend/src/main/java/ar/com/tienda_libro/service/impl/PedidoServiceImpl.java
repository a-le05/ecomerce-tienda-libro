package ar.com.tienda_libro.service.impl;

import ar.com.tienda_libro.dto.request.LibroPedidoRequestDTO;
import ar.com.tienda_libro.dto.request.PedidoRequestDTO;
import ar.com.tienda_libro.dto.response.PedidoResponseDTO;
import ar.com.tienda_libro.entity.Libro;
import ar.com.tienda_libro.entity.LibroPedido;
import ar.com.tienda_libro.entity.Pedido;
import ar.com.tienda_libro.entity.Usuario;
import ar.com.tienda_libro.enums.EstadoPedido;
import ar.com.tienda_libro.exceptions.NotFoundException;
import ar.com.tienda_libro.mapper.PedidoMapper;
import ar.com.tienda_libro.repository.LibroPedidoRepository;
import ar.com.tienda_libro.repository.LibroRepository;
import ar.com.tienda_libro.repository.PedidoRepository;
import ar.com.tienda_libro.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PedidoServiceImpl implements ar.com.tienda_libro.service.interf.PedidoService {

    private final PedidoRepository pedidoRepository;
    private final LibroPedidoRepository libroPedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final LibroRepository libroRepository;
    private final PedidoMapper pedidoMapper;

    @Override
    public PedidoResponseDTO crearPedido(PedidoRequestDTO pedidoRequest) {
        // Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(pedidoRequest.getIdUsuario())
                .orElseThrow(() -> new NotFoundException(
                        "Usuario con ID " + pedidoRequest.getIdUsuario() + " no encontrado"
                ));

        // Validar que la lista de libros no esté vacía
        if (pedidoRequest.getLibros() == null || pedidoRequest.getLibros().isEmpty()) {
            throw new IllegalArgumentException("El pedido debe contener al menos un libro");
        }

        // Crear el pedido
        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setListaLibros(new ArrayList<>());

        BigDecimal precioTotal = BigDecimal.ZERO;

        // Procesar cada libro del pedido
        for (LibroPedidoRequestDTO libroDTO : pedidoRequest.getLibros()) {
            // Buscar el libro
            Libro libro = libroRepository.findById(libroDTO.getIdLibro())
                    .orElseThrow(() -> new NotFoundException(
                            "Libro con ID " + libroDTO.getIdLibro() + " no encontrado"
                    ));

            // Validar stock disponible
            if (libro.getStock() < libroDTO.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el libro '" + libro.getTitulo() +
                        "'. Disponible: " + libro.getStock() + ", Solicitado: " + libroDTO.getCantidad()
                );
            }

            // Descontar stock
            libro.setStock(libro.getStock() - libroDTO.getCantidad());
            libroRepository.save(libro);

            // Crear LibroPedido
            LibroPedido libroPedido = new LibroPedido();
            libroPedido.setLibro(libro);
            libroPedido.setUsuario(usuario);
            libroPedido.setPedido(pedido);
            libroPedido.setCantidad(libroDTO.getCantidad());
            libroPedido.setEstadoPedido(EstadoPedido.PENDIENTE);

            // Agregar a la lista del pedido
            pedido.getListaLibros().add(libroPedido);

            // Calcular precio total
            BigDecimal precioLibro = libro.getPrecio().multiply(BigDecimal.valueOf(libroDTO.getCantidad()));
            precioTotal = precioTotal.add(precioLibro);
        }

        // Establecer precio total
        pedido.setPrecioTotal(precioTotal);

        // Guardar el pedido (cascade guardará también los LibroPedido)
        pedido = pedidoRepository.save(pedido);

        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO obtenerPedidoPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + id + " no encontrado"));
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public List<PedidoResponseDTO> listarTodosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> listarPedidosPorUsuario(Long idUsuario) {
        // Verificar que el usuario exista
        if (!usuarioRepository.existsById(idUsuario)) {
            throw new NotFoundException("Usuario con ID " + idUsuario + " no encontrado");
        }

        return pedidoRepository.findByUsuarioIdUsuario(idUsuario)
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PedidoResponseDTO> obtenerMisPedidos() {
        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));

        return pedidoRepository.findByUsuarioIdUsuario(usuario.getIdUsuario())
                .stream()
                .map(pedidoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PedidoResponseDTO actualizarEstadoPedido(Long idPedido, Long idLibroPedido, EstadoPedido nuevoEstado) {
        // Buscar el pedido
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        // Buscar el LibroPedido específico
        LibroPedido libroPedido = libroPedidoRepository.findById(idLibroPedido)
                .orElseThrow(() -> new NotFoundException("LibroPedido con ID " + idLibroPedido + " no encontrado"));

        // Verificar que el LibroPedido pertenezca al Pedido
        if (!libroPedido.getPedido().getIdPedido().equals(idPedido)) {
            throw new IllegalArgumentException("El LibroPedido no pertenece al Pedido especificado");
        }

        // Actualizar estado
        libroPedido.setEstadoPedido(nuevoEstado);
        libroPedidoRepository.save(libroPedido);

        // Devolver el pedido actualizado
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    public PedidoResponseDTO cancelarPedido(Long idPedido) {
        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + idPedido + " no encontrado"));

        // Cancelar todos los LibroPedido del pedido y restaurar stock
        for (LibroPedido libroPedido : pedido.getListaLibros()) {
            if (libroPedido.getEstadoPedido() == EstadoPedido.PENDIENTE ||
                libroPedido.getEstadoPedido() == EstadoPedido.CONFIRMADO) {

                // Restaurar stock del libro
                Libro libro = libroPedido.getLibro();
                libro.setStock(libro.getStock() + libroPedido.getCantidad());
                libroRepository.save(libro);

                // Cambiar estado a cancelado
                libroPedido.setEstadoPedido(EstadoPedido.CANCELADO);
            } else {
                throw new IllegalArgumentException(
                        "No se puede cancelar el pedido. Algunos items ya están en proceso de envío o entregados."
                );
            }
        }

        pedido = pedidoRepository.save(pedido);
        return pedidoMapper.toResponseDTO(pedido);
    }

    @Override
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public void eliminarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido con ID " + id + " no encontrado"));

        pedidoRepository.delete(pedido);
    }
}
