package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LibroResponseDTO {

    private Long idLibro;
    private String titulo;
    private String autor;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precio;
    private Integer stock;
    private LocalDateTime creadoEn;
    protected CategoriaResponseDTO categoria;
}
