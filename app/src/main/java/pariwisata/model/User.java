package pariwisata.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String id;
    private String username;
    private String email;
    private String password;
    private String role;
    private Timestamp createdAt;
    private List<Wishlist> wishlist = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public User(String id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = "user";
    }

    public String getId() {
        return this.id;
    }

    public String getRole() {
        return this.role;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Wishlist> getWishlists() {
        return this.wishlist;
    }

    public void setWishlists(List<Wishlist> wishlist) {
        this.wishlist = wishlist;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public Timestamp getCreatedAt() {
        return this.createdAt;
    }
}
