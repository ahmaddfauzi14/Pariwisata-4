package pariwisata.database.migration;

import java.sql.SQLException;
import java.sql.Statement;

import pariwisata.database.Db;

public class MediaSosialMigration extends Migration {
    @Override
    public void initialize() {
        String query = """
                CREATE TABLE IF NOT EXISTS social_media (
                    id TEXT PRIMARY KEY,
                    destination_id TEXT NOT NULL,
                    platform_name TEXT NOT NULL,
                    url TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE CASCADE
                )
                """;

        try (
            Statement stmt = Db.connection.createStatement();
        ) {
            stmt.execute(query);

            System.out.println("Migration media_sosial berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
