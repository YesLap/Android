package br.sendlook.yeslap.view;

public class Users {

    private String id_user;
    private String username_user;
    private String status_user;


    public Users() {

    }

    public Users(String id_user, String username_user, String status_user) {
        this.id_user = id_user;
        this.username_user = username_user;
        this.status_user = status_user;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getUsername_user() {
        return username_user;
    }

    public void setUsername_user(String username_user) {
        this.username_user = username_user;
    }

    public String getStatus_user() {
        return status_user;
    }

    public void setStatus_user(String status_user) {
        this.status_user = status_user;
    }
}
