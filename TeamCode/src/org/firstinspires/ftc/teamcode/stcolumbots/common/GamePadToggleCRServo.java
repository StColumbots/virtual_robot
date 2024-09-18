package org.firstinspires.ftc.teamcode.stcolumbots.common;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;


/**
 * Operation to assist with Gamepad actions on DCMotors
 */
public class GamePadToggleCRServo extends StColumbotsComponent {

    private final ButtonControl buttonControl;
    private final CRServo crservo;
    private final Gamepad gamepad;
    private final float crservopower;
    private boolean crservoOn = false;
    private boolean lastButtonState = false;


    /**
     * Constructor for operation.  Telemetry enabled by default.
     *
     * @param opMode
     * @param gamepad       Gamepad
     * @param motor         DcMotor to operate on
     * @param buttonControl {@link ButtonControl}
     * @param power         power to apply when using gamepad buttons
     * @param showTelemetry  display the power values on the telemetry
     */
    public GamePadToggleCRServo(StColumbotsOpMode opMode, Gamepad gamepad, CRServo crservo, ButtonControl buttonControl, float power, boolean showTelemetry) {
        super(opMode);

        this.gamepad = gamepad;
        this.crservo = crservo;
        this.buttonControl = buttonControl;
        this.crservopower = power;

    }
    public GamePadToggleCRServo(StColumbotsOpMode opMode, Gamepad gamepad, CRServo crservo , ButtonControl buttonControl, float power) {
        this(opMode,gamepad,crservo,buttonControl,power,true);
    }


    /**
     * Update servo with latest gamepad state
     */
    public void update() {
        // Only toggle when the button state changes from false to true, ie when the
        // button is pressed down (and not when the button comes back up)
        boolean pressed = buttonPressed(gamepad, buttonControl);
        if (pressed && lastButtonState != pressed) {
            crservoOn = !crservoOn;
            float power = crservoOn ? crservopower : 0.0f;
            crservo.setPower(power);
        }
        lastButtonState = pressed;
    }


}
