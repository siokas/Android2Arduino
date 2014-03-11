/*
 *  This is an application which is the "MeddleMan" to connect
 *  an Android device with the Arduino through the WiFi connection.
 *  It uses a Server Socket in order to let the Android device to 
 *  communicate with it forwards the incoming message to the Serial
 *  port of the computer which is connected to the Arduino.
 *
 *  Created By
 *  Apostolos Siokas
 *
*/

// Do all the nessesery imports
import processing.net.*;
import processing.serial.*;

Serial myPort; // Serial port to send the data to Arduino
int mySerialPortList = 0; // Which port is connected to the Arduino
Server myServer; // Server in order to connect with Android
int port = 2222; // Port 2222 in which the demon listens for connections
boolean myServerRunning = true; // Boolean if the server is up and running
int direction = 1;
int textLine = 100; // Find the line on the screen
String message = ""; // String of  the message
String showOnScreen = ""; // String what to show on screen
int val; // The value that the Android is going to send  

void setup()
{
  size(400, 400); // initiate the size of the window
  textFont(createFont("SanSerif", 16)); // Text font and text size
  myServer = new Server(this, port); // Starts a myServer on port 2222
  String portName = Serial.list()[mySerialPortList]; // Take the port in which the Arduino is connected
  myPort = new Serial(this, portName, 9600); // Make the connection with this port through serial cable
  background(0);
}

// This Method is called if the mouse is pressed
void mousePressed()
{
  myServer.stop(); // Stop the server
  myServerRunning = false;
}

// This is the loop Method of the Prossesing Language
void draw()
{
  if ( myPort.available() > 0) {  // If data is available,
    showOnScreen = "" + myPort.read();    
    text("" + showOnScreen, 15, textLine);    // read it and store it in val
  }
  
  // if the server is up and running
  if (myServerRunning == true)
  {
    text("LED on/off Android-Arduino-Processing:\n------------------------------------", 15, 45);
    Client thisClient = myServer.available(); // Accept all the incoming connections
    if (thisClient != null) { 
      if (thisClient.available() > 0) { // If there is a connection do the following
        message = thisClient.readString(); // Read the incoming message
        int x = Integer.parseInt(message.trim()); // Parse the incoming string into integer
        if(x == 1){ // If the message is 1
          showOnScreen = "ON"; // Show 'ON' on the screen and
          myPort.write(1); // Send '1' to the Arduino
        }
        else if (x == 0){// If the message is 0
          showOnScreen = "OFF"; // Show 'OFF' on screen and
          myPort.write(0);// Send 0 to the Arduino
        }
        text("" + thisClient.ip() + " turns it " + showOnScreen, 15, textLine); // Show the messages on the screen
        textLine = textLine + 35; // After the message change the line
        myPort.write(message);
      }
    }
  } 
  else 
  {
    text("Server Stopped", 15, textLine); // If the sever is stoped show the nessesery message on the screen
  }
}
