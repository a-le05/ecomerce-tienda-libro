package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseñaResponseDTO {

    private Long idReseña;
    private String contenido;
    private Integer calificacion;
    private LocalDateTime creadoEn;

    //Datos relacionado
    private Long idLibro;
    private String tituloLibro;

    private Long idUsuario;
    private String nombreUsuario;
}
