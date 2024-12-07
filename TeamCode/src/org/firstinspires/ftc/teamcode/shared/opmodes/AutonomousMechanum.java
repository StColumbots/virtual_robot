package org.firstinspires.ftc.teamcode.shared.opmodes;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.shared.common.RobotOpMode;
import org.firstinspires.ftc.teamcode.shared.config.MechanumBotConfiguration;
import org.firstinspires.ftc.teamcode.shared.drive.NormalisedMecanumDrive;
import org.firstinspires.ftc.teamcode.shared.tasks.GoToTask;
import org.firstinspires.ftc.teamcode.shared.tasks.MecanumDriveTask;
import org.firstinspires.ftc.teamcode.shared.tasks.Task;


import java.util.ArrayDeque;

@Autonomous(name = "Task Based Mecanum Autonomous", group  = "Mecanum")
public class AutonomousMechanum extends RobotOpMode {
    private MechanumBotConfiguration config;
    private NormalisedMecanumDrive drive;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    private double heading;

    @Override
    protected void onInit() {
        config = MechanumBotConfiguration.newConfig(hardwareMap, telemetry);

        drive = new NormalisedMecanumDrive(this,
                config.frontLeftMotor, config.frontRightMotor,
                config.backLeftMotor, config.backRightMotor, true);

//        tasks.add(new MecanumDriveTask(this, 1, drive, 1, 0, 0));
//        tasks.add(new GoToTask(this, 4, drive, config.otos, 0, 600, 90, 0.5, 20, 5));
//        tasks.add(new GoToTask(this, 6, drive, config.otos, 600, 600, 181,0.5, 20, 5));
//        tasks.add(new GoToTask(this, 4, drive, config.otos, 0, 600, 269, 0.5, 20, 5));
//        tasks.add(new GoToTask(this, 4, drive, config.otos, 0, 0, 0, 0.5, 20, 5));
        tasks.add(new GoToTask(this, 4, drive, config.otos, 0, 500, 0, 0.5, 20, 5));
        tasks.add(new GoToTask(this, 6, drive, config.otos, 500, 500, 0,0.5, 20, 5));
        tasks.add(new GoToTask(this, 4, drive, config.otos, 500, 0, 0, 0.5, 20, 5));
        tasks.add(new GoToTask(this, 4, drive, config.otos, 0, 0, 0, 0.5, 20, 5));
//        tasks.add(new MecanumDriveTask(this, 1, drive, 0, 1, 0));
//        tasks.add(new MecanumDriveTask(this, 3, drive, 0, 0.1, 1));

        config.otos.setOffset(new SparkFunOTOS.Pose2D(0,0,0));

    }

    @Override
    protected void activeLoop() throws InterruptedException {
        telemetry.addData("FL", config.frontLeftMotor.getPower());
        telemetry.addData("FR", config.frontRightMotor.getPower());
        telemetry.addData("BL", config.backLeftMotor.getPower());
        telemetry.addData("BR", config.backRightMotor.getPower());
        telemetry.addData("x", config.otos.getPosition().x);
        telemetry.addData("y", config.otos.getPosition().y);
        telemetry.addData("h", config.otos.getPosition().h);
        Task currentTask = tasks.peekFirst();
        if (currentTask == null) {
            this.setOperationsCompleted();
            return;
        }
        currentTask.run();
        if (currentTask.isFinished()){
            tasks.removeFirst();

        }
        if (tasks.isEmpty()) {
            drive.setSpeedXYR(0, 0, 0);
            drive.update();
        }
    }
}