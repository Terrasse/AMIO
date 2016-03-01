package net.telecomnancy.projetamio;

import android.graphics.Color;

import java.util.Date;

/**
 * Created by Terry on 01/03/2016.
 */
public class Mote {

    private String name;
    private String temperature;
    private String humidity;
    private String light;
    private boolean lastLightActive=true;
    private String lastalert;

    public String getLastalert() {
        return lastalert;
    }

    public void setLastalert(String lastalert) {
        this.lastalert = lastalert;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }

    private String lastupdate;

    public Mote(){

    }

    public Mote(String name, String temperature, String humidity, String lightstatus, String lastupdate, String lastalert,boolean lastLightActive) {
        this.name = name;
        this.temperature = temperature;
        this.humidity = humidity;
        this.light = lightstatus;
        this.lastupdate = lastupdate;
        this.lastalert = lastalert;
        this.lastLightActive=lastLightActive;
    }

    public Mote(History history,String id){
        this.temperature=String.valueOf(history.getMoteHistory(id,IotlabType.TEMPERATURE).get(0).getValue());
        this.humidity=String.valueOf(history.getMoteHistory(id, IotlabType.HUMIDITY).get(0).getValue());
        this.light=String.valueOf(history.getMoteHistory(id,IotlabType.LIGHT1).get(0).getValue());
        try {
            this.lastLightActive = history.getMoteHistory(id, IotlabType.LIGHT1).get(1).getValue() > MainActivity.LIGHT_ON_OFF_STEP;
        }catch(Exception e) {
            this.lastLightActive = true;
        }
        this.name=id;
        this.lastupdate=String.valueOf(history.getMoteHistory(id).get(0).getDateInTimestamp());
        this.lastalert=String.valueOf(history.getLastalert(id));
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

    public String getLight() {
        return light;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public boolean getLastLightActive() {
        return lastLightActive;
    }

    public void setLastLightActive(boolean lastLightActive) {
        this.lastLightActive = lastLightActive;
    }

    @Override
    public String toString() {
        return "Mote{" +
                "name='" + name + '\'' +
                ", temperature='" + temperature + '\'' +
                ", humidity='" + humidity + '\'' +
                ", light=" + light +
                '}';
    }

    public int getLightColor(){
        int value= Double.valueOf(this.light).intValue();
        if(value>MainActivity.LIGHT_ON_OFF_STEP) return Color.GREEN;
        else if(MainActivity.LIGHT_ON_OFF_STEP>=value && MainActivity.LIGHT_ON_OFF_STEP>0) return Color.BLACK;
        else return Color.LTGRAY;
    }
    public boolean isOn(){
        int value= Double.valueOf(this.light).intValue();
        return value>MainActivity.LIGHT_ON_OFF_STEP;
    }


    public boolean becomeActive(){
        if(!lastLightActive && isOn()){
            return true;
        }
        return false;
    }
    public Date getDate(){
        return new Date(Long.valueOf(getLastupdate())*1000);
    }

}
