package net.telecomnancy.projetamio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by Terry on 28/02/2016.
 */
public class MyBootBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // >cd D:\Utilisateurs\Terry\AppData\Local\Android\sdk\platform-tools
        // adb.exe shell am broadcast -a android.intent.action.BOOT_COMPLETED
        Log.d("MyBootBroadcastReceiver", "Reception d'un boardcast");

        // récupération des préférences de l'utilisateur & demarrage du service
        SharedPreferences prefs = context.getSharedPreferences(MainActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        Boolean startOnBoot = prefs.getBoolean(MainActivity.CBONSTARTSTATE_KEY,false);

        if(startOnBoot){
            Log.d("MyBootBroadcastReceiver", "Demarrage de MyService : ");
            Intent startServiceIntent = new Intent(context, PollingService.class);
            context.startService(startServiceIntent);
        }

    }
}
