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
import android.widget.Spinner;
import android.widget.Switch;

import java.io.IOException;
import java.util.Calendar;

/**
 * Created by sam on 29/02/2016.
 */
public class SettingActivity extends AppCompatActivity {

    public final static String ON_START_STATE_KEY="key_onStartState";
    public final static String PLAGE_HORAIRE_KEY="key_plageHoraire";
    public final static String MAIL_KEY="key_mail";
    public final static String LIGHT_LEVEL_KEY="key_lightLevel";
    public final static String PREFERENCES_KEY="key_preference";

    public SharedPreferences sharedPreferences;


    CheckBox cb1;
    Spinner npS1;
    Spinner npE1;

    CheckBox cb2;
    Spinner npS2;
    Spinner npE2;

    CheckBox cb3;
    Spinner npS3;
    Spinner npE3;

    CheckBox cb4;
    Spinner npS4;
    Spinner npE4;

    CheckBox cb5;
    Spinner npS5;
    Spinner npE5;

    CheckBox cb6;
    Spinner npS6;
    Spinner npE6;

    CheckBox cb7;
    Spinner npS7;
    Spinner npE7;

    EditText mail;

    CheckBox b_StartAtBoot;

    NumberPicker npLight;


    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);


        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        final Button b_save = (Button) findViewById(R.id.save);
        if(b_save!=null){
            b_save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlageHoraire ph = getPlageHoraire();
                    Log.d(SettingActivity.class.getName(), "PlageHoraire : " + ph.toString());
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    try {
                        editor.putString(PLAGE_HORAIRE_KEY, PlageHoraire.toJson(ph));
                    } catch (IOException e) {
                        Log.e(SettingActivity.class.getName(), "Fail to convert preferences: " + e.getMessage());
                    }
                    editor.putString(MAIL_KEY, mail.getText().toString());
                    editor.putInt(LIGHT_LEVEL_KEY, npLight.getValue());
                    editor.putBoolean(ON_START_STATE_KEY, b_StartAtBoot.isChecked());
                    MainActivity.setLightOnOffStep(npLight.getValue());
                    editor.commit();
                    SettingActivity.this.finish();
                }
            });}
        cb1 = (CheckBox) findViewById(R.id.checkBox1);
        npS1 = (Spinner) findViewById(R.id.startpicker1);
        npE1 = (Spinner) findViewById(R.id.endPicker1);
        initBox(cb1, npS1, npE1);

        cb2 = (CheckBox) findViewById(R.id.checkBox2);
        npS2 = (Spinner) findViewById(R.id.startpicker2);
        npE2 = (Spinner) findViewById(R.id.endPicker2);
        initBox(cb2,npS2,npE2);

        cb3 = (CheckBox) findViewById(R.id.checkBox3);
        npS3 = (Spinner) findViewById(R.id.startpicker3);
        npE3 = (Spinner) findViewById(R.id.endPicker3);
        initBox(cb3,npS3,npE3);

        cb4 = (CheckBox) findViewById(R.id.checkBox4);
        npS4 = (Spinner) findViewById(R.id.startpicker4);
        npE4 = (Spinner) findViewById(R.id.endPicker4);
        initBox(cb4,npS4,npE4);

        cb5 = (CheckBox) findViewById(R.id.checkBox5);
        npS5 = (Spinner) findViewById(R.id.startpicker5);
        npE5 = (Spinner) findViewById(R.id.endPicker5);
        initBox(cb5,npS5,npE5);

        cb6 = (CheckBox) findViewById(R.id.checkBox6);
        npS6 = (Spinner) findViewById(R.id.startpicker6);
        npE6 = (Spinner) findViewById(R.id.endPicker6);
        initBox(cb6,npS6,npE6);

        cb7 = (CheckBox) findViewById(R.id.checkBox7);
        npS7 = (Spinner) findViewById(R.id.startpicker7);
        npE7 = (Spinner) findViewById(R.id.endPicker7);
        initBox(cb7,npS7,npE7);



        mail = (EditText) findViewById(R.id.mail);
        mail.setText(sharedPreferences.getString(MAIL_KEY,""));
        //GetPreference

        String plageHoraireJson = sharedPreferences.getString(PLAGE_HORAIRE_KEY,"");
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


        // récupération de la checkbox StartAtBoot
        b_StartAtBoot = (CheckBox) findViewById(R.id.checkBoxStart);

        // prise en compte des preferences de l'application
        b_StartAtBoot.setChecked(sharedPreferences.getBoolean(ON_START_STATE_KEY, false));


        npLight = (NumberPicker) findViewById(R.id.lightpicker);
        npLight.setMinValue(0);
        npLight.setMaxValue(1000);
        npLight.setValue(sharedPreferences.getInt(LIGHT_LEVEL_KEY,250));

    }

    private void initValue(CheckBox cb, final Spinner start, final Spinner end, PlageHoraire.CreneauHoraire ch) {
        cb.setChecked(true);
        setValue(start, ch.getAfternoon());
        setValue(end,ch.getNight());
    }


    private void initBox(CheckBox cb, final Spinner start, final Spinner end){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    start.setEnabled(true);
                    end.setEnabled(true);
                } else {
                    start.setEnabled(false);
                    end.setEnabled(false);
                }
            }
        });
        start.setEnabled(false);
        end.setEnabled(false);
    }
    private PlageHoraire getPlageHoraire(){
        PlageHoraire ph = new PlageHoraire();
        addDay(cb1,npS1,npE1,Calendar.MONDAY,ph);
        addDay(cb2,npS2,npE2,Calendar.TUESDAY,ph);
        addDay(cb3,npS3,npE3,Calendar.WEDNESDAY,ph);
        addDay(cb4,npS4,npE4,Calendar.THURSDAY,ph);
        addDay(cb5, npS5, npE5, Calendar.FRIDAY, ph);
        addDay(cb6, npS6, npE6, Calendar.SATURDAY, ph);
        addDay(cb7, npS7, npE7, Calendar.SUNDAY, ph);
        return ph;
    }
    private void addDay(CheckBox cb, Spinner start, Spinner end, int day, PlageHoraire ph ){
        Log.d("vsz", "day");
        if(cb.isChecked()){
            ph.setDayHours(day, getValue(start), getValue(end));
        }
    }
    private NotificationType getValue(Spinner sp){
        sp.getSelectedItem();
        if(sp.getSelectedItem().toString()==null){
            Log.d("vsz","0"+sp.getSelectedItem().toString());
            return NotificationType.NONE;
        }
        if(NotificationType.EMAIL.getValue().equalsIgnoreCase(String.valueOf((sp.getSelectedItem().toString())))){
            Log.d("vsz","1");
            return NotificationType.EMAIL;
        }
        if(NotificationType.NOTIFICATION.getValue().equalsIgnoreCase(String.valueOf((sp.getSelectedItem().toString())))){
            Log.d("vsz", "2");
            return NotificationType.NOTIFICATION;
        }
        return NotificationType.NONE;
    }

    private void setValue(Spinner sp, NotificationType type){
        switch (type){
            case NONE: sp.setSelection(0);
                break;
            case NOTIFICATION: sp.setSelection(1);
                break;
            case EMAIL: sp.setSelection(2);
                break;
            default: sp.setSelection(0);
        }
    }
}
