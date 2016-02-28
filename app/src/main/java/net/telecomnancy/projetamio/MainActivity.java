package net.telecomnancy.projetamio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    // sharedPreferencies

    public final static String CBONSTARTSTATE_KEY="key_CBOnStartState";
    public final static String PREFERENCES_KEY="key_PREFERENCES";
    public SharedPreferences sharedPreferences;


    // My Service
    Intent i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configuration de la toolbar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // instanciation du service
        this.i= new Intent(this,MainService.class);

        // récupération des différents objets graphiques concernant le MainService
        final ToggleButton b_onOff = (ToggleButton) findViewById(R.id.toggleButtonOnOff);
        final TextView b_tv2 = (TextView) findViewById(R.id.textViewTV2);

        Log.d("MainActivity", "Création du listener toogleButton/OnOff");

        // creation du listener du bouton OnOff pour l'arrêt de le demarrage du service
        b_onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startService(i);
                    b_tv2.setText("en cours");
                } else {
                    stopService(i);
                    b_tv2.setText("arrêté");
                }
            }
        });
        // répération des préferences de l'application
        sharedPreferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);

        // récupération de la checkbox StartAtBoot
        CheckBox b_StartAtBoot = (CheckBox) findViewById(R.id.checkBoxStart);

        // prise en compte des preferences de l'application
        b_StartAtBoot.setChecked(sharedPreferences.getBoolean(CBONSTARTSTATE_KEY,false));

        b_StartAtBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolean state=false;
                if(isChecked) {
                    state = true;
                    Log.d("MainActivity","L'application est lancé au démarrage du smartphone");
                } else {
                    state = false;
                    Log.d("MainActivity","L'application n'est pas lancé au démarrage du smartphone");
                }
                // enregistrement de la modification dans les préferences
                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putBoolean(CBONSTARTSTATE_KEY,state);
                editor.commit();
            }
        });

        // liaison du button refresh
        final TextView tv_result = (TextView) findViewById(R.id.textViewTV4);
        final Button b_refresh = (Button) findViewById(R.id.buttonRefresh);
        b_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownloadWebpageTask downloadWebpageTask = new DownloadWebpageTask(getApplicationContext(),tv_result);
                downloadWebpageTask.execute(getString(R.string.API_url));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("MainActivity", "Arret" +
                "" +
                " du programme");
        stopService(this.i);
        super.onDestroy();
    }
}
