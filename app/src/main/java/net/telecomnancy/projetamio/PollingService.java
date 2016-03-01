package net.telecomnancy.projetamio;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PollingService extends Service {
    private static boolean isRunning = false;

    // Messages
    static final int MSG_SET_TV4 = 1;
    static final int MSG_UNREGISTER_CLIENT = 2;
    static final int MSG_REGISTER_CLIENT = 3;
    static final int MSG_UPDATE = 4;
    static final int MSG_CALLBACK_CLIENT = 5;

    // Task
    TimerTask pollingTask;
    Timer timer;

    // Objects
    History history;

    // Messagers
    private ArrayList<Messenger> clients = new ArrayList<Messenger>();
    private final Messenger mMessenger= new Messenger(new ServiceIncomingHandler());

    // Handler messengerService
    private class ServiceIncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.d("ServiceIncomingHandler","Reception d'un message");
            switch (msg.what) {
                case MSG_REGISTER_CLIENT:
                    Log.d("ServiceIncomingHandler", "Client registred " + msg.replyTo);
                    clients.add(msg.replyTo);
                    break;
                case MSG_UNREGISTER_CLIENT:
                    Log.d("PollingService", "Client unregistred " + msg.replyTo);
                    clients.remove(msg.replyTo);
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }
        }
    }

    // asyncTask
    private class DownloadWebpageTask extends AsyncTask<String, Void, List<String>> {
        private List status;
        private long id;

        public DownloadWebpageTask() {
            super();
            this.status = new ArrayList();
            this.id = System.currentTimeMillis();
        }

        @Override
        protected List<String> doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                List<String> data = new ArrayList<String>();
                for(String currentString: urls){
                    Log.i("DonwloadWebpageTask", "GET(id=" + id + ") url : " + currentString);
                    data.add(downloadUrl(currentString));
                }
                return data;
            } catch (IOException e) {
                Log.d("DonwloadWebpageTask", "GET(id=" + id + ") failed");
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<String> data) {
            super.onPostExecute(data);

            // traitement du résultat de téléchargement de la page web
            for (int i=0; i<data.size(); i++){
                Log.i("DonwloadWebpageTask", "GET(id=" + id + ") status : " + status.get(i));
                try {
                if ((int)(status.get(i)) != 200) {
                    throw new FailedConnectionException("");
                } else {
                    Log.i("DonwloadWebpageTask", "GET(id=" + id + ") data : " + data.get(i));
                    history.addJSONIotlabDatas(data.get(i));
                }
                } catch(FailedConnectionException | IOException e){
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(),getApplicationContext().getString(R.string.API_failed), duration);
                    toast.show();
                }
            }
            notifyAllClient();
        }

        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;
            // Only display the first 500 characters of the retrieved
            // web page content.
            int len = 500;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(100000 /* milliseconds */);
                conn.setConnectTimeout(150000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                // Starts the query
                conn.connect();
                status.add(conn.getResponseCode());
                is = conn.getInputStream();

                // Convert the InputStream into a string
                String contentAsString = readIt(is, len);
                return contentAsString;
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        // Reads an InputStream and converts it to a String.
        public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    public PollingService(){
       super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        Log.d("PollingService", "Démarrage du PollingService");

        // History
        this.history=new History();

        // récupération ded informations polling
        Resources res = getResources();
        int polling_period = res.getInteger(R.integer.PollingService_period); // intervalle de polling

        // récupération des urls de pollng
        final List<String> urls= new ArrayList<String>();
        urls.add(res.getString(R.string.API_url_humidity));
        urls.add(res.getString(R.string.API_url_light1));
        urls.add(res.getString(R.string.API_url_temperature));



        // Creation d'une tâche asynchrone réccurente
        Log.d("PollingService", "Creation de PollingTask(r=" + polling_period + ")");

        this.pollingTask = new TimerTask(){
            @Override
            public void run() {
                new DownloadWebpageTask().execute(urls.get(0),urls.get(1),urls.get(2));
            }
        };

        this.timer = new Timer(true);
        this.timer.scheduleAtFixedRate(this.pollingTask, 0, polling_period);
        isRunning = true;
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("PollingService","Destruction du service");
        this.timer.cancel();
        this.timer.purge();
        this.pollingTask.cancel();
        super.onDestroy();
        isRunning = false;
    }


    /**
     * When binding to the service, we return an interface to our messenger
     * for sending messages to the service.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public boolean stopService(Intent name) {
        Log.d("PollingService", "Arrêt du service");
        return super.stopService(name);
    }

    /*    private void showNotification() {
            // In this sample, we'll use the same text for the ticker and the expanded notification
            CharSequence text = getText(R.string.PollingService_started);

            // The PendingIntent to launch our activity if the user selects this notification
            PendingIntent contentIntent = PendingIntent.getActivity(this, 0,new Intent(this, MainActivity.IncomingHandler.class), 0);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.ic_polling)
                            .setContentTitle("My notification")
                            .setContentText("Hello World!");
            // Set the info for the views that show in the notification panel.
            // Sets an ID for the notification
            int mNotificationId = 001;
            // Gets an instance of the NotificationManager service

            // Builds the notification and issues it.
            this.mNM.notify(mNotificationId, mBuilder.build());
        }*/

    public void notifyAllClient(){
        // TODO : refactoring -> permet de récupérer de passer d'un IotlabData à un liste de motes
        List<Mote> motes=new ArrayList<Mote>();
        for(String currentKey:history.getHistory().keySet()) {
            motes.add(new Mote(history,currentKey));
        }

        // transformation en json
        String stringMotes="";
        try {
            stringMotes=MotesUtils.toJSON(motes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // send des motes aux clients
        for (int i = 0; i < clients.size(); i++) {
            try {
                //Send data as a String
                Bundle b = new Bundle();
                b.putString("str1",stringMotes);
                Message msg = Message.obtain(null, MSG_UPDATE);
                msg.setData(b);
                clients.get(i).send(msg);
            } catch (RemoteException e) {
                // If we get here, the client is dead, and we should remove it from the list
                Log.d("PollingService","Client removed : " + clients.get(i));
                clients.remove(i);
            }
        }
    }

    public static boolean isRunning()
    {
        return isRunning;
    }
}
