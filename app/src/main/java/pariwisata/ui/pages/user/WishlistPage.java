package pariwisata.ui.pages.user;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import pariwisata.dao.DestinationRepository;
import pariwisata.model.Destination;
import pariwisata.model.Wishlist;
import pariwisata.service.WishlistService;
import pariwisata.util.ImageHelper;
import pariwisata.util.Session;
import pariwisata.ui.components.UserSidebar;

import java.util.List;

public class WishlistPage {

    public Parent getView(Stage stage) {

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        UserSidebar sidebar = new UserSidebar(stage, UserSidebar.Page.WISHLIST);

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
         * JUDUL
         * =====================================================
         */

        Label pageTitle = new Label("Wishlist Saya");

        pageTitle.getStyleClass().add("section-title");

        pageTitle.setStyle(
                "-fx-font-size: 26px;" +
                "-fx-font-weight: bold;"
        );

        /*
         * =====================================================
         * HANDLE TAMU / BELUM LOGIN
         * =====================================================
         */

        if (Session.isGuest() || !Session.isLoggedIn()) {

            VBox guestBox = new VBox(16);

            guestBox.setAlignment(Pos.CENTER);

            guestBox.setPadding(new Insets(60));

            Label msg = new Label(
                    "Kamu harus login untuk menyimpan wishlist."
            );

            msg.setStyle(
                    "-fx-font-size: 16px;" +
                    "-fx-text-fill: #6B7280;"
            );

            Button loginBtn = new Button("Login Sekarang");

            loginBtn.getStyleClass().add("primary-button");

            loginBtn.setPrefHeight(45);

            loginBtn.setOnAction(e ->
                    new pariwisata.ui.pages.auth.LoginPage().show(stage)
            );

            guestBox.getChildren().addAll(msg, loginBtn);

            content.getChildren().addAll(pageTitle, guestBox);

            ScrollPane sp = new ScrollPane(content);

            sp.setFitToWidth(true);

            sp.getStyleClass().add("scroll-pane");

            return sp;
        }

        /*
         * =====================================================
         * AMBIL DATA WISHLIST
         * =====================================================
         */

        WishlistService wishlistService = new WishlistService();

        DestinationRepository destinationRepository =
                new DestinationRepository();

        String userId = Session.getUser().getId();

        /*
         * =====================================================
         * CONTAINER CARD
         * =====================================================
         */

        FlowPane wishlistContainer = new FlowPane();

        wishlistContainer.setHgap(20);

        wishlistContainer.setVgap(20);

        /*
         * LOAD WISHLIST
         */

        Runnable loadWishlist = () -> {

            wishlistContainer.getChildren().clear();

            List<Wishlist> wishlists =
                    wishlistService.getUserWishlist(userId);

            if (wishlists == null || wishlists.isEmpty()) {

                Label emptyLabel = new Label(
                        "Belum ada destinasi di wishlist kamu."
                );

                emptyLabel.setStyle(
                        "-fx-font-size: 15px;" +
                        "-fx-text-fill: #6B7280;"
                );

                wishlistContainer.getChildren().add(emptyLabel);

                return;
            }

            for (Wishlist wishlist : wishlists) {

                Destination destination =
                        destinationRepository.getById(
                                wishlist.getDestinationId()
                        );

                if (destination == null) continue;

                /*
                 * KARTU WISHLIST
                 */

                VBox card = new VBox(12);

                card.setPrefWidth(300);

                card.setMaxWidth(300);

                card.setPadding(new Insets(16));

                card.setStyle(
                        "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #E0E7FF;" +
                        "-fx-border-radius: 14;" +
                        "-fx-border-width: 1;"
                );

                /*
                 * IMAGE
                 */

                ImageView imageView = new ImageView();

                imageView.setFitWidth(268);

                imageView.setFitHeight(160);

                imageView.setPreserveRatio(false);

                imageView.setSmooth(true);

                imageView.getStyleClass().add("card-image");

                try {

                    if (destination.getPhotoUrl() != null &&
                            !destination.getPhotoUrl().isEmpty()) {

                        String imgUrl = ImageHelper.convertToDirectUrl(destination.getPhotoUrl());
                        imageView.setImage(
                                new Image(imgUrl, true)
                        );
                    }

                } catch (Exception ignored) {}

                /*
                 * CATEGORY
                 */

                Label categoryLabel = new Label(
                        destination.getCategory()
                );

                categoryLabel.getStyleClass().add("category-label");

                /*
                 * TITLE
                 */

                Label titleLabel = new Label(
                        destination.getName()
                );

                titleLabel.getStyleClass().add("card-title");

                titleLabel.setWrapText(true);

                /*
                 * ADDRESS
                 */

                Label addressLabel = new Label(
                        destination.getAddress()
                );

                addressLabel.getStyleClass().add("card-subtitle");

                addressLabel.setWrapText(true);

                /*
                 * SPACER
                 */

                Region spacer = new Region();

                VBox.setVgrow(spacer, Priority.ALWAYS);

                /*
                 * TOMBOL HAPUS
                 */

                Button removeBtn = new Button("Hapus dari Wishlist");

                removeBtn.setMaxWidth(Double.MAX_VALUE);

                removeBtn.setPrefHeight(42);

                removeBtn.setStyle(
                        "-fx-background-color: #FEE2E2;" +
                        "-fx-text-fill: #DC3545;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-weight: bold;" +
                        "-fx-font-size: 13px;" +
                        "-fx-cursor: hand;"
                );

                final String destId = destination.getId();

                removeBtn.setOnAction(e -> {

                    try {

                        wishlistService.removeFromWishlist(userId, destId);

                        loadWishlistContainer(
                                wishlistContainer,
                                wishlistService,
                                destinationRepository,
                                userId,
                                stage
                        );

                    } catch (IllegalArgumentException ex) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Gagal");
                        alert.setHeaderText(null);
                        alert.setContentText(ex.getMessage());
                        alert.showAndWait();
                    }
                });

                /*
                 * TOMBOL DETAIL
                 */

                Button detailBtn = new Button("Lihat Detail");

                detailBtn.setMaxWidth(Double.MAX_VALUE);

                detailBtn.setPrefHeight(42);

                detailBtn.getStyleClass().add("primary-button");

                detailBtn.setOnAction(e -> {

                    DetailDestinationPage detailPage =
                            new DetailDestinationPage();

                    Scene scene = new Scene(
                            detailPage.getView(stage, destination),
                            1400,
                            850
                    );

                    scene.getStylesheets().addAll(
                            getClass().getResource(
                                    "/pariwisata/ui/style/theme.css"
                            ).toExternalForm(),
                            getClass().getResource(
                                    "/pariwisata/ui/style/detail.css"
                            ).toExternalForm()
                    );

                    stage.setScene(scene);

                    stage.show();
                });

                card.getChildren().addAll(
                        imageView,
                        categoryLabel,
                        titleLabel,
                        addressLabel,
                        spacer,
                        detailBtn,
                        removeBtn
                );

                wishlistContainer.getChildren().add(card);
            }
        };

        loadWishlist.run();

        /*
         * =====================================================
         * TAMBAH KE CONTENT
         * =====================================================
         */

        content.getChildren().addAll(
                pageTitle,
                wishlistContainer
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
                        "/pariwisata/ui/style/homepage.css").toExternalForm()
        );

        return root;
    }

    /*
     * =========================================================
     * HELPER: RELOAD WISHLIST CONTAINER
     * =========================================================
     */

    private void loadWishlistContainer(

            FlowPane container,

            WishlistService wishlistService,

            DestinationRepository destinationRepository,

            String userId,

            Stage stage

    ) {

        container.getChildren().clear();

        List<Wishlist> wishlists =
                wishlistService.getUserWishlist(userId);

        if (wishlists == null || wishlists.isEmpty()) {

            Label emptyLabel = new Label(
                    "Belum ada destinasi di wishlist kamu."
            );

            emptyLabel.setStyle(
                    "-fx-font-size: 15px;" +
                    "-fx-text-fill: #6B7280;"
            );

            container.getChildren().add(emptyLabel);

            return;
        }

        for (Wishlist wishlist : wishlists) {

            Destination destination =
                    destinationRepository.getById(
                            wishlist.getDestinationId()
                    );

            if (destination == null) continue;

            VBox card = new VBox(12);

            card.setPrefWidth(300);

            card.setMaxWidth(300);

            card.setPadding(new Insets(16));

            card.setStyle(
                    "-fx-background-color: white;" +
                    "-fx-background-radius: 14;" +
                    "-fx-border-color: #E0E7FF;" +
                    "-fx-border-radius: 14;" +
                    "-fx-border-width: 1;"
            );

            ImageView imageView = new ImageView();

            imageView.setFitWidth(268);

            imageView.setFitHeight(160);

            imageView.setPreserveRatio(false);

            imageView.getStyleClass().add("card-image");

            try {

                if (destination.getPhotoUrl() != null &&
                        !destination.getPhotoUrl().isEmpty()) {

                    String imgUrl = ImageHelper.convertToDirectUrl(destination.getPhotoUrl());
                    imageView.setImage(
                            new Image(imgUrl, true)
                    );
                }

            } catch (Exception ignored) {}

            Label categoryLabel = new Label(destination.getCategory());

            categoryLabel.getStyleClass().add("category-label");

            Label titleLabel = new Label(destination.getName());

            titleLabel.getStyleClass().add("card-title");

            titleLabel.setWrapText(true);

            Label addressLabel = new Label(destination.getAddress());

            addressLabel.getStyleClass().add("card-subtitle");

            addressLabel.setWrapText(true);

            Region spacer = new Region();

            VBox.setVgrow(spacer, Priority.ALWAYS);

            Button removeBtn = new Button("Hapus dari Wishlist");

            removeBtn.setMaxWidth(Double.MAX_VALUE);

            removeBtn.setPrefHeight(42);

            removeBtn.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                    "-fx-text-fill: #DC3545;" +
                    "-fx-background-radius: 10;" +
                    "-fx-font-weight: bold;" +
                    "-fx-font-size: 13px;" +
                    "-fx-cursor: hand;"
            );

            final String destId = destination.getId();

            removeBtn.setOnAction(e -> {

                try {

                    wishlistService.removeFromWishlist(userId, destId);

                    loadWishlistContainer(
                            container,
                            wishlistService,
                            destinationRepository,
                            userId,
                            stage
                    );

                } catch (IllegalArgumentException ex) {

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Gagal");
                    alert.setHeaderText(null);
                    alert.setContentText(ex.getMessage());
                    alert.showAndWait();
                }
            });

            Button detailBtn = new Button("Lihat Detail");

            detailBtn.setMaxWidth(Double.MAX_VALUE);

            detailBtn.setPrefHeight(42);

            detailBtn.getStyleClass().add("primary-button");

            detailBtn.setOnAction(e -> {

                DetailDestinationPage detailPage =
                        new DetailDestinationPage();

                Scene scene = new Scene(
                        detailPage.getView(stage, destination),
                        1400,
                        850
                );

                scene.getStylesheets().addAll(
                        getClass().getResource(
                                "/pariwisata/ui/style/theme.css"
                        ).toExternalForm(),
                        getClass().getResource(
                                "/pariwisata/ui/style/detail.css"
                        ).toExternalForm()
                );

                stage.setScene(scene);

                stage.show();
            });

            card.getChildren().addAll(
                    imageView,
                    categoryLabel,
                    titleLabel,
                    addressLabel,
                    spacer,
                    detailBtn,
                    removeBtn
            );

            container.getChildren().add(card);
        }
    }
}
