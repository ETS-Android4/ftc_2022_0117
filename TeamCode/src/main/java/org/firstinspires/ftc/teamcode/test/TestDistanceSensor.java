package org.firstinspires.ftc.teamcode.test;

import org.firstinspires.ftc.teamcode.hardware.MyDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="SensorTest", group="Drive")
public class TestDistanceSensor extends LinearOpMode {

    MyDistanceSensor odsSensor;

    @Override
    public void runOpMode() throws InterruptedException {
        odsSensor = new MyDistanceSensor(hardwareMap , telemetry, "testsensor");
        waitForStart();
        while (opModeIsActive()) {
                odsSensor.getLightValue();
                odsSensor.getRawLightValue();

                telemetry.addData("Light Value", odsSensor.getLightValue());
                telemetry.addData("Light Value", odsSensor.getRawLightValue());
                telemetry.update();
        }
    }
}
