package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DireccionResponseDTO {//Este DTO lo devolves al cliente cuando colsultas datos

    private Long idDireccion;
    private String calle;
    private String ciudad;
    private String estado;
    private String codigoPostal;
    private String pais;
    private LocalDateTime creadoEn;
}
