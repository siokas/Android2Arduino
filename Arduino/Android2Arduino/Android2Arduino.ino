/*
 * This is an Arduino application that takes some values from the Serial port
 * and do differend things depending on the values.
 * It created in order to make a connection between Android devices and Arduino.
 * The Android device sends some valuse to the 'MeddleMan' which is a server
 * running on a computer and the server whenever it accepts a message, it
 * forwards it to the Serial port and this application is waiting to read 
 * these values.
 *
 * Created by Apostolos Siokas
 *
*/

  char val; // Stores the incomming value
  int ledPin = 8; // The LED pin port number
 
  void setup() {
    pinMode(ledPin, OUTPUT); // Initiate the pinMode of the LED pin
    Serial.begin(9600); // Starts the Serial with 9600 rate
  }
 
  void loop() {
    if (Serial.available()) { // Find if there are some values on the Serial
      val = Serial.read(); // Read the and store the in a variable
     }
     if (val == '1') digitalWrite(ledPin, HIGH); // If the value is '1' send some volts to this pin
     else if(val=='0') digitalWrite(ledPin, LOW); // If th value is '0' send 0 volts to this pin     
     delay(1); // Make a small delay until the next read
   }
 }
