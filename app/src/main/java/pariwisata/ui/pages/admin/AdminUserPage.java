package pariwisata.ui.pages.admin;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import pariwisata.model.User;
import pariwisata.service.UserService;
import pariwisata.ui.components.AdminSidebar;
import pariwisata.util.Session;

import java.util.List;
import java.util.stream.Collectors;

public class AdminUserPage {

    public Parent getView(Stage stage) {

        AdminSidebar sidebar = new AdminSidebar(stage, AdminSidebar.Page.USERS);

        VBox content = new VBox(24);
        content.setPadding(new Insets(40));
        content.getStyleClass().add("admin-root");

        /* ---- Header ---- */
        HBox headerRow = new HBox();
        headerRow.setAlignment(Pos.CENTER_LEFT);

        VBox headerText = new VBox(4);
        Label title = new Label("Kelola Pengguna");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");
        Label sub = new Label("Lihat, edit role, dan hapus akun pengguna.");
        sub.setStyle("-fx-font-size: 13px; -fx-text-fill: #6B7280;");
        headerText.getChildren().addAll(title, sub);

        headerRow.getChildren().add(headerText);

        /* ---- Filter tabs ---- */
        HBox filterRow = new HBox(10);
        filterRow.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("Cari username atau email...");
        searchField.setPrefWidth(340);
        searchField.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-padding: 10 14 10 14;" +
            "-fx-font-size: 13px;"
        );

        ComboBox<String> roleFilter = new ComboBox<>();
        roleFilter.getItems().addAll("Semua", "user", "admin");
        roleFilter.setValue("Semua");
        roleFilter.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;" +
            "-fx-font-size: 13px;"
        );

        filterRow.getChildren().addAll(searchField, roleFilter);

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

        loadUserTable(stage, tableContainer, feedback, null, "Semua");

        searchField.textProperty().addListener((obs, o, nv) ->
            loadUserTable(stage, tableContainer, feedback, nv.trim(), roleFilter.getValue())
        );
        roleFilter.valueProperty().addListener((obs, o, nv) ->
            loadUserTable(stage, tableContainer, feedback, searchField.getText().trim(), nv)
        );

        content.getChildren().addAll(headerRow, filterRow, feedback, tableContainer);

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

    private void loadUserTable(Stage stage, VBox tableContainer, Label feedback,
                                String keyword, String roleFilter) {
        tableContainer.getChildren().clear();

        List<User> allUsers = UserService.getAll();
        if (allUsers == null) allUsers = new java.util.ArrayList<>();

        /* filter */
        List<User> filtered = allUsers.stream()
            .filter(u -> {
                boolean matchRole = "Semua".equals(roleFilter) ||
                    (u.getRole() != null && u.getRole().equalsIgnoreCase(roleFilter));
                boolean matchKeyword = keyword == null || keyword.isEmpty() ||
                    (u.getUsername() != null && u.getUsername().toLowerCase().contains(keyword.toLowerCase())) ||
                    (u.getEmail() != null && u.getEmail().toLowerCase().contains(keyword.toLowerCase()));
                return matchRole && matchKeyword;
            })
            .collect(Collectors.toList());

        /* header */
        tableContainer.getChildren().add(buildUserRow(null, true, stage, tableContainer, feedback));

        if (filtered.isEmpty()) {
            Label empty = new Label("Tidak ada pengguna ditemukan.");
            empty.setStyle("-fx-padding: 24 20; -fx-text-fill: #9CA3AF; -fx-font-size: 14px;");
            tableContainer.getChildren().add(empty);
            return;
        }

        for (int i = 0; i < filtered.size(); i++) {
            HBox row = buildUserRow(filtered.get(i), false, stage, tableContainer, feedback);
            if (i % 2 == 1) row.setStyle("-fx-background-color: #F8FAFF;");
            tableContainer.getChildren().add(row);
        }
    }

    /* =========================================================
     * BUILD ROW
     * ========================================================= */

    private HBox buildUserRow(User user, boolean isHeader, Stage stage,
                               VBox tableContainer, Label feedback) {
        HBox row = new HBox(0);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(12, 20, 12, 20));

        if (isHeader) {
            Label no    = cell("#",         true); no.setPrefWidth(40);
            Label uname = cell("Username",  true); uname.setPrefWidth(180); uname.setMinWidth(50); HBox.setHgrow(uname, Priority.ALWAYS);
            Label email = cell("Email",     true); email.setPrefWidth(260); email.setMinWidth(50); HBox.setHgrow(email, Priority.ALWAYS);
            Label role  = cell("Role",      true); role.setPrefWidth(90); role.setMinWidth(50);
            Region sp   = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);
            Label act   = cell("Aksi",      true); act.setPrefWidth(160); act.setMinWidth(50);

            row.getChildren().addAll(no, uname, email, role, sp, act);
            row.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 16 16 0 0;");
            return row;
        }

        /* data row */
        String currentUserId = Session.getUser() != null ? Session.getUser().getId() : "";
        boolean isSelf = user.getId().equals(currentUserId);

        Label no    = cell("-",                                               false); no.setPrefWidth(40);
        Label uname = cell(orEmpty(user.getUsername()),                      false); uname.setPrefWidth(180); uname.setMinWidth(50); HBox.setHgrow(uname, Priority.ALWAYS);
        Label email = cell(orEmpty(user.getEmail()),                         false); email.setPrefWidth(260); email.setMinWidth(50); HBox.setHgrow(email, Priority.ALWAYS);

        /* role badge */
        Label roleLbl = new Label(user.getRole() == null ? "user" : user.getRole());
        roleLbl.setStyle(
            "-fx-background-color: " + ("admin".equalsIgnoreCase(user.getRole()) ? "#EAF4FF" : "#F0FDF4") + ";" +
            "-fx-text-fill: "         + ("admin".equalsIgnoreCase(user.getRole()) ? "#0D6EFD" : "#16A34A") + ";" +
            "-fx-font-size: 11px;" +
            "-fx-font-weight: bold;" +
            "-fx-padding: 3 10 3 10;" +
            "-fx-background-radius: 20;"
        );
        roleLbl.setPrefWidth(90);
        roleLbl.setMinWidth(50);

        Region sp = new Region(); HBox.setHgrow(sp, Priority.ALWAYS);

        /* actions */
        Button editBtn = actionButton("Edit Role", "#8B5CF6");
        Button delBtn  = actionButton("Hapus",     "#DC3545");

        if (isSelf) {
            delBtn.setDisable(true);
            delBtn.setStyle(delBtn.getStyle() + "-fx-opacity: 0.4;");
        }

        editBtn.setOnAction(e -> showEditRoleDialog(stage, user, tableContainer, feedback));

        delBtn.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION,
                "Hapus akun \"" + user.getUsername() + "\"? Tindakan ini tidak dapat dibatalkan.",
                ButtonType.YES, ButtonType.NO);
            a.setTitle("Konfirmasi Hapus");
            a.setHeaderText(null);
            a.showAndWait().ifPresent(btn -> {
                if (btn == ButtonType.YES) {
                    boolean ok = UserService.delete(user.getId());
                    showFeedback(feedback, ok ? "Akun berhasil dihapus." : "Gagal menghapus akun.", ok);
                    loadUserTable(stage, tableContainer, feedback, null, "Semua");
                }
            });
        });

        HBox actions = new HBox(8, editBtn, delBtn);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.setPrefWidth(160);

        row.getChildren().addAll(no, uname, email, roleLbl, sp, actions);
        return row;
    }

    /* =========================================================
     * EDIT ROLE DIALOG
     * ========================================================= */

    private void showEditRoleDialog(Stage stage, User user,
                                     VBox tableContainer, Label feedback) {
        StackPane overlay = new StackPane();
        overlay.setStyle("-fx-background-color: rgba(0,0,0,0.45);");

        VBox dialog = new VBox(20);
        dialog.setMaxWidth(400);
        dialog.setPadding(new Insets(32));
        dialog.setStyle(
            "-fx-background-color: white;" +
            "-fx-background-radius: 20;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.22), 30, 0, 0, 8);"
        );

        Label dlgTitle = new Label("Edit Role Pengguna");
        dlgTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1B1F3B;");

        Label nameInfo = new Label("Pengguna: " + user.getUsername());
        nameInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #6B7280;");

        Label emailInfo = new Label("Email: " + orEmpty(user.getEmail()));
        emailInfo.setStyle("-fx-font-size: 13px; -fx-text-fill: #9CA3AF;");

        ComboBox<String> roleBox = new ComboBox<>();
        roleBox.getItems().addAll("user", "admin");
        roleBox.setValue(user.getRole() != null ? user.getRole() : "user");
        roleBox.setMaxWidth(Double.MAX_VALUE);
        roleBox.setStyle(
            "-fx-background-color: #F8FAFF;" +
            "-fx-border-color: #E0E7FF;" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-font-size: 13px;"
        );

        Label errLabel = new Label(""); errLabel.setStyle("-fx-text-fill: #DC3545; -fx-font-size: 13px;"); errLabel.setVisible(false);

        Button saveBtn   = new Button("Simpan Role");
        Button cancelBtn = new Button("Batal");
        saveBtn.setStyle("-fx-background-color: #8B5CF6; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 10 24 10 24;");
        cancelBtn.setStyle("-fx-background-color: #F3F4F6; -fx-text-fill: #374151; -fx-background-radius: 10; -fx-padding: 10 18 10 18;");

        HBox btnRow = new HBox(12, cancelBtn, saveBtn);
        btnRow.setAlignment(Pos.CENTER_RIGHT);

        VBox roleSection = new VBox(6);
        Label roleLabel = new Label("Role");
        roleLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #374151;");
        roleSection.getChildren().addAll(roleLabel, roleBox);

        dialog.getChildren().addAll(dlgTitle, nameInfo, emailInfo, roleSection, errLabel, btnRow);
        overlay.getChildren().add(dialog);
        overlay.setAlignment(Pos.CENTER);
        StackPane.setAlignment(dialog, Pos.CENTER);

        BorderPane stageRoot = (BorderPane) stage.getScene().getRoot();
        StackPane wrapper;
        if (stageRoot.getCenter() instanceof StackPane sp2) { wrapper = sp2; }
        else { wrapper = new StackPane(stageRoot.getCenter()); stageRoot.setCenter(wrapper); }
        wrapper.getChildren().add(overlay);

        cancelBtn.setOnAction(e -> wrapper.getChildren().remove(overlay));
        saveBtn.setOnAction(e -> {
            String newRole = roleBox.getValue();
            if (newRole == null) { errLabel.setText("Pilih role."); errLabel.setVisible(true); return; }
            user.setRole(newRole);
            boolean ok = UserService.update(user);
            wrapper.getChildren().remove(overlay);
            showFeedback(feedback, ok ? "Role berhasil diperbarui." : "Gagal memperbarui role.", ok);
            loadUserTable(stage, tableContainer, feedback, null, "Semua");
        });
    }

    /* helpers */
    private Label cell(String t, boolean h) {
        Label l = new Label(t);
        l.setStyle(h
            ? "-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #6B7280;"
            : "-fx-font-size: 13px; -fx-text-fill: #374151;"
        );
        return l;
    }
    private Button actionButton(String t, String color) {
        Button b = new Button(t);
        b.setStyle(
            "-fx-background-color: " + color + "1A;" +
            "-fx-text-fill: " + color + ";" +
            "-fx-font-size: 12px; -fx-font-weight: bold;" +
            "-fx-background-radius: 8; -fx-padding: 5 12;"
        );
        return b;
    }
    private void showFeedback(Label l, String msg, boolean ok) {
        l.setText(msg);
        l.setStyle(ok
            ? "-fx-text-fill: #16A34A; -fx-font-size: 13px; -fx-font-weight: bold;"
            : "-fx-text-fill: #DC3545; -fx-font-size: 13px; -fx-font-weight: bold;"
        );
        l.setVisible(true);
    }
    private String orEmpty(String s) { return s == null ? "" : s; }
}
