package pariwisata.database.migration;

import java.sql.SQLException;
import java.sql.Statement;

import pariwisata.database.Db;

public class ReviewMigration extends Migration {
    @Override
    public void initialize() {
        String query = """
                CREATE TABLE IF NOT EXISTS reviews (
                    id TEXT PRIMARY KEY,
                    user_id TEXT NOT NULL,
                    destination_id TEXT NOT NULL,
                    rating INTEGER NOT NULL,
                    comment TEXT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                    FOREIGN KEY (destination_id) REFERENCES destinations(id) ON DELETE CASCADE
                )
                """;

        try (
            Statement stmt = Db.connection.createStatement();
        ) {
            stmt.execute(query);

            System.out.println("Migration reviews berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
