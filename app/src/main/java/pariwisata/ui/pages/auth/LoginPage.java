package pariwisata.ui.pages.auth;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import pariwisata.model.User;
import pariwisata.service.UserService;
import pariwisata.util.Navigator;
import pariwisata.util.Session;
import pariwisata.ui.pages.auth.RegisterPage;
import pariwisata.ui.pages.user.HomepagePage;
import pariwisata.ui.pages.admin.AdminDashboardPage;

public class LoginPage {

    public void show(Stage stage) {

        /*
         * =====================================================
         * LOAD FONT
         * =====================================================
         */

        Font.loadFont(
                getClass().getResourceAsStream(
                        "/pariwisata/assets/fonts/Poppins-Regular.ttf"
                ),
                14
        );

        /*
         * =====================================================
         * ROOT
         * =====================================================
         */

        BorderPane root = new BorderPane();
        root.getStyleClass().add("main-background");

        /*
         * =====================================================
         * LEFT SIDE (BACKGROUND IMAGE)
         * =====================================================
         */

        StackPane leftPane = new StackPane();
        leftPane.setPrefWidth(700);

        BackgroundImage backgroundImage = new BackgroundImage(
                new Image(
                        getClass().getResourceAsStream(
                                "/pariwisata/assets/images/auth/login-background.jpg"
                        )
                ),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(100, 100, true, true, true, true)
        );

        leftPane.setBackground(new Background(backgroundImage));

        /*
         * =====================================================
         * OVERLAY
         * =====================================================
         */

        Region overlay = new Region();
        overlay.setStyle("-fx-background-color: rgba(0,59,122,0.55);");
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        /*
         * =====================================================
         * HERO TEXT
         * =====================================================
         */

        VBox heroBox = new VBox(15);
        heroBox.setPadding(new Insets(60));
        heroBox.setAlignment(Pos.BOTTOM_LEFT);
        heroBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label heroTitle = new Label("Jelajahi\nKeindahan\nMakassar");
        heroTitle.getStyleClass().add("hero-title");
        heroTitle.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label heroSubtitle = new Label(
                "Temukan destinasi wisata dan\nkuliner terbaik Kota Makassar"
        );
        heroSubtitle.getStyleClass().add("hero-subtitle");
        heroSubtitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        heroBox.getChildren().addAll(heroTitle, heroSubtitle);

        leftPane.getChildren().addAll(overlay, heroBox);
        StackPane.setAlignment(heroBox, Pos.BOTTOM_LEFT);

        /*
         * =====================================================
         * RIGHT SIDE (LOGIN FORM)
         * =====================================================
         */

        StackPane rightPane = new StackPane();
        rightPane.setPadding(new Insets(40));

        VBox loginCard = new VBox(20);
        loginCard.setMaxWidth(420);
        loginCard.getStyleClass().add("auth-card");

        /*
         * =====================================================
         * LOGO — gunakan aset gambar wisatarasa-logo.png
         * =====================================================
         */

        javafx.scene.Node logoNode;
        try {
            java.io.InputStream logoStream = getClass().getResourceAsStream(
                    "/pariwisata/assets/logo/wisatarasa-logo.png"
            );
            if (logoStream == null) throw new Exception("Logo resource not found");
            Image logoImage = new Image(logoStream);
            if (logoImage.isError()) throw new Exception("Logo image error");
            ImageView logoImg = new ImageView(logoImage);
            logoImg.setFitWidth(180);
            logoImg.setPreserveRatio(true);
            logoImg.setSmooth(true);
            logoNode = logoImg;
            javafx.scene.effect.ColorAdjust colorAdjust = new javafx.scene.effect.ColorAdjust();
            colorAdjust.setBrightness(-1.0); // -1.0 = hitam penuh
            logoImg.setEffect(colorAdjust);
        } catch (Exception e) {
            /* fallback: teks jika gambar gagal dimuat */
            Label fallbackLogo = new Label("WisataRasa");
            fallbackLogo.setStyle(
                    "-fx-font-size: 30px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #0D6EFD;"
            );
            logoNode = fallbackLogo;
        }

        /*
         * =====================================================
         * TITLE
         * =====================================================
         */

        Label title = new Label("Welcome Back");
        title.getStyleClass().add("auth-title");

        Label subtitle = new Label("Silakan login untuk melanjutkan");
        subtitle.getStyleClass().add("auth-subtitle");

        /*
         * =====================================================
         * EMAIL FIELD
         * =====================================================
         */

        VBox emailBox = new VBox(8);
        Label emailLabel = new Label("Email");
        TextField emailField = new TextField();
        emailField.setPromptText("Masukkan email");
        emailField.getStyleClass().add("modern-textfield");
        emailBox.getChildren().addAll(emailLabel, emailField);

        /*
         * =====================================================
         * PASSWORD FIELD
         * =====================================================
         */

        VBox passwordBox = new VBox(8);
        Label passwordLabel = new Label("Password");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Masukkan password");
        passwordField.getStyleClass().add("modern-textfield");
        passwordBox.getChildren().addAll(passwordLabel, passwordField);

        /*
         * =====================================================
         * ERROR LABEL
         * =====================================================
         */

        Label errorLabel = new Label("");
        errorLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
        errorLabel.setWrapText(true);
        errorLabel.setVisible(false);

        /*
         * =====================================================
         * LOGIN BUTTON
         * =====================================================
         */

        Button loginButton = new Button("Login");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setPrefHeight(50);
        loginButton.getStyleClass().add("primary-button");

        /*
         * =====================================================
         * LANJUT SEBAGAI TAMU
         * =====================================================
         */

        Button guestButton = new Button("Lanjut sebagai Tamu");
        guestButton.setMaxWidth(Double.MAX_VALUE);
        guestButton.setPrefHeight(44);
        guestButton.getStyleClass().add("secondary-button");

        /*
         * =====================================================
         * REGISTER LINK
         * =====================================================
         */

        Hyperlink registerLink = new Hyperlink("Belum punya akun? Register");
        registerLink.setBorder(Border.EMPTY);

        /*
         * =====================================================
         * ACTION LOGIN
         * =====================================================
         */

        loginButton.setOnAction(e -> {
            String email    = emailField.getText().trim();
            String password = passwordField.getText();
            errorLabel.setVisible(false);

            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Email dan password wajib diisi.");
                errorLabel.setVisible(true);
                return;
            }

            try {
                User user = UserService.login(email, password);
                Session.login(user);

                if (Session.isAdmin()) {
                    Navigator.switchScene(stage, new AdminDashboardPage().getView(stage));
                } else {
                    new HomepagePage().show(stage);
                }
            } catch (IllegalArgumentException ex) {
                errorLabel.setText(ex.getMessage());
                errorLabel.setVisible(true);
            }
        });

        /*
         * =====================================================
         * ACTION TAMU
         * =====================================================
         */

        guestButton.setOnAction(e -> {
            Session.loginAsGuest();
            new HomepagePage().show(stage);
        });

        /*
         * =====================================================
         * ACTION REGISTER
         * =====================================================
         */

        registerLink.setOnAction(e -> new RegisterPage().show(stage));

        /*
         * =====================================================
         * ADD COMPONENT
         * =====================================================
         */

        loginCard.getChildren().addAll(
                logoNode,
                title,
                subtitle,
                emailBox,
                passwordBox,
                errorLabel,
                loginButton,
                guestButton,
                registerLink
        );

        rightPane.getChildren().add(loginCard);

        root.setLeft(leftPane);
        root.setCenter(rightPane);

        /*
         * =====================================================
         * SCENE
         * =====================================================
         */

        Scene scene = new Scene(root, 1400, 850);
        scene.getStylesheets().addAll(
                getClass().getResource("/pariwisata/ui/style/theme.css").toExternalForm(),
                getClass().getResource("/pariwisata/ui/style/auth.css").toExternalForm()
        );

        stage.setTitle("Login - WisataRasa");
        stage.setScene(scene);
        stage.show();
    }
}
