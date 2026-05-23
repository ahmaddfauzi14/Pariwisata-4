package pariwisata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pariwisata.database.Db;
import pariwisata.model.Wishlist;

public class WishlistsRepository implements RepositoryMethod<Wishlist, String> {
    @Override
    public boolean create(Wishlist wishlist) {
        String query = "INSERT INTO wishlists (id, user_id, destination_id) VALUES (?, ?, ?)";
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            String id = UUID.randomUUID().toString();
            pstmt.setString(1, id);
            pstmt.setString(2, wishlist.getUserId());
            pstmt.setString(3, wishlist.getDestinationId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Wishlist getById(String id) {
        String query = """
                SELECT * FROM wishlists WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                Wishlist wishlist = new Wishlist(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getString("created_at")
                );
                return wishlist;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Wishlist> getByUserId(String user_id) {
        String query = """
                SELECT * FROM wishlists WHERE user_id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, user_id);
            var rs = pstmt.executeQuery();
            List<Wishlist> wishlists = new ArrayList<>();
            while (rs.next()) {
                Wishlist wishlist = new Wishlist(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getString("created_at")
                );
                wishlists.add(wishlist);
            }
            return wishlists;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Wishlist> getAll() {
        List<Wishlist> wishlists = new ArrayList<>();
        String query = """
                SELECT * FROM wishlists
                """;
        
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                Wishlist wishlist = new Wishlist(
                    rs.getString("id"),
                    rs.getString("user_id"),
                    rs.getString("destination_id"),
                    rs.getString("created_at")
                );
                wishlists.add(wishlist);
            }
            return wishlists;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(Wishlist wishlist) {
        String query = """
                UPDATE wishlists SET user_id = ?, destination_id = ? WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, wishlist.getUserId());
            pstmt.setString(2, wishlist.getDestinationId());
            pstmt.setString(3, wishlist.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = """
                DELETE FROM wishlists WHERE id = ?
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