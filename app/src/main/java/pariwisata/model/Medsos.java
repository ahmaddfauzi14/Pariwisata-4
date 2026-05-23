package pariwisata.model;

import java.sql.Timestamp;

public class Medsos {
    private String id;
    private String destination_id;
    private String platform_name;
    private String url;
    private Timestamp created_at;

    public Medsos(String id, String destination_id, String platform_name, String url) {
        this.id = id;
        this.destination_id = destination_id;
        this.platform_name = platform_name;
        this.url = url;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDestinationId() {
        return this.destination_id;
    }

    public void setDestinationId(String destination_id) {
        this.destination_id = destination_id;
    }

    public String getPlatformName() {
        return this.platform_name;
    }

    public void setPlatformName(String platform_name) {
        this.platform_name = platform_name;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getCreatedAt() {
        return this.created_at;
    }
}
