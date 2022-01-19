/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.HardwarePushbot;
import org.firstinspires.ftc.teamcode.hardware.MyServo;

import java.text.DecimalFormat;


/**
 * This file illustrates the concept of driving a path based on encoder counts.
 * It uses the common Pushbot hardware class to define the drive on the robot.
 * The code is structured as a LinearOpMode
 *
 * The code REQUIRES that you DO have encoders on the wheels,
 *   otherwise you would use: PushbotAutoDriveByTime;
 *
 *  This code ALSO requires that the drive Motors have been configured such that a positive
 *  power command moves them forwards, and causes the encoders to count UP.
 *
 *   The desired path in this example is:
 *   - Drive forward for 48 inches
 *   - Spin right for 12 Inches
 *   - Drive Backwards for 24 inches
 *   - Stop and close the claw.
 *
 *  The code is written using a method called: encoderDrive(speed, leftInches, rightInches, timeoutS)
 *  that performs the actual movement.
 *  This methods assumes that each movement is relative to the last stopping place.
 *  There are other ways to perform encoder based moves, but this method is probably the simplest.
 *  This code uses the RUN_TO_POSITION mode to enable the Motor controllers to generate the run profile
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="MecanumMaster", group="Drive")
//@Disabled

public class MecanumMaster extends LinearOpMode {
    /* Declare OpMode members. */
    HardwarePushbot robot = new HardwarePushbot(telemetry);   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();
    MyServo testServo;
    MyServo secondServo;

    static final double COUNTS_PER_MOTOR_REV = 1200;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    double leftx = 0;
    double lefty = 0;
    double rightx = 0;
    double frontLeftValue;
    double backLeftValue;
    double frontRightValue;
    double backRightValue;
    double max;

    static final int    BLUE_LED    = 0;     // Blue LED channel on DIM
    static final int    RED_LED     = 1;     // Red LED Channel on DIM

    DeviceInterfaceModule   dim;
    DecimalFormat decFormt;
    boolean currentLED = true;

    @Override
    public void runOpMode() throws InterruptedException {
        try {
            dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");
        } catch (Exception ex){
            telemetry.addData("Dim not found.Requires Mono Robotics", "/");
            dim = null;
        }
        try {
            testServo = new MyServo(hardwareMap , telemetry, "servo");
            secondServo = new MyServo(hardwareMap , telemetry, "2servo");
        } catch (Exception ex){
            telemetry.addData("Servo not found.Requires Installation", "/");
            testServo = null;
            secondServo = null;
        }


        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Hello   hihihihihihihihihihihihih", "Resetting Encoders");    //
        telemetry.update();

        sleep(1000);

        robot.leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        decFormt = new DecimalFormat("###0.00");



        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        // _______________________________________________________________________________
        // START OF PROGRAM
        //________________________________________________________________________________while

        robot.leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        runtime.reset();
        while (opModeIsActive()) {
            lefty = gamepad1.left_stick_y;
            leftx = gamepad1.left_stick_x;
            rightx = gamepad1.right_stick_x;
            frontLeftValue = lefty - leftx - rightx;
            backLeftValue = lefty + leftx - rightx;
            frontRightValue = lefty + leftx + rightx;
            backRightValue = lefty - leftx + rightx;



            if (       (Math.abs(frontLeftValue) > 1)
                    || (Math.abs(backLeftValue) > 1)
                    || (Math.abs(frontRightValue) > 1)
                    || (Math.abs(backRightValue) > 1)   )
            {
                max =  Math.max(Math.abs(frontLeftValue), Math.abs(backLeftValue));
                max =  Math.max(max, Math.abs(frontRightValue));
                max =  Math.max(max, Math.abs(backRightValue));
                frontLeftValue = frontLeftValue/max;
                backLeftValue = backLeftValue/max;
                frontRightValue = frontRightValue/max;
                backRightValue = backRightValue/max;
            }

            robot.leftDriveFront.setPower(frontLeftValue);
            robot.leftDriveBack.setPower(backLeftValue);
            robot.rightDriveFront.setPower(frontRightValue);
            robot.rightDriveBack.setPower(backRightValue);

            if (runtime.milliseconds() > 1000) {
                currentLED = !currentLED;
                if ( dim!=null){  dim.setLED(BLUE_LED, currentLED); }
                runtime.reset();
            }

            if ( testServo!=null && secondServo != null){
                testServo.setPosition(gamepad1.right_trigger);
                secondServo.setPosition(gamepad1.left_trigger);
                telemetry.addData("joystick =", decFormt.format(testServo.getRecordedValue()));
                telemetry.addData("tpi =", decFormt.format(testServo.getPosition()));
                telemetry.addData("joystick =", decFormt.format(secondServo.getRecordedValue()));
                telemetry.addData("tpi =", decFormt.format(secondServo.getPosition()));
           }


            telemetry.addData("LY", (decFormt.format(lefty)));
            telemetry.addData("LX", decFormt.format(leftx));
            telemetry.addData("B", gamepad1.b);
            telemetry.addData("X", gamepad1.x);
            telemetry.addData("RX", decFormt.format(rightx));
            telemetry.addData("LF", decFormt.format(frontLeftValue));
            telemetry.addData("BF", decFormt.format(backLeftValue));
            telemetry.addData("FR", decFormt.format(frontRightValue));
            telemetry.addData("BR", decFormt.format(backRightValue));
            telemetry.update();

        }


        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }

}