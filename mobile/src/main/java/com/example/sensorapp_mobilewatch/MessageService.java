package com.example.sensorapp_mobilewatch;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;
import java.io.*;
import com.example.shared_mobile_watch.DataSensor;


public class MessageService extends WearableListenerService {

    public void onMessageReceived(MessageEvent messageEvent) {

        // Caso in cui è arrivato un dato da uno dei sensori
        if (messageEvent.getPath().equals("/data")) {

            final DataSensor[] message = getDataSensor(messageEvent.getData());
            Intent messageIntent = new Intent();
            messageIntent.setAction(Intent.ACTION_SEND);
            messageIntent.putExtra("message", message);


            //Broadcast the received Data Layer messages locally
            //inoltro il pacchetto perchè non è buona norma accedere al file system da un listener????
            // quindi lo inoltro e altri scriveranno su file
            LocalBroadcastManager.getInstance(this).sendBroadcast(messageIntent);
        }
    }



    public DataSensor[] getDataSensor(byte[] b) {

        ByteArrayInputStream bis = new ByteArrayInputStream(b);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(bis);
            return (DataSensor[]) in.readObject();
        }
        catch (IOException e) { Log.e("MainMobile", e.getLocalizedMessage(), e);}
        catch (ClassNotFoundException e) { Log.e("MainMobile", e.getLocalizedMessage(), e); }
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {ex.printStackTrace();}
        }
        return null;
    }
}
