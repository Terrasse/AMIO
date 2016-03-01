package net.telecomnancy.projetamio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Terry on 01/03/2016.
 * doc : http://tutos-android-france.com/listview-afficher-une-liste-delements/
 */
public class MoteAdapter extends ArrayAdapter<Mote> {
    public MoteAdapter(Context context, List<Mote> motes) {
        super(context, 0, motes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_capteur,parent, false);
        }

        MoteViewHolder viewHolder = (MoteViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new MoteViewHolder();
            viewHolder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.humidity = (TextView) convertView.findViewById(R.id.humidity);
            viewHolder.sensor_name = (TextView) convertView.findViewById(R.id.sensor_name);
            viewHolder.status = (ImageView) convertView.findViewById(R.id.status);
            viewHolder.last_alert = (TextView) convertView.findViewById(R.id.last_alert);
            viewHolder.last_update = (TextView) convertView.findViewById(R.id.last_update);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Mote current = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.temperature.setText("Temperature : "+current.getTemperature()+"°C");
        viewHolder.humidity.setText("Humidity : " + current.getHumidity()+"%");
        viewHolder.sensor_name.setText("Mote : "+current.getName());
        viewHolder.last_alert.setText("Last alert : "+current.getLastalert());
        Date testDate = new Date(Long.valueOf(current.getLastupdate())*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy hh:mm:ss a");
        String newFormat = formatter.format(testDate);
        viewHolder.last_update.setText("Last update : " +newFormat);
        viewHolder.status.setBackgroundColor(current.getLightColor());
        return convertView;
    }
        private class MoteViewHolder{
            public TextView temperature;
            public TextView humidity;
            public TextView sensor_name;
            public ImageView status;
            public TextView last_alert;
            public TextView last_update;
        }
}
