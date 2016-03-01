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
        protected NotificationType afternoon=NotificationType.NONE;
        protected NotificationType night=NotificationType.NONE;
        public CreneauHoraire(){}
        public CreneauHoraire (NotificationType start,NotificationType end){
            this.afternoon=start;
            this.night=end;
        }
        public void setAfternoon(NotificationType afternoon){
            this.afternoon=afternoon;
        }
        public void setNight(NotificationType night){
            this.night=night;
        }

        public NotificationType getAfternoon() {
            return afternoon;
        }

        public NotificationType getNight() {
            return night;
        }

//        public boolean isOnCreneau(Date d){
//            if(d!=null && d.getHours()>=startHour && d.getHours()<endHour ){
//                return true;
//            }
//            return false;
//        }
    }
    private Map<Integer, CreneauHoraire> plages;

    public PlageHoraire(){
        plages=new HashMap<>();
    }

    public void setDayHours(int day,NotificationType afternoon,NotificationType night){
        if(!(day>=1 && day <=7)){
           return ;
        }
        plages.put(day, new CreneauHoraire(afternoon, night));
    }
    public void removeDay(int day){
        plages.remove(day);
    }

    public Map<Integer,CreneauHoraire> getPlages(){
        return plages;
    }
    public static NotificationType OnPlage(PlageHoraire ph, Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);
        //verifier fin de nuit de la veille
        int day = c.get(Calendar.DAY_OF_WEEK);
        int yesturday = c.get(Calendar.DAY_OF_WEEK) - 1 % 7;
        yesturday = yesturday==0 ? 7 : yesturday;
        if(d.getHours()<6 && ph.getPlages().containsKey(yesturday) && ph.getPlages().get(yesturday).getNight()!=null &&  ph.getPlages().get(yesturday).getNight()!=NotificationType.NONE){
            return ph.getPlages().get(yesturday).getNight();
        }
        if(d.getHours()>=19 && d.getHours()<23 && ph.getPlages().containsKey(day) && ph.getPlages().get(day).getAfternoon()!=null &&  ph.getPlages().get(day).getAfternoon()!=NotificationType.NONE){
            return ph.getPlages().get(day).getAfternoon();
        }
        if(d.getHours()>=23 && ph.getPlages().containsKey(day) && ph.getPlages().get(day).getNight()!=null &&  ph.getPlages().get(day).getNight()!=NotificationType.NONE){
            return ph.getPlages().get(day).getNight();
        }
        return NotificationType.NONE;
    }

    public static String toJson(PlageHoraire ph) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter jw = new JsonWriter(sw);
        jw.setIndent("  ");
        jw.beginArray();
        for(Integer key : ph.getPlages().keySet()){
            jw.beginObject();
            jw.name(JSON_KEY_DAY).value(key);
            jw.name(JSON_KEY_START).value(ph.getPlages().get(key).getAfternoon().getValue());
            jw.name(JSON_KEY_END).value(ph.getPlages().get(key).getNight().getValue());
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
            Integer key=null;
            NotificationType start=null, end=null;
            while (jr.hasNext()) {
                String name = jr.nextName();
                if (name.equals(JSON_KEY_DAY)) {
                    key = jr.nextInt();
                } else if (name.equals(JSON_KEY_START)) {
                    String label = jr.nextString();
                    for(NotificationType t : NotificationType.values()){
                        if(label.equalsIgnoreCase(t.getValue())) {
                            start = t;
                            break;
                        }
                    }
                } else if (name.equals(JSON_KEY_END)) {
                    String label = jr.nextString();
                    for(NotificationType t : NotificationType.values()){
                        if(label.equalsIgnoreCase(t.getValue())) {
                            end = t;
                            break;
                        }
                    };
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
            sw.append(""+key+" afternoon:"+plages.get(key).getAfternoon().getValue()+" night:"+plages.get(key).getNight().getValue()+"\n");
        }
        return sw.toString();
    }
}
