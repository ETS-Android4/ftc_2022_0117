package org.firstinspires.ftc.teamcode.test;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractAuton;
//import org.firstinspires.ftc.teamcode.framework.userhardware.DoubleTelemetry;
import org.firstinspires.ftc.teamcode.hardware.image.ImageViewer;
import org.firstinspires.ftc.teamcode.hardware.vuforia.VuforiaImpl;

import java.util.ArrayList;
import java.util.Collections;

//@TeleOp(name = "VisionTest", group = "Test")
//@Disabled
@TeleOp(name = "VisionTest Draw Lines", group = "Sensor")

public class VisionTest extends LinearOpMode {

    VuforiaImpl vuforia;
    ImageViewer imageViewer;
    int currentRow = -1;

    @Override
    public void runOpMode( ) {
        vuforia = new VuforiaImpl(hardwareMap, false, true);
        imageViewer = new ImageViewer(vuforia.getRotation());

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        while ( opModeIsActive())  {

           Bitmap bitmap = vuforia.getImage();

           ArrayList<Integer> rows = new ArrayList<>();

           for(int r = 0; r < bitmap.getHeight(); r+=16) {
              int y = 0;
              for(int c = 0; c < bitmap.getWidth(); c+=16) {
                int pixel = bitmap.getPixel(c, r);
                y += (Color.red(pixel) + Color.green(pixel) / 2) - Color.blue(pixel);
              }
              rows.add(y);
           }

           //telemetry.addDataDB(DoubleTelemetry.LogMode.INFO, rows);

           rows = filterArray(rows, 5);

           //telemetry.addDataDB(DoubleTelemetry.LogMode.INFO, rows);

           if(currentRow == -1) currentRow = bitmap.getHeight() / 2;

           ArrayList<Integer> pixels = new ArrayList<>();

           for(int p = 0; p < bitmap.getWidth(); p++) {
               int pixel = bitmap.getPixel(p, currentRow);
               bitmap.setPixel(p, currentRow, Color.rgb(0, 0, 0));
               pixels.add(Color.red(pixel) + Color.green(pixel) + Color.blue(pixel));
           }

           positionFromPixels(pixels);

           for(int p = 0; p < bitmap.getHeight(); p++) {
               // white
               bitmap.setPixel((bitmap.getWidth() / 5) * 1, p, Color.rgb(254, 254, 254));
               // green
               bitmap.setPixel((bitmap.getWidth() / 5) * 2, p, Color.rgb(16 , 242, 91));
               // red
               bitmap.setPixel((bitmap.getWidth() / 5) * 3, p, Color.rgb(255, 0, 0));
               // blue
               bitmap.setPixel((bitmap.getWidth() / 5) * 4, p, Color.rgb(0, 0, 255));
           }

           /*int above = 0, below = 0;

           for(int r = 0; r < rows.size(); r++) {
               if(rows.get(r) == -1) continue;
               for(int c = 0; c < bitmap.getWidth(); c++) {
                   bitmap.setPixel(c, r * 8, Color.rgb(254, 254, 254));
               }
               if(r * 8 < currentRow) {
                   above++;
               } else {
                   below++;
               }
           }*/

           //currentRow += (below - above) * 5;

           imageViewer.setImage(bitmap);

        } // end of runOpMode

    }

    // draw circle example
    // https://stackoverflow.com/questions/9007977/draw-circle-using-pixels-applied-in-an-image-with-for-loop


    public void positionFromPixels(ArrayList<Integer> pixels) {
        int numPixels = pixels.size();

        //telemetry.addData( "Unfiltered: " , pixels);

        pixels = filterArray(pixels, 5);

        //telemetry.addData( "Filtered: " , pixels);

        int left = 0, center = 0, right = 0;
        for(int p = 0; p < numPixels; p++) {
            if(pixels.get(p) == -1) pixels.set(p, 255);
            pixels.set(p, 255 - pixels.get(p));
            if(pixels.get(p) == 0) continue;
            if(p > (numPixels / 4.0) && p < (numPixels / 5.0) * 2) {
                left++;
            } else if(p > (numPixels / 5.0) * 2 && p < (numPixels / 5) * 3) {
                center++;
            } else if(p > (numPixels / 5) * 3 && p < (numPixels / 4) * 3){
                right++;
            }
        }

        telemetry.addData( "Left: " + left + " Center: " + center + " Right: " + right, " ");

        if(left > center && left > right) {
            telemetry.addData( "Left", " ");
        } else if(center > right) {
            telemetry.addData( "Center", "");
        } else {
            telemetry.addData( "Right","");
        }
        telemetry.update();
    }

    public ArrayList<Integer> filterArray(ArrayList<Integer> pixels, int level) {
        int numPixels = pixels.size();

        ArrayList<Integer> sortedPixels = (ArrayList<Integer>) pixels.clone();

        Collections.sort(sortedPixels);

        for(int p = 0; p < numPixels; p++) {
            int total = 0;
            total += pixels.get(p);
            if(p != 0) total += pixels.get(p - 1);
            if(p != pixels.size() - 1) total += pixels.get(p + 1);
            pixels.set(p, total / 3);
        }

        int cutoff = sortedPixels.get(numPixels / level);

        for(int p = 0; p < numPixels; p++) {
            if(pixels.get(p) > cutoff) {
                pixels.set(p, -1);
            }
        }

        return pixels;
    }
}
