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


    public final static String toJSON(List<Mote> motes) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jw.setIndent("  ");
        jw.beginArray();
        for(Mote current: motes){
            jw.beginObject();
            jw.name(JSON_KEY_TEMPERATURE).value(current.getTemperature());
            jw.name(JSON_KEY_HUMIDITY).value(current.getHumidity());
            jw.name(JSON_KEY_LIGHTSTATUS).value(current.getLightstatus());
            jw.name(JSON_KEY_NAME).value(current.getName());
            jw.endObject();
        }
        jw.endArray();
        jw.close();
        Log.d("MotesUtils", "ToJson result: " + sw.toString());
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
                    mote.setHumidity(jr.nextString());
                } else if (name.equals(JSON_KEY_LIGHTSTATUS)) {
                    mote.setLightstatus(jr.nextInt());
                }else {
                    jr.skipValue();
                }
            }
            motes.add(mote);
            Log.d("MotesUtils","ToMote result: "+mote.toString());
            jr.endObject();
        }
        jr.endArray();
        jr.close();
        return motes;
    }
}
