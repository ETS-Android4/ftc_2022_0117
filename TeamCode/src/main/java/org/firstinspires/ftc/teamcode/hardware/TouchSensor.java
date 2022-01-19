package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TouchSensor {

    private Telemetry sensorTelemetry;
    DeviceInterfaceModule dim;   // hub
    DigitalChannel        digIn; // input




    public TouchSensor(HardwareMap hwMap, Telemetry telemetry, String name) {
        this.sensorTelemetry = telemetry;
        dim = hwMap.get(DeviceInterfaceModule.class, "dim");   //  Use generic form of device mapping
        digIn  = hwMap.get(DigitalChannel.class, name);     //  Use generic form of device mapping
        digIn.setMode(DigitalChannel.Mode.INPUT);
    }

    public boolean getSwitchValue() {
        boolean temp;
        temp = digIn.getState();
        return(temp);

    }
}
