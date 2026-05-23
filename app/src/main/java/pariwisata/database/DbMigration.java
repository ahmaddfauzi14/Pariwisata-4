package pariwisata.database;

import java.sql.Connection;
import java.sql.SQLException;



public class DbMigration {
    public static void migrate(Connection conn) throws SQLException {
        new pariwisata.database.migration.DestinationMigration().initialize();
        new pariwisata.database.migration.UserMigration().initialize();
        new pariwisata.database.migration.MediaSosialMigration().initialize();
        new pariwisata.database.migration.ReviewMigration().initialize();
        new pariwisata.database.migration.WishlistMigration().initialize();
    }
}
