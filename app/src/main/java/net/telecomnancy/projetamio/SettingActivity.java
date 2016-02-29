package net.telecomnancy.projetamio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by sam on 29/02/2016.
 */
public class SettingActivity extends AppCompatActivity {

    public SharedPreferences sharedPreferences;


    CheckBox cb1;
    NumberPicker npS1;
    NumberPicker npE1;

    CheckBox cb2;
    NumberPicker npS2;
    NumberPicker npE2;

    CheckBox cb3;
    NumberPicker npS3;
    NumberPicker npE3;

    CheckBox cb4;
    NumberPicker npS4;
    NumberPicker npE4;

    CheckBox cb5;
    NumberPicker npS5;
    NumberPicker npE5;

    CheckBox cb6;
    NumberPicker npS6;
    NumberPicker npE6;

    CheckBox cb7;
    NumberPicker npS7;
    NumberPicker npE7;

    EditText mail;



    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);
        Log.d("vsztest", "In second seting Layout");
        final Button b_save = (Button) findViewById(R.id.save);
        if(b_save!=null){
            b_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlageHoraire ph = getPlageHoraire();
                    Log.d(SettingActivity.class.getName(), "PlageHoraire : " + ph.toString());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    try {
                        editor.putString(MainActivity.PLAGE_HORAIRE_KEY, PlageHoraire.toJson(ph));
                    } catch (IOException e) {
                        Log.e(SettingActivity.class.getName(), "Fail to convert preferences: " + e.getMessage());
                    }
                    editor.putString(MainActivity.MAIL_KEY, mail.getText().toString());
                    editor.commit();
                    SettingActivity.this.finish();
                }
            });}
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        npS1 = (NumberPicker) findViewById(R.id.startpicker1);
        npE1 = (NumberPicker) findViewById(R.id.endPicker1);
        initBox(cb1, npS1, npE1);

        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        npS2 = (NumberPicker) findViewById(R.id.startpicker2);
        npE2 = (NumberPicker) findViewById(R.id.endPicker2);
        initBox(cb2,npS2,npE2);

        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        npS3 = (NumberPicker) findViewById(R.id.startpicker3);
        npE3 = (NumberPicker) findViewById(R.id.endPicker3);
        initBox(cb3,npS3,npE3);

        cb4 = (CheckBox) findViewById(R.id.checkBox4);
        npS4 = (NumberPicker) findViewById(R.id.startpicker4);
        npE4 = (NumberPicker) findViewById(R.id.endPicker4);
        initBox(cb4,npS4,npE4);

        cb5 = (CheckBox) findViewById(R.id.checkBox5);
        npS5 = (NumberPicker) findViewById(R.id.startpicker5);
        npE5 = (NumberPicker) findViewById(R.id.endPicker5);
        initBox(cb5,npS5,npE5);

        cb6 = (CheckBox) findViewById(R.id.checkBox6);
        npS6 = (NumberPicker) findViewById(R.id.startpicker6);
        npE6 = (NumberPicker) findViewById(R.id.endPicker6);
        initBox(cb6,npS6,npE6);

        cb7 = (CheckBox) findViewById(R.id.checkBox7);
        npS7 = (NumberPicker) findViewById(R.id.startpicker7);
        npE7 = (NumberPicker) findViewById(R.id.endPicker7);
        initBox(cb7,npS7,npE7);


        sharedPreferences = getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);

        mail = (EditText) findViewById(R.id.mail);
        mail.setText(sharedPreferences.getString(MainActivity.MAIL_KEY,""));
        //GetPreference

        String plageHoraireJson = sharedPreferences.getString(MainActivity.PLAGE_HORAIRE_KEY,"");
        try {
            if(!(plageHoraireJson==null || plageHoraireJson.isEmpty())){
                PlageHoraire ph = PlageHoraire.fromJson(plageHoraireJson);
                for(Integer key : ph.getPlages().keySet()){
                    switch (key){
                        case Calendar.MONDAY :
                            initValue(cb1,npS1,npE1, ph.getPlages().get(key));
                            break;
                        case Calendar.TUESDAY :
                            initValue(cb2,npS2,npE2, ph.getPlages().get(key));
                            break;
                        case Calendar.WEDNESDAY :
                            initValue(cb3,npS3,npE3, ph.getPlages().get(key));
                            break;
                        case Calendar.THURSDAY :
                            initValue(cb4,npS4,npE4, ph.getPlages().get(key));
                            break;
                        case Calendar.FRIDAY :
                            initValue(cb5,npS5,npE5, ph.getPlages().get(key));
                            break;
                        case Calendar.SATURDAY :
                            initValue(cb6,npS6,npE6, ph.getPlages().get(key));
                            break;
                        case Calendar.SUNDAY :
                            initValue(cb7,npS7,npE7, ph.getPlages().get(key));
                            break;
                    }
                }
            }
        } catch (IOException e) {
            Log.e(SettingActivity.class.getName(),"Fail to parse preferences: "+e.getMessage());
        }
    }

    private void initValue(CheckBox cb, final NumberPicker start, final NumberPicker end, PlageHoraire.CreneauHoraire ch) {
        cb.setChecked(true);
        start.setValue(ch.getStartHour());
        end.setValue(ch.getEndHour());
    }


    private void initBox(CheckBox cb, final NumberPicker start, final NumberPicker end){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    start.setEnabled(true);
                    end.setEnabled(true);
                }else{
                    start.setEnabled(false);
                    end.setEnabled(false);
                }
            }
        });
        start.setEnabled(false);
        start.setMaxValue(24);
        start.setMinValue(0);
        end.setEnabled(false);
        end.setMaxValue(24);
        end.setMinValue(0);
    }
    private PlageHoraire getPlageHoraire(){
        PlageHoraire ph = new PlageHoraire();
        addDay(cb1,npS1,npE1,Calendar.MONDAY,ph);
        addDay(cb2,npS2,npE2,Calendar.TUESDAY,ph);
        addDay(cb3,npS3,npE3,Calendar.WEDNESDAY,ph);
        addDay(cb4,npS4,npE4,Calendar.THURSDAY,ph);
        addDay(cb5,npS5,npE5,Calendar.FRIDAY,ph);
        addDay(cb6,npS6,npE6,Calendar.SATURDAY ,ph);
        addDay(cb7,npS7,npE7,Calendar.SUNDAY,ph);
        return ph;
    }
    private void addDay(CheckBox cb, NumberPicker start, NumberPicker end, int day, PlageHoraire ph ){
        if(cb.isChecked()){
            ph.setDayHours(day,start.getValue(),end.getValue());
        }
    }
}
