package pariwisata.ui.pages.user;

import java.util.List;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import pariwisata.App;
import pariwisata.model.Destination;
import pariwisata.model.Medsos;
import pariwisata.model.Review;
import pariwisata.service.MedsosService;
import pariwisata.service.ReviewService;
import pariwisata.service.WishlistService;
import pariwisata.ui.components.UserSidebar;
import pariwisata.util.ImageHelper;
import pariwisata.util.Session;

public class DetailDestinationPage {

    private final MedsosService medsosService = new MedsosService();
    private final ReviewService reviewService = new ReviewService();
    private final WishlistService wishlistService = new WishlistService();

    public Parent getView(Stage stage, Destination destination) {

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        UserSidebar sidebar = new UserSidebar(stage, UserSidebar.Page.HOME);

        /*
         * =====================================================
         * ROOT CONTENT
         * =====================================================
         */

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.getStyleClass().add("detail-container");

        /*
         * =====================================================
         * HERO IMAGE
         * =====================================================
         */

        StackPane imageContainer = new StackPane();
        imageContainer.setPrefHeight(450);
        imageContainer.setMaxWidth(Double.MAX_VALUE);
        imageContainer.getStyleClass().add("detail-image-container");

        ImageView imageView = new ImageView();
        imageView.setFitHeight(450);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(true);

        // Bind image width dynamically to the content width minus padding to prevent
        // horizontal scroll
        imageView.fitWidthProperty().bind(content.widthProperty().subtract(60));

        loadDetailImage(imageView, destination.getPhotoUrl());

        Rectangle clip = new Rectangle();
        clip.setHeight(450);
        clip.setArcWidth(30);
        clip.setArcHeight(30);
        // Bind clip width to match the imageView width dynamically
        clip.widthProperty().bind(imageView.fitWidthProperty());
        imageView.setClip(clip);

        /*
         * =====================================================
         * WISHLIST BADGE di atas gambar (pojok kanan atas)
         * =====================================================
         */

        boolean[] isWishlist = { false };
        if (Session.isLoggedIn()) {
            try {
                isWishlist[0] = wishlistService.isDestinationInWishlist(
                        Session.getUser().getId(), destination.getId());
            } catch (Exception ignored) {
            }
        }

        Button wishlistBadge = new Button();
        wishlistBadge.setPrefSize(50, 50);
        wishlistBadge.setMinSize(50, 50);
        updateWishlistBadge(wishlistBadge, isWishlist[0]);
        StackPane.setAlignment(wishlistBadge, Pos.TOP_RIGHT);
        StackPane.setMargin(wishlistBadge, new Insets(16));

        if (Session.isLoggedIn()) {
            wishlistBadge.setOnAction(e -> {
                try {
                    if (isWishlist[0]) {
                        wishlistService.removeFromWishlist(
                                Session.getUser().getId(), destination.getId());
                        isWishlist[0] = false;
                    } else {
                        wishlistService.addToWishlist(
                                Session.getUser().getId(), destination.getId());
                        isWishlist[0] = true;
                    }
                    updateWishlistBadge(wishlistBadge, isWishlist[0]);
                } catch (Exception ex) {
                    System.out.println("Wishlist error: " + ex.getMessage());
                }
            });
        } else {
            wishlistBadge.setStyle(wishlistBadge.getStyle() + "-fx-opacity: 0.5;");
        }

        imageContainer.getChildren().addAll(imageView, wishlistBadge);

        /*
         * =====================================================
         * CATEGORY
         * =====================================================
         */

        Label categoryLabel = new Label(destination.getCategory());
        categoryLabel.setStyle(
                "-fx-background-color: #EAF4FF;" +
                        "-fx-text-fill: #0D6EFD;" +
                        "-fx-padding: 8 18 8 18;" +
                        "-fx-background-radius: 20;" +
                        "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;");

        /*
         * =====================================================
         * TITLE
         * =====================================================
         */

        Label titleLabel = new Label(destination.getName());
        titleLabel.getStyleClass().add("detail-title");

        /*
         * =====================================================
         * ADDRESS (dengan ikon ic-location)
         * =====================================================
         */

        HBox addressRow = new HBox(8);
        addressRow.setAlignment(Pos.CENTER_LEFT);
        ImageView locIcon = loadIcon("ic-location", 16);
        Label addressLabel = new Label(destination.getAddress());
        addressLabel.getStyleClass().add("subtitle");
        addressRow.getChildren().addAll(locIcon, addressLabel);

        /*
         * =====================================================
         * INFO ROW (harga, jam, status)
         * =====================================================
         */

        FlowPane infoRow = new FlowPane(30, 16);
        infoRow.setAlignment(Pos.CENTER_LEFT);

        // Harga
        HBox priceBox = buildInfoChip("ic-price", "Harga : Rp." + destination.getPrice(), "#0D6EFD", "#EAF4FF");

        // Jam Operasional
        HBox timeBox = buildInfoChip("ic-clock",
                "Jam : " + destination.getOpenHour() + " - " + destination.getCloseHour(),
                "#1B1F3B", "#F1F5F9");

        // Status
        String statusColor = destination.getOperationalStatus().toLowerCase().contains("buka") ? "#16A34A" : "#DC2626";
        String statusBg = destination.getOperationalStatus().toLowerCase().contains("buka") ? "#DCFCE7" : "#FEE2E2";
        HBox statusBox = buildInfoChip("", "Status : " + destination.getOperationalStatus(), statusColor, statusBg);

        infoRow.getChildren().addAll(priceBox, timeBox, statusBox);

        /*
         * =====================================================
         * DESCRIPTION
         * =====================================================
         */

        Label descTitle = new Label("Deskripsi");
        descTitle.getStyleClass().add("section-title");

        Label descriptionLabel = new Label(destination.getDescription());
        descriptionLabel.setWrapText(true);
        descriptionLabel.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-text-fill: #1B1F3B;" +
                        "-fx-line-spacing: 8;");

        /*
         * =====================================================
         * SOCIAL MEDIA SECTION
         * =====================================================
         */

        VBox socialSection = buildSocialMediaSection(destination);

        /*
         * =====================================================
         * REVIEW SECTION
         * =====================================================
         */

        VBox reviewSection = buildReviewSection(destination);

        /*
         * =====================================================
         * BUTTON CONTAINER
         * =====================================================
         */

        HBox buttonContainer = new HBox(15);
        buttonContainer.setAlignment(Pos.CENTER_LEFT);

        Button mapsButton = new Button();
        mapsButton.getStyleClass().add("primary-button");
        mapsButton.setPrefHeight(45);
        HBox mapsContent = new HBox(8);
        mapsContent.setAlignment(Pos.CENTER);

        ImageView mapsIcon = loadIcon("ic-location", 16);
        ColorAdjust whiteEffect = new ColorAdjust();
        whiteEffect.setBrightness(1.0);
        mapsIcon.setEffect(whiteEffect);

        Label mapsLabel = new Label("Google Maps");
        mapsLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        mapsContent.getChildren().addAll(mapsIcon, mapsLabel);
        mapsButton.setGraphic(mapsContent);

        Button backButton = new Button();
        backButton.getStyleClass().add("secondary-button");
        backButton.setPrefHeight(45);
        HBox backContent = new HBox(8);
        backContent.setAlignment(Pos.CENTER);

        Label backLabel = new Label("Kembali");
        backLabel.setStyle("-fx-text-fill: #0D6EFD; -fx-font-weight: bold;");

        backContent.getChildren().addAll(loadIcon("ic-logout", 16), backLabel);
        backButton.setGraphic(backContent);

        mapsButton.setOnAction(e -> {
            String url = destination.getMapUrl();
            if (url != null && !url.isEmpty()) {
                App.hostServices.showDocument(url);
            }
        });
        backButton.setOnAction(e -> new HomepagePage().show(stage));

        buttonContainer.getChildren().addAll(mapsButton, backButton);

        /*
         * =====================================================
         * ADD ALL
         * =====================================================
         */

        content.getChildren().addAll(
                imageContainer,
                categoryLabel,
                titleLabel,
                addressRow,
                infoRow,
                descTitle,
                descriptionLabel,
                socialSection,
                reviewSection,
                buttonContainer);

        /*
         * =====================================================
         * SCROLLPANE
         * =====================================================
         */

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white;");

        /*
         * =====================================================
         * ROOT LAYOUT
         * =====================================================
         */

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        root.getStyleClass().add("main-background");
        root.getStylesheets().addAll(
                getClass().getResource("/pariwisata/ui/style/theme.css").toExternalForm(),
                getClass().getResource("/pariwisata/ui/style/sidebar.css").toExternalForm(),
                getClass().getResource("/pariwisata/ui/style/detail.css").toExternalForm());

        return root;
    }

    /*
     * =====================================================
     * SOCIAL MEDIA SECTION
     * =====================================================
     */

    private VBox buildSocialMediaSection(Destination destination) {
        VBox section = new VBox(14);

        Label title = new Label("Media Sosial");
        title.getStyleClass().add("section-title");

        List<Medsos> medsosList = null;
        try {
            medsosList = medsosService.getMedsosbyDestination(destination.getId());
        } catch (Exception ignored) {
        }

        if (medsosList == null || medsosList.isEmpty()) {
            Label empty = new Label("Tidak ada media sosial yang terdaftar.");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
            section.getChildren().addAll(title, empty);
            return section;
        }

        FlowPane socialFlow = new FlowPane(10, 10);
        for (Medsos m : medsosList) {
            Button btn = new Button(m.getPlatformName());
            btn.getStyleClass().add("detail-social-button");

            String iconName = getPlatformIcon(m.getPlatformName());
            ImageView icon = loadIcon(iconName, 16);
            btn.setGraphic(icon);

            btn.setOnAction(e -> {
                String url = m.getUrl();
                if (url != null && !url.isEmpty()) {
                    if (!url.startsWith("http://") && !url.startsWith("https://")) {
                        url = "https://" + url;
                    }
                    App.hostServices.showDocument(url);
                }
            });
            socialFlow.getChildren().add(btn);
        }

        section.getChildren().addAll(title, socialFlow);
        return section;
    }

    private String getPlatformIcon(String platform) {
        if (platform == null)
            return "ic-wisata";
        String p = platform.toLowerCase();
        if (p.contains("instagram"))
            return "ic-instagram";
        if (p.contains("facebook"))
            return "ic-facebook";
        if (p.contains("youtube"))
            return "ic-youtube";
        if (p.contains("tiktok"))
            return "ic-tiktok";
        return "ic-wisata";
    }

    /*
     * =====================================================
     * REVIEW SECTION
     * =====================================================
     */

    private VBox buildReviewSection(Destination destination) {
        VBox section = new VBox(16);

        HBox headerRow = new HBox(16);
        headerRow.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label("Ulasan");
        title.getStyleClass().add("section-title");

        double avg = 0;
        List<Review> reviews = null;
        try {
            reviews = reviewService.getReviewsByDestination(destination.getId());
            avg = reviewService.getAverageRating(destination.getId());
        } catch (Exception ignored) {
        }

        Label ratingAvg = new Label(String.format("%.1f ★", avg));
        ratingAvg.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;" +
                        "-fx-text-fill: #F59E0B;");

        headerRow.getChildren().addAll(title, ratingAvg);

        VBox reviewList = new VBox(10);

        if (reviews == null || reviews.isEmpty()) {
            Label empty = new Label("Belum ada ulasan untuk destinasi ini.");
            empty.setStyle("-fx-font-size: 13px; -fx-text-fill: #94A3B8;");
            reviewList.getChildren().add(empty);
        } else {
            for (Review r : reviews) {
                reviewList.getChildren().add(buildReviewCard(r));
            }
        }

        section.getChildren().addAll(headerRow, reviewList);

        if (Session.isLoggedIn()) {
            VBox[] formHolder = { null };
            formHolder[0] = buildReviewForm(destination, reviewList, formHolder);
            section.getChildren().add(formHolder[0]);
        } else {
            Label loginNote = new Label("Login untuk menambahkan ulasan.");
            loginNote.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748B; -fx-font-style: italic;");
            section.getChildren().add(loginNote);
        }

        return section;
    }

    private VBox buildReviewCard(Review review) {
        VBox card = new VBox(6);
        card.setPadding(new Insets(14));
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-background-radius: 14;" +
                        "-fx-border-color: #E8F0FA;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 14;" +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 6, 0, 0, 2);");

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++)
            stars.append(i < review.getRating() ? "★" : "☆");
        Label starsLabel = new Label(stars.toString());
        starsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #F59E0B;");

        Label commentLabel = new Label(review.getComment());
        commentLabel.setWrapText(true);
        commentLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #334155;");

        card.getChildren().addAll(starsLabel, commentLabel);
        return card;
    }

    private VBox buildReviewForm(Destination destination, VBox reviewList, VBox[] formHolder) {
        VBox form = new VBox(12);
        form.setPadding(new Insets(18));
        form.setStyle(
                "-fx-background-color: #F8FAFC;" +
                        "-fx-background-radius: 16;" +
                        "-fx-border-color: #E2E8F0;" +
                        "-fx-border-width: 1;" +
                        "-fx-border-radius: 16;");

        Label formTitle = new Label("Tulis Ulasan Anda");
        formTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        Label ratingLabel = new Label("Rating :");
        ratingLabel.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748B;");

        HBox starRow = new HBox(6);
        starRow.setAlignment(Pos.CENTER_LEFT);
        int[] selectedRating = { 0 };
        Label[] starLabels = new Label[5];

        for (int i = 0; i < 5; i++) {
            final int idx = i + 1;
            Label star = new Label("☆");
            star.setStyle("-fx-font-size: 28px; -fx-text-fill: #CBD5E1; -fx-cursor: hand;");
            star.setOnMouseClicked(e -> {
                selectedRating[0] = idx;
                for (int j = 0; j < 5; j++) {
                    starLabels[j].setText(j < idx ? "★" : "☆");
                    starLabels[j].setStyle("-fx-font-size: 28px; -fx-cursor: hand; -fx-text-fill: " +
                            (j < idx ? "#F59E0B;" : "#CBD5E1;"));
                }
            });
            starLabels[i] = star;
            starRow.getChildren().add(star);
        }

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Tulis komentar Anda (minimal 10 karakter)...");
        commentArea.setPrefRowCount(4);
        commentArea.setWrapText(true);
        commentArea.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: #CBD5E1;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10;");

        Label feedback = new Label("");
        feedback.setVisible(false);
        feedback.setWrapText(true);
        feedback.setStyle("-fx-font-size: 13px;");

        Button submitBtn = new Button();
        submitBtn.getStyleClass().add("primary-button");
        submitBtn.setPrefHeight(42);
        HBox submitContent = new HBox(8);
        submitContent.setAlignment(Pos.CENTER);

        ImageView submitIcon = loadIcon("ic-edit", 16);
        ColorAdjust whiteEffect = new ColorAdjust();
        whiteEffect.setBrightness(1.0);
        submitIcon.setEffect(whiteEffect);

        Label submitLabel = new Label("Kirim Ulasan");
        submitLabel.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");

        submitContent.getChildren().addAll(submitIcon, submitLabel);
        submitBtn.setGraphic(submitContent);

        submitBtn.setOnAction(e -> {
            try {
                int rating = selectedRating[0];
                String text = commentArea.getText().trim();

                reviewService.createReview(
                        Session.getUser().getId(),
                        destination.getId(),
                        rating,
                        text);

                Review newReview = new Review(
                        java.util.UUID.randomUUID().toString(),
                        Session.getUser().getId(),
                        destination.getId(),
                        rating,
                        text);
                reviewList.getChildren().add(buildReviewCard(newReview));

                selectedRating[0] = 0;
                for (Label sl : starLabels) {
                    sl.setText("☆");
                    sl.setStyle("-fx-font-size: 28px; -fx-cursor: hand; -fx-text-fill: #CBD5E1;");
                }
                commentArea.clear();

                feedback.setText("✓ Ulasan berhasil dikirim!");
                feedback.setStyle("-fx-font-size: 13px; -fx-text-fill: #16A34A;");
                feedback.setVisible(true);

            } catch (Exception ex) {
                feedback.setText("✗ " + ex.getMessage());
                feedback.setStyle("-fx-font-size: 13px; -fx-text-fill: #DC2626;");
                feedback.setVisible(true);
            }
        });

        form.getChildren().addAll(formTitle, ratingLabel, starRow, commentArea, feedback, submitBtn);
        return form;
    }

    /*
     * =====================================================
     * UTIL: info chip dengan ikon
     * =====================================================
     */

    private HBox buildInfoChip(String iconName, String text, String textColor, String bgColor) {
        HBox chip = new HBox(8);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.setPadding(new Insets(8, 16, 8, 16));
        chip.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                        "-fx-background-radius: 20;");
        ImageView icon = loadIcon(iconName, 16);
        Label label = new Label(text);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + textColor + ";");
        chip.getChildren().addAll(icon, label);
        return chip;
    }

    private void updateWishlistBadge(Button btn, boolean inWishlist) {
        if (inWishlist) {
            ImageView icon = loadIcon("ic-favorite-filled", 22);
            btn.setGraphic(icon);
            btn.setStyle(
                    "-fx-background-color: #FEE2E2;" +
                            "-fx-background-radius: 50;" +
                            "-fx-border-width: 0;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 8, 0, 0, 2);");
        } else {
            ImageView icon = loadIcon("ic-favorite", 22);
            btn.setGraphic(icon);
            btn.setStyle(
                    "-fx-background-color: rgba(255,255,255,0.9);" +
                            "-fx-background-radius: 50;" +
                            "-fx-border-color: #CBD5E1;" +
                            "-fx-border-radius: 50;" +
                            "-fx-border-width: 2;" +
                            "-fx-cursor: hand;" +
                            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.10), 8, 0, 0, 2);");
        }
    }

    private void loadDetailImage(ImageView imageView, String photoUrl) {
        if (photoUrl == null || photoUrl.isBlank()) {
            return;
        }

        final String finalUrl = ImageHelper.convertToDirectUrl(photoUrl);

        Thread thread = new Thread(() -> {
            try {
                // Load preserving ratio to avoid scaling issues and let the fitWidth bind do
                // the work
                Image image = new Image(finalUrl, 0, 450, true, true, false);
                if (!image.isError()) {
                    Platform.runLater(() -> imageView.setImage(image));
                } else {
                    System.out.println("Detail: gagal memuat gambar - " + finalUrl
                            + " | " + image.getException());
                }
            } catch (Exception e) {
                System.out.println("Detail: exception gambar - " + e.getMessage());
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
                            "/pariwisata/assets/icons/" + iconName + ".png"));
            iv.setImage(img);
        } catch (Exception e) {
            /* fallback */
        }
        iv.setFitWidth(size);
        iv.setFitHeight(size);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }

}
