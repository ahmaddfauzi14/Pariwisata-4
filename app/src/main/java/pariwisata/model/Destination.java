package pariwisata.model;

import java.sql.Timestamp;

public class Destination {
    private String id;
    private String name;
    private String category;
    private String address;
    private String price;
    private String mapUrl;
    private String operationalStatus;
    private String openHour;
    private String closeHour;
    private String photoUrl;
    private Timestamp createdAt;

    public Destination(String id, String name, String category, String address, String price, String mapUrl,
            String operationalStatus, String openHour, String closeHour, String photoUrl) {
        this.id = id;
        this.name = name;
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
    }
