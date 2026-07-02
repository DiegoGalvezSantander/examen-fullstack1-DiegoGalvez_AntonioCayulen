package cl.duoc.review.controller;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewCreateDTO;
import cl.duoc.review.dto.ReviewDTO;
import cl.duoc.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
@Tag(name = "Reviews", description = "Endpoint basico para crear reseñas")
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    @Operation(summary = "Crear una reseña")
    public ResponseEntity<ApiResponse<ReviewDTO>> create(
            @RequestHeader("Authorization") String authHeader,
            @Valid @RequestBody ReviewCreateDTO dto) {

        log.info("Creando reseña");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Header faltante o incorrecto");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Token incorrecto", null));
        }

        String token = authHeader.substring(7);
        if (!reviewService.validateToken(token)) {
            log.warn("Token invalido");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Token invalido", null));
        }

        ReviewDTO created = reviewService.createReview(dto);
        log.info("Reseña creada con exito");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "Reseña creada", created));
    }

    @GetMapping
    @Operation(summary = "Obtener todas las reseñas")
    public ResponseEntity<ApiResponse<List<ReviewDTO>>> getAll() {
        log.info("Listando reseñas");
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Listado obtenido", reviews));
    }
}