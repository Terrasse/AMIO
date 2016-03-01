package net.telecomnancy.projetamio;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Terry on 01/03/2016.
 */
public class MotesUtils {
    public final static String JSON_KEY_TEMPERATURE="temperature";
    public final static String JSON_KEY_HUMIDITY="humidity";
    public final static String JSON_KEY_LIGHTSTATUS="lightstatus";
    public final static String JSON_KEY_NAME="name";
    public final static String JSON_KEY_TIMESTAMP="timestamp";
    public final static String JSON_KEY_LASTALERT="lastalert";
    public final static String JSON_KEY_LASTSTATE="laststate";



    public final static String toJSON(List<Mote> motes) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jw.setIndent("  ");
        jw.beginArray();
        for(Mote current: motes){
            jw.beginObject();
            jw.name(JSON_KEY_TEMPERATURE).value(current.getTemperature());
            jw.name(JSON_KEY_HUMIDITY).value(current.getHumidity());
            jw.name(JSON_KEY_LIGHTSTATUS).value(current.getLight());
            jw.name(JSON_KEY_NAME).value(current.getName());
            jw.name(JSON_KEY_TIMESTAMP).value(current.getLastupdate());
            jw.name(JSON_KEY_LASTALERT).value(current.getLastalert());
            jw.name(JSON_KEY_LASTSTATE).value(Boolean.toString(current.getLastLightActive()));
            jw.endObject();
        }
        jw.endArray();
        jw.close();
        return sw.toString();
    }

    public static List<Mote> fromJson(String json ) throws IOException {
        List<Mote> motes=new ArrayList<Mote>();
        StringReader rw = new StringReader(json);
        JsonReader jr = new JsonReader(rw);
        jr.beginArray();
        while (jr.hasNext()) {
            jr.beginObject();
            Mote mote = new Mote();
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals(JSON_KEY_TEMPERATURE)) {
                    mote.setTemperature(jr.nextString());
                } else if (name.equals(JSON_KEY_HUMIDITY)) {
                    mote.setHumidity(jr.nextString());
                } else if (name.equals(JSON_KEY_NAME)) {
                    mote.setName(jr.nextString());
                } else if (name.equals(JSON_KEY_LIGHTSTATUS)) {
                    mote.setLight(jr.nextString());
                }else if (name.equals(JSON_KEY_TIMESTAMP)) {
                    mote.setLastupdate(jr.nextString());
                } else if (name.equals(JSON_KEY_LASTALERT)) {
                    mote.setLastalert(jr.nextString());
                } else if (name.equals(JSON_KEY_LASTSTATE)) {
                    mote.setLastLightActive(Boolean.valueOf(jr.nextString()));
                } else {
                        jr.skipValue();
                }
            }
            motes.add(mote);
            jr.endObject();
        }
        jr.endArray();
        jr.close();
        return motes;
    }
}
