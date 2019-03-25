/*
 Name:		radar.ino
 Created:	3/22/2019 12:51:43 PM
 Author:	karim
*/

#include <ESP8266WiFi.h>
#include <Servo.h>
#include "ConnectionServer.h"

const char *ssid = "your_wifi_name";
const char *password = "password";

ConnectionServer conn(2019);
Servo servo;
int angle = 0;
int factor = 1;
void setup()
{
    WiFi.mode(WIFI_STA);
    WiFi.begin(ssid, password);

    //Wait until wifi is connected 
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        //Serial.print(F("."));
    }
    servo.attach(D2);
}

void loop()
{
    if (conn.process()) 
    {
        //if new client connects, reset position to initial position
        angle = 0;
        factor = 1;
        servo.write(angle);
        delay(500);
        angle = 0;
    }
    //Move to current angle position
    servo.write(angle);
    delay(5); //wait for 5ms
    int value = analogRead(A0); //then read value

    //Send to the client
    //TODO: Add a thershold  mechanism 
    conn.sendData(angle, value, factor);

    //Calculate new angle value
    angle += 2 * factor;
    if (angle < 0 || angle > 180)
    {
        factor *= -1;
        angle += 2 * factor;
    }
}
