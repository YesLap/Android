package br.sendlook.yeslap.view;

public class Favorites {

    private String id_user;
    private String status_user;
    private String username_user;
    private String last_seen;
    private String image;

    public Favorites() {

    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getStatus_user() {
        return status_user;
    }

    public void setStatus_user(String status_user) {
        this.status_user = status_user;
    }

    public String getUsername_user() {
        return username_user;
    }

    public void setUsername_user(String username_user) {
        this.username_user = username_user;
    }

    public String getLast_seen() {
        return last_seen;
    }

    public void setLast_seen(String last_seen) {
        this.last_seen = last_seen;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
