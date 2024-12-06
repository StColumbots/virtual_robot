package org.firstinspires.ftc.teamcode.shared.tasks;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.teamcode.shared.common.RobotOpMode;
import org.firstinspires.ftc.teamcode.shared.drive.NormalisedMecanumDrive;


public class GoToTask extends BaseTask  implements Task {
    private final NormalisedMecanumDrive drive;
    private final SparkFunOTOS odometry;
    private final double target_x;
    private final double target_y;

    private final double max_speed;
    private final double target_error;
    private SparkFunOTOS.Pose2D current;
    private double distance;

    public GoToTask(RobotOpMode opMode, double time, NormalisedMecanumDrive drive, SparkFunOTOS odometry, double x, double y, double speed, double error) {
        super(opMode, time);

        this.drive = drive;
        this.odometry = odometry;
        this.target_x = x;
        this.target_y = y;
        this.max_speed = speed;
        this.target_error = error;
    }

    @Override
    public void init() {
        current = odometry.getPosition();
        double deltax = target_x - current.x;
        double deltay = target_y - current.y;
        distance = Math.sqrt(deltax*deltax+deltay*deltay);
        opMode.telemetry.addLine("distance " + distance);
    }

    @Override
    public void run() {
        current = odometry.getPosition();


        double deltax = target_x - current.x;
        double deltay = target_y - current.y;
        distance = Math.sqrt(deltax*deltax+deltay*deltay);
        double angle = Math.atan2(deltay, deltax);
        double speed = max_speed;
        if (distance < 300) {
            speed = distance/300;
            if (speed < 0.1) {
                speed = 0.1;
            }
        }
        System.out.println(String.format("dX %4.2f  dY %4.2f  d %4.2f h %4.1f", deltax, deltay, distance, angle*180.0/Math.PI));
        opMode.telemetry.addLine("Distance: " + distance);
        opMode.telemetry.addLine("Angle: " + angle);
        opMode.telemetry.addLine("Speed: " + speed);
        opMode.telemetry.update();
        if (isFinished()) {
            return;
        }

        drive.setSpeedXYR(deltax/distance * speed, -deltay/distance * speed, 0);
//        drive.setSpeedXYR(deltax/distance, 0, 0);
        drive.update();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || distance < target_error;
    }
}
