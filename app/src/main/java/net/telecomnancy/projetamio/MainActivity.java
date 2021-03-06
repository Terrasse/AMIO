package net.telecomnancy.projetamio;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import java.io.IOException;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    public static int LIGHT_ON_OFF_STEP = 250;


    // sharedPreferencies
    public SharedPreferences sharedPreferences;


    // My Service
    private Intent mPoolingServiceIntend;
    private boolean mPollingService_isBound;

    // My interface
    public TextView tv_2, tv_4, tv_6, tv_archive  ;
    public ToggleButton b_onOff;
    ListView mListView;

    // My objects
    List<Mote> motes;

    // Messengers
    Messenger mServiceMessenger = null;
    Messenger mMessenger = new Messenger(new IncomingHandler());


    // handler qui va reçevoir les notifications du service
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case PollingService.MSG_UPDATE:
                    Log.d("MainActivity", "update view");
                    String str1 = msg.getData().getString("str1");
                    MoteAdapter adapter = null;
                    try {
                        adapter = new MoteAdapter(MainActivity.this, MotesUtils.fromJson(str1));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mListView.setAdapter(adapter);
                    for(Mote m : adapter.getItems()){
                        if(m.becomeActive()){
                            PlageHoraire ph = getPreferedPlageHoraire();
                            switch (PlageHoraire.OnPlage(ph, m.getDate())){
                                case NOTIFICATION:
                                    Log.i(MainActivity.class.getName(), "send Notification");
                                    sendNotification(m);
                                    break;
                                case EMAIL:
                                    Log.i(MainActivity.class.getName(), "send email");
                                    new MailService().execute(getPreferedMail(),m.getName());
                                    break;
                            }
                        }
                    }
                    break;
                case PollingService.MSG_CALLBACK_CLIENT:
                    Log.d("MainIncomingHandler","Callback reçu");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }

    // va permettre d'envoyer un message au service
    private ServiceConnection networkServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            mServiceMessenger = new Messenger(service);
            try {
                Message msg = Message.obtain(null,PollingService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mServiceMessenger.send(msg);
                Log.d("MainActivity", getString(R.string.PollingService_connected));
            } catch (RemoteException e) {
                // Here, the service has crashed even before we were able to connect
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName className) {
            // This is called when the connection with the service has been unexpectedly disconnected - process crashed.
            mServiceMessenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // configuration de la toolbar
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //action du bouton setting
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingActivity.class));
                return false;
            }
        });


        // répération des préferences de l'application
        sharedPreferences = getSharedPreferences(SettingActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);
        LIGHT_ON_OFF_STEP = getPreferedLightLevel();

        // récupération des différents objets graphiques concernant le PollingService
        b_onOff = (ToggleButton) findViewById(R.id.toggleButtonOnOff);
        this.tv_2 = (TextView) findViewById(R.id.textViewTV2);

        Log.d("MainActivity", "Création du listener toogleButton/OnOff");

        mPoolingServiceIntend = new Intent(this, PollingService.class);

        if (PollingService.isRunning()) {
            doBindService();
        }

        // creation du listener du bouton OnOff pour l'arrêt de le demarrage du service
        b_onOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_2.setText("en cours");
                    startService(mPoolingServiceIntend);
                    doBindService();
                } else {
                    tv_2.setText("arrêté");
                    doUnbindService();
                    stopService(mPoolingServiceIntend);
                }
            }
        });

        // listView
        mListView = (ListView) findViewById(R.id.listView);

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
        super.onDestroy();
    }

    void doBindService() {
        Log.d("MainActivity", "Bind PollingService");
        bindService(mPoolingServiceIntend, networkServiceConnection, Context.BIND_AUTO_CREATE);
        mPollingService_isBound = true;
        tv_2.setText("connected");

    }

    void doUnbindService() {
        Log.d("MainActivity", "Unbind PollingService");
        if (mPollingService_isBound) {
            // If we have received the service, and hence registered with it, then now is the time to unregister.
            if (mServiceMessenger != null) {
                try {
                    Message msg = Message.obtain(null, PollingService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(networkServiceConnection);
            mPollingService_isBound = false;
            tv_2.setText("disconnected");
        }
    }
    static void setLightOnOffStep(int value){
        LIGHT_ON_OFF_STEP=value;
    }

    public PlageHoraire getPreferedPlageHoraire(){
        try {
            return PlageHoraire.fromJson(sharedPreferences.getString(SettingActivity.PLAGE_HORAIRE_KEY, ""));
        }catch (Exception e){
            Log.e(MainActivity.class.getName(),"Erreur to get pref plageHoraire");
            return new PlageHoraire();
        }
    }
    public String getPreferedMail(){
        return sharedPreferences.getString(SettingActivity.MAIL_KEY, "");
    }
    public int getPreferedLightLevel(){
        return sharedPreferences.getInt(SettingActivity.LIGHT_LEVEL_KEY, 250);
    }
    public void sendNotification(Mote m){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_polling)
                        .setContentTitle("Alert_AMIO")
                        .setContentText("Sensor :"+m.getName()+"\n Light ON");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

    // The stack builder object will contain an artificial back stack for the
    // started Activity.
    // This ensures that navigating backward from the Activity leads out of
    // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(10, mBuilder.build());
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(500);
    }

}
