package org.firstinspires.ftc.teamcode.test;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.vuforia.Vuforia;

//import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;

@TeleOp(name = "VuforiaTest Pixel ", group = "Sensor")

public class VuforiaTest extends LinearOpMode {
    Vuforia vuforia;
    Bitmap image;


    @Override
    public void runOpMode( ) {
        vuforia = new Vuforia(hardwareMap, true);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while( opModeIsActive() ) {
            getPixel();
            telemetry.update();
        }

    }


    public void getPixel() {
        image = vuforia.getImage();
        if (image != null) {
           // int[][][] pixels = new int[image.getWidth()][image.getHeight()][4];
            /*for (int SMOOTHING = 0; SMOOTHING < image.getWidth(); SMOOTHING++) {
                for (int h = 0; h < image.getHeight(); h++) {
                    pixels[SMOOTHING][h][0] = Color.red(image.getPixel(SMOOTHING, h));
                    pixels[SMOOTHING][h][1] = Color.green(image.getPixel(SMOOTHING, h));
                    pixels[SMOOTHING][h][2] = Color.blue(image.getPixel(SMOOTHING, h));
                    pixels[SMOOTHING][h][3] = Color.alpha(image.getPixel(SMOOTHING, h));
                }
            }*/

           telemetry.addData("R = ", Integer.toHexString(Color.red(image.getPixel(10, 10)) ));
           telemetry.addData("G = ", Integer.toHexString(Color.green(image.getPixel(10, 10)) ));
           telemetry.addData("B = ", Integer.toHexString(Color.blue(image.getPixel(10, 10)) ));
           telemetry.addData("INT = ", Integer.toHexString(image.getPixel(10, 10) ));
           telemetry.addData("Widh   = ", image.getWidth() );
           telemetry.addData("Height = ", image.getHeight() );
        } else {
           telemetry.addData("Image is null","");
        }
    }

}
