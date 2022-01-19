package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.hardware.MyServo;

@TeleOp(name="ServoTest", group="Drive")
//@Disabled
public  class TeleopServo extends LinearOpMode {

    MyServo testServo;
    MyServo secondServo;


    @Override
    public void runOpMode() throws InterruptedException {

        testServo = new MyServo(hardwareMap , telemetry, "servo");
        secondServo = new MyServo(hardwareMap , telemetry, "2servo");
        waitForStart();
        while (opModeIsActive()) {
            if (gamepad1.b) {
                testServo.sweepBack_and_Forth(10);
            }
        }
    }





}