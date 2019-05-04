/*
 Name:		ServoDriver.h
 Created:	05/04/2019 13:02:40 PM
 Author:	karim
*/
#ifndef __SERVODRIVER_H
#define __SERVODRIVER_H

#include <Arduino.h>
#include "core_esp8266_waveform.h"

#define MIN_PULSE_WIDTH 200      // the shortest pulse sent to a servo
#define MAX_PULSE_WIDTH 3000     // the longest pulse sent to a servo
#define DEFAULT_PULSE_WIDTH 1500 // default pulse width when servo is attached
#define REFRESH_INTERVAL 20000   // minumim time to refresh servos in microseconds

// Class for the driving the servo.
class ServoDriver
{
  private:
    uint8_t _pin;

    void write_waveform(int value)
    {
        startWaveform(_pin, (uint32_t)value, REFRESH_INTERVAL - value, 0);
    }

  public:
    void attach(uint8_t pin)
    {
        _pin = pin;
        pinMode(pin, OUTPUT);
        digitalWrite(pin, LOW);
        write(DEFAULT_PULSE_WIDTH);
    }

    void write(int value)
    {
        value = constrain(value, 0, 180);
        long val = map(value, 0, 180, MIN_PULSE_WIDTH, MAX_PULSE_WIDTH);
        write_waveform(val);
    }
};

#endif
