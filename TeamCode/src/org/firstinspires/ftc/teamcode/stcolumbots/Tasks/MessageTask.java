package org.firstinspires.ftc.teamcode.stcolumbots.Tasks;

import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;

public class MessageTask extends BaseTask implements Task {

    private String message;


    /**
     * the number of nanoseconds in a second
     */
    public static final double NANOS_IN_SECONDS = 1000000000.0;


    public MessageTask(StColumbotsOpMode opMode, double time, String message) {
        super(opMode, time);
        this.message = message;

    }

    @Override
    public void run() {
        if (isFinished()) {
            return;
        }
        opMode.telemetry.addData("Message",String.format("%s %.2f", message, time));
    }

}
