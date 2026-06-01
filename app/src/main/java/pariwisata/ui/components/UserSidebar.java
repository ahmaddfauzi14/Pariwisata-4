package pariwisata.ui.components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.*;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;

import pariwisata.util.Session;
import pariwisata.ui.pages.user.HomepagePage;
import pariwisata.ui.pages.user.ProfilePage;
import pariwisata.ui.pages.user.WishlistPage;
import pariwisata.ui.pages.auth.LoginPage;
import pariwisata.util.Navigator;

public class UserSidebar extends VBox {

    public enum Page { HOME, WISHLIST, PROFILE }

    public UserSidebar(Stage stage, Page activePage) {

        /*
         * ===================================================
         * SIDEBAR ROOT
         * ===================================================
         */

        setPrefWidth(240);
        setMinWidth(240);
        setMaxWidth(240);
        setSpacing(0);
        setPadding(new Insets(0));
        getStyleClass().add("user-sidebar");

        /*
         * ===================================================
         * LOGO SECTION
         * ===================================================
         */

        VBox logoSection = new VBox();
        logoSection.setPadding(new Insets(28, 24, 20, 24));
        logoSection.setAlignment(Pos.CENTER_LEFT);
        logoSection.getStyleClass().add("sidebar-logo-section");

        ImageView logoImg = new ImageView();

        try {
            Image logo = new Image(
                getClass().getResourceAsStream(
                    "/pariwisata/assets/logo/wisatarasa-logo.png"
                )
            );
            logoImg.setImage(logo);
            logoImg.setFitWidth(148);
            logoImg.setPreserveRatio(true);
            logoImg.setSmooth(true);
        } catch (Exception e) {
            /* fallback: teks */
        }

        logoSection.getChildren().add(logoImg);

        /*
         * ===================================================
         * DIVIDER
         * ===================================================
         */

        Region divider1 = new Region();
        divider1.setPrefHeight(1);
        divider1.setMaxWidth(Double.MAX_VALUE);
        divider1.getStyleClass().add("sidebar-divider");

        /*
         * ===================================================
         * MENU SECTION
         * ===================================================
         */

        VBox menuSection = new VBox(4);
        menuSection.setPadding(new Insets(16, 12, 16, 12));
        VBox.setVgrow(menuSection, Priority.ALWAYS);

        Label menuLabel = new Label("MENU");
        menuLabel.getStyleClass().add("sidebar-section-label");
        menuLabel.setPadding(new Insets(0, 8, 8, 8));

        HBox homeItem    = buildNavItem("ic-wisata",    "Beranda",   activePage == Page.HOME,     stage, "home");
        HBox wishItem    = buildNavItem("ic-favorite",  "Wishlist",  activePage == Page.WISHLIST,  stage, "wishlist");
        HBox profileItem = buildNavItem("ic-profile",   "Profil",    activePage == Page.PROFILE,   stage, "profile");

        /* Ubah warna icon wishlist menjadi putih di sidebar */
        ImageView wishIcon = (ImageView) wishItem.getChildren().get(0);
        ColorAdjust whiteEffect = new ColorAdjust();
        whiteEffect.setBrightness(1.0);
        wishIcon.setEffect(whiteEffect);

        menuSection.getChildren().addAll(
            menuLabel,
            homeItem,
            wishItem,
            profileItem
        );

        /*
         * ===================================================
         * DIVIDER 2
         * ===================================================
         */

        Region divider2 = new Region();
        divider2.setPrefHeight(1);
        divider2.setMaxWidth(Double.MAX_VALUE);
        divider2.getStyleClass().add("sidebar-divider");

        /*
         * ===================================================
         * USER INFO + LOGOUT
         * ===================================================
         */

        VBox bottomSection = new VBox(12);
        bottomSection.setPadding(new Insets(16, 12, 24, 12));

        /* avatar + nama */
        HBox userInfo = new HBox(12);
        userInfo.setAlignment(Pos.CENTER_LEFT);
        userInfo.setPadding(new Insets(4, 8, 4, 8));

        /* avatar circle */
        StackPane avatar = new StackPane();
        avatar.setPrefSize(38, 38);
        avatar.setMinSize(38, 38);
        avatar.getStyleClass().add("sidebar-avatar");

        String initial = Session.isGuest() ? "T"
            : (Session.getUser() != null && Session.getUser().getUsername() != null
               ? String.valueOf(Session.getUser().getUsername().charAt(0)).toUpperCase()
               : "U");

        Label avatarLabel = new Label(initial);
        avatarLabel.getStyleClass().add("sidebar-avatar-label");
        avatar.getChildren().add(avatarLabel);

        VBox userText = new VBox(2);
        Label userName = new Label(Session.getDisplayName().isEmpty() ? "Tamu" : Session.getDisplayName());
        userName.getStyleClass().add("sidebar-username");
        Label userRole = new Label(Session.isAdmin() ? "Admin" : Session.isGuest() ? "Tamu" : "User");
        userRole.getStyleClass().add("sidebar-userrole");
        userText.getChildren().addAll(userName, userRole);

        userInfo.getChildren().addAll(avatar, userText);

        /* tombol logout */
        HBox logoutItem = buildNavItem("ic-logout", "Keluar", false, stage, "logout");
        logoutItem.getStyleClass().add("sidebar-item-danger");

        bottomSection.getChildren().addAll(userInfo, logoutItem);

        /*
         * ===================================================
         * COMPOSE
         * ===================================================
         */

        getChildren().addAll(
            logoSection,
            divider1,
            menuSection,
            divider2,
            bottomSection
        );
    }

    /*
     * =======================================================
     * BUILD NAV ITEM
     * =======================================================
     */

    private HBox buildNavItem(
        String iconName,
        String label,
        boolean active,
        Stage stage,
        String target
    ) {
        HBox item = new HBox(12);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(11, 14, 11, 14));
        item.setCursor(Cursor.HAND);
        item.setMaxWidth(Double.MAX_VALUE);
        item.getStyleClass().add(active ? "sidebar-item-active" : "sidebar-item");

        /* icon */
        ImageView icon = loadIcon(iconName, active);

        /* label */
        Label text = new Label(label);
        text.getStyleClass().add(active ? "sidebar-item-text-active" : "sidebar-item-text");

        item.getChildren().addAll(icon, text);

        /* klik */
        item.setOnMouseClicked(e -> navigate(stage, target));

        /* hover effect (hanya non-active) */
        if (!active) {
            item.setOnMouseEntered(ev -> item.getStyleClass().setAll("sidebar-item", "sidebar-item-hover"));
            item.setOnMouseExited(ev  -> item.getStyleClass().setAll("sidebar-item"));
        }

        return item;
    }

    private ImageView loadIcon(String iconName, boolean active) {
        ImageView iv = new ImageView();
        try {
            Image img = new Image(
                getClass().getResourceAsStream(
                    "/pariwisata/assets/icons/" + iconName + ".png"
                )
            );
            iv.setImage(img);
        } catch (Exception e) {
            /* fallback: biarkan kosong */
        }
        iv.setFitWidth(20);
        iv.setFitHeight(20);
        iv.setPreserveRatio(true);
        iv.setSmooth(true);
        return iv;
    }

    private void navigate(Stage stage, String target) {
        switch (target) {
            case "home"     -> new HomepagePage().show(stage);
            case "wishlist" -> {
                Navigator.switchScene(
                    stage,
                    new WishlistPage().getView(stage)
                );
            }
            case "profile"  -> {
                Navigator.switchScene(
                    stage,
                    new ProfilePage().getView(stage)
                );
            }
            case "logout"   -> {
                Session.logout();
                new LoginPage().show(stage);
            }
        }
    }
}