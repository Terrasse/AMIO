package net.telecomnancy.projetamio;

import android.util.JsonReader;
import android.util.Log;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 28/02/2016.
 */
public class IotlabParser {
    public static final String CHAMP_TIMESTAMP ="timestamp";
    public static final String CHAMP_label ="timestamp";
    public static final String CHAMP_ID ="mote";
    public static final String CHAMP_VALUE ="value";

    public static List<IotlabData> getIotlabDatas(String json) throws IOException {

        JsonReader reader = new JsonReader(new StringReader(json));
        try {
            return readIotlabDataArray(reader);
        } finally {
            reader.close();
        }
    }

    private static List<IotlabData>  readIotlabDataArray(JsonReader reader) throws IOException {
        List<IotlabData>  datas = new ArrayList();


        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            reader.beginArray();
            while (reader.hasNext()) {
                datas.add(readIotlabData(reader));
            }
            reader.endArray();
        }
        reader.endObject();
        return datas;
    }

    private static IotlabData readIotlabData(JsonReader reader) throws IOException {
        String id="";
        IotlabType type=null;
        long timestamp=0;
        double value=0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(CHAMP_ID)) {
                id = reader.nextString();
                new IotlabData(id, timestamp, type, value);
            } else if (name.equals(CHAMP_label)) {
                String label = reader.nextString();
                for(IotlabType t : IotlabType.values()){
                    if(label.equalsIgnoreCase(t.getValue())) {
                        type = t;
                        break;
                    }
                }
            } else if (name.equals(CHAMP_TIMESTAMP)) {
                timestamp = reader.nextLong();
            } else if (name.equals(CHAMP_VALUE)) {
                value = reader.nextDouble();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new IotlabData(id, timestamp, type, value);
    }
}
