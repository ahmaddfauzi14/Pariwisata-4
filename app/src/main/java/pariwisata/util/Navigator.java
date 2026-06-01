package pariwisata.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    public static void switchScene(Stage stage, Parent root) {

        Scene scene = new Scene(root, 1400, 850);

        scene.getStylesheets().addAll(
            Navigator.class.getResource("/pariwisata/ui/style/theme.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/sidebar.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/admin.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/homepage.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/wisata.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/kuliner.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/detail.css").toExternalForm(),
            Navigator.class.getResource("/pariwisata/ui/style/profile.css").toExternalForm()
        );

        stage.setScene(scene);
        stage.show();
    }
}