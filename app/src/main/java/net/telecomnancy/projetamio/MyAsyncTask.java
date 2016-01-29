package net.telecomnancy.projetamio;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Terry on 21/01/2016.
 */
public class MyAsyncTask extends AsyncTask<String,Integer,Long> {

    @Override
    protected Long doInBackground(String... params) {
        Log.d("MyAsyncTask","Execution de MyAsyncTask");
        publishProgress(100);
        return null;
    }

}
