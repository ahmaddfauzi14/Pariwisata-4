package pariwisata.database.migration;

import java.sql.SQLException;
import java.sql.Statement;

import pariwisata.database.Db;

public class DestinationMigration extends Migration {
    @Override
    public void initialize() {
        String query = """
                CREATE TABLE IF NOT EXISTS destinations (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    category TEXT NOT NULL,
                    address TEXT NOT NULL UNIQUE,
                    photo_url TEXT,
                    price DOUBLE NOT NULL,
                    map_url TEXT,
                    operational_status TEXT NOT NULL,
                    open_hour TEXT NOT NULL,
                    close_hour TEXT NOT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (
            Statement stmt = Db.connection.createStatement();
        ) {
            stmt.execute(query);

            System.out.println("Migration destinasi berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
