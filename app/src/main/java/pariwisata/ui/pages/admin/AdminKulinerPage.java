package pariwisata.ui.pages.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import pariwisata.model.Destination;
import pariwisata.model.Medsos;
import pariwisata.service.DestinationService;
import pariwisata.service.MedsosService;
import pariwisata.ui.components.AdminSidebar;

import java.util.List;

public class AdminKulinerPage {

    private final DestinationService service       = new DestinationService();
    private final MedsosService      medsosService = new MedsosService();

    public Parent getView(Stage stage) {

        AdminSidebar sidebar = new AdminSidebar(stage, AdminSidebar.Page.KULINER);

        VBox content = new VBox(24);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("admin-root");

        /* ---- Header ---- */
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(4);
        Label title = new Label("Kelola Kuliner");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");
        Label sub = new Label("Tambah, edit, dan hapus data kuliner & restoran.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #6B7280;");
        headerText.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Tambah Kuliner");
        addBtn.setStyle(
            "-fx-background-color: #F59E0B;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 0 20 0 20;" +
            "-fx-pref-height: 42;"
        );

        headerRow.getChildren().addAll(headerText, spacer, addBtn);

        /* ---- Search ---- */
        TextField searchField = new TextField();
        searchField.setPromptText("Cari kuliner berdasarkan nama atau alamat...");
        searchField.setMaxWidth(420);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-font-size: 13px;"
        );

        /* ---- Table ---- */
        VBox tableContainer = new VBox(0);
        tableContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #E8F0FA;" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 3);"
        );

        Label feedback = new Label("");
        feedback.setVisible(false);
        feedback.setWrapText(true);

        loadKulinerTable(stage, tableContainer, feedback, null);

        searchField.textProperty().addListener((obs, o, nv) ->
            loadKulinerTable(stage, tableContainer, feedback, nv.trim())
        );

        addBtn.setOnAction(e ->
            showDestinationDialog(stage, null, tableContainer, feedback)
        );

        content.getChildren().addAll(headerRow, searchField, feedback, tableContainer);

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.getStyleClass().add("scroll-pane");

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
     * LOAD TABLE
     * ========================================================= */

    private void loadKulinerTable(Stage stage, VBox tableContainer, Label feedback, String keyword) {
        tableContainer.getChildren().clear();

        List<Destination> list;
        if (keyword == null || keyword.isEmpty()) {
            list = service.filterByCategory("kuliner");
        } else {
            list = service.searchDestinations(keyword);
            if (list != null) list.removeIf(d -> !"kuliner".equalsIgnoreCase(d.getCategory()));
        }

        tableContainer.getChildren().add(
            buildRow("Nama", "Alamat", "Harga", "Status", "Jam", null, true, stage, tableContainer, feedback)
        );

        if (list == null || list.isEmpty()) {
            Label empty = new Label("Tidak ada data kuliner ditemukan.");
            empty.setStyle("-fx-padding: 24 20; -fx-text-fill: #9CA3AF; -fx-font-size: 14px;");
            tableContainer.getChildren().add(empty);
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            Destination d = list.get(i);
            HBox row = buildRow(
                d.getName(),
                d.getAddress() == null ? "-" : d.getAddress(),
                d.getPrice() == null || d.getPrice().isEmpty() ? "Gratis" : "Rp " + d.getPrice(),
                d.getOperationalStatus() == null ? "-" : d.getOperationalStatus(),
                (d.getOpenHour() == null ? "-" : d.getOpenHour()) + " – " + (d.getCloseHour() == null ? "-" : d.getCloseHour()),
                d, false, stage, tableContainer, feedback
            );
            if (i % 2 == 1) row.setStyle("-fx-background-color: #FFFBEB;");
            tableContainer.getChildren().add(row);
        }
    }

    private HBox buildRow(String c1, String c2, String c3, String c4, String c5,
                           Destination data, boolean isHeader, Stage stage,
                           VBox tableContainer, Label feedback) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(11, 20, 11, 20));

        Label l1 = cell(c1, isHeader); l1.setPrefWidth(200); l1.setMinWidth(50); HBox.setHgrow(l1, Priority.ALWAYS);
        Label l2 = cell(c2, isHeader); l2.setPrefWidth(190); l2.setMinWidth(50); HBox.setHgrow(l2, Priority.ALWAYS);
        Label l3 = cell(c3, isHeader); l3.setPrefWidth(110); l3.setMinWidth(50); HBox.setHgrow(l3, Priority.ALWAYS);
        Label l4 = cell(c4, isHeader); l4.setPrefWidth(100); l4.setMinWidth(50); HBox.setHgrow(l4, Priority.ALWAYS);
        Label l5 = cell(c5, isHeader); l5.setPrefWidth(130); l5.setMinWidth(50); HBox.setHgrow(l5, Priority.ALWAYS);
        row.getChildren().addAll(l1, l2, l3, l4, l5);

        if (!isHeader && data != null) {
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Button editBtn = actionButton("Edit",  "#F59E0B");
            Button delBtn  = actionButton("Hapus", "#DC3545");

            editBtn.setOnAction(e -> showDestinationDialog(stage, data, tableContainer, feedback));
            delBtn.setOnAction(e -> {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus \"" + data.getName() + "\"?", ButtonType.YES, ButtonType.NO);
                a.setHeaderText(null);
                a.showAndWait().ifPresent(btn -> {
                    if (btn == ButtonType.YES) {
                        boolean ok = service.deleteDestination(data.getId());
                        showFeedback(feedback, ok ? "Dihapus." : "Gagal.", ok);
                        loadKulinerTable(stage, tableContainer, feedback, null);
                    }
                });
            });

            row.getChildren().addAll(sp, new HBox(8, editBtn, delBtn));
        } else if (isHeader) {
            Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            row.getChildren().addAll(sp, cell("Aksi", true));
            row.setStyle("-fx-background-color: #FEF9EC; -fx-background-radius: 16 16 0 0;");
        }
        return row;
    }

    /* =========================================================
     * DIALOG
     * ========================================================= */

    private void showDestinationDialog(Stage stage, Destination existing,
                                        VBox tableContainer, Label feedback) {
        boolean isEdit = existing != null;

        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");

        VBox dialog = new VBox(20);
        dialog.setMaxWidth(560);
        dialog.setPadding(new Insets(32));
        dialog.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 30, 0, 0, 8);"
        );

        Label dlgTitle = new Label(isEdit ? "Edit Kuliner" : "Tambah Kuliner Baru");
        dlgTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        TextField nameField    = field("Nama Kuliner *");
        TextField addressField = field("Alamat *");
        TextArea  descField    = new TextArea(isEdit && existing.getDescription() != null ? existing.getDescription() : "");
        descField.setPromptText("Deskripsi *");
        descField.setPrefRowCount(3);
        descField.setStyle(fieldStyle());
        TextField priceField   = field("Harga rata-rata");
        TextField mapField     = field("URL Google Maps");
        TextField photoField   = field("URL Foto");
        TextField openField    = field("Jam Buka");
        TextField closeField   = field("Jam Tutup");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Buka", "Tutup", "Tutup Sementara");
        statusBox.setPromptText("Status");
        statusBox.setMaxWidth(Double.MAX_VALUE);
        statusBox.setStyle(fieldStyle());

        if (isEdit) {
            nameField.setText(orEmpty(existing.getName()));
            addressField.setText(orEmpty(existing.getAddress()));
            priceField.setText(orEmpty(existing.getPrice()));
            mapField.setText(orEmpty(existing.getMapUrl()));
            photoField.setText(orEmpty(existing.getPhotoUrl()));
            openField.setText(orEmpty(existing.getOpenHour()));
            closeField.setText(orEmpty(existing.getCloseHour()));
            if (existing.getOperationalStatus() != null) statusBox.setValue(existing.getOperationalStatus());
        }

        Label errLabel = new Label(""); errLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;"); errLabel.setVisible(false);

        Button saveBtn   = new Button(isEdit ? "Simpan" : "Tambah");
        Button cancelBtn = new Button("Batal");
        saveBtn.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 28 10 28;");
        cancelBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 10 22 10 22;");

        HBox btnRow = new HBox(12, cancelBtn, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        dialog.getChildren().addAll(
            dlgTitle,
            lf("Nama Kuliner *", nameField), lf("Alamat *", addressField), lf("Deskripsi *", descField),
            lf("Harga", priceField),
            new HBox(16, lf("Jam Buka", openField), lf("Jam Tutup", closeField)),
            lf("Status", statusBox), lf("URL Google Maps", mapField), lf("URL Foto", photoField),
            buildMedsosSection(isEdit ? existing : null),
            errLabel, btnRow
        );

        ScrollPane dialogScroll = new ScrollPane(dialog);
        dialogScroll.setFitToWidth(true);
        dialogScroll.setMaxWidth(560);
        dialogScroll.setMaxHeight(750);
        dialogScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        overlay.getChildren().add(dialogScroll);
        overlay.setAlignment(Pos.CENTER);
        StackPane.setAlignment(dialogScroll, Pos.CENTER);

        BorderPane stageRoot = (BorderPane) stage.getScene().getRoot();
        StackPane wrapper;
        if (stageRoot.getCenter() instanceof StackPane sp2) { wrapper = sp2; }
        else { wrapper = new StackPane(stageRoot.getCenter()); stageRoot.setCenter(wrapper); }
        wrapper.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> wrapper.getChildren().remove(overlay));
        saveBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String addr = addressField.getText().trim();
            String desc = descField.getText().trim();
            if (name.isEmpty() || addr.isEmpty() || desc.isEmpty()) {
                errLabel.setText("Nama, alamat, deskripsi wajib diisi."); errLabel.setVisible(true); return;
            }
            try {
                boolean ok;
                String savedId;
                if (isEdit) {
                    ok = service.updateDestination(existing.getId(), name, "kuliner", desc, addr,
                        priceField.getText().trim(), mapField.getText().trim(), statusBox.getValue(),
                        openField.getText().trim(), closeField.getText().trim(), photoField.getText().trim());
                    savedId = existing.getId();
                } else {
                    ok = service.createDestination(name, "kuliner", desc, addr,
                        priceField.getText().trim(), mapField.getText().trim(), statusBox.getValue(),
                        openField.getText().trim(), closeField.getText().trim(), photoField.getText().trim());
                    savedId = null;
                    try {
                        java.util.List<Destination> all = service.getAllDestinations();
                        if (all != null) {
                            for (Destination d : all) {
                                if (d.getName().equals(name) && d.getAddress().equals(addr)) {
                                    savedId = d.getId();
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }

                if (ok && savedId != null && !pendingMedsos.isEmpty()) {
                    for (Medsos m : pendingMedsos) {
                        try { medsosService.createMedsos(savedId, m.getPlatformName(), m.getUrl()); }
                        catch (Exception ignored) {}
                    }
                }

                wrapper.getChildren().remove(overlay);
                showFeedback(feedback, ok ? (isEdit ? "Diperbarui." : "Ditambahkan.") : "Gagal.", ok);
                loadKulinerTable(stage, tableContainer, feedback, null);
            } catch (Exception ex) { errLabel.setText(ex.getMessage()); errLabel.setVisible(true); }
        });
    }

    /* helpers */
    private TextField field(String p) { TextField f = new TextField(); f.setPromptText(p); f.setStyle(fieldStyle()); return f; }
    private String fieldStyle() { return "-fx-background-color: #F8FAFF; -fx-border-color: #E0E7FF; -fx-border-radius: 8; -fx-background-radius: 8; -fx-padding: 9 12; -fx-font-size: 13px;"; }
    private VBox lf(String t, javafx.scene.Node c) { Label l = new Label(t); l.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;"); return new VBox(4, l, c); }
    private Button actionButton(String t, String color) { Button b = new Button(t); b.setStyle("-fx-background-color: " + color + "1A; -fx-text-fill: " + color + "; -fx-font-size: 12px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 5 12;"); return b; }
    private Label cell(String t, boolean h) { Label l = new Label(t); l.setStyle(h ? "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6B7280;" : "-fx-font-size: 13px; -fx-text-fill: #374151;"); return l; }
    private void showFeedback(Label l, String msg, boolean ok) { l.setText(msg); l.setStyle(ok ? "-fx-text-fill: #16A34A; -fx-font-size: 13px; -fx-font-weight: bold;" : "-fx-text-fill: #DC3545; -fx-font-size: 13px; -fx-font-weight: bold;"); l.setVisible(true); }
    private String orEmpty(String s) { return s == null ? "" : s; }

    /* =========================================================
     * MEDSOS SECTION BUILDER
     * ========================================================= */

    // Shared pending list diakses via tag pada VBox
    private java.util.List<Medsos> pendingMedsos = new java.util.ArrayList<>();

    private VBox buildMedsosSection(Destination existing) {
        pendingMedsos.clear();

        Label medsosTitle = new Label("Media Sosial");
        medsosTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        VBox medsosList = new VBox(8);

        if (existing != null) {
            try {
                java.util.List<Medsos> saved = medsosService.getMedsosbyDestination(existing.getId());
                if (saved != null) {
                    for (Medsos m : saved) {
                        medsosList.getChildren().add(buildMedsosRow(m, medsosList));
                    }
                }
            } catch (Exception ignored) {}
        }

        HBox addRow = new HBox(8);
        addRow.setAlignment(Pos.CENTER_LEFT);

        TextField platformField = new TextField();
        platformField.setPromptText("Platform (misal: Instagram)");
        platformField.setStyle(fieldStyle());
        platformField.setPrefWidth(160);

        TextField urlField = new TextField();
        urlField.setPromptText("URL akun media sosial");
        urlField.setStyle(fieldStyle());
        HBox.setHgrow(urlField, Priority.ALWAYS);

        Button addBtn = new Button("+ Tambah");
        addBtn.setStyle(
            "-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7;" +
            "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;"
        );
        addBtn.setOnAction(ev -> {
            String plat = platformField.getText().trim();
            String url  = urlField.getText().trim();
            if (plat.isEmpty() || url.isEmpty()) return;
            Medsos m = new Medsos(java.util.UUID.randomUUID().toString(), "", plat, url);
            pendingMedsos.add(m);
            medsosList.getChildren().add(buildMedsosRow(m, medsosList));
            platformField.clear();
            urlField.clear();
        });

        addRow.getChildren().addAll(platformField, urlField, addBtn);

        VBox section = new VBox(10, medsosTitle, medsosList, addRow);
        section.setStyle(
            "-fx-background-color: #F8FAFF;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 14;"
        );
        return section;
    }

    private HBox buildMedsosRow(Medsos m, VBox listContainer) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 6 10;"
        );

        Label platformLbl = new Label(m.getPlatformName());
        platformLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #0D6EFD; -fx-min-width: 110;");

        Label urlLbl = new Label(m.getUrl());
        urlLbl.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B;");
        HBox.setHgrow(urlLbl, Priority.ALWAYS);

        Button delBtn = new Button("Hapus");
        delBtn.setStyle(
            "-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626;" +
            "-fx-font-size: 11px; -fx-font-weight: bold;" +
            "-fx-background-radius: 6; -fx-padding: 4 10; -fx-cursor: hand;"
        );
        delBtn.setOnAction(ev -> {
            if (m.getDestinationId() != null && !m.getDestinationId().isEmpty()) {
                try { medsosService.deleteMedsos(m.getId()); } catch (Exception ignored) {}
            }
            pendingMedsos.remove(m);
            listContainer.getChildren().remove(row);
        });

        row.getChildren().addAll(platformLbl, urlLbl, delBtn);
        return row;
    }
}

