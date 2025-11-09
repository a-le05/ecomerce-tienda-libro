package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.Reseña;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReseñaRepository extends JpaRepository<Reseña, Long> {

    // Buscar todas las reseñas de un libro
    List<Reseña> findByLibroIdLibro(Long idLibro);

    // Buscar todas las reseñas de un usuario
    List<Reseña> findByUsuarioIdUsuario(Long idUsuario);

    // Buscar reseñas de un libro con cierta calificación mínima
    List<Reseña> findByLibroIdLibroAndCalificacionGreaterThanEqual(Long idLibro, int calificacion);

    // Buscar reseñas de un usuario para un libro específico
    List<Reseña> findByUsuarioIdUsuarioAndLibroIdLibro(Long idUsuario, Long idLibro);
}
