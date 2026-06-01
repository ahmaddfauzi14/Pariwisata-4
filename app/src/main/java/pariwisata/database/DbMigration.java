package pariwisata.database;

import java.sql.Connection;
import java.util.UUID;

import pariwisata.dao.UserRepository;
import pariwisata.model.User;
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

        seedAdminUser();
    }

    private static void seedAdminUser() {
        UserRepository userRepository = new UserRepository();
        if (userRepository.getByEmail("admin01@gmail.com") == null) {
            User admin = new User(
                UUID.randomUUID().toString(),
                "Admin-1",
                "admin01@gmail.com",
                "12345678"
            );
            admin.setRole("admin");
            userRepository.create(admin);
            System.out.println("Admin-1 account seeded successfully!");
        }
    }

}