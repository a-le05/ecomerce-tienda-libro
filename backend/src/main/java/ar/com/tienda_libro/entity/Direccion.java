package ar.com.tienda_libro.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "direcciones") //Definomos el nombre de la tabla en base datos
public class Direccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_direccion")
    private Long idDireccion;

    //name = "calle" -> indicamos el nombre de la columna en la base de datos
    //length=100 -> El tipo sera VARCHAR(100), longitud maxima 100
    @Column(name = "calle", length = 100)
    private String calle;

    @Column(name = "ciudad", length = 100)
    private String ciudad;

    @Column(name = "estado", length = 100)
    private String estado;

    @Column(name = "codigo_postal", length = 20)
    private String codigoPostal;

    @Column(name = "pais", length = 100)
    private String pais;

    //Relacion con Usuario
    //Esto indica que muchas direcciones pueden pertenecer a un mismo usuario
    //@ManyToOne -> Muchos registros de esta tabla ""direcciones" se asocian a uno de la tabla usuarios
    @ManyToOne(fetch = FetchType.LAZY) //fetch = FetchType.LAZY -> Solo se carga la informacion del usuario cuando se necesita (optimizacion)
    @JoinColumn(name = "id_usuario") //define el nombre de la columna FK(foreign Key) que enlaza con la tabla usuarios
    private Usuario usuario;

    @CreationTimestamp //Esta notacion automaticamente guarda la fecha y la hora en que se crea el registro
    @Column(name = "creado_en")
    private LocalDateTime creadoEn;

}
