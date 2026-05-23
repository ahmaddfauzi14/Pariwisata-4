package pariwisata.model;

public class Wishlist {
    private String id;
    private String userId;
    private String destinationId;
    private String createdAt;

    public Wishlist(String id, String userId, String destinationId, String createdAt) {
        this.id = id;
        this.userId = userId;
        this.destinationId = destinationId;
        this.createdAt = createdAt;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDestinationId() {
        return this.destinationId;
    }

    public void setDestinationId(String destinationId) {
        this.destinationId = destinationId;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
