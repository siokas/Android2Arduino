package com.example.android_arduino;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.List;

import com.example.android_arduino.Wait.Requestor;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements
		Requestor, SensorEventListener{

	Socket client;
	Button on, off, connect;
	EditText et;
	DataOutputStream dos;
	String server = "";
	Thread t;
	MediaPlayer mp;
	boolean sensorON = false;
	float x,y,z;
	float xmax=0,ymax=0,zmax=0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		on = (Button) findViewById(R.id.on);
		off = (Button) findViewById(R.id.off);
		connect = (Button) findViewById(R.id.connect);
		et = (EditText) findViewById(R.id.server);
		t = new Thread();

		on.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Wait w = new Wait(MainActivity.this, MainActivity.this, et
						.getText().toString(), "1");
				w.start();
				
			}

		});

		off.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Wait w = new Wait(MainActivity.this, MainActivity.this, et
						.getText().toString(), "0");
				w.start();
				
			}

		});

		connect.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("X = " + xmax);
				System.out.println("Y = " + ymax);
				System.out.println("Z = " + zmax);
				/*
				 * try { Wait w = new Wait(MainActivity.this, MainActivity.this,
				 * et .getText().toString(), "0"); w.start(); w.connect(); }
				 * catch (UnknownHostException e) {
				 * Toast.makeText(MainActivity.this, "Unknown Host",
				 * Toast.LENGTH_SHORT).show(); } catch (IOException e) {
				 * Toast.makeText(MainActivity.this, "Not Connected",
				 * Toast.LENGTH_SHORT).show(); }
				 * 
				 * }
				 */
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void OnResponse(Wait w, int response) {
		if (response == 55) {
			mp = MediaPlayer.create(MainActivity.this, R.raw.comic);
		} else if (response == 66) {
			mp = MediaPlayer.create(MainActivity.this, R.raw.emergency);
		}
		runOnUiThread(new Runnable() {
			public void run() {
				mp.start();
			}
		});
	}

	@Override
	protected void onResume() {

		super.onResume();

		SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		List<Sensor> sensors = sensorManager
				.getSensorList(Sensor.TYPE_ACCELEROMETER);

		Sensor sensor = sensors.get(0);
		sensorManager.registerListener(this, sensor,
				SensorManager.SENSOR_MIN);

	}

	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	public void onSensorChanged(SensorEvent event) {

		x = event.values.clone()[0];
		y = event.values.clone()[1];
		z = event.values.clone()[2];
		
		if(x>xmax)
			xmax = x;
		if(y>ymax)
			ymax = y;
		if(z>zmax)
			zmax = z;
		
		if (x >= 39 && z>=19) {
			if (!sensorON) {
				System.out.println("ON");
				Wait w = new Wait(MainActivity.this, MainActivity.this, et
						.getText().toString(), "1");
				w.start();
			}
			sensorON = true;
		} else if (x <= -9) {
			
			if (sensorON) {
				System.out.println("OFF");
				Wait w = new Wait(MainActivity.this, MainActivity.this, et
						.getText().toString(), "0");
				w.start();
			}
			sensorON = false;
		}

	}

}