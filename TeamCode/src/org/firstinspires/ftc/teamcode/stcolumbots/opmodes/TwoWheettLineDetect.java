package org.firstinspires.ftc.teamcode.stcolumbots.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;


import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveEncTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.Task;
import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;
import org.firstinspires.ftc.teamcode.stcolumbots.common.TankDrive;
import org.firstinspires.ftc.teamcode.stcolumbots.config.GameChangersBotConfiguration;
import org.firstinspires.ftc.teamcode.stcolumbots.config.TwoWheelBotConfiguration;

import java.util.ArrayDeque;

@Autonomous(name = "Two Wheet Line detect")
public class TwoWheettLineDetect extends StColumbotsOpMode {
    private GameChangersBotConfiguration config;
    private TankDrive drive;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    @Override
    protected void onInit() {
        config = GameChangersBotConfiguration.newConfig(hardwareMap, telemetry);

        drive = new TankDrive(this, gamepad1, config.leftMotor, config.rightMotor);
        drive.setCountsPerCm(100.0);
        config.leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        config.rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        tasks.add(new ATaskThatDrivesALeftAndRightMotorAndDoesntCareWhatConfigItIs(this, 15.0, drive, 0.4, 0.4, config.leftColorSensor));
    }

    @Override
    protected void activeLoop() throws InterruptedException {

        telemetry.addData("Num Tasks", tasks.size());
        telemetry.addData("loop count", loopCount);
        Task currentTask = tasks.peekFirst();
        if (currentTask == null) {
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()){
            tasks.removeFirst();

        }
        if (tasks.isEmpty()) {
            drive.setPower(0, 0);
            drive.update();
        }
    }
}