package net.telecomnancy.projetamio;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sam on 01/03/2016.
 */
public class MailService extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... urls) {
        if(urls.length!=2){
            Log.e(MailService.class.getName(), "Bad MailService Parameter");
        }
        try {
            GmailSender sender = new GmailSender("amio.app3@gmail.com", "schwartz.dervaux");
            sender.sendMail("AMIO_ALERT",
                    getBody(urls[1]),
                    "amio.app3@gmail.com",
                    urls[0]);
        } catch (Exception e) {
            Log.e(MailService.class.getName(), e.getMessage(), e);
        }
        return null;
    }
    private String getBody(String id){
        return id ;
    }
}