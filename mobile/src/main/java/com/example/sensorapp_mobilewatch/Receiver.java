package com.example.sensorapp_mobilewatch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.shared_mobile_watch.DataSensor;

import java.io.FileOutputStream;
import java.io.IOException;

//Define a nested class that extends BroadcastReceiver
public class Receiver extends BroadcastReceiver {

    int iterazione = 0;
    MainActivity main;

    public Receiver(MainActivity m){
        main = m;
    }

    public void onReceive(Context context, Intent intent) {
        // ottengo un array di oggetti DataSensor da uno stream di byte
        DataSensor buffer[];
        buffer = (DataSensor[]) intent.getSerializableExtra("message");

        for(int i=0; i<50; i++){
            Log.i("data" + iterazione + " " + i, buffer[i].getSensorType() + " " );
        }
        iterazione++;
        writeFile(buffer);
    }

    public void writeFile(DataSensor buf[]){

        try {

            String msg = null;
            FileOutputStream fw = null;

            for(int i=0; i<50; i++) {
                if(buf[i].getSensorType() == 6){     // pressure
                    fw = new FileOutputStream(main.pressure_file.getAbsoluteFile(), true);
                    msg = buf[i].getTimestamp() + "," + buf[i].getValue0()+"\n";
                }
                else{
                    msg = buf[i].getTimestamp() + "," + buf[i].getValue0() + "," + buf[i].getValue1() + "," + buf[i].getValue2()+ "\n";
                    switch (buf[i].getSensorType()){
                        case 1:
                            fw = new FileOutputStream(main.accelerometr_file.getAbsoluteFile(), true);
                            break;
                        case 2:
                            fw = new FileOutputStream(main.magnetometr_file.getAbsoluteFile(), true);
                            break;
                        case 4:
                            fw = new FileOutputStream(main.gyroscope_file.getAbsoluteFile(), true);
                            break;
                    }
                }
                fw.write(msg.getBytes());
                fw.close();
            }
        } catch (IOException e) { Log.e("Exception", e.getLocalizedMessage(), e); }

    }
}
