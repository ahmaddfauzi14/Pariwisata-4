package pariwisata.ui.components;
 
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
 
/**
 * Dialog konfirmasi logout yang menarik.
 * Menggunakan aset ic-logout.png dan ic-profile.png dari resources.
 *
 * Cara pakai:
 *   LogoutConfirmDialog dialog = new LogoutConfirmDialog(ownerStage, username);
 *   if (dialog.showAndWait()) {
 *       Session.logout();
 *       new LoginPage().show(stage);
 *   }
 */
public class LogoutConfirmDialog {
 
    private final Stage owner;
    private final String username;
    private boolean confirmed = false;
 
    public LogoutConfirmDialog(Stage owner, String username) {
        this.owner    = owner;
        this.username = username;
    }
 
    /**
     * Tampilkan dialog. Return true jika user klik "Ya, Keluar".
     */
    public boolean showAndWait() {
 
        /* -------------------------------------------------- */
        /* STAGE                                               */
        /* -------------------------------------------------- */
        Stage dialog = new Stage(StageStyle.TRANSPARENT);
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(owner);
 
        /* -------------------------------------------------- */
        /* OVERLAY (semi-transparent background)              */
        /* -------------------------------------------------- */
        StackPane overlay = new StackPane();
        overlay.setAlignment(Pos.CENTER);
        overlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.55);");
 
        /* -------------------------------------------------- */
        /* CARD                                               */
        /* -------------------------------------------------- */
        VBox card = new VBox(0);
        card.setAlignment(Pos.TOP_CENTER);
        card.setMaxWidth(400);
        card.setMinWidth(400);
        card.setMaxHeight(Region.USE_PREF_SIZE);
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.35), 32, 0, 0, 8);"
        );
 
        /* ---- HEADER (biru gelap) ---- */
        VBox header = new VBox(12);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(32, 24, 24, 24));
        header.setStyle(
            "-fx-background-color: linear-gradient(to bottom right, #0d47a1, #1565c0);" +
            "-fx-background-radius: 20 20 0 0;"
        );
 
        /* Lingkaran ikon logout */
        StackPane iconCircle = new StackPane();
        iconCircle.setPrefSize(72, 72);
        iconCircle.setMinSize(72, 72);
        iconCircle.setStyle(
            "-fx-background-color: rgba(255,255,255,0.18);" +
            "-fx-background-radius: 36;"
        );
 
        ImageView logoutIcon = new ImageView();
        try {
            Image img = new Image(
                getClass().getResourceAsStream("/pariwisata/assets/icons/ic-logout.png")
            );
            logoutIcon.setImage(img);
            ColorAdjust white = new ColorAdjust();
            white.setBrightness(1.0);
            logoutIcon.setEffect(white);
        } catch (Exception ignored) {}
        logoutIcon.setFitWidth(34);
        logoutIcon.setFitHeight(34);
        logoutIcon.setPreserveRatio(true);
        logoutIcon.setSmooth(true);
        iconCircle.getChildren().add(logoutIcon);
 
        Label titleLabel = new Label("Konfirmasi Keluar");
        titleLabel.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 18px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );
 
        header.getChildren().addAll(iconCircle, titleLabel);
 
        /* ---- BODY ---- */
        VBox body = new VBox(8);
        body.setAlignment(Pos.CENTER);
        body.setPadding(new Insets(28, 28, 0, 28));
 
        /* Avatar + nama pengguna */
        HBox userRow = new HBox(12);
        userRow.setAlignment(Pos.CENTER);
        userRow.setPadding(new Insets(0, 0, 4, 0));
 
        StackPane avatarBox = new StackPane();
        avatarBox.setPrefSize(44, 44);
        avatarBox.setMinSize(44, 44);
        avatarBox.setStyle(
            "-fx-background-color: #e3f0ff;" +
            "-fx-background-radius: 22;"
        );
 
        ImageView profileIcon = new ImageView();
        try {
            Image pImg = new Image(
                getClass().getResourceAsStream("/pariwisata/assets/icons/ic-profile.png")
            );
            profileIcon.setImage(pImg);
            ColorAdjust blueAdj = new ColorAdjust();
            blueAdj.setSaturation(0.3);
            profileIcon.setEffect(blueAdj);
        } catch (Exception ignored) {}
        profileIcon.setFitWidth(22);
        profileIcon.setFitHeight(22);
        profileIcon.setPreserveRatio(true);
        avatarBox.getChildren().add(profileIcon);
 
        VBox nameBox = new VBox(2);
        nameBox.setAlignment(Pos.CENTER_LEFT);
        Label nameLabel = new Label(
            (username != null && !username.isEmpty()) ? username : "Pengguna"
        );
        nameLabel.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );
        Label roleLabel = new Label("Sedang aktif");
        roleLabel.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #6B7A99;"
        );
        nameBox.getChildren().addAll(nameLabel, roleLabel);
        userRow.getChildren().addAll(avatarBox, nameBox);
 
        /* Garis pemisah tipis */
        Region sep = new Region();
        sep.setPrefHeight(1);
        sep.setMaxWidth(Double.MAX_VALUE);
        sep.setStyle("-fx-background-color: #DCE5F2;");
        VBox.setMargin(sep, new Insets(12, 0, 12, 0));
 
        /* Teks pertanyaan */
        Label questionLabel = new Label("Apakah kamu yakin ingin keluar?");
        questionLabel.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 14px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );
        questionLabel.setWrapText(true);
        questionLabel.setAlignment(Pos.CENTER);
 
        Label subLabel = new Label("Kamu harus login kembali untuk\nmengakses akunmu.");
        subLabel.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: #6B7A99;" +
            "-fx-text-alignment: center;"
        );
        subLabel.setAlignment(Pos.CENTER);
        subLabel.setWrapText(true);
 
        body.getChildren().addAll(userRow, sep, questionLabel, subLabel);
 
        /* ---- TOMBOL ---- */
        HBox buttonRow = new HBox(12);
        buttonRow.setAlignment(Pos.CENTER);
        buttonRow.setPadding(new Insets(24, 28, 32, 28));
 
        /* Tombol Batal */
        Button cancelBtn = new Button("Batal");
        cancelBtn.setPrefWidth(160);
        cancelBtn.setPrefHeight(44);
        cancelBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #EAF4FF;" +
            "-fx-text-fill: #0d47a1;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #b3d4ff;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;"
        );
        cancelBtn.setOnMouseEntered(e -> cancelBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #d0e8ff;" +
            "-fx-text-fill: #0d47a1;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #0d47a1;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;"
        ));
        cancelBtn.setOnMouseExited(e -> cancelBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: #EAF4FF;" +
            "-fx-text-fill: #0d47a1;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-border-color: #b3d4ff;" +
            "-fx-border-width: 1.5;" +
            "-fx-border-radius: 12;"
        ));
 
        /* Tombol Ya, Keluar */
        Button confirmBtn = new Button("Ya, Keluar");
        confirmBtn.setPrefWidth(160);
        confirmBtn.setPrefHeight(44);
        confirmBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to right, #e53935, #c62828);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;"
        );
        confirmBtn.setOnMouseEntered(e -> confirmBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to right, #c62828, #b71c1c);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(198,40,40,0.45), 12, 0, 0, 4);"
        ));
        confirmBtn.setOnMouseExited(e -> confirmBtn.setStyle(
            "-fx-font-family: 'Poppins';" +
            "-fx-font-size: 13px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-color: linear-gradient(to right, #e53935, #c62828);" +
            "-fx-text-fill: white;" +
            "-fx-background-radius: 12;" +
            "-fx-cursor: hand;"
        ));
 
        buttonRow.getChildren().addAll(cancelBtn, confirmBtn);
 
        /* ---- SUSUN CARD ---- */
        card.getChildren().addAll(header, body, buttonRow);
 
        /* -------------------------------------------------- */
        /* ACTION                                              */
        /* -------------------------------------------------- */
        cancelBtn.setOnAction(e -> {
            confirmed = false;
            dialog.close();
        });
        confirmBtn.setOnAction(e -> {
            confirmed = true;
            dialog.close();
        });
 
        /* Klik overlay = batal */
        overlay.setOnMouseClicked(e -> {
            if (e.getTarget() == overlay) {
                confirmed = false;
                dialog.close();
            }
        });
 
        /* -------------------------------------------------- */
        /* LAYOUT                                              */
        /* -------------------------------------------------- */
        overlay.getChildren().add(card);
 
        Scene scene = new Scene(overlay);
        scene.setFill(Color.TRANSPARENT);
 
        try {
            scene.getStylesheets().add(
                getClass().getResource("/pariwisata/ui/style/theme.css").toExternalForm()
            );
        } catch (Exception ignored) {}
 
        dialog.setScene(scene);
 
        dialog.setWidth(owner.getWidth());
        dialog.setHeight(owner.getHeight());
        dialog.setX(owner.getX());
        dialog.setY(owner.getY());
 
        dialog.showAndWait();
        return confirmed;
    }
}