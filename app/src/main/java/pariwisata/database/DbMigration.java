package pariwisata.database;

import java.sql.Connection;

import pariwisata.database.migration.DestinationMigration;
import pariwisata.database.migration.MediaSosialMigration;
import pariwisata.database.migration.ReviewMigration;
import pariwisata.database.migration.UserMigration;
import pariwisata.database.migration.WishlistMigration;

public class DbMigration {

    public static void migrate(Connection connection) {
        new UserMigration().initialize();
        new DestinationMigration().initialize();
        new ReviewMigration().initialize();
        new WishlistMigration().initialize();
        new MediaSosialMigration().initialize();
    }

}