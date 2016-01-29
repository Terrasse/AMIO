package net.telecomnancy.projetamio;

import android.util.Log;

import java.util.TimerTask;

/**
 * Created by Terry on 21/01/2016.
 */
public class MyTask extends TimerTask {
    int i;

    public MyTask(){
        Log.d("MyTask", "Instanciation de MyTask");
        i=0;
    }

    @Override
    public void run() {
        i++;
        Log.d("MyTask","Itération n°"+i);
    }


}
