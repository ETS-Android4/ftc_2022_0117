package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class DigitalOutLed {

    private Telemetry sensorTelemetry;
    DeviceInterfaceModule dim;   // hub
    DigitalChannel DigitalOut;



    public DigitalOutLed(HardwareMap hwMap, Telemetry telemetry, String name) {
        this.sensorTelemetry = telemetry;
        dim = hwMap.get(DeviceInterfaceModule.class, "dim");
        DigitalOut = hwMap.get(DigitalChannel.class, name);
        DigitalOut.setMode(DigitalChannel.Mode.OUTPUT);
    }

    public void setDigitalOut(boolean input) {
        DigitalOut.setState(input);
    }
}
