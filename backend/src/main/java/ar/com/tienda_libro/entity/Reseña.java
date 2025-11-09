package ar.com.tienda_libro.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity
@Table(name = "resenas")
public class Reseña {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_resena")
    private Long idReseña;

    @Column(name = "contenido", nullable = false, length = 500)
    private String contenido;

    @Column(name = "calificacion", nullable = false)
    private int calificacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_libro")
    private Libro libro;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Column(name = "creado_en", updatable = false)
    private final LocalDateTime creadoEn = LocalDateTime.now();

    // equals()
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reseña reseña = (Reseña) o;
        return calificacion == reseña.calificacion &&
                Objects.equals(idReseña, reseña.idReseña) &&
                Objects.equals(contenido, reseña.contenido) &&
                Objects.equals(libro, reseña.libro) &&
                Objects.equals(usuario, reseña.usuario);
    }

    // hashCode()
    @Override
    public int hashCode() {
        return Objects.hash(idReseña, contenido, calificacion, libro, usuario);
    }

    // canEqual()
    public boolean canEqual(Object other) {
        return other instanceof Reseña;
    }
}
