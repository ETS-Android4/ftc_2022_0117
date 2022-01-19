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

package org.firstinspires.ftc.teamcode.auto;

import android.widget.Button;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.ThreadPool;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.hardware.HardwarePushbot;

import java.util.Locale;


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

@Autonomous(name="90DegreeGyro", group="Auto")
//@Disabled
public class Rev90DegreeGyro extends LinearOpMode {
  /* Declare OpMode members. */
    HardwarePushbot robot;   // Use a Pushbot's hardware
    ModernRoboticsI2cGyro   gyro    = null;
    private ElapsedTime     runtime = new ElapsedTime();

    static final int     COUNTS_PER_MOTOR_REV    = 1120 ;    // eg: TETRIX Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                      (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.9;
    double     turn_speed              = 0.5;

    int newTarget;
    int MotorStop;
    int newRightTarget;
    double tolerenceA;
    double tolerenceB;
    double buttonDriveSpeed = 0.5;
    double CurrentAngle;
    boolean dpadUp, dpaddown;
    double speed;
    double error;
    String stringAngle;
    boolean localrightbumper;

    // The IMU sensor object
    BNO055IMU imu;
    Orientation angles;
    Acceleration gravity;
    BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

    public void initialize() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot  = new HardwarePushbot(telemetry);
        robot.init(hardwareMap);

        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        dpadUp = false;
        localrightbumper = false;

        // Fix for all Four motors
        robot.leftDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.leftDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightDriveFront.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightDriveFront.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    //Make a button to reset gyro
    @Override
    public void runOpMode() throws InterruptedException {

        initialize();

        telemetry.addData("<" , "A = 90 deg, B = 45 deg, X = -90 deg, Y = -45 deg");
        telemetry.update();
        // Wait for the game to start (driver presses PLAY)

        boolean stayInLoop = false;
        waitForStart();


        while (opModeIsActive()) {
            if (gamepad1.a == true) {
                gyroTurn(turn_speed, 90, "Button A", stayInLoop);
            }
            if (gamepad1.b == true) {
                gyroTurn(turn_speed, 45, "Button B", stayInLoop);
            }
            if (gamepad1.x == true) {
                gyroTurn(turn_speed, -90, "Button X", stayInLoop);
            }
            if (gamepad1.y == true) {
                gyroTurn(turn_speed, -45, "Button Y", stayInLoop);
            }

            if ((gamepad1.right_bumper == true) && (localrightbumper == false))  {
                localrightbumper = true;
                // stayInLoop = !stayInLoop;

                if (stayInLoop == false) {
                    stayInLoop = true;
                }
                else{
                    stayInLoop = false;
                }
            }
            if (gamepad1.left_bumper == true) {
                imu.initialize(parameters);
            }
            localrightbumper = gamepad1.right_bumper;


            if ((gamepad1.dpad_up == true) && (dpadUp == false))  {
                dpadUp = true;
                turn_speed = turn_speed + 0.1;
                if (turn_speed >= 1) {
                    turn_speed = 1;
                }
            }
            dpadUp = gamepad1.dpad_up;

            if ((gamepad1.dpad_down == true) && (dpaddown == false)) {
                dpaddown = true;
                turn_speed = turn_speed - 0.1;
                if (turn_speed <= 0.2) {
                    turn_speed = 0.2;
                }
            }
            dpaddown = gamepad1.dpad_down;


            telemetry.addData("Turn Speed = %d", turn_speed);
            telemetry.addData(">", "Robot Heading = %.2f", getAngle());
            telemetry.addData(">", "Robot Roll = %.2f", getRoll());
            telemetry.addData(">", "Robot Pitch = %.2f", getPitch());
            telemetry.addData("stay in loop:", stayInLoop);

            telemetry.update();
        }// While Opmode

        // Stop all motion;
        robot.leftDriveFront.setPower(0);
        robot.rightDriveFront.setPower(0);
        robot.leftDriveBack.setPower(0);
        robot.rightDriveBack.setPower(0);

    } // Run Opmode



    public void gyroTurn (  double InitialSpeed, double TargetAngle, String Button_Press, boolean stayInLoop) {
        //Monitor whether the robot has reached target angle
        //a) Check if robot has reached the target angle,
        //   if target angle, exit while loop
        CurrentAngle = getAngle();
        tolerenceA = TargetAngle-2;
        tolerenceB = TargetAngle+2;
        while (opModeIsActive() && CurrentAngle <= tolerenceA || CurrentAngle >= tolerenceB || stayInLoop == true) {
            //b) Calculate the error
            error = TargetAngle - CurrentAngle;
            //c) Covert the error into a speed
            speed = error / TargetAngle * InitialSpeed;
            //d) Apply Speed to the motors
            //   If the Initial Speed is < 1 then speed = speed * -1
            if (TargetAngle < 0) {
                speed = speed * -1;
            }
            robot.leftDriveFront.setPower(speed);
            robot.rightDriveFront.setPower(-speed);//
            robot.leftDriveBack.setPower(speed);
            robot.rightDriveBack.setPower(-speed);
            //e) Loop to check the Target angle
            CurrentAngle = getAngle();
            //f) Show telemetry to show Current Angle and speed
            telemetry.addData("Robot Heading = %d", angles.firstAngle);
            telemetry.addData("Robot Speed = %d", speed);
            telemetry.addData("Button pressed: ", Button_Press);
            telemetry.addData("stay in loop:", stayInLoop);
            telemetry.update();
            if (userStopRequested()){
                break;
            }
        }
        //Robot is finished stop all the motors
        robot.leftDriveFront.setPower(0);
        robot.rightDriveFront.setPower(0);//
        robot.leftDriveBack.setPower(0);
        robot.rightDriveBack.setPower(0);

    }// GyroTurn

    public boolean userStopRequested() {
        return (gamepad1.right_bumper);
    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }

    public double getAngle() {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return(angles.firstAngle);
    }
    public double getRoll() {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return(angles.secondAngle);
    }
    public double getPitch() {
        angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return(angles.thirdAngle);
    }

}// Linear Opmode