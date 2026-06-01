package pariwisata.ui.pages.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import pariwisata.model.User;
import pariwisata.service.UserService;
import pariwisata.util.Session;
import pariwisata.ui.components.UserSidebar;

public class ProfilePage {

    public Parent getView(Stage stage) {

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        UserSidebar sidebar = new UserSidebar(stage, UserSidebar.Page.PROFILE);

        /*
         * =====================================================
         * ROOT CONTENT
         * =====================================================
         */

        VBox content = new VBox(30);

        content.setPadding(new Insets(40));

        content.getStyleClass().add("homepage-container");

        /*
         * =====================================================
         * JUDUL HALAMAN
         * =====================================================
         */

        Label pageTitle = new Label("Profil Saya");

        pageTitle.getStyleClass().add("section-title");

        pageTitle.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        /*
         * =====================================================
         * AVATAR + INFO SECTION
         * =====================================================
         */

        HBox profileHeader = new HBox(30);

        profileHeader.setAlignment(Pos.CENTER_LEFT);

        profileHeader.setPadding(new Insets(24));

        profileHeader.setStyle(
                "-fx-background-color: #F8FAFF;" +
                "-fx-background-radius: 16;" +
                "-fx-border-color: #E0E7FF;" +
                "-fx-border-radius: 16;" +
                "-fx-border-width: 1;"
        );

        /*
         * AVATAR CIRCLE
         */

        StackPane avatarPane = new StackPane();

        avatarPane.setPrefSize(90, 90);

        avatarPane.setMinSize(90, 90);

        avatarPane.setStyle(
                "-fx-background-color: #0D6EFD;" +
                "-fx-background-radius: 50;"
        );

        User user = Session.getUser();

        String initials = (user != null && user.getUsername() != null &&
                !user.getUsername().isEmpty())
                ? String.valueOf(user.getUsername().charAt(0)).toUpperCase()
                : "T";

        Label initialsLabel = new Label(initials);

        initialsLabel.setStyle(
                "-fx-font-size: 36px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: white;"
        );

        avatarPane.getChildren().add(initialsLabel);

        /*
         * INFO TEXT
         */

        VBox infoBox = new VBox(8);

        infoBox.setAlignment(Pos.CENTER_LEFT);

        String displayName = Session.isGuest()
                ? "Tamu"
                : (user != null ? user.getUsername() : "-");

        String displayEmail = (user != null && user.getEmail() != null)
                ? user.getEmail()
                : "Mode Tamu";

        String displayRole = (user != null && user.getRole() != null)
                ? user.getRole().substring(0, 1).toUpperCase() +
                  user.getRole().substring(1)
                : "Tamu";

        Label nameLabel = new Label(displayName);

        nameLabel.setStyle(
                "-fx-font-size: 22px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1B1F3B;"
        );

        Label emailLabel = new Label(displayEmail);

        emailLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-text-fill: #6B7280;"
        );

        Label roleLabel = new Label(displayRole);

        roleLabel.setStyle(
                "-fx-background-color: #EAF4FF;" +
                "-fx-text-fill: #0D6EFD;" +
                "-fx-padding: 4 14 4 14;" +
                "-fx-background-radius: 20;" +
                "-fx-font-size: 13px;" +
                "-fx-font-weight: bold;"
        );

        infoBox.getChildren().addAll(
                nameLabel,
                emailLabel,
                roleLabel
        );

        profileHeader.getChildren().addAll(
                avatarPane,
                infoBox
        );

        /*
         * =====================================================
         * FORM EDIT PROFIL (hanya untuk user login)
         * =====================================================
         */

        VBox editSection = new VBox(20);

        if (!Session.isGuest() && user != null) {

            Label editTitle = new Label("Edit Profil");

            editTitle.setStyle(
                    "-fx-font-size: 18px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1B1F3B;"
            );

            /*
             * USERNAME FIELD
             */

            VBox usernameBox = new VBox(8);

            Label usernameLabel = new Label("Username");

            TextField usernameField = new TextField(user.getUsername());

            usernameField.setMaxWidth(500);

            usernameField.getStyleClass().add("modern-textfield");

            usernameBox.getChildren().addAll(
                    usernameLabel,
                    usernameField
            );

            /*
             * GANTI PASSWORD SECTION
             */

            Label pwTitle = new Label("Ganti Password");

            pwTitle.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #1B1F3B;"
            );

            VBox oldPwBox = new VBox(8);

            Label oldPwLabel = new Label("Password Lama");

            PasswordField oldPwField = new PasswordField();

            oldPwField.setPromptText("Masukkan password lama");

            oldPwField.setMaxWidth(500);

            oldPwField.getStyleClass().add("modern-textfield");

            oldPwBox.getChildren().addAll(
                    oldPwLabel,
                    oldPwField
            );

            VBox newPwBox = new VBox(8);

            Label newPwLabel = new Label("Password Baru");

            PasswordField newPwField = new PasswordField();

            newPwField.setPromptText("Minimal 8 karakter");

            newPwField.setMaxWidth(500);

            newPwField.getStyleClass().add("modern-textfield");

            newPwBox.getChildren().addAll(
                    newPwLabel,
                    newPwField
            );

            /*
             * ERROR / SUCCESS LABEL
             */

            Label feedbackLabel = new Label("");

            feedbackLabel.setWrapText(true);

            feedbackLabel.setVisible(false);

            feedbackLabel.setMaxWidth(500);

            /*
             * TOMBOL SIMPAN
             */

            HBox buttonRow = new HBox(15);

            Button saveProfileBtn = new Button("Simpan Profil");

            saveProfileBtn.getStyleClass().add("primary-button");

            saveProfileBtn.setPrefHeight(45);

            Button changePasswordBtn = new Button("Ganti Password");

            changePasswordBtn.getStyleClass().add("secondary-button");

            changePasswordBtn.setPrefHeight(45);

            buttonRow.getChildren().addAll(
                    saveProfileBtn,
                    changePasswordBtn
            );

            /*
             * ACTION SIMPAN PROFIL
             */

            saveProfileBtn.setOnAction(e -> {

                String newUsername = usernameField.getText().trim();

                feedbackLabel.setVisible(false);

                if (newUsername.isEmpty()) {

                    feedbackLabel.setText("Username tidak boleh kosong.");
                    feedbackLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
                    feedbackLabel.setVisible(true);
                    return;
                }

                user.setUsername(newUsername);

                boolean success = UserService.update(user);

                if (success) {

                    Session.login(user);

                    nameLabel.setText(newUsername);

                    feedbackLabel.setText("Profil berhasil diperbarui.");
                    feedbackLabel.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 13px;");

                } else {

                    feedbackLabel.setText("Gagal memperbarui profil.");
                    feedbackLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
                }

                feedbackLabel.setVisible(true);
            });

            /*
             * ACTION GANTI PASSWORD
             */

            changePasswordBtn.setOnAction(e -> {

                String oldPw = oldPwField.getText();
                String newPw = newPwField.getText();

                feedbackLabel.setVisible(false);

                if (oldPw.isEmpty() || newPw.isEmpty()) {

                    feedbackLabel.setText("Password lama dan baru wajib diisi.");
                    feedbackLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
                    feedbackLabel.setVisible(true);
                    return;
                }

                try {

                    boolean success = UserService.changePassword(
                            user.getId(),
                            oldPw,
                            newPw
                    );

                    if (success) {

                        oldPwField.clear();
                        newPwField.clear();

                        feedbackLabel.setText("Password berhasil diubah.");
                        feedbackLabel.setStyle("-fx-text-fill: #16A34A; -fx-font-size: 13px;");

                    } else {

                        feedbackLabel.setText("Gagal mengubah password.");
                        feedbackLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
                    }

                } catch (IllegalArgumentException ex) {

                    feedbackLabel.setText(ex.getMessage());
                    feedbackLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
                }

                feedbackLabel.setVisible(true);
            });

            editSection.getChildren().addAll(

                    editTitle,

                    usernameBox,

                    pwTitle,

                    oldPwBox,

                    newPwBox,

                    feedbackLabel,

                    buttonRow
            );

        } else {

            /*
             * PESAN UNTUK TAMU
             */

            Label guestMsg = new Label(
                    "Anda sedang masuk sebagai Tamu.\n" +
                    "Login untuk mengakses dan mengedit profil."
            );

            guestMsg.setWrapText(true);

            guestMsg.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-text-fill: #6B7280;"
            );

            Button loginBtn = new Button("Login Sekarang");

            loginBtn.getStyleClass().add("primary-button");

            loginBtn.setPrefHeight(45);

            loginBtn.setOnAction(e ->
                    new pariwisata.ui.pages.auth.LoginPage().show(stage)
            );

            editSection.getChildren().addAll(
                    guestMsg,
                    loginBtn
            );
        }

        /*
         * =====================================================
         * LOGOUT BUTTON
         * =====================================================
         */

        Button logoutBtn = new Button("Logout");

        logoutBtn.getStyleClass().add("secondary-button");

        logoutBtn.setPrefHeight(44);

        logoutBtn.setStyle(
                "-fx-text-fill: #DC3545;" +
                "-fx-border-color: #DC3545;"
        );

        logoutBtn.setOnAction(e -> {

            Session.logout();

            new pariwisata.ui.pages.auth.LoginPage().show(stage);
        });

        /*
         * =====================================================
         * ADD ALL
         * =====================================================
         */

        content.getChildren().addAll(

                pageTitle,

                profileHeader,

                editSection,

                logoutBtn
        );

        /*
         * =====================================================
         * SCROLLPANE
         * =====================================================
         */

        ScrollPane scrollPane = new ScrollPane(content);

        scrollPane.setFitToWidth(true);

        scrollPane.getStyleClass().add("scroll-pane");

        /*
         * =====================================================
         * ROOT LAYOUT  (sidebar kiri | konten kanan)
         * =====================================================
         */

        BorderPane root = new BorderPane();

        root.setLeft(sidebar);

        root.setCenter(scrollPane);

        root.getStyleClass().add("main-background");

        root.getStylesheets().addAll(
                getClass().getResource(
                        "/pariwisata/ui/style/theme.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/sidebar.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/homepage.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/profile.css").toExternalForm()
        );

        return root;
    }
}
