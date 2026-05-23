package pariwisata.model;

public class Review {
    private String id;
    private String userId;
    private String destinationId;
    private int rating;
    private String comment;

    public Review(String id, String userId, String destinationId, int rating, String comment) {
        this.id = id;
        this.userId = userId;
        this.destinationId = destinationId;
        this.rating = rating;
        this.comment = comment;
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

    public int getRating() {
        return this.rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
