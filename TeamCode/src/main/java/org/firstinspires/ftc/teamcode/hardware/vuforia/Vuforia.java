package org.firstinspires.ftc.teamcode.hardware.vuforia;

import android.app.Activity;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.view.Surface;

import com.vuforia.CameraDevice;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import org.firstinspires.ftc.teamcode.framework.abstractopmodes.AbstractOpMode;

public class Vuforia {
    private VuforiaLocalizer vuforia;
    private VuforiaTrackable template;

    protected Vuforia( HardwareMap hw, boolean viewer, boolean led) {
        this( hw,"BACK", viewer, led);
    }

    protected Vuforia(HardwareMap hw, String camera, boolean viewer, boolean led ) {
        VuforiaLocalizer.Parameters parameters;
        if (viewer) {
//            int cameraMonitorViewId = AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", AbstractOpMode.getOpModeInstance().hardwareMap.appContext.getPackageName());
            int cameraMonitorViewId = hw.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hw.appContext.getPackageName());
            parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        } else {
            parameters = new VuforiaLocalizer.Parameters();
        }
        parameters.vuforiaLicenseKey = "AbuxcJX/////AAABmXadAYnA80uwmb4Rhy4YmvIh7qg/f2yrRu1Nd8O7sSufbUWHSv1jDhunwDBItvFchrvkc8EjTzjh97m2kAPy8YOjBclQbEBtuR8qcIfrGofASCZh2M6vQ0/Au+YbhYh0MLLdNrond+3YjkLswv6+Se3eVGw9y9fPGamiABzIrosjUdanAOWemf8BtuQUW7EqXa4mNPtQ+2jpZQO2sqtqxGu1anHQCD0S/PvdZdB7dRkyWaH6XTZCat5gZ0fpFH/aLWMFP4yiknlgYbjT7gklUAqyDX81pNrQhWWY4dOFnz2WiWhkCt+MNZMLKH5SdsyC7gwKI/r3h51pTwgXZfyYymB60eYAFqEUpeTrL+4LmltN";
        if (camera.equals("BACK")) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        } else if (camera.equals("FRONT")) {
            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        } else {
            //parameters.cameraName = AbstractOpMode.getOpModeInstance().hardwareMap.get(WebcamName.class, camera);
            parameters.cameraName = hw.get(WebcamName.class, camera);
        }
        this.vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(1);
        vuforia.enableConvertFrameToBitmap();
        setLED(led);
    }

    public Vuforia(HardwareMap hw, boolean viewer) {
        this(hw, viewer, true);
    }

    protected Vuforia(HardwareMap hw, String camera, boolean viewer) {
        this(hw, camera, viewer, false);
    }

    protected VuforiaLocalizer getVuforia() {
        return vuforia;
    }

    protected VuforiaTrackable getTemplate() {
        return template;
    }

    public Bitmap getImage() {

        Bitmap map = null;

        try {
            VuforiaLocalizer.CloseableFrame frame = vuforia.getFrameQueue().take();
            vuforia.getFrameQueue().clear();
            map = vuforia.convertFrameToBitmap(frame);

            frame.close();
        } catch (InterruptedException e) {
        }


        return map;
    }

    protected double[] getPose() {
        OpenGLMatrix openpose = ((VuforiaTrackableDefaultListener) template.getListener()).getPose();

        if (openpose == null) return null;

        VectorF trans = openpose.getTranslation();
        Orientation rot = Orientation.getOrientation(openpose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        double[] pose = {trans.get(0), trans.get(1), trans.get(2), rot.firstAngle, rot.secondAngle, rot.thirdAngle};

        return pose;
    }

    protected void startTracking(String asset) {
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset(asset);
        template = relicTrackables.get(0);
        template.setName(asset); // can help in debugging; otherwise not necessary
        relicTrackables.activate();
    }

    protected void setLED(boolean on) {
        CameraDevice.getInstance().setFlashTorchMode(on); //Turns light on camera on
    }

    protected int getRotation() {
        return getRotation(AppUtil.getInstance().getActivity(), vuforia.getCameraName());
    }

    private int getRotation(Activity activity, CameraName cameraName) {
        int rotation = 0;

        if (cameraName instanceof BuiltinCameraName) {
            int displayRotation = 0;
            switch (activity.getWindowManager().getDefaultDisplay().getRotation()) {
                case Surface.ROTATION_0: displayRotation = 0; break;
                case Surface.ROTATION_90: displayRotation = 90; break;
                case Surface.ROTATION_180: displayRotation = 180; break;
                case Surface.ROTATION_270: displayRotation = 270; break;
            }

            VuforiaLocalizer.CameraDirection cameraDirection = ((BuiltinCameraName) cameraName).getCameraDirection();

            for (int cameraId = 0; cameraId < Camera.getNumberOfCameras(); cameraId++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(cameraId, cameraInfo);

                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT && cameraDirection == VuforiaLocalizer.CameraDirection.FRONT) {
                    rotation = - displayRotation - cameraInfo.orientation;
                    break;
                }
                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK && cameraDirection == VuforiaLocalizer.CameraDirection.BACK) {
                    rotation = displayRotation - cameraInfo.orientation;
                    break;
                }
            }
        }

        while (rotation < 0) {
            rotation += 360;
        }
        rotation %= 360;
        return rotation;
    }
}
