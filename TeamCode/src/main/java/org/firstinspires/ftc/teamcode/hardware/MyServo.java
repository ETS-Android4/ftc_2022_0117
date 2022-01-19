package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;

public class MyServo {

    static final double INCREMENT   = 0.01;     // amount to slew servo each CYCLE_MS cycle
    static final int    CYCLE_MS    =   50;     // period of each cycle
    static final double MAX_POS     =  1.0;     // Maximum rotational position
    static final double MIN_POS     =  0.0;     // Minimum rotational position
    double  position = (MAX_POS - MIN_POS) / 2; // Start at halfway position
    boolean rampUp = true;
    private ElapsedTime runtime;
    private double record_position;
    Servo   servo;
    private Telemetry servoTelemetry;

    public MyServo(HardwareMap hwMap, Telemetry telemetry, String name) {

        this.servoTelemetry = telemetry;
        servo = hwMap.get(Servo.class, name);
        runtime = new ElapsedTime();
    }
    public void sweepBack_and_Forth (int secondsToRun) {
        runtime.reset();
        while (runtime.seconds() < secondsToRun) {
            if (rampUp) {
                // Keep stepping up until we hit the max value.
                position += INCREMENT;
                if (position >= MAX_POS) {
                    position = MAX_POS;
                    rampUp = !rampUp;   // Switch ramp direction
                }
            }
            else {
                // Keep stepping down until we hit the min value.
                position -= INCREMENT;
                if (position <= MIN_POS) {
                    position = MIN_POS;
                    rampUp = !rampUp;  // Switch ramp direction
                }
            }


            // Display the current value
            servoTelemetry.addData("Servo Position", "%5.2f", position);
            servoTelemetry.update();
            // Set the servo to the new position and pause;
            servo.setPosition(position);

            try {
                sleep(CYCLE_MS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();  // set interrupt flag
                System.out.println("Failed to compute sum");
            }


        }
    }
    public void setPosition(double num_position) {
        record_position = num_position;
        servo.setPosition(num_position);
    }
    public  double getPosition() {
       return(servo.getPosition());
    }
    public double getRecordedValue(){
        return (record_position);
    }
}



