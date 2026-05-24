package pariwisata.service;

import java.util.List;
import java.util.UUID;

import pariwisata.dao.WishlistsRepository;
import pariwisata.model.Wishlist;

public class WishlistService {
    private WishlistsRepository wishlistRepository;

    public WishlistService() {
        this.wishlistRepository = new WishlistsRepository();
    }

    public boolean addToWishlist(String userId, String destinationId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }

        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        List<Wishlist> userWishlist = wishlistRepository.getByUserId(userId);
        if (userWishlist != null) {
            for (Wishlist w : userWishlist) {
                if (w.getDestinationId().equals(destinationId)) {
                    throw new IllegalArgumentException(
                        "Destinasi sudah ada di wishlist Anda"
                    );
                }
            }
        }

        String id = UUID.randomUUID().toString();
        Wishlist wishlist = new Wishlist(
            id, userId, destinationId, 
            new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date())
        );

        return wishlistRepository.create(wishlist);
    }

    public boolean removeFromWishlist(String userId, String destinationId) {

        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }

        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        List<Wishlist> userWishlist = wishlistRepository.getByUserId(userId);
        if (userWishlist != null) {
            for (Wishlist w : userWishlist) {
                if (w.getDestinationId().equals(destinationId)) {
                    return wishlistRepository.delete(w.getId());
                }
            }
        }

        throw new IllegalArgumentException(
            "Wishlist item tidak ditemukan"
        );
    }

    public List<Wishlist> getUserWishlist(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }
        return wishlistRepository.getByUserId(userId);
    }

    public boolean isDestinationInWishlist(String userId, String destinationId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }
        if (destinationId == null || destinationId.trim().isEmpty()) {
            throw new IllegalArgumentException("Destination ID tidak boleh kosong");
        }

        List<Wishlist> userWishlist = wishlistRepository.getByUserId(userId);
        if (userWishlist == null) {
            return false;
        }

        for (Wishlist w : userWishlist) {
            if (w.getDestinationId().equals(destinationId)) {
                return true;
            }
        }

        return false;
    }

    public boolean clearWishlist(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }

        List<Wishlist> userWishlist = wishlistRepository.getByUserId(userId);
        if (userWishlist == null || userWishlist.isEmpty()) {
            throw new IllegalArgumentException("Wishlist kosong atau tidak ditemukan");
        }

        boolean allDeleted = true;
        for (Wishlist w : userWishlist) {
            if (!wishlistRepository.delete(w.getId())) {
                allDeleted = false;
            }
        }

        return allDeleted;
    }

    public Wishlist getWishlistById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Wishlist ID tidak boleh kosong");
        }
        return wishlistRepository.getById(id);
    }

    public int getWishlistCount(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("User ID tidak boleh kosong");
        }

        List<Wishlist> userWishlist = wishlistRepository.getByUserId(userId);
        return userWishlist == null ? 0 : userWishlist.size();
    }

    public List<Wishlist> getAllWishlists() {
        return wishlistRepository.getAll();
    }
}
