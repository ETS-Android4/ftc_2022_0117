package org.firstinspires.ftc.teamcode.hardware.image;

import android.graphics.Bitmap;

//import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.hardware.vuforia.VuforiaImpl;

public class ImageProcessor {

    private VuforiaImpl vuforia;
    private ImageViewer viewer;

    public ImageProcessor(HardwareMap hw) {
        this(hw,false);
    }

    public ImageProcessor(HardwareMap hw, boolean led) {
        vuforia = new VuforiaImpl(hw, "BACK",false, false);
        //vuforia = new VuforiaImpl("WebCam",false, false);
        viewer = new ImageViewer(vuforia.getRotation());
    }

    public Bitmap getImage() {
        return vuforia.getImage();
    }

    public void setImage(Bitmap image) {
        viewer.setImage(image);
    }

    public void shutdown() {
        viewer.clearImage();
    }

    public static void drawHorizontalLine(Bitmap bitmap, int position, int start, int end, int width, int color) {
        for(int p = start; p < end; p++) {
            for(int w = 0; w < width; w++) {
                //if(AbstractOpMode.isRunActive()) return;
                bitmap.setPixel(p, position + w, color);
            }
        }



    }


    public static void drawHorizontalLineRun(Bitmap bitmap, int position, int start, int end, int width, int color) {
        for(int p = start; p < end; p++) {
            for(int w = 0; w < width; w++) {

                bitmap.setPixel(p, position + w, color);
            }
        }



    }





    public static void drawHorizontalLine(Bitmap bitmap, int position, int width, int color) {
        drawHorizontalLine(bitmap, position, 0, bitmap.getWidth(), width, color);
    }

    public static void drawHorizontalLine(Bitmap bitmap, int position, int color) {
        drawHorizontalLine(bitmap, position, 1, color);
    }

    public static void drawVerticalLine(Bitmap bitmap, int position, int start, int end, int width, int color) {
        for(int p = start; p < end; p++) {
            for(int w = 0; w < width; w++) {
               // if(AbstractOpMode.isRunActive()) return;
                bitmap.setPixel(position + w, p, color);
            }
        }
    }


    public static void drawVerticalLineRun(Bitmap bitmap, int position, int start, int end, int width, int color) {
        for(int p = start; p < end; p++) {
            for(int w = 0; w < width; w++) {

                bitmap.setPixel(position + w, p, color);
            }
        }
    }



    public static void drawVerticalLine(Bitmap bitmap, int position, int width, int color) {
        drawVerticalLine(bitmap, position, 0, bitmap.getHeight(), width, color);
    }

    public static void drawVerticalLine(Bitmap bitmap, int position, int color) {
        drawVerticalLine(bitmap, position, 1, color);
    }

    public static void drawBox(Bitmap bitmap, int x, int y, int b, int h, int width, int color) {
        drawHorizontalLine(bitmap, y, x, x + b, width, color);
        drawHorizontalLine(bitmap, y + h, x, x + b + width, width, color);
        drawVerticalLine(bitmap, x, y, y + h, width, color);
        drawVerticalLine(bitmap, x + b, y, y + h + width, width, color);
    }

    public static void drawBoxRun(Bitmap bitmap, int x, int y, int b, int h, int width, int color) {
        drawHorizontalLineRun(bitmap, y, x, x + b, width, color);
        drawHorizontalLineRun(bitmap, y + h, x, x + b + width, width, color);
        drawVerticalLineRun(bitmap, x, y, y + h, width, color);
        drawVerticalLineRun(bitmap, x + b, y, y + h + width, width, color);
    }


}
