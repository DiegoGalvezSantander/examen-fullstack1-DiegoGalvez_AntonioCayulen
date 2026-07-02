package cl.duoc.review.service;

import cl.duoc.review.dto.ApiResponse;
import cl.duoc.review.dto.ReviewCreateDTO;
import cl.duoc.review.dto.ReviewDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient.Builder webClientBuilder;

  
    public boolean validateToken(String token) {
        try {
            ApiResponse<?> response = webClientBuilder.build()
                    .get()
                    .uri("http://login/api/v1/users/validate?token=" + token)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {})
                    .block();
            return response != null && response.getCode() == 200;
        } catch (Exception e) {
            return false;
        }
    }

  
    public boolean validateDestinationExists(Long destinationId) {
        try {
            ApiResponse<?> response = webClientBuilder.build()
                    .get()
                    .uri("http://destination/api/v1/destination/destinations/exists?id=" + destinationId)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<ApiResponse<?>>() {})
                    .block();
            return response != null && response.getCode() == 200 && (Boolean) response.getData();
        } catch (Exception e) {
            return false;
        }
    }

   
    public ReviewDTO createReview(ReviewCreateDTO dto) {
        if (!validateDestinationExists(dto.getDestinationId())) {
            throw new IllegalArgumentException("El destino con ID " + dto.getDestinationId() + " no existe.");
        }

        Review review = Review.builder()
                .destinationId(dto.getDestinationId())
                .username(dto.getUsername())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

   
    public List<ReviewDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ReviewDTO convertToDTO(Review review) {
        return new ReviewDTO(
                review.getId(),
                review.getDestinationId(),
                review.getUsername(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
        );
    }
}