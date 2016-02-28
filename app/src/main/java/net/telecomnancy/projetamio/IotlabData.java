package net.telecomnancy.projetamio;

import java.util.Date;

/**
 * Created by sam on 28/02/2016.
 */
public class IotlabData {
    private Date date;
    private IotlabType type;
    private String id;
    private double value;

    public IotlabData(){}
    public IotlabData( String id, long timestamp, IotlabType type,double value){
        this.id = id;
        this.type=type;
        this.date=new Date(timestamp);;
        this.value=value;
    }

    public Date getDate() { return date;}

    public void setDate(long timestamp) { this.date = new Date(timestamp); }
    public void setDate(Date date) { this.date = date; }

    public IotlabType getLabel() {
        return type;
    }

    public void setLabel(IotlabType type) {
        this.type = type;
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
