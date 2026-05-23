package pariwisata.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Db {
    public static String url = "jdbc:sqlite:db/pariwisata.db";
    public static Connection connection;
    public static PreparedStatement ps;
    public static ResultSet rs;

    public static void connection() {
        try {
            connection = DriverManager.getConnection(url); 
            System.out.println("Koneksi berhasil!");
            DbMigration.migrate(connection);
        } catch (SQLException e) {
            // e.printStackTrace();
        }
    }

}
