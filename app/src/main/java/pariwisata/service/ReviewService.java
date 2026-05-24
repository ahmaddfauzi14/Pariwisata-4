package pariwisata.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pariwisata.dao.ReviewRepository;
import pariwisata.model.Review;

public class ReviewService {
    private ReviewRepository reviewRepository;

    public ReviewService() {
        this.reviewRepository = new ReviewRepository();
    }

    public boolean createReview(String userId, String destinationId, int rating, String comment) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }

        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus antara 1-5");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Komentar tidak boleh kosong");
        }
        if (comment.length() < 10) {
            throw new IllegalArgumentException("Komentar minimal 10 karakter");
        }

        String id = UUID.randomUUID().toString();
        Review review = new Review(id, userId, destinationId, rating, comment);

        return reviewRepository.create(review);
    }

    public List<Review> getReviewsByDestination(String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }
        return reviewRepository.getByDestinationId(destinationId);
    }

    public List<Review> getReviewsByUser(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }
        return reviewRepository.getByUserId(userId);
    }

    public double getAverageRating(String destinationId) {
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        List<Review> reviews = reviewRepository.getByDestinationId(destinationId);
        if (reviews == null || reviews.isEmpty()) {
            return 0.0;
        }

        int totalRating = 0;
        for (Review review : reviews) {
            totalRating += review.getRating();
        }

        return (double) totalRating / reviews.size();
    }

    public boolean updateReview(String id, int rating, String comment) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Review ID tidak boleh kosong");
        }

        Review review = reviewRepository.getById(id);
        if (review == null) {
            throw new IllegalArgumentException("Review tidak ditemukan");
        }

        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating harus antara 1-5");
        }

        if (comment == null || comment.trim().isEmpty()) {
            throw new IllegalArgumentException("Komentar tidak boleh kosong");
        }
        if (comment.length() < 10) {
            throw new IllegalArgumentException("Komentar minimal 10 karakter");
        }

        review.setRating(rating);
        review.setComment(comment);

        return reviewRepository.update(review);
    }

    public boolean deleteReview(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Review ID tidak boleh kosong");
        }

        Review review = reviewRepository.getById(id);
        if (review == null) {
            throw new IllegalArgumentException("Review tidak ditemukan");
        }

        return reviewRepository.delete(id);
    }

    public Review getReviewById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Review ID tidak boleh kosong");
        }
        return reviewRepository.getById(id);
    }

    public List<Review> getAllReviews() {
        return reviewRepository.getAll();
    }

    public List<String> getTopRatedDestinations() {
        List<Review> allReviews = reviewRepository.getAll();
        if (allReviews == null || allReviews.isEmpty()) {
            return new ArrayList<>();
        }

        java.util.Map<String, List<Review>> reviewsByDestination = new java.util.HashMap<>();
        for (Review review : allReviews) {
            reviewsByDestination
                .computeIfAbsent(review.getDestinationId(), k -> new ArrayList<>())
                .add(review);
        }

        List<String> topDestinations = new ArrayList<>(reviewsByDestination.keySet());
        topDestinations.sort((dest1, dest2) -> {
            double avg1 = getAverageRating(dest1);
            double avg2 = getAverageRating(dest2);
            return Double.compare(avg2, avg1);
        });

        return topDestinations;
    }
}
