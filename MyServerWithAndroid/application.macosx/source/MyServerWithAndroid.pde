import processing.net.*;
import processing.serial.*;

Serial myPort;
Server myServer;
int port = 2222;
boolean myServerRunning = true;
int bgColor = 0;
int direction = 1;
int textLine = 100;
String message = "";
String showOnScreen = "";

void setup()
{
  size(400, 400);
  textFont(createFont("SanSerif", 16));
  myServer = new Server(this, port); // Starts a myServer on port 2222
  String portName = Serial.list()[5]; 
  myPort = new Serial(this, portName, 9600);
  background(0);
}

void mousePressed()
{
  myServer.stop();
  myServerRunning = false;
}

void draw()
{
  if (myServerRunning == true)
  {
    text("LED on/off Android-Arduino-Processing:\n------------------------------------", 15, 45);
    Client thisClient = myServer.available();
    if (thisClient != null) {
      if (thisClient.available() > 0) {
        
        message = thisClient.readString();
        int x = Integer.parseInt(message.trim());
        if(x == 1){
          showOnScreen = "ON";
        }
        else{
          showOnScreen = "OFF";
        }
        text("" + thisClient.ip() + " turns it " + showOnScreen, 15, textLine);
        textLine = textLine + 35;
        myPort.write(message);
      }
    }
  } 
  else 
  {
    text("Server Stopped", 15, textLine);
  }
}
