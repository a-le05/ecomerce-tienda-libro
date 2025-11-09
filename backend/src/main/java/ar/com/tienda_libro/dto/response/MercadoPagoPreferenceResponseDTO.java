package ar.com.tienda_libro.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MercadoPagoPreferenceResponseDTO {

    private String preferenceId;
    private String initPoint; // URL para redireccionar al usuario al checkout de MP
    private String sandboxInitPoint; // URL para modo sandbox
}
