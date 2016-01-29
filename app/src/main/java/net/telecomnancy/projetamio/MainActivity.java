package net.telecomnancy.projetamio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {

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
        ToggleButton b_onOff = (ToggleButton) findViewById(R.id.toggleButtonOnOff);
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

        // récupération de la checkbox StartAtBoot
        CheckBox b_StartAtBoot = (CheckBox) findViewById(R.id.checkBoxStart);
        b_StartAtBoot.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    Log.d("MainActivity","Le service MainService est lancé au démarrage");
                } else {
                    Log.d("MainActivity","Le service MainService est arrêté au démarrage");
                }
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
        Log.d("MainActivity", "Arret du programme");
        stopService(this.i);
        super.onDestroy();
    }
}
