package pariwisata.util;

import pariwisata.model.User;

/** Menyimpan state sesi: siapa yang sedang login (bisa null = guest). */
public class Session {
    private static User currentUser = null;
    private static boolean isGuest = false;

    public static void loginAsGuest() {
        currentUser = null;
        isGuest = true;
    }

    public static void login(User user) {
        currentUser = user;
        isGuest = false;
    }

    public static void logout() {
        currentUser = null;
        isGuest = false;
    }

    public static User getUser() { return currentUser; }

    public static boolean isLoggedIn() { return currentUser != null; }

    public static boolean isGuest() { return isGuest; }

    public static boolean isAdmin() {
        return currentUser != null && "admin".equalsIgnoreCase(currentUser.getRole());
    }

    /** Nama tampilan: username jika login, "Tamu" jika guest */
    public static String getDisplayName() {
        if (currentUser != null) return currentUser.getUsername();
        if (isGuest) return "Tamu";
        return "";
    }
}