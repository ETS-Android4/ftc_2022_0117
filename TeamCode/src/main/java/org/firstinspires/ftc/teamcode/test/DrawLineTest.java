package org.firstinspires.ftc.teamcode.test;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.image.ImageViewer;
import org.firstinspires.ftc.teamcode.hardware.vuforia.Vuforia;
import org.firstinspires.ftc.teamcode.hardware.vuforia.VuforiaImpl;

import java.util.ArrayList;
import java.util.List;

//import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;

@TeleOp(name = "DriveLineTest ", group = "Sensor")
//@Disabled
public class DrawLineTest extends LinearOpMode {
    VuforiaImpl vuforia;
    ImageViewer imageViewer;
    Bitmap image;

    int current_x;
    int current_y;
    int previous_x;
    int previous_y;



    @Override
    public void runOpMode( ) {
        vuforia = new VuforiaImpl(hardwareMap, false,true);
        imageViewer = new ImageViewer(vuforia.getRotation());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        current_x = 0;
        current_y = 0;
        previous_x = 0;
        previous_y = 0;


        while( opModeIsActive() ) {

            // 1) get the image
            image = vuforia.getImage();
            // 2) get joystick values, update current_x, current y
            // reference:  gamepad1.left_stick_y gamepad1.leftstick_x
            // whenever the |joystick_x| > 0, the update current_x++;
            // whenever the |joystick_y| > 0, the update current_x++;
            if (gamepad1.left_stick_x < 0){
                current_x++;
                if (current_x > image.getWidth()){
                    current_x = image.getWidth() - 1;
                }
            }
            if (gamepad1.left_stick_x > 0){

                current_x--;
                if(current_x < 0){
                    current_x = 0;
                }
            }
            if (gamepad1.left_stick_y < 0){
                current_y++;
                if (current_y > image.getHeight()){
                    current_y = image.getHeight() - 1;
                }
            }
            if (gamepad1.left_stick_y > 0){

                current_y--;
                if(current_y < 0){
                    current_y = 0;
                }
            }


            // 3) store the values in a list if not the same previous_x, previous_y
            List x_pathList = new ArrayList();
            List y_pathList = new ArrayList();
            // https://stackoverflow.com/questions/42610637/how-to-build-an-array-of-arraylistinteger-in-java
            // https://docs.oracle.com/javase/7/docs/api/java/util/ArrayList.html
            if(current_x != previous_x && current_y != previous_y){
                x_pathList.add(current_x);
                y_pathList.add(current_y);
            }

            // 4) update previous_x and previous_y because the x, y has been saved
            // to the list
            previous_x = current_x;
            previous_y = current_y;

            drawCross(30);
            // 5) draw the joystick values with call to drawPixelfromList
            // and update the image
            imageViewer.setImage(image);
            telemetry.addData(" x = ",current_x );
            telemetry.addData(" y = ",current_y );
            telemetry.update();
        }

    }

    public void drawCross(int length) {
        for(int j = -length;j <= length; j++){
            setPixel(current_x,current_y+j, 255, 255, 255);
            setPixel(current_x+j,current_y, 255, 255, 255);
        }
        // This funcction cycles thru the list and calls bitmap.setPixel
        // for each item in the list
    }
    public void setPixel(int input_x, int input_y, int red_value, int green_value, int blue_value){
        if(input_x < 0){
            input_x = 0;
        }
        if(input_x> image.getWidth()){
            input_x = image.getWidth();
        }
        if(input_y < 0){
            input_y = 0;
        }
        if(input_y> image.getHeight()){
            input_y = image.getHeight();
        }
        image.setPixel( input_x, input_y, Color.rgb(red_value, green_value, blue_value));   // yellow color
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
