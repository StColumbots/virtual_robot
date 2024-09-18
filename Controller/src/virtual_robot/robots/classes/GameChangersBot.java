package virtual_robot.robots.classes;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.hardware.configuration.MotorType;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import virtual_robot.controller.BotConfig;
import virtual_robot.controller.VirtualBot;
import virtual_robot.controller.VirtualField;
import virtual_robot.controller.VirtualRobotController;
import virtual_robot.util.AngleUtils;

/**
 * For internal use only. Represents a robot with two standard wheels, color sensor, four distance sensors,
 * a Gyro Sensor, and a Servo-controlled arm on the back.
 * <p>
 * GameChangersBot is the controller class for the "game_changers_bot.fxml" markup file.
 */
@BotConfig(name = "Game Changers Bot", filename = "game_changers_bot")
public class GameChangersBot extends VirtualBot {

    private MotorType motorType;
    private DcMotorExImpl leftMotor = null;
    private DcMotorExImpl rightMotor = null;
    private GyroSensorImpl gyro = null;
    private VirtualRobotController.ColorSensorImpl leftColorSensor = null;
    private VirtualRobotController.ColorSensorImpl rightColorSensor = null;
    private ServoImpl servo = null;
    private VirtualRobotController.DistanceSensorImpl[] distanceSensors = null;

    //The backServoArm object is instantiated during loading via a fx:id property.
    @FXML
    Rectangle backServoArm;

    @FXML
    Circle leftSensorCircle;
    @FXML
    Circle rightSensorCircle;

    private double wheelCircumference;
    private double interWheelDistance;

    private Rectangle leftCrossX;
    private Rectangle leftCrossY;
    private Rectangle rightCrossX;
    private Rectangle rightCrossY;

    private final Rotate sensorRotate = new Rotate(0.0, 0.0, 0.0);
    private BooleanProperty debugGraphicsVisibleProperty = new SimpleBooleanProperty(false);


    public GameChangersBot() {
        super();
    }

    public void initialize() {
        hardwareMap.setActive(true);
        leftMotor = (DcMotorExImpl) hardwareMap.get(DcMotorEx.class, "left_motor");
        rightMotor = (DcMotorExImpl) hardwareMap.get(DcMotorEx.class, "right_motor");
        distanceSensors = new VirtualRobotController.DistanceSensorImpl[]{
                hardwareMap.get(VirtualRobotController.DistanceSensorImpl.class, "front_distance"),
                hardwareMap.get(VirtualRobotController.DistanceSensorImpl.class, "left_distance"),
                hardwareMap.get(VirtualRobotController.DistanceSensorImpl.class, "back_distance"),
                hardwareMap.get(VirtualRobotController.DistanceSensorImpl.class, "right_distance")
        };
        gyro = (GyroSensorImpl) hardwareMap.gyroSensor.get("gyro_sensor");
        leftColorSensor = (VirtualRobotController.ColorSensorImpl) hardwareMap.colorSensor.get("left_color_sensor");
        rightColorSensor = (VirtualRobotController.ColorSensorImpl) hardwareMap.colorSensor.get("right_color_sensor");
        servo = (ServoImpl) hardwareMap.servo.get("back_servo");
        wheelCircumference = Math.PI * botWidth / 4.5;
        interWheelDistance = botWidth * 8.0 / 9.0;
        hardwareMap.setActive(false);

        leftCrossX = new Rectangle(0.0, 0.0, 40.0, 2.0);
        leftCrossX.setFill(Color.color(0.9, 0.1, 0.1));
        leftCrossX.visibleProperty().bind(debugGraphicsVisibleProperty);
        leftCrossY = new Rectangle(0.0, 0.0, 2.0, 40.0);
        leftCrossY.setFill(Color.color(0.9, 0.1, 0.1));
        leftCrossY.visibleProperty().bind(debugGraphicsVisibleProperty);
        rightCrossX = new Rectangle(0.0, 0.0, 40.0, 2.0);
        rightCrossX.setFill(Color.color(0.1, 0.1, 0.9));
        rightCrossX.visibleProperty().bind(debugGraphicsVisibleProperty);
        rightCrossY = new Rectangle(0.0, 0.0, 2.0, 40.0);
        rightCrossY.setFill(Color.color(0.1, 0.1, 0.9));
        rightCrossY.visibleProperty().bind(debugGraphicsVisibleProperty);
        fieldPane.getChildren().addAll(leftCrossX, leftCrossY, rightCrossX, rightCrossY);
        backServoArm.getTransforms().add(new Rotate(0, 37.5, 67.5));
    }

    protected void createHardwareMap() {
        motorType = MotorType.Neverest40;
        hardwareMap = new HardwareMap();
        hardwareMap.put("left_motor", new DcMotorExImpl(motorType));
        hardwareMap.put("right_motor", new DcMotorExImpl(motorType));
        String[] distNames = new String[]{"front_distance", "left_distance", "back_distance", "right_distance"};
        for (String name : distNames) hardwareMap.put(name, controller.new DistanceSensorImpl());
        hardwareMap.put("gyro_sensor", new GyroSensorImpl(this, 10));
        hardwareMap.put("left_color_sensor", controller.new ColorSensorImpl());
        hardwareMap.put("right_color_sensor", controller.new ColorSensorImpl());
        hardwareMap.put("back_servo", new ServoImpl());
    }

    public synchronized void updateStateAndSensors(double millis) {
        double deltaLeftPos = leftMotor.update(millis);
        double deltaRightPos = rightMotor.update(millis);
        double leftWheelDist = -deltaLeftPos * wheelCircumference / motorType.TICKS_PER_ROTATION;
        double rightWheelDist = deltaRightPos * wheelCircumference / motorType.TICKS_PER_ROTATION;
        double distTraveled = (leftWheelDist + rightWheelDist) / 2.0;
        double headingChange = (rightWheelDist - leftWheelDist) / interWheelDistance;
        double deltaRobotX = -distTraveled * Math.sin(headingRadians + headingChange / 2.0);
        double deltaRobotY = distTraveled * Math.cos(headingRadians + headingChange / 2.0);
        x += deltaRobotX;
        y += deltaRobotY;
        if (x > (VirtualField.HALF_FIELD_WIDTH - halfBotWidth)) x = VirtualField.HALF_FIELD_WIDTH - halfBotWidth;
        else if (x < (halfBotWidth - VirtualField.HALF_FIELD_WIDTH)) x = halfBotWidth - VirtualField.HALF_FIELD_WIDTH;
        if (y > (VirtualField.HALF_FIELD_WIDTH - halfBotWidth)) y = VirtualField.HALF_FIELD_WIDTH - halfBotWidth;
        else if (y < (halfBotWidth - VirtualField.HALF_FIELD_WIDTH)) y = halfBotWidth - VirtualField.HALF_FIELD_WIDTH;
        headingRadians += headingChange;
        if (headingRadians > Math.PI) headingRadians -= 2.0 * Math.PI;
        else if (headingRadians < -Math.PI) headingRadians += 2.0 * Math.PI;
        gyro.updateHeading(headingRadians * 180.0 / Math.PI);

        // Robot coordinates 0,0 in the centre, the scene positions are 0,0 in the left,top
        double sceneX = VirtualField.HALF_FIELD_WIDTH + x;
        double sceneY = -(y - VirtualField.HALF_FIELD_WIDTH);

        sensorRotate.setAngle(-headingRadians * 180.0 / Math.PI);
        sensorRotate.setPivotX(sceneX);
        sensorRotate.setPivotY(sceneY);
        Point2D leftScenePoint = sensorRotate.transform(sceneX - halfBotWidth + leftSensorCircle.getCenterX(), sceneY - halfBotWidth + leftSensorCircle.getCenterY());
        Point2D rightScenePoint = sensorRotate.transform(sceneX - halfBotWidth + rightSensorCircle.getCenterX() + 5.0, sceneY - halfBotWidth + rightSensorCircle.getCenterY());


        leftColorSensor.updateColor(leftScenePoint.getX() - VirtualField.HALF_FIELD_WIDTH, -(leftScenePoint.getY() - VirtualField.HALF_FIELD_WIDTH));
        leftCrossX.setTranslateX(leftScenePoint.getX() - 20.0);
        leftCrossX.setTranslateY(leftScenePoint.getY() - 1.0);
        leftCrossY.setTranslateX(leftScenePoint.getX() - 1.0);
        leftCrossY.setTranslateY(leftScenePoint.getY() - 20.0);
        leftSensorCircle.setFill(Color.rgb(leftColorSensor.red(), leftColorSensor.green(), leftColorSensor.blue()));

        rightColorSensor.updateColor(rightScenePoint.getX() - VirtualField.HALF_FIELD_WIDTH, -(rightScenePoint.getY() - VirtualField.HALF_FIELD_WIDTH));
        rightCrossX.setTranslateX(rightScenePoint.getX() - 20.0);
        rightCrossX.setTranslateY(rightScenePoint.getY() - 1.0);
        rightCrossY.setTranslateX(rightScenePoint.getX() - 1.0);
        rightCrossY.setTranslateY(rightScenePoint.getY() - 20.0);
        rightSensorCircle.setFill(Color.rgb(rightColorSensor.red(), rightColorSensor.green(), rightColorSensor.blue()));

        final double piOver2 = Math.PI / 2.0;
        for (int i = 0; i < 4; i++) {
            double sensorHeading = AngleUtils.normalizeRadians(headingRadians + i * piOver2);
            distanceSensors[i].updateDistance(x - halfBotWidth * Math.sin(sensorHeading),
                    y + halfBotWidth * Math.cos(sensorHeading), sensorHeading);
        }
    }

    public synchronized void updateDisplay() {
        super.updateDisplay();
        ((Rotate) backServoArm.getTransforms().get(0)).setAngle(-180.0 * servo.getInternalPosition());
    }

    public void powerDownAndReset() {
        leftMotor.stopAndReset();
        rightMotor.stopAndReset();
        gyro.deinit();
    }


}
