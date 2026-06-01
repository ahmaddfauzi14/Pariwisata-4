package pariwisata.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class SearchSection extends VBox {

    private final TextField searchField;

    private final Button allButton;

    private final Button wisataButton;

    private final Button kulinerButton;

    public SearchSection(){

        /*
         * =====================================================
         * ROOT
         * =====================================================
         */

        setSpacing(20);

        setPadding(new Insets(10,0,10,0));

        /*
         * =====================================================
         * SEARCH FIELD
         * =====================================================
         */

        searchField = new TextField();

        searchField.setPromptText(
                "Cari destinasi Makassar..."
        );

        searchField.getStyleClass().add(
                "search-field"
        );

        searchField.setPrefHeight(48);

        /*
         * =====================================================
         * FILTER BUTTON CONTAINER
         * =====================================================
         */

        HBox filterContainer =
                new HBox(12);

        filterContainer.setAlignment(
                Pos.CENTER_LEFT
        );

        /*
         * =====================================================
         * BUTTONS
         * =====================================================
         */

        allButton = new Button("Semua");

        wisataButton = new Button("Wisata");

        kulinerButton = new Button("Kuliner");

        /*
         * =====================================================
         * STYLE
         * =====================================================
         */

        allButton.getStyleClass().add(
                "filter-button-active"
        );

        wisataButton.getStyleClass().add(
                "filter-button"
        );

        kulinerButton.getStyleClass().add(
                "filter-button"
        );

        /*
         * =====================================================
         * BUTTON SIZE
         * =====================================================
         */

        allButton.setPrefHeight(42);

        wisataButton.setPrefHeight(42);

        kulinerButton.setPrefHeight(42);

        /*
         * =====================================================
         * ADD ALL
         * =====================================================
         */

        filterContainer.getChildren().addAll(

                allButton,

                wisataButton,

                kulinerButton
        );

        getChildren().addAll(

                searchField,

                filterContainer
        );
    }

    /*
     * =========================================================
     * GETTERS
     * =========================================================
     */

    public TextField getSearchField() {
        return searchField;
    }

    public Button getAllButton() {
        return allButton;
    }

    public Button getWisataButton() {
        return wisataButton;
    }

    public Button getKulinerButton() {
        return kulinerButton;
    }
}