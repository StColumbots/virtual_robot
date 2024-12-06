package org.firstinspires.ftc.teamcode.shared.tasks;


import org.firstinspires.ftc.teamcode.shared.common.RobotOpMode;

public class MessageTask extends BaseTask implements Task {

    private String message;

    public MessageTask(RobotOpMode opMode, double time, String message) {
        super(opMode, time);
        this.message = message;

    }

    @Override
    public void run() {
        if (isFinished()) {
            return;
        }
        opMode.telemetry.addLine(String.format("%s %4.2f", message, time));
    }

}
