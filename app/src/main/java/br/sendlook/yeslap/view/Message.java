package br.sendlook.yeslap.view;

public class Message {

    private String id_sender;
    private String id_receiver;
    private String message;
    private String date_app;
    private String key;

    public Message() {
    }

    public String getId_sender() {
        return id_sender;
    }

    public void setId_sender(String id_sender) {
        this.id_sender = id_sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate_app() {
        return date_app;
    }

    public void setDate_app(String date_app) {
        this.date_app = date_app;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId_receiver() {
        return id_receiver;
    }

    public void setId_receiver(String id_receiver) {
        this.id_receiver = id_receiver;
    }
}
