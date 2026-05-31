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
import pariwisata.util.Navigator;

import java.util.List;

public class AdminWisataPage {

    private final DestinationService service       = new DestinationService();
    private final MedsosService      medsosService = new MedsosService();

    public Parent getView(Stage stage) {

        AdminSidebar sidebar = new AdminSidebar(stage, AdminSidebar.Page.WISATA);

        VBox content = new VBox(24);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("admin-root");

        /* ---- Header ---- */
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(4);
        Label title = new Label("Kelola Wisata");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");
        Label sub = new Label("Tambah, edit, dan hapus data destinasi wisata.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #6B7280;");
        headerText.getChildren().addAll(title, sub);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addBtn = new Button("+ Tambah Wisata");
        addBtn.getStyleClass().add("primary-button");
        addBtn.setPrefHeight(42);
        addBtn.setStyle(
            "-fx-background-color: #0D6EFD;" +
            "-fx-text-fill: white;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 10;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 0 20 0 20;"
        );

        headerRow.getChildren().addAll(headerText, spacer, addBtn);

        /* ---- Search bar ---- */
        TextField searchField = new TextField();
        searchField.setPromptText("Cari wisata berdasarkan nama atau alamat...");
        searchField.setMaxWidth(420);
        searchField.getStyleClass().add("modern-textfield");
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-font-size: 13px;"
        );

        /* ---- Table container ---- */
        VBox tableContainer = new VBox(0);
        tableContainer.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 16;" +
            "-fx-border-color: #E8F0FA;" +
            "-fx-border-radius: 16;" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.05), 8, 0, 0, 3);"
        );

        /* Feedback label */
        Label feedback = new Label("");
        feedback.setVisible(false);
        feedback.setWrapText(true);

        /* Load table */
        loadWisataTable(stage, tableContainer, feedback, null);

        /* Search trigger */
        searchField.textProperty().addListener((obs, oldVal, newVal) ->
            loadWisataTable(stage, tableContainer, feedback, newVal.trim())
        );

        /* Add button opens dialog */
        addBtn.setOnAction(e -> {
            showDestinationDialog(stage, null, tableContainer, feedback, "wisata");
        });

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

    private void loadWisataTable(Stage stage, VBox tableContainer, Label feedback, String keyword) {
        tableContainer.getChildren().clear();

        List<Destination> list;
        if (keyword == null || keyword.isEmpty()) {
            list = service.filterByCategory("wisata");
        } else {
            list = service.searchDestinations(keyword);
            if (list != null) {
                list.removeIf(d -> !"wisata".equalsIgnoreCase(d.getCategory()));
            }
        }

        /* Header */
        HBox header = buildRow("Nama", "Alamat", "Harga", "Status", "Jam Buka", null, true, stage, tableContainer, feedback);
        tableContainer.getChildren().add(header);

        if (list == null || list.isEmpty()) {
            Label empty = new Label("Tidak ada data wisata ditemukan.");
            empty.setStyle("-fx-padding: 24 20 24 20; -fx-text-fill: #9CA3AF; -fx-font-size: 14px;");
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
                d,
                false,
                stage,
                tableContainer,
                feedback
            );
            if (i % 2 == 1) row.setStyle("-fx-background-color: #F8FAFF;");
            tableContainer.getChildren().add(row);
        }
    }

    /* =========================================================
     * BUILD ROW
     * ========================================================= */

    private HBox buildRow(String col1, String col2, String col3, String col4, String col5,
                           Destination data, boolean isHeader, Stage stage,
                           VBox tableContainer, Label feedback) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(11, 20, 11, 20));

        Label l1 = cell(col1, isHeader); l1.setPrefWidth(200); l1.setMinWidth(50); HBox.setHgrow(l1, Priority.ALWAYS);
        Label l2 = cell(col2, isHeader); l2.setPrefWidth(190); l2.setMinWidth(50); HBox.setHgrow(l2, Priority.ALWAYS);
        Label l3 = cell(col3, isHeader); l3.setPrefWidth(110); l3.setMinWidth(50); HBox.setHgrow(l3, Priority.ALWAYS);
        Label l4 = cell(col4, isHeader); l4.setPrefWidth(100); l4.setMinWidth(50); HBox.setHgrow(l4, Priority.ALWAYS);
        Label l5 = cell(col5, isHeader); l5.setPrefWidth(130); l5.setMinWidth(50); HBox.setHgrow(l5, Priority.ALWAYS);

        row.getChildren().addAll(l1, l2, l3, l4, l5);

        if (!isHeader && data != null) {
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);

            Button editBtn = actionButton("Edit", "#0D6EFD");
            Button delBtn  = actionButton("Hapus", "#DC3545");

            editBtn.setOnAction(e -> showDestinationDialog(stage, data, tableContainer, feedback, "wisata"));
            delBtn.setOnAction(e -> {
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                    "Hapus \"" + data.getName() + "\"?", ButtonType.YES, ButtonType.NO);
                confirm.setTitle("Konfirmasi Hapus");
                confirm.setHeaderText(null);
                confirm.showAndWait().ifPresent(btn -> {
                    if (btn == ButtonType.YES) {
                        boolean ok = service.deleteDestination(data.getId());
                        showFeedback(feedback, ok ? "Data berhasil dihapus." : "Gagal menghapus data.", ok);
                        loadWisataTable(stage, tableContainer, feedback, null);
                    }
                });
            });

            HBox actions = new HBox(8, editBtn, delBtn);
            actions.setAlignment(Pos.CENTER_RIGHT);
            row.getChildren().addAll(sp, actions);
        } else if (isHeader) {
            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            Label act = cell("Aksi", true); act.setPrefWidth(120);
            row.getChildren().addAll(sp, act);
            row.setStyle(
                "-fx-background-color: #F1F5F9;" +
                "-fx-background-radius: 16 16 0 0;"
            );
        }

        return row;
    }

    /* =========================================================
     * DIALOG TAMBAH / EDIT
     * ========================================================= */

    private void showDestinationDialog(Stage stage, Destination existing,
                                        VBox tableContainer, Label feedback, String category) {
        boolean isEdit = existing != null;
        String dialogTitle = isEdit ? "Edit Wisata" : "Tambah Wisata Baru";

        /* backdrop */
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

        Label dlgTitle = new Label(dialogTitle);
        dlgTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        /* fields */
        TextField nameField   = field("Nama Wisata *");
        TextField addressField = field("Alamat *");
        TextArea  descField   = new TextArea(isEdit && existing.getDescription() != null ? existing.getDescription() : "");
        descField.setPromptText("Deskripsi *");
        descField.setPrefRowCount(3);
        descField.setStyle(fieldStyle());
        TextField priceField  = field("Harga (isi 0 jika gratis)");
        TextField mapField    = field("URL Google Maps");
        TextField photoField  = field("URL Foto");
        TextField openField   = field("Jam Buka (contoh: 08:00)");
        TextField closeField  = field("Jam Tutup (contoh: 17:00)");

        ComboBox<String> statusBox = new ComboBox<>();
        statusBox.getItems().addAll("Buka", "Tutup", "Tutup Sementara");
        statusBox.setPromptText("Status Operasional");
        statusBox.setMaxWidth(Double.MAX_VALUE);
        statusBox.setStyle(fieldStyle());

        if (isEdit) {
            nameField.setText(existing.getName() != null ? existing.getName() : "");
            addressField.setText(existing.getAddress() != null ? existing.getAddress() : "");
            priceField.setText(existing.getPrice() != null ? existing.getPrice() : "");
            mapField.setText(existing.getMapUrl() != null ? existing.getMapUrl() : "");
            photoField.setText(existing.getPhotoUrl() != null ? existing.getPhotoUrl() : "");
            openField.setText(existing.getOpenHour() != null ? existing.getOpenHour() : "");
            closeField.setText(existing.getCloseHour() != null ? existing.getCloseHour() : "");
            if (existing.getOperationalStatus() != null) statusBox.setValue(existing.getOperationalStatus());
        }

        Label errLabel = new Label("");
        errLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;");
        errLabel.setVisible(false);

        /* buttons */
        Button saveBtn   = new Button(isEdit ? "Simpan Perubahan" : "Tambah Wisata");
        Button cancelBtn = new Button("Batal");

        saveBtn.setStyle(
            "-fx-background-color: #0D6EFD; -fx-text-fill: white;" +
            "-fx-font-weight: bold; -fx-background-radius: 10;" +
            "-fx-padding: 10 28 10 28;"
        );
        cancelBtn.setStyle(
            "-fx-background-color: #F3F4F6; -fx-text-fill: #374151;" +
            "-fx-background-radius: 10; -fx-padding: 10 22 10 22;"
        );

        HBox btnRow = new HBox(12, cancelBtn, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        /*
         * =====================================================
         * MEDSOS SECTION
         * =====================================================
         */

        Label medsosTitle = new Label("Media Sosial");
        medsosTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        // Container daftar medsos yang sudah ada
        VBox medsosList = new VBox(8);

        // Load medsos existing jika edit
        if (isEdit) {
            try {
                List<Medsos> existingMedsos = medsosService.getMedsosbyDestination(existing.getId());
                if (existingMedsos != null) {
                    for (Medsos m : existingMedsos) {
                        medsosList.getChildren().add(buildMedsosRow(m, medsosList, null, null));
                    }
                }
            } catch (Exception ignored) {}
        }

        // Form tambah medsos baru
        HBox addMedsosRow = new HBox(8);
        addMedsosRow.setAlignment(Pos.CENTER_LEFT);

        TextField platformField = new TextField();
        platformField.setPromptText("Platform (misal: Instagram)");
        platformField.setStyle(fieldStyle());
        platformField.setPrefWidth(160);

        TextField urlMedsosField = new TextField();
        urlMedsosField.setPromptText("URL akun media sosial");
        urlMedsosField.setStyle(fieldStyle());
        HBox.setHgrow(urlMedsosField, Priority.ALWAYS);

        Button addMedsosBtn = new Button("+ Tambah");
        addMedsosBtn.setStyle(
            "-fx-background-color: #E0F2FE; -fx-text-fill: #0284C7;" +
            "-fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 14;"
        );

        // Simpan sementara medsos baru (belum ada ID destinasi saat create)
        java.util.List<Medsos> pendingMedsos = new java.util.ArrayList<>();

        addMedsosBtn.setOnAction(ev -> {
            String platform = platformField.getText().trim();
            String urlMed   = urlMedsosField.getText().trim();
            if (platform.isEmpty() || urlMed.isEmpty()) return;

            Medsos tempMedsos = new Medsos(
                java.util.UUID.randomUUID().toString(), "", platform, urlMed
            );
            pendingMedsos.add(tempMedsos);
            medsosList.getChildren().add(buildMedsosRow(tempMedsos, medsosList, pendingMedsos, null));
            platformField.clear();
            urlMedsosField.clear();
        });

        addMedsosRow.getChildren().addAll(platformField, urlMedsosField, addMedsosBtn);

        VBox medsosSection = new VBox(10,
            medsosTitle,
            medsosList,
            addMedsosRow
        );
        medsosSection.setStyle(
            "-fx-background-color: #F8FAFF;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 14;"
        );

        // Wrap dialog dalam ScrollPane agar muat
        dialog.getChildren().addAll(
            dlgTitle,
            labeledField("Nama Wisata *", nameField),
            labeledField("Alamat *", addressField),
            labeledField("Deskripsi *", descField),
            labeledField("Harga", priceField),
            new HBox(16,
                labeledField("Jam Buka", openField),
                labeledField("Jam Tutup", closeField)
            ),
            labeledField("Status Operasional", statusBox),
            labeledField("URL Google Maps", mapField),
            labeledField("URL Foto", photoField),
            medsosSection,
            errLabel,
            btnRow
        );

        /* wrap dialog dalam ScrollPane agar bisa di-scroll jika konten panjang */
        ScrollPane dialogScroll = new ScrollPane(dialog);
        dialogScroll.setFitToWidth(true);
        dialogScroll.setMaxWidth(560);
        dialogScroll.setMaxHeight(750);
        dialogScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        overlay.getChildren().add(dialogScroll);
        overlay.setAlignment(Pos.CENTER);
        StackPane.setAlignment(dialogScroll, Pos.CENTER);

        /* inject overlay into the stage's current scene root temporarily */
        BorderPane stageRoot = (BorderPane) stage.getScene().getRoot();
        StackPane wrapper;
        if (stageRoot.getCenter() instanceof StackPane) {
            wrapper = (StackPane) stageRoot.getCenter();
        } else {
            wrapper = new StackPane(stageRoot.getCenter());
            stageRoot.setCenter(wrapper);
        }
        wrapper.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> wrapper.getChildren().remove(overlay));

        saveBtn.setOnAction(e -> {
            String name     = nameField.getText().trim();
            String address  = addressField.getText().trim();
            String desc     = descField.getText().trim();
            String price    = priceField.getText().trim();
            String map      = mapField.getText().trim();
            String photo    = photoField.getText().trim();
            String open     = openField.getText().trim();
            String close    = closeField.getText().trim();
            String status   = statusBox.getValue();

            if (name.isEmpty() || address.isEmpty() || desc.isEmpty()) {
                errLabel.setText("Nama, alamat, dan deskripsi wajib diisi.");
                errLabel.setVisible(true);
                return;
            }

            try {
                boolean ok;
                String savedId;
                if (isEdit) {
                    ok = service.updateDestination(existing.getId(), name, category, desc, address,
                            price, map, status, open, close, photo);
                    savedId = existing.getId();
                } else {
                    // createDestination tidak return ID, ambil dari getAll setelah create
                    ok = service.createDestination(name, category, desc, address,
                            price, map, status, open, close, photo);
                    // ambil ID destinasi baru (last created)
                    savedId = null;
                    try {
                        java.util.List<Destination> all = service.getAllDestinations();
                        if (all != null && !all.isEmpty()) {
                            for (Destination d : all) {
                                if (d.getName().equals(name) && d.getAddress().equals(address)) {
                                    savedId = d.getId();
                                }
                            }
                        }
                    } catch (Exception ignored) {}
                }

                // Simpan medsos pending (untuk destinasi baru)
                if (ok && savedId != null && !pendingMedsos.isEmpty()) {
                    for (Medsos m : pendingMedsos) {
                        try {
                            medsosService.createMedsos(savedId, m.getPlatformName(), m.getUrl());
                        } catch (Exception ignored) {}
                    }
                }

                wrapper.getChildren().remove(overlay);
                showFeedback(feedback,
                    ok ? (isEdit ? "Data berhasil diperbarui." : "Data berhasil ditambahkan.")
                       : "Gagal menyimpan data.",
                    ok
                );
                loadWisataTable(stage, tableContainer, feedback, null);
            } catch (Exception ex) {
                errLabel.setText(ex.getMessage());
                errLabel.setVisible(true);
            }
        });
    }

    /* =========================================================
     * UTIL HELPERS
     * ========================================================= */

    private TextField field(String prompt) {
        TextField f = new TextField();
        f.setPromptText(prompt);
        f.setStyle(fieldStyle());
        return f;
    }

    private String fieldStyle() {
        return "-fx-background-color: #F8FAFF;" +
               "-fx-border-color: #E0E7FF;" +
               "-fx-border-radius: 8;" +
               "-fx-background-radius: 8;" +
               "-fx-padding: 9 12 9 12;" +
               "-fx-font-size: 13px;";
    }

    private VBox labeledField(String labelText, javafx.scene.Node control) {
        Label l = new Label(labelText);
        l.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        VBox box = new VBox(4, l, control);
        if (control instanceof HBox) HBox.setHgrow(control, Priority.ALWAYS);
        return box;
    }

    private Button actionButton(String text, String color) {
        Button b = new Button(text);
        b.setStyle(
            "-fx-background-color: " + color + "1A;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 12px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 8;" +
            "-fx-padding: 5 12 5 12;" +
            "-fx-cursor: hand;"
        );
        return b;
    }

    private Label cell(String text, boolean header) {
        Label l = new Label(text);
        l.setWrapText(false);
        l.setStyle(header
            ? "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6B7280;"
            : "-fx-font-size: 13px; -fx-text-fill: #374151;"
        );
        return l;
    }

    private void showFeedback(Label lbl, String msg, boolean success) {
        lbl.setText(msg);
        lbl.setStyle(success
            ? "-fx-text-fill: #16A34A; -fx-font-size: 13px; -fx-font-weight: bold;"
            : "-fx-text-fill: #DC3545; -fx-font-size: 13px; -fx-font-weight: bold;"
        );
        lbl.setVisible(true);
    }

    private HBox buildMedsosRow(Medsos m, VBox listContainer,
                                 java.util.List<Medsos> pendingList,
                                 String destinationId) {
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
        urlLbl.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(urlLbl, Priority.ALWAYS);

        Button delBtn = new Button("Hapus");
        delBtn.setStyle(
            "-fx-background-color: #FEE2E2; -fx-text-fill: #DC2626;" +
            "-fx-font-size: 11px; -fx-font-weight: bold;" +
            "-fx-background-radius: 6; -fx-padding: 4 10; -fx-cursor: hand;"
        );
        delBtn.setOnAction(ev -> {
            // Hapus dari DB jika sudah tersimpan (ada destination_id valid)
            if (m.getDestinationId() != null && !m.getDestinationId().isEmpty()) {
                try { medsosService.deleteMedsos(m.getId()); } catch (Exception ignored) {}
            }
            // Hapus dari pending list
            if (pendingList != null) pendingList.remove(m);
            listContainer.getChildren().remove(row);
        });

        row.getChildren().addAll(platformLbl, urlLbl, delBtn);
        return row;
    }
}
