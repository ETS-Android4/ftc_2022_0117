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

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.HardwareMap;


import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import static java.lang.Thread.sleep;


/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */



public class HardwarePushbot {
    /* Public OpMode members. */
    public DcMotor leftDriveFront = null;
    public DcMotor rightDriveFront = null;
    public DcMotor rightDriveBack = null;
    public DcMotor leftDriveBack = null;
    public double targetcountLF;
    public double targetcountRF;
    public double targetcountLB;
    public double targetcountRB;
    public double strafe_x_count = 0;

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    private Telemetry telemetry1;

    /* Constructor */
    public HardwarePushbot(Telemetry telemetry) {

        this.telemetry1 = telemetry;

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftDriveFront = hwMap.get(DcMotor.class, "frontleft");
        leftDriveBack = hwMap.get(DcMotor.class, "backleft");
        rightDriveFront = hwMap.get(DcMotor.class, "frontright");
        rightDriveBack = hwMap.get(DcMotor.class, "backright");


        leftDriveFront.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        leftDriveBack.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark
        rightDriveFront.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightDriveBack.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors

        leftDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Set to REVERSE if using AndyMark motors
        rightDriveFront.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Set to REVERSE if using AndyMark motors
        rightDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Set to REVERSE if using AndyMark motors
        leftDriveBack.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE); // Set to REVERSE if using AndyMark motors


        leftDriveBack.setPower(0);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        rightDriveFront.setPower(0);


        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftDriveFront.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);


        leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        targetcountLF = 0;
        targetcountLB = 0;
        targetcountRF = 0;
        targetcountRB = 0;


    }

    public void moveForward(double distance, double speed) throws InterruptedException {

        double targetCount;
        leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightDriveBack.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        telemetry1.addData("lf = ", leftDriveFront.getCurrentPosition());
        telemetry1.addData("lb = ", leftDriveBack.getCurrentPosition());
        telemetry1.addData("rf = ", rightDriveFront.getCurrentPosition());
        telemetry1.addData("rb = ", rightDriveBack.getCurrentPosition());
        telemetry1.update();
        sleep(1000);

        targetCount = distance * 100;

        targetcountLF = targetcountLF + targetCount;
        targetcountLB = targetcountLB + targetCount;
        targetcountRF = targetcountRF + targetCount;
        targetcountRB = targetcountRB + targetCount;

        leftDriveFront.setPower(speed);
        leftDriveBack.setPower(speed);
        rightDriveFront.setPower(speed);
        rightDriveBack.setPower(speed);

        while ((leftDriveFront.getCurrentPosition() < targetCount) || (leftDriveBack.getCurrentPosition() < targetCount)
                || (rightDriveFront.getCurrentPosition() < targetCount) || (rightDriveBack.getCurrentPosition() < targetCount)) {
            telemetry1.addData("lf = ", leftDriveFront.getCurrentPosition());
            telemetry1.addData("lb = ", leftDriveBack.getCurrentPosition());
            telemetry1.addData("rf = ", rightDriveFront.getCurrentPosition());
            telemetry1.addData("rb = ", rightDriveBack.getCurrentPosition());
            telemetry1.addData("target", targetcountLB);
            telemetry1.addData("target", targetCount);
            telemetry1.update();

        }

        leftDriveBack.setPower(0);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        rightDriveFront.setPower(0);


    }

    public void moveBackwards(double distance, double speed) {
        double targetCount;
        targetCount = distance * 100;
        targetcountLF = targetcountLF - targetCount;
        targetcountLB = targetcountLB - targetCount;
        targetcountRF = targetcountRF - targetCount;
        targetcountRB = targetcountRB - targetCount;
        speed = -1 * speed;
        leftDriveFront.setPower(speed);
        leftDriveBack.setPower(speed);
        rightDriveFront.setPower(speed);
        rightDriveBack.setPower(speed);

        while ((leftDriveFront.getCurrentPosition() > targetcountLF) || (leftDriveBack.getCurrentPosition() > targetcountLB)
                || (rightDriveFront.getCurrentPosition() > targetcountRF) || (rightDriveBack.getCurrentPosition() > targetcountRB)) {
            telemetry1.addData("lf = ", leftDriveFront.getCurrentPosition());
            telemetry1.addData("lb = ", leftDriveBack.getCurrentPosition());
            telemetry1.addData("rf = ", rightDriveFront.getCurrentPosition());
            telemetry1.addData("rb = ", rightDriveBack.getCurrentPosition());
            telemetry1.addData("target", targetcountLB);
            telemetry1.update();

        }

        leftDriveBack.setPower(0);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        rightDriveFront.setPower(0);


    }
    public void moveRight(double distance, double speed) {
        double targetCount;
        targetCount = distance * 200;

        strafe_x_count = strafe_x_count + targetCount;

        leftDriveFront.setPower(speed * 0.85);
        leftDriveBack.setPower(-1 * speed);
        rightDriveFront.setPower(-1 * speed);
        rightDriveBack.setPower(speed * 0.85);

        while ((rightDriveBack.getCurrentPosition() < strafe_x_count)) {
            telemetry1.addData("lf = ", leftDriveFront.getCurrentPosition());
            telemetry1.addData("lb = ", leftDriveBack.getCurrentPosition());
            telemetry1.addData("rf = ", rightDriveFront.getCurrentPosition());
            telemetry1.addData("rb = ", rightDriveBack.getCurrentPosition());
            telemetry1.addData("target", strafe_x_count);
            telemetry1.update();

        }

        leftDriveBack.setPower(0);
        leftDriveFront.setPower(0);
        rightDriveBack.setPower(0);
        rightDriveFront.setPower(0);




    }


// end of class
}
// end of func





