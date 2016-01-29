package net.telecomnancy.projetamio;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Timer;

public class MainService extends Service {
    public final static int TIMER_PERIODE= 2*1000;
    MyTask task;
    Timer taskTimer;
    MyAsyncTask asyncTask;

    public MainService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Log.d("MainService","Démarrage du service");

        // Creation d'un tâche asynchroe
        Log.d("MainService", "Creation de MyAsyncTask (r="+TIMER_PERIODE+")");
        asyncTask=new MyAsyncTask();
        asyncTask.execute("Salut");

        // Creation d'une tâche réccurente
        Log.d("MainService", "Creation de MyTask(r="+TIMER_PERIODE+")");
        this.task=new MyTask();
        this.taskTimer=new Timer(true);

        this.taskTimer.scheduleAtFixedRate(this.task, 0, TIMER_PERIODE);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("MainService","Destruction du service");
        this.taskTimer.cancel();
        this.taskTimer.purge();
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d("MainService","Arrêt du service");
        return super.stopService(name);
    }
}
