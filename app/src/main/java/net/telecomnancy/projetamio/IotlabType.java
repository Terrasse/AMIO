package net.telecomnancy.projetamio;

/**
 * Created by sam on 28/02/2016.
 */
public enum IotlabType {
    TEMPERATURE("temperature"),
    LIGHT("light");

    protected String value;
    private IotlabType(String value){
        this.value=value;
    }

    public String getValue() {
        return value;
    }
}
