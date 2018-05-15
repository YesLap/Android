package br.sendlook.yeslap.view;

public class Message {

    private String uidSender;
    private String uidAddressee;
    private String message;
    private String date;
    private String key;

    public Message() {
    }

    public String getUidSender() {
        return uidSender;
    }

    public void setUidSender(String uidSender) {
        this.uidSender = uidSender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUidAddressee() {
        return uidAddressee;
    }

    public void setUidAddressee(String uidAddressee) {
        this.uidAddressee = uidAddressee;
    }
}
