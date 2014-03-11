/*
 *  This is an Android application which connects to a server (in a computer) and sends some values.
 *  It created in order to make a connection between an Android device and an Arduino using a server.
 *  This application creates a socket and makes the connection with the server and sends some data
 *  which are taken from the accelerometer sensor of the device.
 *  If the user tilts (or shakes) the phone to the left the application sends a '1' to the server
 *  and if the user tilts the phone to the right the application sends a '0' to the server.
 *  The server's job is to forward this message to the Arduino in order to establish the connection
 *  between the Android device and Arduino.
 *
 *  This application is Created and Designed by
 *  Apostolos Siokas
 */

package gr.siokas.android2arduino.demoapp;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;




public class MainActivity extends ActionBarActivity implements SensorEventListener {

    Socket client;
    Button on, off, connect;
    EditText et;
    DataOutputStream dos;
    boolean sensorON = false;
    float x, y, z;
    float xmax = 0, ymax = 0, zmax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        on = (Button) findViewById(R.id.on);
        off = (Button) findViewById(R.id.off);
        connect = (Button) findViewById(R.id.connect);
        et = (EditText) findViewById(R.id.server);

        // This Method is called if the "on" button is pressed
        on.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Send send = new Send(MainActivity.this, et
                        .getText().toString(), "1"); // Make the call to the server and send 1
                send.start(); // Start the Thread

            }

        });

        // This Method is called if the "off" button is pressed
        off.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Send send = new Send(MainActivity.this, et
                        .getText().toString(), "0"); // Make the call to the server and send 1
                send.start(); // Start the Thread

            }

        });

        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Code to make the connection
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    protected void onResume() {

        super.onResume();

        // This initiates the Accelerometer Sensor
        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = sensorManager
                .getSensorList(Sensor.TYPE_ACCELEROMETER);

        // This initiates the Sensor Manager
        Sensor sensor = sensors.get(0);
        sensorManager.registerListener(this, sensor,
                SensorManager.SENSOR_MIN);

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // onAccuracyChanged() Method
    }

    // This Method is called whenever the accelerometer sensor changes values
    public void onSensorChanged(SensorEvent event) {

        x = event.values.clone()[0];

        // Check if the x value is over 9 (the phone tilts to the left)
        if (x >= 9) {
            if (!sensorON) { // Check if the sensor is already on
                System.out.println("ON");
                Send send = new Send(MainActivity.this, et
                        .getText().toString(), "1"); // Make the call to the server and send 1
                send.start(); // Start the thread
            }
            sensorON = true;
        } else if (x <= -9) { // Check if the x value is over -9 (the phone tilts to the right)
            if (sensorON) { // Check if the sensor is already off
                System.out.println("OFF");
                Send send = new Send(MainActivity.this, et
                        .getText().toString(), "0"); // Make the call to the server and send 0
                send.start();
            }
            sensorON = false;
        }

    }

}