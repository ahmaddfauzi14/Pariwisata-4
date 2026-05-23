package pariwisata.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pariwisata.database.Db;
import pariwisata.model.Medsos;

public class MedsosRepository implements RepositoryMethod<Medsos, String> {
    @Override
    public boolean create(Medsos medsos) {
        String query = "INSERT INTO social_media (id, destination_id, platform_name, url) VALUES (?, ?, ?, ?)";
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            String id = UUID.randomUUID().toString();
            pstmt.setString(1, id);
            pstmt.setString(2, medsos.getDestinationId());
            pstmt.setString(3, medsos.getPlatformName());
            pstmt.setString(4, medsos.getUrl());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }

    @Override
    public Medsos getById(String id) {
        String query = """
                SELECT * FROM social_media WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, id);
            var rs = pstmt.executeQuery();
            if (rs.next()) {
                Medsos medsos = new Medsos(
                    rs.getString("id"),
                    rs.getString("destination_id"),
                    rs.getString("platform_name"),
                    rs.getString("url")
                );
                return medsos;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Medsos> getByDestinationId(String destination_id) {
        String query = """
                SELECT * FROM social_media WHERE destination_id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, destination_id);
            var rs = pstmt.executeQuery();
            List<Medsos> medsosList = new ArrayList<>();
            while (rs.next()) {
                Medsos medsos = new Medsos(
                    rs.getString("id"),
                    rs.getString("destination_id"),
                    rs.getString("platform_name"),
                    rs.getString("url")
                );
                medsosList.add(medsos);
            }
            return medsosList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Medsos> getAll() {
        List<Medsos> medsosList = new ArrayList<>();
        String query = """
                SELECT * FROM social_media
                """;
        
        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            var rs = pstmt.executeQuery();
            while (rs.next()) {
                Medsos medsos = new Medsos(
                    rs.getString("id"),
                    rs.getString("destination_id"),
                    rs.getString("platform_name"),
                    rs.getString("url")
                );
                medsosList.add(medsos);
            }
            return medsosList;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean update(Medsos medsos) {
        String query = """
                UPDATE social_media SET platform_name = ?, destination_id = ?, url = ? WHERE id = ?
                """;

        try (
            PreparedStatement pstmt = Db.connection.prepareStatement(query);
        ) {
            pstmt.setString(1, medsos.getPlatformName());
            pstmt.setString(2, medsos.getDestinationId());
            pstmt.setString(3, medsos.getUrl());
            pstmt.setString(4, medsos.getId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        String query = """
                DELETE FROM social_media WHERE id = ?
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