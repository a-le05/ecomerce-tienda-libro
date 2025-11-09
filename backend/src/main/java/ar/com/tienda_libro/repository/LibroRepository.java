package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {

    // Buscar libro por título
    Optional<Libro> findByTitulo(String titulo);

    // Buscar libros por autor
    List<Libro> findByAutor(String autor);

    // Buscar libros por categoría
    List<Libro> findByCategoriaIdCategoria(Long idCategoria);

    // Verificar si ya existe un libro por título y autor
    boolean existsByTituloAndAutor(String titulo, String autor);

    // Buscar libros por texto en título o autor (optimizado con query)
    @Query("SELECT l FROM Libro l WHERE " +
           "LOWER(l.titulo) LIKE LOWER(CONCAT('%', :texto, '%')) OR " +
           "LOWER(l.autor) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<Libro> buscarPorTexto(@Param("texto") String texto);
}
