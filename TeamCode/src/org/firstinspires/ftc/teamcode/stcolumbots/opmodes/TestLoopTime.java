package org.firstinspires.ftc.teamcode.stcolumbots.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;

@TeleOp(name = "TestLoopTime", group = "StColumbots")
public class TestLoopTime extends StColumbotsOpMode {
    /**
     * Implement this method to define the code to run when the Init button is pressed on the Driver station.
     */
    @Override
    protected void onInit() {

    }

    /**
     * Implement this method to define the code to run when the Start button is pressed on the Driver station.
     * This method will be called on each hardware cycle
     *
     * @throws InterruptedException
     */
    @Override
    protected void activeLoop() throws InterruptedException {

        telemetry.addData("Loop time:", movingAverageTimer.averageString());
    }
}
