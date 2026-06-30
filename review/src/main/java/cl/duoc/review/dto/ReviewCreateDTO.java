package cl.duoc.review.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ReviewCreateDTO {

    @NotNull(message = "El id del destino no puede ser nulo")
    private Long destinationId;

    @NotBlank(message = "El username no puede estar vacío")
    private String username;

    @NotNull(message = "La nota es obligatoria")
    @Min(value = 1, message = "La nota mínima es 1")
    @Max(value = 5, message = "La nota máxima es 5")
    private Integer rating;

    @Size(max = 500, message = "El comentario no puede exceder los 500 caracteres")
    private String comment;
}