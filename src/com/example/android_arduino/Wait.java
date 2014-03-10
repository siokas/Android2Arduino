package com.example.android_arduino;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.widget.Toast;

public class Wait implements Runnable {

	String server = "";
	String onoff = "";
	Requestor _req = null;
	Socket client = null;
	Activity x = null;

	public interface Requestor {
		void OnResponse(Wait w, int response);
	}

	public Wait(final Activity x, Requestor req, String server, String onoff) {
		_req = req;
		this.x = x;
		this.server = server;
		this.onoff = onoff;
	}

	public void start() {
		Thread t = new Thread(this);
		t.start();
	}

	public void closeSoc() {
		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect() throws UnknownHostException, IOException {
		client = new Socket(server, 2222);
		display("Connected");
	}

	public void onoff() {
		
		try {
			client = new Socket(server, 2222);
			DataOutputStream dos = new DataOutputStream(
					client.getOutputStream());
			dos.writeUTF(onoff);
		} catch (UnknownHostException e) {
			display("Unknown Host");
			e.printStackTrace();
		} catch (IOException e) {
			display("IO Exception");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		DataInputStream dis;
		try {
			client = new Socket(server, 2222);
			DataOutputStream dos = new DataOutputStream(
					client.getOutputStream());
			dos.writeUTF(onoff);
			dis = new DataInputStream(client.getInputStream());
			int mess = Integer.parseInt((dis.readUTF().toString()));
			_req.OnResponse(this, mess);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void display(final String string) {
		x.runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(x, string, Toast.LENGTH_LONG).show();
			}
		});
	}
}
