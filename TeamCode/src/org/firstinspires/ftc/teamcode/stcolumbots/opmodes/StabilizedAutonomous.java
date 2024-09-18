package org.firstinspires.ftc.teamcode.stcolumbots.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveEncTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.TankDriveGyroTask;
import org.firstinspires.ftc.teamcode.stcolumbots.Tasks.Task;
import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;
import org.firstinspires.ftc.teamcode.stcolumbots.common.TankDrive;
import org.firstinspires.ftc.teamcode.stcolumbots.config.GameChangersBotConfiguration;
import org.firstinspires.ftc.teamcode.stcolumbots.config.TwoWheelBotConfiguration;

import java.util.ArrayDeque;

@Autonomous(name = "Stabilized Autonomous", group  = "StColumbots")
public class StabilizedAutonomous extends StColumbotsOpMode {
    private GameChangersBotConfiguration config;
    private TankDrive drive;
    private ArrayDeque<Task> tasks = new ArrayDeque<>();

    private double heading;

    @Override
    protected void onInit() {
        config = GameChangersBotConfiguration.newConfig(hardwareMap, telemetry);

        config.gyroSensor.init();

        drive = new TankDrive(this, gamepad1, config.leftMotor, config.rightMotor);
        tasks.add(new TankDriveGyroTask(this, config.gyroSensor, 0.5, drive, .8, 1));
        tasks.add(new TankDriveGyroTask(this, config.gyroSensor, 20, drive, .8, 1));
    }

    @Override
    protected void activeLoop() throws InterruptedException {
        telemetry.addData("Gyro", config.gyroSensor.getHeading());
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