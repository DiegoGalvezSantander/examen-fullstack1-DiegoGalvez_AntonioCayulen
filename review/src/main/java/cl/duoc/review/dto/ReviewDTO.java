package cl.duoc.review.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long destinationId;
    private String username;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
}