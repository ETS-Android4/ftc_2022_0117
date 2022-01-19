package org.firstinspires.ftc.teamcode.hardware.image;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.view.Gravity;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.internal.system.AppUtil;

import java.nio.ByteBuffer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

public class ImageViewer {

    private AppUtil appUtil;
    private Activity activity;
    private ViewGroup imageViewParent;
    private ImageView imageView;
    private FrameLayout.LayoutParams imageViewLayoutParams;

    private int rotation;

    public ImageViewer() {
        this(0);
    }

    public ImageViewer(int rotation) {
        this.rotation = rotation;

        appUtil = AppUtil.getInstance();

        activity = appUtil.getActivity();

        int cameraMonitorViewId = activity.getResources().getIdentifier(
                "tfodMonitorViewId", "id", activity.getPackageName());

        imageViewParent = activity.findViewById(cameraMonitorViewId);

        appUtil.synchronousRunOnUiThread(() -> {
            imageView = new ImageView(activity);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT));
            if (rotation != 0) {
                imageView.setRotation(360 - rotation);
            }
            imageViewLayoutParams = null;
            imageViewParent.addView(imageView);
            imageViewParent.setVisibility(VISIBLE);
        });
    }

    public void setImage(byte[] bytes, int width, int height) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.copyPixelsFromBuffer(ByteBuffer.wrap(bytes).duplicate());
        setImage(bitmap);
    }

    public void setImage(Bitmap bitmap) {
        appUtil.synchronousRunOnUiThread(() -> {
            if (imageView != null && bitmap != null) {
                if (imageViewLayoutParams == null) {
                    double width = bitmap.getWidth();
                    double height = bitmap.getHeight();

                    if (rotation % 180 != 0) {
                        double swap = width;
                        width = height;
                        height = swap;
                    }

                    double scale = Math.min(imageView.getWidth() / width, imageView.getHeight() / height);
                    width *= scale;
                    height *= scale;

                    if (rotation % 180 != 0) {
                        double swap = width;
                        width = height;
                        height = swap;
                    }

                    imageViewLayoutParams = new FrameLayout.LayoutParams((int) width, (int) height, Gravity.CENTER);
                    imageView.setLayoutParams(imageViewLayoutParams);
                }
                imageView.setImageBitmap(bitmap);
                imageView.invalidate();
            }
        });
    }

    public void clearImage() {
        if (imageView != null) {
            appUtil.synchronousRunOnUiThread(() -> {
                imageViewParent.removeView(imageView);
                imageViewParent.setVisibility(GONE);
            });
        }
    }
}
