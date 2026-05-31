package pariwisata.util;

/**
 * Helper untuk memproses URL gambar.
 * Mengkonversi Google Drive share link ke direct download URL
 * agar bisa dimuat oleh JavaFX Image.
 */
public class ImageHelper {

    /**
     * Konversi Google Drive share link ke direct download URL.
     *
     * Input:  https://drive.google.com/file/d/FILE_ID/view?usp=sharing
     * Output: https://drive.google.com/uc?export=download&id=FILE_ID
     *
     * Jika URL bukan Google Drive link, dikembalikan apa adanya.
     */
    public static String convertToDirectUrl(String url) {
        if (url == null || url.isBlank()) {
            return url;
        }

        String trimmed = url.trim();

        // Tambah https:// jika belum ada
        if (!trimmed.startsWith("http://") && !trimmed.startsWith("https://") && !trimmed.startsWith("file:")) {
            trimmed = "https://" + trimmed;
        }

        // Konversi Google Drive share link ke direct image URL
        // lh3.googleusercontent.com/d/FILE_ID mengembalikan gambar langsung
        if (trimmed.contains("drive.google.com/file/d/")) {
            try {
                String fileId = trimmed.split("/file/d/")[1].split("/")[0];
                return "https://lh3.googleusercontent.com/d/" + fileId;
            } catch (Exception e) {
                System.out.println("Gagal parse Google Drive URL: " + trimmed);
            }
        }

        // Handle format lain: drive.google.com/open?id=FILE_ID
        if (trimmed.contains("drive.google.com") && trimmed.contains("id=")) {
            try {
                String fileId = trimmed.split("id=")[1].split("&")[0];
                return "https://lh3.googleusercontent.com/d/" + fileId;
            } catch (Exception e) {
                System.out.println("Gagal parse Google Drive URL: " + trimmed);
            }
        }

        return trimmed;
    }
}
