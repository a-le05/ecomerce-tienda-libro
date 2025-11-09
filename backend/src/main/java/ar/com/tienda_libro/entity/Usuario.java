package ar.com.tienda_libro.entity;

import ar.com.tienda_libro.enums.UsuarioRol;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data //Generamos Getter, Setters, toString()
@Entity //Indicamos que esta clase será una tabla de base de datos
@Table(name = "usuarios")  //Definimos el nombre de la tabla
@Builder //permite construir objetos con el patrón builder
@AllArgsConstructor //Cosntructor con argumentos
@NoArgsConstructor //Constructor sin argumentos
public class Usuario {

    @Id  //Identificador unico
    @GeneratedValue(strategy = GenerationType.IDENTITY) //El valor se genera automaticamente en bd
    @Column(name = "id_usuario") //Nombre del campo en bd
    private Long idUsuario;

    @NotBlank(message = "El nombre es obligatorio") //Devuelve un error 400. No puede quedar vacio, campo obligatorio
    @Column(name = "nombre") //Nombre del campo en bd
    private String nombre;

    @NotBlank(message = "Se requiere correo electrónico") //No puede quedar vacio
    @Column(unique = true, name = "email") //Impide que haya dos usuarios con el mismo email, nombre del campo
    private String email;

    @NotBlank(message = "Se requiere de contraseña") //Contraseña obligatoria del usuario
    @Column(name = "contrasena") //Nombre del campo
    private String contrasena;

    @NotBlank(message = "Se requiere número de teléfono") //No puede quedar vacio
    @Column(name = "numero_telefono") //Nombre del campo
    private String numeroTelefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 30)
    private UsuarioRol rol;

    //Un usuario puede tener muchos pedidos
    //fetch = FetchType.LAZY -> no carga los pedidos automaticamente al traer el usuario(optimiza rendimiento)
    //cascade = CascadeType.ALL -> si se elimina el usuario, tambien se eliminan sis pedidos
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<LibroPedido> listaPedido;

    //Cada usuario tiene una direccion
    //mapperBY = usuario -> indica que en la tabla direccion hay un campo con id_usuario que referencia a usuaro
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "usuario")
    private Direccion direccion;

    //Fecha de creacion del usuario, se asigna automaticamente la momento de crear la identidad.
    //final -> No se peude cambiar una vez creada
    @Column(name = "creado_en")
    private final LocalDateTime creadoEn = LocalDateTime.now();

    // Metodos de comparacion y utilidad
    //Define cuando dos objetos Usuarios se consideran iguales
    @Override
    public boolean equals(Object o){
        if(this == o) return true; //Si es el mismo objeto en memoria
        if(o == null || getClass() != o.getClass()) return false; //Si no es la misma clase
        Usuario usuario = (Usuario) o;
        return Objects.equals(idUsuario, usuario.idUsuario) &&
                Objects.equals(nombre, usuario.nombre) &&
                Objects.equals(email, usuario.email) &&
                Objects.equals(contrasena, usuario.contrasena) &&
                Objects.equals(numeroTelefono, usuario.numeroTelefono) &&
                rol == usuario.rol &&
                Objects.equals(listaPedido, usuario.listaPedido) &&
                Objects.equals(direccion, usuario.direccion);
    }

    //Retorna un numero unico basado en los mismos campos que equals()
    //Importante para que las colecciones como HashSet o HashMap funcionen correctamente
    @Override
    public int hashCode(){
        return Objects.hash(idUsuario, nombre, email, contrasena, numeroTelefono, rol, listaPedido, direccion);
    }

    //Se usa como buena practica cuando una clase puede tener subclases
    //indica que solo se debe comparar con isntancias de Usuario
    public boolean canEqual(Object other) {
        return other instanceof Usuario;
    }
}
