package net.telecomnancy.projetamio;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by sam on 29/02/2016.
 */
public class PlageHoraire {
    public static String JSON_KEY_DAY="day";
    public static String JSON_KEY_START="start";
    public static String JSON_KEY_END="end";

    public static class CreneauHoraire{
        protected int startHour;
        protected int endHour;
        public CreneauHoraire(){}
        public CreneauHoraire (int start,int end){
            this.startHour=start;
            this.endHour=end;
        }
        public void setStartHour(int startHour){
            this.startHour=startHour;
        }
        public void setEndHour(int endHour){
            this.endHour=endHour;
        }

        public int getStartHour() {
            return startHour;
        }

        public int getEndHour() {
            return endHour;
        }

        public boolean isOnCreneau(Date d){
            if(d!=null && d.getHours()>=startHour && d.getHours()<endHour ){
                return true;
            }
            return false;
        }
    }
    private Map<Integer, CreneauHoraire> plages;

    public PlageHoraire(){
        plages=new HashMap<>();
    }

    public void setDayHours(int day,int startHour,int endHour){
        if(!(day>=1 && day <=7)){
           return ;
        }
        plages.put(day, new CreneauHoraire(startHour, endHour));
    }
    public void removeDay(int day){
        plages.remove(day);
    }
    public void setWeekEndHours(int startHour,int endHour){
        setDayHours(Calendar.SUNDAY, startHour, endHour);
        setDayHours(Calendar.SATURDAY, startHour, endHour);
    }
    public void setWeekHours(int startHour,int endHour){
        setDayHours(Calendar.MONDAY, startHour, endHour);
        setDayHours(Calendar.THURSDAY, startHour, endHour);
        setDayHours(Calendar.TUESDAY, startHour, endHour);
        setDayHours(Calendar.WEDNESDAY, startHour, endHour);
        setDayHours(Calendar.FRIDAY, startHour, endHour);
    }
    public void setAllDayHours(int startHour,int endHour){
        setWeekEndHours(startHour, endHour);
        setWeekHours(startHour, endHour);
    }
    public Map<Integer,CreneauHoraire> getPlages(){
        return plages;
    }
    public static boolean isOnPlage(PlageHoraire ph, Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        if (!ph.getPlages().containsKey(c.get(Calendar.DAY_OF_WEEK))) {
            return false;
        }
        return ph.getPlages().get(c.get(Calendar.DAY_OF_WEEK)).isOnCreneau(d);
    }

    public static String toJson(PlageHoraire ph) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jw.setIndent("  ");
        jw.beginArray();
        for(Integer key : ph.getPlages().keySet()){
            jw.beginObject();
            jw.name(JSON_KEY_DAY).value(key);
            jw.name(JSON_KEY_START).value(ph.getPlages().get(key).getStartHour());
            jw.name(JSON_KEY_END).value(ph.getPlages().get(key).getEndHour());
            jw.endObject();
        }
        jw.endArray();
        jw.close();
        Log.d(PlageHoraire.class.getName(), "ToJson result: " + sw.toString());
        return sw.toString();
    }

    public static PlageHoraire fromJson(String json ) throws IOException {
        PlageHoraire ph = new PlageHoraire();
        StringReader rw = new StringReader(json);
        JsonReader jr = new JsonReader(rw);
        jr.beginArray();
        while (jr.hasNext()) {
            jr.beginObject();
            Integer key=null, start=null, end=null;
            CreneauHoraire ch = new CreneauHoraire();
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals(JSON_KEY_DAY)) {
                    key = jr.nextInt();
                } else if (name.equals(JSON_KEY_START)) {
                    start = jr.nextInt();
                } else if (name.equals(JSON_KEY_END)) {
                    end=jr.nextInt();
                } else {
                    jr.skipValue();
                }
            }
            Log.d(PlageHoraire.class.getName(),""+key+", start:"+start+", end:"+end);
            ph.setDayHours(key,start,end);
            jr.endObject();
        }
        jr.endArray();
        jr.close();
        return ph;
    }


    public String toString(){
        StringWriter sw = new StringWriter();
        sw.write("");
        for(Integer key : plages.keySet()){
            sw.append(""+key+" start:"+plages.get(key).getStartHour()+" end:"+plages.get(key).getEndHour()+"\n");
        }
        return sw.toString();
    }
}
