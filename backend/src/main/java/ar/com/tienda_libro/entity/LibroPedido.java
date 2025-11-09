package ar.com.tienda_libro.entity;

import ar.com.tienda_libro.enums.EstadoPedido;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "libro_pedidos")
public class LibroPedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_libro_pedido")
    private Long idLibroPedido;

    @Column(nullable = false, name = "cantidad")
    private int cantidad;

    @Enumerated(EnumType.STRING) //Guarda el nombre del enum no el numero.
    @Column(name = "estado_pedido", length = 30)
    private EstadoPedido estadoPedido;

    //Relaciones entre entidades

    @ManyToOne(fetch = FetchType.LAZY) //Evita sobrecargar consultas
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pedido")
    private Pedido pedido;

    @CreationTimestamp //Se asigna automáticamente al crear el registro (sin necesidad de LocalDateTime.now() manual)
    @Column(name = "creado_en", updatable = false) //updatable = false evita que se cambie al actualizar.
    private LocalDateTime creadoEn;


    //Metodos de comparacion
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LibroPedido)) return false;
        LibroPedido that = (LibroPedido) o;

        // Si ambos tienen ID (ya persistidos)
        if (this.idLibroPedido != null && that.idLibroPedido != null) {
            return this.idLibroPedido.equals(that.idLibroPedido);
        }

        // Comparación por contenido si aún no se persistió
        return cantidad == that.cantidad &&
                Objects.equals(libro, that.libro) &&
                Objects.equals(usuario, that.usuario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idLibroPedido != null ? idLibroPedido : libro, cantidad, usuario);
    }

    public boolean canEqual(Object other) {
        return other instanceof LibroPedido;
    }
}
