package net.telecomnancy.projetamio;

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
import java.util.List;

public class MainActivity extends AppCompatActivity {
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
                ;
                MainActivity.this.startActivity(new Intent(MainActivity.this ,SettingActivity.class));
                return false;
            }
        });
//        toolbar.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                ;
//                MainActivity.this.startActivity(new Intent(MainActivity.this ,SettingActivity.class));
//                return false;
//            }
//        });

        // répération des préferences de l'application
        sharedPreferences = getSharedPreferences(SettingActivity.PREFERENCES_KEY, Context.MODE_PRIVATE);

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
            if (mServiceMessenger  != null) {
                try {
                    Message msg = Message.obtain(null, PollingService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mServiceMessenger.send(msg);
                }
                catch (RemoteException e) {
                    // There is nothing special we need to do if the service has crashed.
                }
            }
            // Detach our existing connection.
            unbindService(networkServiceConnection);
            mPollingService_isBound = false;
            tv_2.setText("disconnected");
        }
    }

}
