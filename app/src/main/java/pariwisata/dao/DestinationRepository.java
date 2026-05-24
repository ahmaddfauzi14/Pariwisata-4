package pariwisata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pariwisata.database.Db;
import pariwisata.model.Destination;

public class DestinationRepository implements RepositoryMethod<Destination, String> {
    @Override
    public boolean create(Destination destination) {
        String query = "INSERT INTO destinations (id, name, category, description, address, price, map_url, operational_status, open_hour, close_hour, photo_url) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, destination.getId());
            pstmt.setString(2, destination.getName());
            pstmt.setString(3, destination.getCategory());
            pstmt.setString(4, destination.getDescription());
            pstmt.setString(5, destination.getAddress());
            pstmt.setString(6, destination.getPrice());
            pstmt.setString(7, destination.getMapUrl());
            pstmt.setString(8, destination.getOperationalStatus());
            pstmt.setString(9, destination.getOpenHour());
            pstmt.setString(10, destination.getCloseHour());
            pstmt.setString(11, destination.getPhotoUrl());
            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Destination getById(String id) {

        String query = """
                SELECT * FROM destinations WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                Destination destination = new Destination(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("address"),
                    rs.getString("price"),
                    rs.getString("map_url"),
                    rs.getString("operational_status"),
                    rs.getString("open_hour"),
                    rs.getString("close_hour"),
                    rs.getString("photo_url")
                );
                
                destination.setReviews(new ReviewRepository().getByDestinationId(destination.getId()));
                destination.setMedsosList(new MedsosRepository().getByDestinationId(destination.getId()));

                return destination;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Destination> getAll() {
        List<Destination> destinations = new ArrayList<>();
        String query = """
                SELECT * FROM destinations
                """;
        
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                Destination destination = new Destination(
                    rs.getString("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("description"),
                    rs.getString("address"),
                    rs.getString("price"),
                    rs.getString("map_url"),
                    rs.getString("operational_status"),
                    rs.getString("open_hour"),
                    rs.getString("close_hour"),
                    rs.getString("photo_url")
                );
                destinations.add(destination);
            }
            return destinations;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(Destination destination) {
        String query = """
                UPDATE destinations SET name = ?, category = ?, description = ?, address = ?, price = ?, map_url = ?, operational_status = ?, open_hour = ?, close_hour = ?, photo_url = ? WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, destination.getName());
            pstmt.setString(2, destination.getCategory());
            pstmt.setString(3, destination.getDescription());
            pstmt.setString(4, destination.getAddress());
            pstmt.setString(5, destination.getPrice());
            pstmt.setString(6, destination.getMapUrl());
            pstmt.setString(7, destination.getOperationalStatus());
            pstmt.setString(8, destination.getOpenHour());
            pstmt.setString(9, destination.getCloseHour());
            pstmt.setString(10, destination.getPhotoUrl());
            pstmt.setString(11, destination.getId());
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = """
                DELETE FROM destinations WHERE id = ?
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
