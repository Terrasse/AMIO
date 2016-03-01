package net.telecomnancy.projetamio;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

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

        HistoryViewHolder viewHolder = (HistoryViewHolder) convertView.getTag();
        if(viewHolder == null){
            viewHolder = new HistoryViewHolder();
            viewHolder.temperature = (TextView) convertView.findViewById(R.id.temperature);
            viewHolder.humidity = (TextView) convertView.findViewById(R.id.text);
            viewHolder.sensor_name = (TextView) convertView.findViewById(R.id.sensor_name);
            viewHolder.status = (ImageView) convertView.findViewById(R.id.status);
            convertView.setTag(viewHolder);
        }

        //getItem(position) va récupérer l'item [position] de la List<Tweet> tweets
        Mote current = getItem(position);

        //il ne reste plus qu'à remplir notre vue
        viewHolder.temperature.setText("Temperature : "+current.getTemperature()+"°C");
        viewHolder.humidity.setText("Humidity : " + current.getTemperature()+"%");
        viewHolder.sensor_name.setText(current.getName());
        viewHolder.status.setImageDrawable(new ColorDrawable(current.getLightstatus()));
        return convertView;
    }
        private class HistoryViewHolder{
            public TextView temperature;
            public TextView humidity;
            public TextView sensor_name;
            public ImageView status;
        }
}
