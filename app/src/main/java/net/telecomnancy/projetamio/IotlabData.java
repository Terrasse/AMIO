package net.telecomnancy.projetamio;

/**
 * Created by sam on 28/02/2016.
 */
public class IotlabData {
    private long timestamp;
    private String label;
    private String id;
    private double value;

    public IotlabData(){}
    public IotlabData( String id, long timestamp, String label,double value){
        this.id = id;
        this.label=label;
        this.timestamp=timestamp;
        this.value=value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
