package net.telecomnancy.projetamio;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Terry on 21/01/2016.
 */
public class MyAsyncTask extends AsyncTask<String,Integer,Long> {

    @Override
    protected Long doInBackground(String... params) {
        Log.d("MyAsyncTask","Récupératon de la métrique du capteur");
        URL url = null;
        try {
            url = new URL("http://iotlab.telecomnancy.eu/rest/data/1/%20light1/last");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
            Log.d("MyAsyncTask",result.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }




        publishProgress(100);
        return null;
    }

}
