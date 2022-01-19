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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.hardware.HardwarePushbot;

/**
 * This file provides basic Telop driving for a Pushbot robot.
 * The code is structured as an Iterative OpMode
 *
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 *
 * This particular OpMode executes a basic Tank Drive Teleop for a PushBot
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="Teleop Tank", group="Drive")

public class TeleopTest extends OpMode{

    /* Declare OpMode members. */
    HardwarePushbot robot;   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

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

    DeviceInterfaceModule dim;

    @Override
    public void init() {
        robot = new HardwarePushbot(telemetry);
        try {
            dim = hardwareMap.get(DeviceInterfaceModule.class, "dim");
        } catch (Exception ex){
            telemetry.addData("Dim not found.Requires Mono Robotics", "/");
            dim = null;
        }
        robot.init(hardwareMap);

        telemetry.addData("Hello   hihihihihihihihihihihihih", "Resetting Encoders");    //
        telemetry.update();

        robot.leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);


        robot.rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
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

        if (dim != null){
            dim.setLED(BLUE_LED, gamepad1.x);
            dim.setLED(RED_LED,  gamepad1.b);
        }




        telemetry.addData("L Y =", lefty);
        telemetry.addData("L X =", leftx);
        telemetry.addData("R X =", rightx);
        telemetry.addData("LF =", frontLeftValue);
        telemetry.addData("BF =", backLeftValue);
        telemetry.addData("FR =", frontRightValue);
        telemetry.addData("BR =", backRightValue);
        telemetry.update();


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
