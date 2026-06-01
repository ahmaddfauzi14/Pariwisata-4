package pariwisata;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.stage.Stage;

import pariwisata.database.Db;
import pariwisata.database.DbMigration;
import pariwisata.ui.pages.auth.LoginPage;

public class App extends Application {

    // Simpan static agar bisa diakses dari mana saja
    public static HostServices hostServices;

    @Override
    public void start(Stage stage) {
        hostServices = getHostServices();
        
        /*
         * =====================================================
         * KONEKSI DATABASE
         * =====================================================
         */
        Db.connection();

        /*
         * =====================================================
         * JALANKAN MIGRASI DATABASE
         * =====================================================
         */
        DbMigration.migrate(Db.connection);

        /*
         * =====================================================
         * TAMPILKAN HALAMAN LOGIN
         * =====================================================
         */
        new LoginPage().show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

        