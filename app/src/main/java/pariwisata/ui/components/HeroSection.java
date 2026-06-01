package pariwisata.ui.components;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class HeroSection extends StackPane {

    private static final String[] HERO_IMAGES = {
        "/pariwisata/assets/images/hero/hero-losari.jpg",
        "/pariwisata/assets/images/hero/hero-99kubah.jpg",
        "/pariwisata/assets/images/hero/hero-rotterdam.jpg"
    };

    private static final double HERO_WIDTH = 1100;
    private static final double HERO_HEIGHT = 350;

    private static final double SLIDE_INTERVAL_SECONDS = 4.0;
    private static final double FADE_DURATION_SECONDS = 1.0;

    private final Image[] images = new Image[HERO_IMAGES.length];
    private int currentIndex = 0;
    private Timeline slideTimer;

    public HeroSection() {

        setPrefSize(HERO_WIDTH, HERO_HEIGHT);
        setMinSize(HERO_WIDTH, HERO_HEIGHT);
        setMaxSize(HERO_WIDTH, HERO_HEIGHT);

        getStyleClass().add("hero-section");

        /*
         * =====================================================
         * LOAD IMAGES
         * =====================================================
         */

        for (int i = 0; i < HERO_IMAGES.length; i++) {
            try {
                Image image = new Image(
                    getClass().getResourceAsStream(HERO_IMAGES[i])
                );

                images[i] = (image.isError() || image.getWidth() == 0)
                    ? null
                    : image;

            } catch (Exception e) {
                images[i] = null;
            }
        }

        /*
         * =====================================================
         * CLIP SELURUH HERO SECTION
         * =====================================================
         */

        Rectangle clip = new Rectangle(HERO_WIDTH, HERO_HEIGHT);
        clip.setArcWidth(60);
        clip.setArcHeight(60);
        setClip(clip);

        /*
         * =====================================================
         * HERO IMAGE
         * =====================================================
         */

        ImageView heroImage = new ImageView();

        heroImage.setFitWidth(HERO_WIDTH);
        heroImage.setFitHeight(HERO_HEIGHT);
        heroImage.setPreserveRatio(false);
        heroImage.setSmooth(true);

        if (images[0] != null) {
            heroImage.setImage(images[0]);
            applyCenterCrop(heroImage, images[0]);
        }

        /*
         * =====================================================
         * OVERLAY
         * =====================================================
         */

        Pane overlay = new Pane();

        overlay.setPrefSize(HERO_WIDTH, HERO_HEIGHT);
        overlay.setMinSize(HERO_WIDTH, HERO_HEIGHT);
        overlay.setMaxSize(HERO_WIDTH, HERO_HEIGHT);

        overlay.setStyle(
            "-fx-background-color: rgba(0,30,80,0.48);"
        );

        /*
         * =====================================================
         * TEXT
         * =====================================================
         */

        VBox textContainer = new VBox(12);

        textContainer.setPrefSize(HERO_WIDTH, HERO_HEIGHT);
        textContainer.setPadding(new Insets(40));
        textContainer.setAlignment(Pos.BOTTOM_LEFT);

        Label title = new Label("Jelajahi Keindahan Makassar");

        title.setStyle(
            "-fx-font-size: 34px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: white;"
        );

        Label subtitle = new Label(
            "Temukan wisata dan kuliner terbaik Kota Makassar"
        );

        subtitle.setStyle(
            "-fx-font-size: 16px;" +
            "-fx-text-fill: white;"
        );

        textContainer.getChildren().addAll(title, subtitle);

        getChildren().addAll(heroImage, overlay, textContainer);

        /*
         * =====================================================
         * SLIDESHOW
         * =====================================================
         */

        slideTimer = new Timeline(
            new KeyFrame(
                Duration.seconds(SLIDE_INTERVAL_SECONDS),
                e -> {

                    int nextIndex =
                        (currentIndex + 1) % images.length;

                    int tried = 0;

                    while (images[nextIndex] == null
                        && tried < images.length) {

                        nextIndex =
                            (nextIndex + 1) % images.length;

                        tried++;
                    }

                    if (images[nextIndex] == null
                        || nextIndex == currentIndex) {
                        return;
                    }

                    final int target = nextIndex;

                    FadeTransition fadeOut =
                        new FadeTransition(
                            Duration.seconds(
                                FADE_DURATION_SECONDS / 2
                            ),
                            heroImage
                        );

                    fadeOut.setFromValue(1.0);
                    fadeOut.setToValue(0.0);

                    fadeOut.setOnFinished(ev -> {

                        heroImage.setImage(images[target]);
                        applyCenterCrop(
                            heroImage,
                            images[target]
                        );

                        currentIndex = target;

                        FadeTransition fadeIn =
                            new FadeTransition(
                                Duration.seconds(
                                    FADE_DURATION_SECONDS / 2
                                ),
                                heroImage
                            );

                        fadeIn.setFromValue(0.0);
                        fadeIn.setToValue(1.0);

                        fadeIn.play();
                    });

                    fadeOut.play();
                }
            )
        );

        slideTimer.setCycleCount(Timeline.INDEFINITE);
        slideTimer.play();

        sceneProperty().addListener(
            (obs, oldScene, newScene) -> {
                if (newScene == null
                    && slideTimer != null) {
                    slideTimer.stop();
                }
            }
        );
    }

    private void applyCenterCrop(
        ImageView imageView,
        Image image
    ) {

        double imageRatio =
            image.getWidth() / image.getHeight();

        double heroRatio =
            HERO_WIDTH / HERO_HEIGHT;

        if (imageRatio > heroRatio) {

            double cropWidth =
                image.getHeight() * heroRatio;

            double x =
                (image.getWidth() - cropWidth) / 2;

            imageView.setViewport(
                new Rectangle2D(
                    x,
                    0,
                    cropWidth,
                    image.getHeight()
                )
            );

        } else {

            double cropHeight =
                image.getWidth() / heroRatio;

            double y =
                (image.getHeight() - cropHeight) / 2;

            imageView.setViewport(
                new Rectangle2D(
                    0,
                    y,
                    image.getWidth(),
                    cropHeight
                )
            );
        }
    }
}