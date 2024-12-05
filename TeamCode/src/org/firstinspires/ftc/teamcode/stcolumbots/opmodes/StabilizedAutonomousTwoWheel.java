package org.firstinspires.ftc.teamcode.stcolumbots.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.MessageTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveGyroTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.Task;
import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;
import org.firstinspires.ftc.teamcode.stcolumbots.common.TankDrive;
import org.firstinspires.ftc.teamcode.stcolumbots.config.GameChangersBotConfiguration;
import org.firstinspires.ftc.teamcode.stcolumbots.config.TwoWheelBotConfiguration;

import java.util.ArrayDeque;

@Autonomous(name = "Two Wheel Autonomous", group  = "Two Wheel")
public class StabilizedAutonomousTwoWheel extends StColumbotsOpMode {
    private TwoWheelBotConfiguration config;
    private TankDrive drive;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    private double heading;

    @Override
    protected void onInit() {
        config = TwoWheelBotConfiguration.newConfig(hardwareMap, telemetry);

        drive = new TankDrive(this, gamepad1, config.leftMotor, config.rightMotor);
        tasks.add(new TankDriveTask(this, 1, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 0.446688, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 0.444447, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 2, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 2, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 2, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, 1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, -1, 1));
        tasks.add(new TankDriveTask(this, 1, drive, 1, 1));
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        telemetry.addData("L", config.leftMotor.getPower());
        telemetry.addData("R", config.rightMotor.getPower());
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