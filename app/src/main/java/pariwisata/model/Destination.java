package pariwisata.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Destination {
    private String id;
    private String name;
    private String description;
    private String category;
    private String address;
    private String price;
    private String mapUrl;
    private String operationalStatus;
    private String openHour;
    private String closeHour;
    private String photoUrl;
    private Timestamp createdAt;

    private List<Medsos> medsosList = new ArrayList<>();
    private List<Review> reviews = new ArrayList<>();

    public Destination(String id, String name, String category, String description, String address, String price, String mapUrl,
            String operationalStatus, String openHour, String closeHour, String photoUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.address = address;
        this.price = price;
        this.mapUrl = mapUrl;
        this.operationalStatus = operationalStatus;
        this.openHour = openHour;
        this.closeHour = closeHour;
        this.photoUrl = photoUrl;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMapUrl() {
        return this.mapUrl;
    }

    public void setMapUrl(String mapUrl) {
        this.mapUrl = mapUrl;
    }

    public String getOperationalStatus() {
        return this.operationalStatus;
    }

    public void setOperationalStatus(String operationalStatus) {
        this.operationalStatus = operationalStatus;
    }

    public String getOpenHour() {
        return this.openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public String getCloseHour() {
        return this.closeHour;
    }

    public void setCloseHour(String closeHour) {
        this.closeHour = closeHour;
    }

    public String getPhotoUrl() {
        return this.photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

        public Timestamp getCreatedAt() {
            return this.createdAt;
        }

    public List<Medsos> getMedsosList() {
        return this.medsosList;
    }

    public void setMedsosList(List<Medsos> medsosList) {
        this.medsosList = medsosList;
    }

    public List<Review> getReviews() {
        return this.reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    }
