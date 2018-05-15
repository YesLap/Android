package br.sendlook.yeslap.view;

public class Favorites {

    private String uid;
    private String lastSeen;

    public Favorites() {

    }

    public Favorites(String uid, String lastSeen) {
        this.uid = uid;
        this.lastSeen = lastSeen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(String lastSeen) {
        this.lastSeen = lastSeen;
    }
}
