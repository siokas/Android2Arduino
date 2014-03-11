/*
 *  Created by Apostolos Siokas
 */
package gr.siokas.android2arduino.demoapp;
import android.app.Activity;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

// Send class implements Runnable in order to make the socket run in another thread than the main thread
public class Send implements Runnable  {

    String server = ""; // The ip of the server provided form the user
    String onoff = ""; // The message to send
    Socket client = null; // The client socket in order to connect to the server
    Activity x = null; // The main activity

    // Constructor (in the constructor we initialize the values)
    public Send(final Activity x, String server, String onoff) {
        this.x = x;
        this.server = server;
        this.onoff = onoff;
    }

    // A method that starts the thread (the thread when it starts it calls the void run() method)
    public void start() {
        Thread t = new Thread(this);
        t.start();
    }

    // A Method to close the connection between the client and the server
    public void closeSoc() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // A method to make the connection between the client and the server
    public void connect() throws Exception {
        client = new Socket(server, 2222); // This client connects to a server which demons in port 2222
        display("Connected");
    }


    // The void run() method which is called when the thread starts
    @Override
    public void run() {
        try {
            client = new Socket(server, 2222);
            DataOutputStream dos = new DataOutputStream(
                    client.getOutputStream()); // Create an output stream in order to send it to the server
            dos.writeUTF(onoff); // Attach to the output stream whatever we want to send
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // A method to display content from this thread to the main thread
    private void display(final String string) {
        x.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(x, string, Toast.LENGTH_LONG).show();
            }
        });
    }
}
