package pariwisata.ui.pages.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import pariwisata.service.DestinationService;
import pariwisata.service.UserService;
import pariwisata.model.Destination;
import pariwisata.model.User;
import pariwisata.ui.components.AdminSidebar;
import pariwisata.util.Session;

import java.util.List;

public class AdminDashboardPage {

    public Parent getView(Stage stage) {

        /*
         * =====================================================
         * SIDEBAR
         * =====================================================
         */

        AdminSidebar sidebar = new AdminSidebar(stage, AdminSidebar.Page.DASHBOARD);

        /*
         * =====================================================
         * LOAD DATA
         * =====================================================
         */

        DestinationService destinationService = new DestinationService();
        List<Destination> allDestinations = destinationService.getAllDestinations();
        List<User>        allUsers        = UserService.getAll();

        int totalDestinasi = allDestinations == null ? 0 : allDestinations.size();
        int totalWisata    = 0;
        int totalKuliner   = 0;

        if (allDestinations != null) {
            for (Destination d : allDestinations) {
                if (d.getCategory() != null) {
                    if (d.getCategory().equalsIgnoreCase("wisata"))   totalWisata++;
                    if (d.getCategory().equalsIgnoreCase("kuliner"))  totalKuliner++;
                }
            }
        }

        int totalUsers = allUsers == null ? 0 : allUsers.size();
        int totalAdmin = 0;
        if (allUsers != null) {
            for (User u : allUsers) {
                if ("admin".equalsIgnoreCase(u.getRole())) totalAdmin++;
            }
        }

        /*
         * =====================================================
         * CONTENT ROOT
         * =====================================================
         */

        VBox content = new VBox(32);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("admin-root");

        /*
         * =====================================================
         * HEADER
         * =====================================================
         */

        VBox header = new VBox(4);

        Label welcome = new Label("Selamat datang, " + Session.getDisplayName() + "!");
        welcome.setStyle(
            "-fx-font-size: 26px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );

        Label subtitle = new Label("Berikut adalah ringkasan data aplikasi WisataRasa Makassar.");
        subtitle.setStyle(
            "-fx-font-size: 14px;" +
            "-fx-text-fill: #6B7280;"
        );

        header.getChildren().addAll(welcome, subtitle);

        /*
         * =====================================================
         * STAT CARDS  — row 1
         * =====================================================
         */

        Label statsTitle = new Label("Statistik Utama");
        statsTitle.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );

        FlowPane statRow = new FlowPane(20, 20);

        VBox cardTotal     = buildStatCard("Total Destinasi", String.valueOf(totalDestinasi),
                                           "Semua data wisata & kuliner", "#0D6EFD", "#EAF4FF");
        VBox cardWisata    = buildStatCard("Wisata",          String.valueOf(totalWisata),
                                           "Tempat wisata aktif",         "#16A34A", "#ECFDF5");
        VBox cardKuliner   = buildStatCard("Kuliner",         String.valueOf(totalKuliner),
                                           "Kuliner & restoran",          "#F59E0B", "#FFFBEB");
        VBox cardUsers     = buildStatCard("Pengguna",        String.valueOf(totalUsers),
                                           "Total akun terdaftar",        "#8B5CF6", "#F5F3FF");

        cardTotal.setMinWidth(200);
        cardWisata.setMinWidth(200);
        cardKuliner.setMinWidth(200);
        cardUsers.setMinWidth(200);

        statRow.getChildren().addAll(cardTotal, cardWisata, cardKuliner, cardUsers);

        /*
         * =====================================================
         * RECENT DESTINATIONS TABLE
         * =====================================================
         */

        VBox recentSection = new VBox(14);

        Label recentTitle = new Label("Data Destinasi Terbaru");
        recentTitle.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );

        VBox tableBox = new VBox(0);
        tableBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #E8F0FA;" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;"
        );
        tableBox.getStyleClass().add("admin-table");

        /* header row */
        HBox tableHeader = buildTableRow(
            "Nama Destinasi", "Kategori", "Alamat", "Status", true
        );
        tableBox.getChildren().add(tableHeader);

        /* data rows (max 8 terbaru) */
        if (allDestinations != null && !allDestinations.isEmpty()) {
            int limit = Math.min(8, allDestinations.size());
            for (int i = 0; i < limit; i++) {
                Destination d = allDestinations.get(i);
                HBox row = buildTableRow(
                    d.getName(),
                    d.getCategory() == null ? "-" : capitalize(d.getCategory()),
                    d.getAddress() == null   ? "-" : d.getAddress(),
                    d.getOperationalStatus() == null ? "-" : d.getOperationalStatus(),
                    false
                );
                if (i % 2 == 1) row.setStyle("-fx-background-color: #F8FAFF;");
                tableBox.getChildren().add(row);
            }
        } else {
            Label empty = new Label("Belum ada data destinasi.");
            empty.setStyle("-fx-padding: 20; -fx-text-fill: #9CA3AF; -fx-font-size: 14px;");
            tableBox.getChildren().add(empty);
        }

        recentSection.getChildren().addAll(recentTitle, tableBox);

        /*
         * =====================================================
         * USER LIST SECTION
         * =====================================================
         */

        VBox userSection = new VBox(14);

        Label userTitle = new Label("Pengguna Terdaftar");
        userTitle.setStyle(
            "-fx-font-size: 17px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #1B1F3B;"
        );

        VBox userTable = new VBox(0);
        userTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #E8F0FA;" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;"
        );

        HBox userHeader = buildUserTableRow("Username", "Email", "Role", true);
        userTable.getChildren().add(userHeader);

        if (allUsers != null && !allUsers.isEmpty()) {
            int limit = Math.min(6, allUsers.size());
            for (int i = 0; i < limit; i++) {
                User u = allUsers.get(i);
                HBox row = buildUserTableRow(
                    u.getUsername() == null ? "-" : u.getUsername(),
                    u.getEmail()    == null ? "-" : u.getEmail(),
                    u.getRole()     == null ? "user" : capitalize(u.getRole()),
                    false
                );
                if (i % 2 == 1) row.setStyle("-fx-background-color: #F8FAFF;");
                userTable.getChildren().add(row);
            }
        } else {
            Label empty = new Label("Belum ada pengguna terdaftar.");
            empty.setStyle("-fx-padding: 20; -fx-text-fill: #9CA3AF; -fx-font-size: 14px;");
            userTable.getChildren().add(empty);
        }

        userSection.getChildren().addAll(userTitle, userTable);

        /*
         * =====================================================
         * COMPOSE CONTENT
         * =====================================================
         */

        content.getChildren().addAll(
            header,
            statsTitle,
            statRow,
            recentSection,
            userSection
        );

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

        /*
         * =====================================================
         * ROOT LAYOUT
         * =====================================================
         */

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(scrollPane);
        root.getStyleClass().add("admin-root");

        root.getStylesheets().addAll(
            getClass().getResource("/pariwisata/ui/style/theme.css").toExternalForm(),
            getClass().getResource("/pariwisata/ui/style/sidebar.css").toExternalForm(),
            getClass().getResource("/pariwisata/ui/style/admin.css").toExternalForm()
        );

        return root;
    }

    /* =========================================================
     * HELPERS
     * ========================================================= */

    private VBox buildStatCard(String title, String value, String sub, String accentColor, String bgColor) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(24));
        card.getStyleClass().add("stat-card");

        Label titleLbl = new Label(title);
        titleLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #6B7280; -fx-font-weight: bold;");

        Label valueLbl = new Label(value);
        valueLbl.setStyle(
            "-fx-font-size: 38px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: " + accentColor + ";"
        );

        Label subLbl = new Label(sub);
        subLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #9CA3AF;");

        /* accent strip top */
        Region strip = new Region();
        strip.setPrefHeight(4);
        strip.setMaxWidth(48);
        strip.setStyle(
            "-fx-background-color: " + accentColor + ";" +
            "-fx-background-radius: 4;"
        );

        card.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 16;" +
            "-fx-padding: 24;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.06), 10, 0, 0, 4);"
        );

        card.getChildren().addAll(strip, titleLbl, valueLbl, subLbl);
        return card;
    }

    private HBox buildTableRow(String col1, String col2, String col3, String col4, boolean isHeader) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));

        Label l1 = makeCell(col1, isHeader, 2.5);
        Label l2 = makeCell(col2, isHeader, 1);
        Label l3 = makeCell(col3, isHeader, 2);
        Label l4 = makeCell(col4, isHeader, 1);

        HBox.setHgrow(l1, Priority.ALWAYS);
        HBox.setHgrow(l2, Priority.NEVER);
        HBox.setHgrow(l3, Priority.ALWAYS);
        HBox.setHgrow(l4, Priority.NEVER);

        l1.setPrefWidth(280);
        l2.setPrefWidth(110);
        l3.setPrefWidth(220);
        l4.setPrefWidth(110);

        l1.setMinWidth(50);
        l2.setMinWidth(50);
        l3.setMinWidth(50);
        l4.setMinWidth(50);

        row.getChildren().addAll(l1, l2, l3, l4);

        if (isHeader) {
            row.setStyle(
                "-fx-background-color: #F1F5F9;" +
                "-fx-background-radius: 16 16 0 0;"
            );
        }

        return row;
    }

    private HBox buildUserTableRow(String col1, String col2, String col3, boolean isHeader) {
        HBox row = new HBox();
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));

        Label l1 = makeCell(col1, isHeader, 1);
        Label l2 = makeCell(col2, isHeader, 2);
        Label l3 = makeCell(col3, isHeader, 1);

        l1.setPrefWidth(180);
        l2.setPrefWidth(280);
        l3.setPrefWidth(100);

        l1.setMinWidth(50);
        l2.setMinWidth(50);
        l3.setMinWidth(50);

        HBox.setHgrow(l1, Priority.ALWAYS);
        HBox.setHgrow(l2, Priority.ALWAYS);
        HBox.setHgrow(l3, Priority.NEVER);

        row.getChildren().addAll(l1, l2, l3);

        if (isHeader) {
            row.setStyle(
                "-fx-background-color: #F1F5F9;" +
                "-fx-background-radius: 16 16 0 0;"
            );
        }

        return row;
    }

    private Label makeCell(String text, boolean header, double weight) {
        Label l = new Label(text);
        l.setWrapText(false);
        if (header) {
            l.setStyle(
                "-fx-font-size: 12px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #6B7280;" +
                "-fx-letter-spacing: 0.5px;"
            );
        } else {
            l.setStyle("-fx-font-size: 13px; -fx-text-fill: #374151;");
        }
        return l;
    }

    private String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
