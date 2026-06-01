package pariwisata.ui.components;
 
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
 
import pariwisata.model.Destination;
import pariwisata.service.WishlistService;
import pariwisata.ui.pages.user.DetailDestinationPage;
import pariwisata.util.ImageHelper;
import pariwisata.util.Session;
 
public class DestinationCard extends VBox {
 
    public DestinationCard(
            Stage stage,
            Destination destination
    ){
 
        /*
         * =====================================================
         * ROOT CARD
         * =====================================================
         */
 
        setPrefWidth(300);
        setMaxWidth(300);
        setMinHeight(420);
        setSpacing(14);
        setPadding(new Insets(16));
        getStyleClass().add("modern-card");
 
        /*
         * =====================================================
         * IMAGE VIEW
         * =====================================================
         */
 
        ImageView imageView = new ImageView();
        imageView.setFitWidth(268);
        imageView.setFitHeight(180);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);
        imageView.getStyleClass().add("card-image");
 
        loadDestinationImage(imageView, destination.getPhotoUrl());
 
        /*
         * =====================================================
         * CATEGORY LABEL
         * =====================================================
         */
 
        Label categoryLabel = new Label(destination.getCategory());
        categoryLabel.getStyleClass().add("category-label");
 
        /*
         * =====================================================
         * TITLE
         * =====================================================
         */
 
        Label titleLabel = new Label(destination.getName());
        titleLabel.getStyleClass().add("card-title");
        titleLabel.setWrapText(true);
 
        /*
         * =====================================================
         * ADDRESS (dengan ikon ic-location)
         * =====================================================
         */
 
        HBox addressRow = new HBox(6);
        addressRow.setAlignment(Pos.CENTER_LEFT);
 
        ImageView locationIcon = loadIcon("ic-location", 14);
 
        Label addressLabel = new Label(destination.getAddress());
        addressLabel.getStyleClass().add("card-subtitle");
        addressLabel.setWrapText(true);
 
        addressRow.getChildren().addAll(locationIcon, addressLabel);
 
        /*
         * =====================================================
         * PRICE
         * =====================================================
         */
 
        HBox priceRow = new HBox(6);
        priceRow.setAlignment(Pos.CENTER_LEFT);
 
        ImageView starIcon = loadIcon("ic-price", 14);
 
        Label priceLabel = new Label(destination.getPrice());
        priceLabel.getStyleClass().add("price-label");
 
        priceRow.getChildren().addAll(starIcon, priceLabel);
 
        /*
         * =====================================================
         * OPERATIONAL STATUS
         * =====================================================
         */
 
        Label statusLabel = new Label(destination.getOperationalStatus());
        statusLabel.getStyleClass().add("status-label");
 
        /*
         * =====================================================
         * SPACER
         * =====================================================
         */
 
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
 
        /*
         * =====================================================
         * BUTTON ROW  (Lihat Detail + Wishlist icon)
         * =====================================================
         */
 
        HBox buttonRow = new HBox(10);
        buttonRow.setAlignment(Pos.CENTER_LEFT);
 
        Button detailButton = new Button("Lihat Detail");
        detailButton.setMaxWidth(Double.MAX_VALUE);
        detailButton.setPrefHeight(45);
        detailButton.getStyleClass().add("primary-button");
        HBox.setHgrow(detailButton, Priority.ALWAYS);
 
        /*
         * =====================================================
         * WISHLIST BUTTON dengan ic-favorite
         * =====================================================
         */
 
        WishlistService wishlistService = new WishlistService();
        boolean[] isInWishlist = {false};
 
        if (Session.isLoggedIn()) {
            try {
                isInWishlist[0] = wishlistService.isDestinationInWishlist(
                        Session.getUser().getId(), destination.getId());
            } catch (Exception ignored) {}
        }
 
        Button wishlistBtn = new Button();
        wishlistBtn.setPrefSize(45, 45);
        wishlistBtn.setMinSize(45, 45);
        updateWishlistButton(wishlistBtn, isInWishlist[0]);
        wishlistBtn.getStyleClass().add("wishlist-button");
 
        if (Session.isLoggedIn()) {
            wishlistBtn.setOnAction(e -> {
                try {
                    if (isInWishlist[0]) {
                        wishlistService.removeFromWishlist(
                                Session.getUser().getId(), destination.getId());
                        isInWishlist[0] = false;
                    } else {
                        wishlistService.addToWishlist(
                                Session.getUser().getId(), destination.getId());
                        isInWishlist[0] = true;
                    }
                    updateWishlistButton(wishlistBtn, isInWishlist[0]);
                } catch (Exception ex) {
                    System.out.println("Wishlist error: " + ex.getMessage());
                }
            });
        } else {
            wishlistBtn.setDisable(true);
            wishlistBtn.setStyle("-fx-opacity: 0.4;");
        }
 
        buttonRow.getChildren().addAll(detailButton, wishlistBtn);
 
        /*
         * =====================================================
         * BUTTON ACTION - navigate ke detail
         * =====================================================
         */
 
        detailButton.setOnAction(e -> {
            DetailDestinationPage detailPage = new DetailDestinationPage();
            Scene scene = new Scene(
                    detailPage.getView(stage, destination),
                    1400,
                    850
            );
 
            scene.getStylesheets().addAll(
                    getClass().getResource("/pariwisata/ui/style/theme.css").toExternalForm(),
                    getClass().getResource("/pariwisata/ui/style/homepage.css").toExternalForm(),
                    getClass().getResource("/pariwisata/ui/style/detail.css").toExternalForm()
            );
 
            stage.setScene(scene);
            stage.show();
        });
 
        /*
         * =====================================================
         * ADD ALL COMPONENT
         * =====================================================
         */
 
        getChildren().addAll(
                imageView,
                categoryLabel,
                titleLabel,
                addressRow,
                priceRow,
                statusLabel,
                spacer,
                buttonRow
        );
    }
 
    private void updateWishlistButton(Button btn, boolean inWishlist) {
        if (inWishlist) {
            ImageView icon = loadIcon("ic-favorite-filled", 20);
            btn.setStyle(
                "-fx-background-color: #FEE2E2;" +
                "-fx-background-radius: 12;" +
                "-fx-border-width: 0;" +
                "-fx-cursor: hand;"
            );
            btn.setGraphic(icon);
        } else {
            ImageView icon = loadIcon("ic-favorite", 20);
            btn.setStyle(
                "-fx-background-color: #F8FAFC;" +
                "-fx-background-radius: 12;" +
                "-fx-border-color: #CBD5E1;" +
                "-fx-border-radius: 12;" +
                "-fx-border-width: 1.5;" +
                "-fx-cursor: hand;"
            );
            btn.setGraphic(icon);
        }
    }
 
    private void loadDestinationImage(ImageView imageView, String photoUrl) {
        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        final String finalUrl = ImageHelper.convertToDirectUrl(photoUrl);

        Thread thread = new Thread(() -> {
            try {
                // Load tanpa background loading agar bisa deteksi error
                Image image = new Image(finalUrl, 268, 180, false, true, false);

                if (!image.isError()) {
                    Platform.runLater(() -> imageView.setImage(image));
                } else {
                    System.out.println("Gagal memuat gambar: " + finalUrl
                            + " | " + image.getException());
                    // Tidak ada gambar pengganti sesuai permintaan
                }
            } catch (Exception e) {
                System.out.println("Exception saat memuat gambar: " + e.getMessage());
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
 
    private ImageView loadIcon(String iconName, double size) {
        ImageView iv = new ImageView();
        try {
            Image img = new Image(
                getClass().getResourceAsStream(
                    "/pariwisata/assets/icons/" + iconName + ".png"
                )
            );
            iv.setImage(img);
        } catch (Exception e) {
            /* fallback: kosong */
        }
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }
}
 