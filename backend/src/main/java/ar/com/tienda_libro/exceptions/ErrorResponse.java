package ar.com.tienda_libro.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorResponse { //Es el DTO que devuelve el API al cliente cuando ocurre un error
    private String message; //el mensaje que pasaste a la excepción.
    private int statusCode; //el código HTTP que corresponde.
    private LocalDateTime timestamp; //la hora en que ocurrió el error.
    private String errorDetails; //un texto adicional para explicar el tipo de error
}