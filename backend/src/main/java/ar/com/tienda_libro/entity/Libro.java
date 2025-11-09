package ar.com.tienda_libro.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Entity //Esta clase se guarda como una tabla en la base de datos
@Table(name = "libros") //Nombre de la tabla
public class Libro {

    @Id //Clase primaria
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Autoincremental en base de datos
    @Column(name = "id_libro")
    private Long idLibro;

    @Column(name = "titulo", nullable = false, length = 150) //Nombre de columna, no puede ser nulo y tiene longitud 150
    private String titulo;

    @Column(name = "autor", length = 100)
    private String autor;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @Column(name = "precio", precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "stock", nullable = false)
    private Integer stock = 0; // Stock disponible del libro

    //Relacion con la categoria, muchos libros pueden pertenecer a una misma catetgoria
    @ManyToOne(fetch = FetchType.LAZY) //La categorai se carga solo cuando se la usa(mejor rendimiento)
    @JoinColumn(name = "id_categoria") //Crea la colimna foranea id_categoria en la tabla libros
    private Categoria categoria;

    @CreationTimestamp //Guarda automaticamente la fecha/hora de creacion del registro
    @Column(name = "creado_en", updatable = false) //updatable = false evita que se cambie al actualizar.
    private LocalDateTime creadoEn;


    // Metodos de comparacion
    //Si ambos libros tienen ID, compara por ID (entidad ya guarda) si no, compara por titulo y autor
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Libro)) return false;
        Libro libro = (Libro) o;

        if (this.idLibro != null && libro.idLibro != null) {
            return this.idLibro.equals(libro.idLibro);
        }

        // Comparación por contenido si aún no está persistido
        return Objects.equals(titulo, libro.titulo) &&
                Objects.equals(autor, libro.autor);
    }

    //Usa idLibros si existe, si no usa titulo y autor
    @Override
    public int hashCode() {
        return Objects.hash(idLibro != null ? idLibro : titulo, autor);
    }

    //una práctica recomendada para herencias y frameworks como Hibernate
    public boolean canEqual(Object other) {
        return other instanceof Libro;
    }
}
