package org.firstinspires.ftc.teamcode.shared.common;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.shared.utils.MovingAverageTimer;

/**
 * OpMode Abstract class that offers additional abstraction for opMode developers
 * including catch-all error handling
 */
public abstract class RobotOpMode extends LinearOpMode {


    protected MovingAverageTimer movingAverageTimer;
    protected long loopCount = 0;
    private boolean operationsCompleted;

    /**
     * Implement this method to define the code to run when the Init button is pressed on the Driver station.
     */
    abstract protected void onInit();


    /**
     * override to this method to perform one time operations after start is pressed
     */
    protected void onStart() throws InterruptedException {
        clearTelemetryData();
        movingAverageTimer.reset();
    }

    /**
     * override to this method to perform one time operations after the activeLoop finishes
     */
    protected void onStop() throws InterruptedException {
    }

    /**
     * Implement this method to define the code to run when the Start button is pressed on the Driver station.
     * This method will be called on each hardware cycle
     *
     * @throws InterruptedException
     */
    abstract protected void activeLoop() throws InterruptedException;


    /**
     * Override this method only if you need to do something outside of onInit() and activeLoop()
     *
     * @throws InterruptedException
     */
    @Override
    public void runOpMode() throws InterruptedException {

        try {
            try {
                setup();
                onInit();
            } catch (Throwable e) {
                ErrorUtil.handleCatchAllException(e, telemetry);
            }

            waitForStart();

            onStart();

            while (opModeIsActive() && !operationsCompleted) {

                try {
                    activeLoop();
                    loopCount++;
                } catch (InterruptedException ie) {
                    throw ie;
                } catch (Throwable e) {
                    ErrorUtil.handleCatchAllException(e, telemetry);
                }

                movingAverageTimer.update();
                telemetry.update();
                idle();
            }

            //wait for user to hit stop
            telemetry.addLine("Op mode complete: Press STOP to exit");
            while (opModeIsActive()) {
                idle();
            }
        } finally {
            onStop();
        }
    }

    private void setup() {

        movingAverageTimer = new MovingAverageTimer(100);

    }

    /**
     * Clear data from the telemetry cache
     */
    public void clearTelemetryData() {
        if (telemetry.isAutoClear()) {
            telemetry.clear();
        } else {
            telemetry.clearAll();
        }
        if (opModeIsActive()) {
            idle();
        }
    }

    /**
     * call to prevent leave the loop calling activeLoop()
     */
    protected void setOperationsCompleted() {
        this.operationsCompleted = true;
        telemetry.addData("Opmode Status", "Operations completed");
    }

}
