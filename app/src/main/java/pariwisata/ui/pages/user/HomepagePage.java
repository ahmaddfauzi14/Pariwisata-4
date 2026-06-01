package pariwisata.ui.pages.user;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import pariwisata.dao.DestinationRepository;
import pariwisata.model.Destination;
import pariwisata.ui.components.DestinationCard;
import pariwisata.ui.components.HeroSection;
import pariwisata.ui.components.SearchSection;
import pariwisata.ui.components.UserSidebar;

import java.util.List;

public class HomepagePage {

    public void show(Stage stage) {

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        UserSidebar sidebar = new UserSidebar(stage, UserSidebar.Page.HOME);

        /*
         * =====================================================
         * MAIN CONTENT
         * =====================================================
         */

        VBox content = new VBox(35);

        content.setPadding(new Insets(30));

        content.getStyleClass().add("homepage-container");

        /*
         * =====================================================
         * HERO SECTION
         * =====================================================
         */

        HeroSection heroSection = new HeroSection();

        /*
         * =====================================================
         * SEARCH SECTION
         * =====================================================
         */

        SearchSection searchSection = new SearchSection();

        /*
         * =====================================================
         * REPOSITORY & DATA
         * =====================================================
         */

        DestinationRepository destinationRepository =
                new DestinationRepository();

        List<Destination> destinationList =
                destinationRepository.getAll();

        /*
         * =====================================================
         * WISATA SECTION
         * =====================================================
         */

        VBox wisataSection = new VBox(20);

        Label wisataTitle = new Label("Wisata Populer");

        wisataTitle.getStyleClass().add("section-title");

        FlowPane wisataContainer = new FlowPane();

        wisataContainer.setHgap(20);

        wisataContainer.setVgap(20);

        /*
         * =====================================================
         * KULINER SECTION
         * =====================================================
         */

        VBox kulinerSection = new VBox(20);

        Label kulinerTitle = new Label("Kuliner Populer");

        kulinerTitle.getStyleClass().add("section-title");

        FlowPane kulinerContainer = new FlowPane();

        kulinerContainer.setHgap(20);

        kulinerContainer.setVgap(20);

        /*
         * =====================================================
         * LOAD AWAL
         * =====================================================
         */

        loadDestinations(stage, destinationList,
                wisataContainer, kulinerContainer,
                wisataSection, kulinerSection, "semua");

        /*
         * =====================================================
         * SEARCH LISTENER
         * =====================================================
         */

        searchSection.getSearchField()
                .textProperty()
                .addListener((obs, oldVal, newVal) -> {

                    wisataContainer.getChildren().clear();
                    kulinerContainer.getChildren().clear();

                    for (Destination destination : destinationList) {

                        if (destination.getName() == null) continue;

                        String name    = destination.getName().toLowerCase();
                        String keyword = newVal.toLowerCase();

                        if (!name.contains(keyword)) continue;

                        DestinationCard card =
                                new DestinationCard(stage, destination);

                        if (destination.getCategory() != null &&
                                destination.getCategory().equalsIgnoreCase("wisata")) {
                            wisataContainer.getChildren().add(card);
                        }

                        if (destination.getCategory() != null &&
                                destination.getCategory().equalsIgnoreCase("kuliner")) {
                            kulinerContainer.getChildren().add(card);
                        }
                    }
                });

        /*
         * =====================================================
         * FILTER BUTTONS
         * =====================================================
         */

        searchSection.getAllButton().setOnAction(e ->
                loadDestinations(stage, destinationList,
                        wisataContainer, kulinerContainer,
                        wisataSection, kulinerSection, "semua"));

        searchSection.getWisataButton().setOnAction(e ->
                loadDestinations(stage, destinationList,
                        wisataContainer, kulinerContainer,
                        wisataSection, kulinerSection, "wisata"));

        searchSection.getKulinerButton().setOnAction(e ->
                loadDestinations(stage, destinationList,
                        wisataContainer, kulinerContainer,
                        wisataSection, kulinerSection, "kuliner"));

        /*
         * =====================================================
         * ASSEMBLE SECTIONS
         * =====================================================
         */

        wisataSection.getChildren().addAll(wisataTitle, wisataContainer);

        kulinerSection.getChildren().addAll(kulinerTitle, kulinerContainer);

        content.getChildren().addAll(
                heroSection,
                searchSection,
                wisataSection,
                kulinerSection
        );

        /*
         * =====================================================
         * SCROLL PANE
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

        /*
         * =====================================================
         * SCENE & CSS
         * =====================================================
         */

        Scene scene = new Scene(root, 1400, 850);

        scene.getStylesheets().addAll(
                getClass().getResource(
                        "/pariwisata/ui/style/theme.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/sidebar.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/homepage.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/wisata.css").toExternalForm(),
                getClass().getResource(
                        "/pariwisata/ui/style/detail.css").toExternalForm()
        );

        stage.setTitle("WisataRasa Makassar");
        stage.setScene(scene);
        stage.show();
    }

    /*
     * =========================================================
     * HELPER: LOAD DESTINATIONS
     * =========================================================
     */

    private void loadDestinations(Stage stage,
                                   List<Destination> destinationList,
                                   FlowPane wisataContainer,
                                   FlowPane kulinerContainer,
                                   VBox wisataSection,
                                   VBox kulinerSection,
                                   String filter) {

        // Tampilkan / sembunyikan section sesuai filter
        boolean showWisata  = filter.equals("semua") || filter.equals("wisata");
        boolean showKuliner = filter.equals("semua") || filter.equals("kuliner");

        wisataSection.setVisible(showWisata);
        wisataSection.setManaged(showWisata);
        kulinerSection.setVisible(showKuliner);
        kulinerSection.setManaged(showKuliner);

        wisataContainer.getChildren().clear();
        kulinerContainer.getChildren().clear();

        boolean wisataFound  = false;
        boolean kulinerFound = false;

        for (Destination destination : destinationList) {

            if (destination.getCategory() == null) continue;

            if (destination.getCategory().equalsIgnoreCase("wisata") && showWisata) {
                wisataFound = true;
                wisataContainer.getChildren().add(
                        new DestinationCard(stage, destination));
            }

            if (destination.getCategory().equalsIgnoreCase("kuliner") && showKuliner) {
                kulinerFound = true;
                kulinerContainer.getChildren().add(
                        new DestinationCard(stage, destination));
            }
        }

        if (!wisataFound && showWisata) {
            Label e = new Label("Belum ada data wisata");
            e.getStyleClass().add("empty-title");
            wisataContainer.getChildren().add(e);
        }

        if (!kulinerFound && showKuliner) {
            Label e = new Label("Belum ada data kuliner");
            e.getStyleClass().add("empty-title");
            kulinerContainer.getChildren().add(e);
        }
    }
}