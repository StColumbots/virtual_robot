package org.firstinspires.ftc.teamcode.stcolumbots.Tasks;

import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;

public class WaitTask extends BaseTask implements Task {


    public WaitTask(StColumbotsOpMode opMode, double time) {
        super(opMode, time);

    }

    void update() {

    }

    @Override
    public void run() {
        if (isFinished()) {
            return;
        }

    }

}
