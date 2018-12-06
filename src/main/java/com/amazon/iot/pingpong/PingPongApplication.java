package com.amazon.iot.pingpong;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.GpioUtil;

public class PingPongApplication {
    public void detectMotionAndGlowLED(){
        System.out.println("Starting Pi4J Motion Sensor Example");
        System.out.println("PIR Motion Sensor is ready and looking for any movement..");

        //This is required to enable Non Privileged Access to avoid applying sudo to run Pi4j programs
        GpioUtil.enableNonPrivilegedAccess();

        //Create gpio controller for PIR Motion Sensor listening on the pin GPIO_04
        final GpioController gpioPIRMotionSensor = GpioFactory.getInstance();
        final GpioPinDigitalInput pirMotionSensor = gpioPIRMotionSensor.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);

        //Create gpio controller for LED listening on the pin GPIO_01 with default PinState as LOW
        final GpioController gpioLED = GpioFactory.getInstance();
        final GpioPinDigitalOutput led = gpioLED.provisionDigitalOutputPin(RaspiPin.GPIO_01,"LED",PinState.LOW);
        led.low();

        //Create and register gpio pin listener on PIRMotion Sensor GPIO Input instance
        pirMotionSensor.addListener((GpioPinListenerDigital) event -> {
            //if the event state is High then print "Intruder Detected" and turn the LED ON by invoking the high() method
            if(event.getState().isHigh()){
                System.out.println("Intruder Detected!");
                led.high();
            }
            //if the event state is Low then print "All is quiet.." and make the LED OFF by invoking the low() method
            if(event.getState().isLow()){
                System.out.println("All is quiet...");
                led.low();
            }
        });

        try {
            // keep program running until user aborts
            for (;;) {
                //Thread.sleep(500);
            }
        }
        catch (final Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String args[]){
        PingPongApplication pingPongApplication = new PingPongApplication();
        pingPongApplication.detectMotionAndGlowLED();
    }
}
