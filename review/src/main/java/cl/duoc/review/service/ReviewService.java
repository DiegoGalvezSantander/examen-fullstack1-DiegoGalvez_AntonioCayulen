package cl.duoc.review.service;

import cl.duoc.review.dto.ReviewCreateDTO;
import cl.duoc.review.dto.ReviewDTO;
import cl.duoc.review.model.Review;
import cl.duoc.review.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDTO create(ReviewCreateDTO dto) {
        Review review = Review.builder()
                .destinationId(dto.getDestinationId())
                .username(dto.getUsername())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();
        Review saved = reviewRepository.save(review);
        return mapToDTO(saved);
    }

    public List<ReviewDTO> getAll() {
        return reviewRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ReviewDTO getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con el ID: " + id));
        return mapToDTO(review);
    }

    public ReviewDTO update(Long id, ReviewCreateDTO dto) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con el ID: " + id));
        
        review.setDestinationId(dto.getDestinationId());
        review.setUsername(dto.getUsername());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        
        Review updated = reviewRepository.save(review);
        return mapToDTO(updated);
    }

    public void delete(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reseña no encontrada con el ID: " + id));
        reviewRepository.delete(review);
    }

    private ReviewDTO mapToDTO(Review review) {
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