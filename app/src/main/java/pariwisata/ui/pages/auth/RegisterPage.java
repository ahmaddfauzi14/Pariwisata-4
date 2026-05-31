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

import pariwisata.service.UserService;

public class RegisterPage {

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

                new BackgroundSize(
                        100,
                        100,
                        true,
                        true,
                        true,
                        true
                )
        );

        leftPane.setBackground(
                new Background(backgroundImage)
        );

        Region overlay = new Region();

        overlay.setStyle(
                "-fx-background-color: rgba(0,59,122,0.55);"
        );
        overlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        VBox heroBox = new VBox(15);

        heroBox.setPadding(new Insets(60));

        heroBox.setAlignment(Pos.BOTTOM_LEFT);

        heroBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        Label heroTitle = new Label(
                "Bergabunglah\nBersama Kami"
        );

        heroTitle.getStyleClass().add("hero-title");
        heroTitle.setStyle("-fx-text-fill: white; -fx-font-size: 36px; -fx-font-weight: bold;");

        Label heroSubtitle = new Label(
                "Daftar dan mulai jelajahi\nwisata & kuliner Makassar"
        );

        heroSubtitle.getStyleClass().add("hero-subtitle");
        heroSubtitle.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        heroBox.getChildren().addAll(
                heroTitle,
                heroSubtitle
        );

        leftPane.getChildren().addAll(
                overlay,
                heroBox
        );

        StackPane.setAlignment(
                heroBox,
                Pos.BOTTOM_LEFT
        );

        /*
         * =====================================================
         * RIGHT SIDE (REGISTER FORM)
         * =====================================================
         */

        StackPane rightPane = new StackPane();

        rightPane.setPadding(new Insets(40));

        ScrollPane scrollPane = new ScrollPane();

        scrollPane.setFitToWidth(true);

        scrollPane.setStyle(
                "-fx-background: transparent;" +
                "-fx-background-color: transparent;"
        );

        VBox registerCard = new VBox(20);

        registerCard.setMaxSize(420, Double.MAX_VALUE);
        registerCard.setAlignment(Pos.TOP_CENTER);

        registerCard.getStyleClass().add("auth-card");

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
            javafx.scene.image.ImageView logoImg = new ImageView(logoImage);
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
                    "-fx-font-size: 28px;" +
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

        Label title = new Label("Buat Akun Baru");

        title.getStyleClass().add("auth-title");

        Label subtitle = new Label(
                "Isi data di bawah untuk mendaftar"
        );

        subtitle.getStyleClass().add("auth-subtitle");

        /*
         * =====================================================
         * USERNAME FIELD
         * =====================================================
         */

        VBox usernameBox = new VBox(8);

        Label usernameLabel = new Label("Username");

        TextField usernameField = new TextField();

        usernameField.setPromptText("Masukkan username");

        usernameField.getStyleClass().add("modern-textfield");

        usernameBox.getChildren().addAll(
                usernameLabel,
                usernameField
        );

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

        emailBox.getChildren().addAll(
                emailLabel,
                emailField
        );

        /*
         * =====================================================
         * PASSWORD FIELD
         * =====================================================
         */

        VBox passwordBox = new VBox(8);

        Label passwordLabel = new Label("Password");

        PasswordField passwordField = new PasswordField();

        passwordField.setPromptText("Minimal 8 karakter");

        passwordField.getStyleClass().add("modern-textfield");

        passwordBox.getChildren().addAll(
                passwordLabel,
                passwordField
        );

        /*
         * =====================================================
         * KONFIRMASI PASSWORD
         * =====================================================
         */

        VBox confirmBox = new VBox(8);

        Label confirmLabel = new Label("Konfirmasi Password");

        PasswordField confirmField = new PasswordField();

        confirmField.setPromptText("Ulangi password");

        confirmField.getStyleClass().add("modern-textfield");

        confirmBox.getChildren().addAll(
                confirmLabel,
                confirmField
        );

        /*
         * =====================================================
         * ERROR LABEL
         * =====================================================
         */

        Label errorLabel = new Label("");

        errorLabel.setStyle(
                "-fx-text-fill: #DC3545;" +
                "-fx-font-size: 13px;"
        );

        errorLabel.setWrapText(true);

        errorLabel.setVisible(false);

        /*
         * =====================================================
         * REGISTER BUTTON
         * =====================================================
         */

        Button registerButton = new Button("Daftar");

        registerButton.setMaxWidth(Double.MAX_VALUE);

        registerButton.setPrefHeight(50);

        registerButton.getStyleClass().add("primary-button");

        /*
         * =====================================================
         * BACK TO LOGIN
         * =====================================================
         */

        Hyperlink loginLink = new Hyperlink(
                "Sudah punya akun? Login"
        );

        loginLink.setBorder(Border.EMPTY);

        /*
         * =====================================================
         * ACTION REGISTER
         * =====================================================
         */

        registerButton.setOnAction(e -> {

            String username = usernameField.getText().trim();
            String email    = emailField.getText().trim();
            String password = passwordField.getText();
            String confirm  = confirmField.getText();

            errorLabel.setVisible(false);

            if (username.isEmpty() || email.isEmpty() ||
                    password.isEmpty() || confirm.isEmpty()) {

                errorLabel.setText("Semua field wajib diisi.");
                errorLabel.setVisible(true);
                return;
            }

            if (!password.equals(confirm)) {

                errorLabel.setText("Password dan konfirmasi tidak sama.");
                errorLabel.setVisible(true);
                return;
            }

            try {

                UserService.register(username, email, password);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Registrasi Berhasil");
                alert.setHeaderText(null);
                alert.setContentText(
                        "Akun berhasil dibuat! Silakan login."
                );
                alert.showAndWait();

                new LoginPage().show(stage);

            } catch (IllegalArgumentException ex) {

                errorLabel.setText(ex.getMessage());
                errorLabel.setVisible(true);
            }
        });

        /*
         * =====================================================
         * ACTION BACK TO LOGIN
         * =====================================================
         */

        loginLink.setOnAction(e ->
                new LoginPage().show(stage)
        );

        /*
         * =====================================================
         * ADD COMPONENT
         * =====================================================
         */

        registerCard.getChildren().addAll(

                logoNode,

                title,
                subtitle,

                usernameBox,

                emailBox,

                passwordBox,

                confirmBox,

                errorLabel,

                registerButton,

                loginLink
        );

        /* Wrap registerCard dalam VBox agar bisa di-center */
        VBox scrollContent = new VBox();
        scrollContent.setAlignment(Pos.CENTER);
        scrollContent.getChildren().add(registerCard);
        scrollContent.setFillWidth(false);

        scrollPane.setContent(scrollContent);

        rightPane.setAlignment(Pos.CENTER);
        rightPane.getChildren().add(scrollPane);

        /*
         * =====================================================
         * SET ROOT
         * =====================================================
         */

        root.setLeft(leftPane);

        root.setCenter(rightPane);

        /*
         * =====================================================
         * SCENE
         * =====================================================
         */

        Scene scene = new Scene(
                root,
                1400,
                850
        );

        /*
         * =====================================================
         * LOAD CSS
         * =====================================================
         */

        scene.getStylesheets().addAll(

                getClass().getResource(
                        "/pariwisata/ui/style/theme.css"
                ).toExternalForm(),

                getClass().getResource(
                        "/pariwisata/ui/style/auth.css"
                ).toExternalForm()
        );

        /*
         * =====================================================
         * STAGE
         * =====================================================
         */

        stage.setTitle("Register - WisataRasa");

        stage.setScene(scene);

        stage.show();
    }
}
