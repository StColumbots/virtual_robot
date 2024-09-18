package org.firstinspires.ftc.teamcode.stcolumbots.config;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.stcolumbots.common.RobotConfiguration;

/**
 * It is assumed that there is a configuration that is currently activated on the robot controller
 * (run menu / Configure Robot ) with the same name as this class.
 * It is also assumed that the device names in the 'init()' method below are the same as the devices
 * named on the activated configuration on the robot.
 */
public class GameChangersBotConfiguration extends RobotConfiguration {
       // Left motors
    public DcMotor leftMotor;
    public DcMotor rightMotor;

    public ColorSensor leftColorSensor;
    public ColorSensor rightColorSensor;

    public GyroSensor gyroSensor;

    /**
     * Assign your class instance variables to the saved device names in the hardware map
     *
     * @param hardwareMap
     * @param telemetry
     */
    @Override
    protected void init(HardwareMap hardwareMap, Telemetry telemetry) {

        setTelemetry(telemetry);

        leftMotor = (DcMotor) getHardwareOn("left_motor", hardwareMap.dcMotor);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightMotor = (DcMotor) getHardwareOn("right_motor", hardwareMap.dcMotor);


        leftColorSensor = hardwareMap.colorSensor.get("left_color_sensor");
        rightColorSensor = hardwareMap.colorSensor.get("right_color_sensor");

        gyroSensor = hardwareMap.gyroSensor.get("gyro_sensor");
    }


    /**
     * Factory method for this class
     *
     * @param hardwareMap
     * @param telemetry
     * @return
     */
    public static GameChangersBotConfiguration newConfig(HardwareMap hardwareMap, Telemetry telemetry) {

        GameChangersBotConfiguration config = new GameChangersBotConfiguration();
        config.init(hardwareMap, telemetry);
        return config;
    }


}
