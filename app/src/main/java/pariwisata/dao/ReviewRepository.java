package pariwisata.dao;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pariwisata.database.Db;
import pariwisata.model.Review;

public class ReviewRepository implements RepositoryMethod<Review, String> {
    @Override
    public boolean create(Review review) {
        String query = "INSERT INTO reviews (id, user_id, destination_id, rating, comment) VALUES (?, ?, ?, ?, ?)";
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            String id = UUID.randomUUID().toString();
            pstmt.setString(1, id);
            pstmt.setString(2, review.getUserId());
            pstmt.setString(3, review.getDestinationId());
            pstmt.setInt(4, review.getRating());
            pstmt.setString(5, review.getComment());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Review getById(String id) {
        String query = """
                SELECT * FROM reviews WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                Review review = new Review(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getInt("rating"),
                    rs.getString("comment")
                );
                return review;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Review> getByDestinationId(String destination_id) {
        String query = """
                SELECT * FROM reviews WHERE destination_id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, destination_id);
            var rs = pstmt.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                Review review = new Review(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getInt("rating"),
                    rs.getString("comment")
                );
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Review> getByUserId(String user_id) {
        String query = """
                SELECT * FROM reviews WHERE user_id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, user_id);
            var rs = pstmt.executeQuery();
            List<Review> reviews = new ArrayList<>();
            while (rs.next()) {
                Review review = new Review(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getInt("rating"),
                    rs.getString("comment")
                );
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Review> getAll() {
        List<Review> reviews = new ArrayList<>();
        String query = """
                SELECT * FROM reviews
                """;
        
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getInt("rating"),
                    rs.getString("comment")
                );
                reviews.add(review);
            }
            return reviews;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(Review review) {
        String query = """
                UPDATE reviews SET user_id = ?, destination_id = ?, rating = ?, comment = ? WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, review.getUserId());
            pstmt.setString(2, review.getDestinationId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setString(5, review.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = """
                DELETE FROM reviews WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}