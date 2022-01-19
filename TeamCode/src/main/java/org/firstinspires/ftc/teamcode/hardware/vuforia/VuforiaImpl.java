package org.firstinspires.ftc.teamcode.hardware.vuforia;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class VuforiaImpl {

    public Vuforia vuforia;

    public VuforiaImpl(HardwareMap hw,boolean viewer, boolean led) {
        vuforia = new Vuforia(hw, viewer, led);
    }

    public VuforiaImpl(HardwareMap hw, String camera, boolean viewer, boolean led) {
        vuforia = new Vuforia(hw, camera, viewer);
    }

    public VuforiaImpl(HardwareMap hw, boolean viewer) {
        this(hw, viewer, false);
    }

    public VuforiaImpl(HardwareMap hw, String camera, boolean viewer) {
        this(hw, camera, viewer, true);
    }

    public VuforiaLocalizer getVuforia() {
        return vuforia.getVuforia();
    }

    public void setLED(boolean on) {
        vuforia.setLED(on);
    }

    public Bitmap getImage() {
        return vuforia.getImage();
    }

    public int getRotation() {
        return vuforia.getRotation();
    }
}
