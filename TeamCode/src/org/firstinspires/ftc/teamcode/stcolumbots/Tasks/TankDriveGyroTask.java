package org.firstinspires.ftc.teamcode.stcolumbots.Tasks;

import com.qualcomm.robotcore.hardware.GyroSensor;

import org.firstinspires.ftc.teamcode.stcolumbots.common.StColumbotsOpMode;
import org.firstinspires.ftc.teamcode.stcolumbots.common.TankDrive;

public class TankDriveGyroTask extends BaseTask implements Task {

    private final TankDrive drive;
    private double leftSpeed;
    private double rightSpeed;
    private double tgtLeftSpeed;
    private double tgtRightSpeed;
    private final GyroSensor gyro;

    public TankDriveGyroTask(StColumbotsOpMode opMode, GyroSensor gyro, double time, TankDrive drive, double leftSpeed, double rightSpeed) {
        super(opMode, time);
        this.drive = drive;
        this.gyro = gyro;

        this.leftSpeed = leftSpeed;
        this.rightSpeed = rightSpeed;
        this.tgtLeftSpeed = leftSpeed;
        this.tgtRightSpeed = rightSpeed;
    }

    @Override
    public void init() {
        super.init();
        drive.setEncoderMode(false);
    }

    @Override
    public void run() {
        if (isFinished()) {
            drive.setPower(0, 0);
            drive.update();
            return;
        }

        this.tgtLeftSpeed = leftSpeed;
        this.tgtRightSpeed = rightSpeed;

        double diff = gyro.getHeading();
        double deltaSpeed = Math.abs(diff) * 0.01;
        double sign = Math.signum(diff);

        leftSpeed = tgtLeftSpeed + (deltaSpeed * sign);
        rightSpeed = tgtRightSpeed -(deltaSpeed * sign);

        drive.setPower(leftSpeed, rightSpeed);
        drive.update();
    }

}
