package pariwisata.database.migration;

import java.sql.SQLException;
import java.sql.Statement;

import pariwisata.database.Db;

public class UserMigration extends Migration {
    @Override
    public void initialize() {
        String query = """
                CREATE TABLE IF NOT EXISTS users (
                    id TEXT PRIMARY KEY,
                    username TEXT NOT NULL,
                    email TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL DEFAULT 'user',
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;

        try (
            Statement stmt = Db.connection.createStatement();
        ) {
            stmt.execute(query);

            System.out.println("Migration users berhasil!");
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}
