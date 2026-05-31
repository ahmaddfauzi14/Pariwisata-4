package pariwisata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.mindrot.jbcrypt.BCrypt;

import pariwisata.database.Db;
import pariwisata.model.Review;
import pariwisata.model.User;
import pariwisata.model.Wishlist;

public class UserRepository implements RepositoryMethod<User, String> {
    private List<Review> reviews = new ArrayList<>();
    private List<Wishlist> wishlists = new ArrayList<>();

    @Override
    public boolean create(User user) {
        String query = "INSERT INTO users (id, username, email, password, role) VALUES (?, ?, ?, ?, ?)";
        String hashPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, user.getId());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, hashPassword);
            pstmt.setString(5, user.getRole());
            return pstmt.executeUpdate() > 0;
            // System.out.println("User berhasil dibuat dengan ID: " + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public User getById(String id) {
        String query = """
                SELECT * FROM users WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                user.setRole(rs.getString("role"));

                user.setReviews(new ReviewRepository().getByUserId(user.getId()));
                user.setWishlists(new WishlistsRepository().getByUserId(user.getId()));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getByEmail(String email) {
        String query = """
                SELECT * FROM users WHERE email = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, email);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                user.setRole(rs.getString("role"));

                user.setReviews(new ReviewRepository().getByUserId(user.getId()));
                user.setWishlists(new WishlistsRepository().getByUserId(user.getId()));

                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<User> users = new ArrayList<>();
        String query = """
                SELECT * FROM users
                """;
        
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                    rs.getString("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password")
                );
                user.setRole(rs.getString("role"));
                users.add(user);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(User user) {
        String query = """
                UPDATE users SET username = ?, email = ?, password = ?, role = ? WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = """
                DELETE FROM users WHERE id = ?
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
