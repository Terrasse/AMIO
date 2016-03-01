package net.telecomnancy.projetamio;

/**
 * Created by sam on 01/03/2016.
 */
public enum NotificationType {
    NONE(""),NOTIFICATION("Notification"),EMAIL("Email");
    protected String value;
    private NotificationType(String value){
        this.value=value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
