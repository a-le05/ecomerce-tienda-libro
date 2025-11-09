package ar.com.tienda_libro.repository;

import ar.com.tienda_libro.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> { //hereda métodos CRUD (save, findById, findAll, delete, etc.)

    Optional<Usuario> findByEmail(String email); //permite buscar un usuario por correo

    boolean existsByEmail(String email); //te ayuda a validar registros duplicados antes de crear un nuevo usuario
}
