package org.firstinspires.ftc.teamcode.shared.tasks;

import com.qualcomm.hardware.sparkfun.SparkFunOTOS;

import org.firstinspires.ftc.teamcode.shared.common.RobotOpMode;
import org.firstinspires.ftc.teamcode.shared.drive.NormalisedMecanumDrive;


public class GoToTask extends BaseTask  implements Task {
    private final NormalisedMecanumDrive drive;
    private final SparkFunOTOS odometry;
    private final double target_x;
    private final double target_y;

    private final double target_h;

    private final double max_speed;
    private final double distance_error;
    private final double angle_error;
    private SparkFunOTOS.Pose2D current;
    private double distance;
    private double h_distance;

    private double last_speedX = 0.0;
    private double last_speedY = 0.0;
    private double last_speedR = 0.0;
    private double last_distance = 0.0;


    public GoToTask(RobotOpMode opMode, double time, NormalisedMecanumDrive drive, SparkFunOTOS odometry,
                    double x, double y, double h, double speed, double distance_error, double angle_error) {
        super(opMode, time);

        this.drive = drive;
        this.odometry = odometry;
        this.target_x = x;
        this.target_y = y;
        this.target_h = h;
        this.max_speed = speed;
        this.distance_error = distance_error;
        this.angle_error = angle_error/180*Math.PI;
    }

    @Override
    public void init() {
        current = odometry.getPosition();
        double deltax = target_x - current.x;
        double deltay = target_y - current.y;
        distance = Math.sqrt(deltax*deltax+deltay*deltay);
        h_distance = target_h - current.h;
        opMode.telemetry.addLine("distance " + distance);
    }

    @Override
    public void run() {
        current = odometry.getPosition();

        double deltax = target_x - current.x;
        double deltay = target_y - current.y;
        distance = Math.sqrt(deltax*deltax+deltay*deltay);
        double angle = Math.atan2(deltay, deltax);
        h_distance = unaliasAngle(target_h - current.h);

        double speed_x_adj = (Math.abs(deltax) > 2 * distance_error) ? 1.0 : (Math.abs(deltax) / (2 * distance_error));
        double speed_y_adj = (Math.abs(deltay) > 2 * distance_error) ? 1.0 : (Math.abs(deltay) / (2 * distance_error));

        double speedx = Math.cos(angle)*max_speed*speed_x_adj;
        double speedy = Math.sin(angle)*max_speed*speed_y_adj;
        double speedr = Math.min(Math.abs(h_distance / 30), max_speed/2) * Math.signum(h_distance);

        if (Math.abs(h_distance) < angle_error) {
            speedr = 0.0;
        }

//        if (last_distance < distance) {
//            drive.setSpeedXYR(0,0,0);
//            drive.update();
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        last_distance = distance;

        System.out.println(String.format("Target[%4.2f,%4.2f] - Current[%4.2f,%4.2f] dX %4.2f sX: %4.2f dY %4.2f sY: %4.2f  d %4.2f dh %4.1f h: %4.1f sH: %4.2f ", target_x, target_y, current.x, current.y, deltax, speedx, deltay, speedy, distance, h_distance, current.h, speedr));
        opMode.telemetry.addLine("Distance: " + distance);
        opMode.telemetry.addLine("Angle: " + angle);
        opMode.telemetry.addLine("Speed: " + max_speed);
        opMode.telemetry.update();
        if (isFinished()) {
            return;
        }


        drive.setSpeedXYR(speedx, -speedy, speedr);
//        drive.setSpeedPolarR(max_speed, -angle * 180 / Math.PI, speedr);
//        drive.setSpeedXYR(deltax/distance, 0, 0);
        drive.update();
    }

    @Override
    public boolean isFinished() {
        return super.isFinished() || (distance < distance_error && h_distance < angle_error);
    }

    public static double unaliasAngle(double angle) {
        angle = angle % 360; // Reduce angle to the range -360 to 360
        if (angle > 180) {
            angle -= 360;
        } else if (angle <= -180) {
            angle += 360;
        }
        return angle;
    }
}
