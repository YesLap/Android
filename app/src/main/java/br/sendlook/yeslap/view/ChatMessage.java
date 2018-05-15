package br.sendlook.yeslap.view;

public class ChatMessage {

    private String uid;
    private String name;
    private String message;
    private String status;

    public ChatMessage() {

    }

    public ChatMessage(String uid, String name, String message, String status) {
        this.uid = uid;
        this.name = name;
        this.message = message;
        this.status = status;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String staus) {
        this.status = staus;
    }
}
