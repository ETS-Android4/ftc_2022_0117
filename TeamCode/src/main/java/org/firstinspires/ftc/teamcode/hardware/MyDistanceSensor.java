package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class MyDistanceSensor {
    OpticalDistanceSensor odsSensor;
    private Telemetry sensorTelemetry;
    double distanceIn = 0;


    public MyDistanceSensor(HardwareMap hwMap, Telemetry telemetry, String name) {
        this.sensorTelemetry = telemetry;
        odsSensor = hwMap.get(OpticalDistanceSensor.class, name);

    }
    public double getLightValue() {
        return odsSensor.getLightDetected();
    }
    public double getRawLightValue() {
        return odsSensor.getRawLightDetected();
    }
    public double getDistance() {
        distanceIn = getLightValue()*1;
        return  distanceIn;
    }




}
