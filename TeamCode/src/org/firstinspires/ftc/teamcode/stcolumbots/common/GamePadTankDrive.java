package org.firstinspires.ftc.teamcode.stcolumbots.common;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Shaun on 2/07/2017.
 */

public class GamePadTankDrive extends StColumbotsComponent {

    final private DcMotor leftMotor;
    final private DcMotor rightMotor;
    final private Gamepad gamepad;

    final private TankDrive tankDrive;



    public GamePadTankDrive(StColumbotsOpMode opMode, Gamepad gamepad, DcMotor leftMotor, DcMotor rightMotor) {
        super(opMode);

        this.gamepad = gamepad;
        this.leftMotor = leftMotor;
        this.rightMotor = rightMotor;
        tankDrive = new TankDrive(opMode, gamepad, leftMotor, rightMotor);
    }

    /*
     * Update the motor power based on the gamepad state
     */
    public void update() {
        float leftPower = scaleTriggerPower(gamepad.left_stick_y);
        float rightPower = scaleTriggerPower(gamepad.right_stick_y);

        tankDrive.setPower(leftPower, rightPower);
        tankDrive.update();
    }

    private static float[] power_curve =
            {0.00f, 0.1f, 0.3f, 0.7f, 1.0f, 1.0f };

    /**
     * The  DC motors are scaled to make it easier to control them at slower speeds
     * The clip method guarantees the value never exceeds the range 0-1.
     */
    private float scaleTriggerPower(float power) {

        // Ensure the values are legal.
        float clipped_power = Range.clip(power, -1, 1);

        // Remember if this is positive or negative
        float sign = Math.signum(clipped_power);

        // Work only with positive numbers for simplicity
        float abs_power = Math.abs(clipped_power);

        // Map the power value [0..1.0] to a power curve index
        int index = (int) (abs_power * (power_curve.length - 1));

        float scaled_power = sign * power_curve[index];

        return scaled_power;
    }


    private static float[] steer_curve =
            {0.00f, 0.2f, 0.2f, 0.4f, 0.6f, 0.7f, 0.8f, 1.0f};

    private float scaleSteerPower(float p_power) {

        // Ensure the values are legal.
        float clipped_power = Range.clip(p_power, -1, 1);

        // Remember if this is positive or negative
        float sign = Math.signum(clipped_power);

        // Work only with positive numbers for simplicity
        float abs_power = Math.abs(clipped_power);

        // Map the power value [0..1.0] to a power curve index
        int index = (int) (abs_power * (steer_curve.length - 1));

        float scaled_power = sign * steer_curve[index];

        return scaled_power;

    }

}
