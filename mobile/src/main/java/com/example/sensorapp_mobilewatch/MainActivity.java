package com.example.sensorapp_mobilewatch;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.shared_mobile_watch.DataSensor;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    ArrayList<Sensor> sensorList;

    int iterazione = 0;
    public File accelerometr_file;
    public File magnetometr_file;
    public File pressure_file;
    public File gyroscope_file;
    public File accelerometr_file_phone;
    public File magnetometr_file_phone;
    public File pressure_file_phone;
    public File gyroscope_file_phone;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // registro il listener che riceve i messaggi inviati dal service MessageService
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        Receiver messageReceiver = new Receiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

        // registro i sensrori che mi interessano
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorList = new ArrayList<Sensor>();
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD));
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE));
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorList.add(sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE));

        // creating a file for each sensor
        // chiedere permesso di accedere alla memoria
        try {

            // FILE OROLOGIO
            accelerometr_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "accelerometr_watch.csv");
            // if file doesnt exists, then create it
            if (!accelerometr_file.exists())
                accelerometr_file.createNewFile();

            magnetometr_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "magnetometr_watch.csv");
            if (!magnetometr_file.exists())
                magnetometr_file.createNewFile();

            pressure_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "  pressure_watch.csv");
            if (!pressure_file.exists())
                pressure_file.createNewFile();

            gyroscope_file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "gyroscope_watch.csv");
            if (!gyroscope_file.exists())
                gyroscope_file.createNewFile();

            // FILE TELEFONO
            accelerometr_file_phone = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "accelerometr_phone.csv");
            // if file doesnt exists, then create it
            if (!accelerometr_file_phone .exists())
                accelerometr_file_phone .createNewFile();

            magnetometr_file_phone = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "magnetometr_phone.csv");
            if (!magnetometr_file_phone .exists())
                magnetometr_file_phone .createNewFile();

            pressure_file_phone = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "  pressure_phone.csv");
            if (!pressure_file_phone .exists())
                pressure_file_phone .createNewFile();

            gyroscope_file_phone  = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "gyroscope_phone.csv");
            if (!gyroscope_file_phone .exists())
                gyroscope_file_phone .createNewFile();


            // apro anche i file outputstream per ogni file???????

            for (Sensor s: sensorList) {
                sensorManager.registerListener(this, s, SensorManager.SENSOR_DELAY_FASTEST);
            }

        }catch (IOException e){e.printStackTrace();}
    }


    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(this);
    }

    // Callback che fa ripartire SensorManager
    protected void onResume(){
        super.onResume();
        //sensorManager.registerListener(this, barometro, SensorManager.SENSOR_DELAY_FASTEST);
    }


    // Stoppo sennò continuo a registrare
    protected void onPause(){
        super.onPause();
    }

    public void onSensorChanged(SensorEvent event) {

        String msg = null;
        FileOutputStream fw = null;
        try{
            if(event.sensor.getType() == 6){     // pressure
                fw = new FileOutputStream(pressure_file_phone.getAbsoluteFile(), true);
                msg = event.timestamp + "," + event.values[0]+"\n";
            }
            else{
                msg = event.timestamp + "," + event.values[0] + "," + event.values[1] + "," + event.values[2]+ "\n";
                switch (event.sensor.getType()) {
                    case 1:
                        fw = new FileOutputStream(accelerometr_file_phone.getAbsoluteFile(), true);
                        break;
                    case 2:
                        fw = new FileOutputStream(magnetometr_file_phone.getAbsoluteFile(), true);
                        break;
                    case 4:
                        fw = new FileOutputStream(gyroscope_file_phone.getAbsoluteFile(), true);
                        break;
                }
            }
            fw.write(msg.getBytes());
            fw.close();
        }catch (IOException e){e.printStackTrace();}
    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {}


}
