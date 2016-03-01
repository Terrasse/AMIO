package net.telecomnancy.projetamio;

import android.graphics.Color;

/**
 * Created by Terry on 01/03/2016.
 */
public class Mote {
    private String name;
    private String temperature;
    private String humidity;
    private int lightstatus;

    public Mote(){

    }

    public Mote(String name, String temperature, String humidity, int lightstatus) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.lightstatus = lightstatus;
    }

    public Mote(History history,String id){
        this.temperature=String.valueOf(history.getMoteHistory(id,IotlabType.TEMPERATURE).get(0).getValue());
        this.humidity=String.valueOf(history.getMoteHistory(id, IotlabType.HUMIDITY).get(0).getValue());
       try {
            if(history.haslightOn(id)){
                this.lightstatus=Color.GREEN;
            }else {
                this.lightstatus=Color.BLACK;
            }
       } catch (MoteDataException e) {
           this.lightstatus=Color.GRAY;
       }
        this.name=id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public int getLightstatus() {
        return lightstatus;
    }

    public void setLightstatus(int lightstatus) {
        this.lightstatus = lightstatus;
    }

    @Override
    public String toString() {
        return "Mote{" +
                "name='" + name + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", lightstatus=" + lightstatus +
                '}';
    }
}
