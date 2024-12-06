package org.firstinspires.ftc.teamcode.shared.config;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.stcolumbots.common.RobotConfiguration;

/**
 * It is assumed that there is a configuration that is currently activated on the robot controller
 * (run menu / Configure Robot ) with the same name as this class.
 * It is also assumed that the device names in the 'init()' method below are the same as the devices
 * named on the activated configuration on the robot.
 */
public class MechanumBotConfiguration extends RobotConfiguration {
       // Left motors
    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;


    public ColorSensor colorSensor;

    public SparkFunOTOS otos;

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

        otos = hardwareMap.get(SparkFunOTOS.class,"sensor_otos");

        frontLeftMotor = (DcMotor) getHardwareOn("front_left_motor", hardwareMap.dcMotor);
        frontRightMotor = (DcMotor) getHardwareOn("front_right_motor", hardwareMap.dcMotor);
        backLeftMotor = (DcMotor) getHardwareOn("back_left_motor", hardwareMap.dcMotor);
        backRightMotor = (DcMotor) getHardwareOn("back_right_motor", hardwareMap.dcMotor);

        colorSensor = hardwareMap.colorSensor.get("color_sensor");

        otos.setAngularUnit(AngleUnit.DEGREES);
        otos.setLinearUnit(DistanceUnit.MM);
        otos.resetTracking();

    }


    /**
     * Factory method for this class
     *
     * @param hardwareMap
     * @param telemetry
     * @return
     */
    public static MechanumBotConfiguration newConfig(HardwareMap hardwareMap, Telemetry telemetry) {

        MechanumBotConfiguration config = new MechanumBotConfiguration();
        config.init(hardwareMap, telemetry);
        return config;
    }


}
