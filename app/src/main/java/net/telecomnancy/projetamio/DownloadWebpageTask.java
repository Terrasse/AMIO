package net.telecomnancy.projetamio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Terry on 28/02/2016.
 */
public class DownloadWebpageTask extends AsyncTask<String, Void, List<IotlabData>> {
    private TextView textView;
    private int status;

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    @Override
    protected List<IotlabData> doInBackground(String... urls) {
        // params comes from the execute() call: params[0] is the url.
        try {
            Log.d("DonwloadWebpageTask", "GET : " + urls[0]);
            return IotlabParser.getIotlabDatas(downloadUrl(urls[0]));

        } catch (IOException |FailedConnectionException e) {
            Log.d("DonwloadWebpageTask", "GET : "+urls[0]+" failed");
            return null;
        }
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(List<IotlabData> result) {
        textView.setText(result.toString());
    }

    private String downloadUrl(String myurl) throws IOException,FailedConnectionException {
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
            status = conn.getResponseCode();
            if(status!=200){
                throw new FailedConnectionException("Failed connection to the webService");
            }
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            Log.d("DonwloadWebpageTask", "GET output :"+contentAsString);
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
        while((line = reader.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }
}

