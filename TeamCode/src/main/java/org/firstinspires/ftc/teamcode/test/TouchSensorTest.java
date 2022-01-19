package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.DigitalOutLed;
import org.firstinspires.ftc.teamcode.hardware.TouchSensor;

@TeleOp(name="TouchSensorTest", group="Drive")
public class TouchSensorTest extends LinearOpMode {
    TouchSensor myTouchSensor;
    DigitalOutLed myLed;

    @Override
    public void runOpMode() throws InterruptedException {
        myTouchSensor = new TouchSensor(hardwareMap , telemetry, "touch");
        myLed = new DigitalOutLed(hardwareMap , telemetry, "led");
        waitForStart();
        while (opModeIsActive()) {

            if(myTouchSensor.getSwitchValue() == true) {
                myLed.setDigitalOut(true);
            }

            else{
                myLed.setDigitalOut(false);
            }


            telemetry.addData("State of button", myTouchSensor.getSwitchValue());
            telemetry.update();


        }
    }
}
