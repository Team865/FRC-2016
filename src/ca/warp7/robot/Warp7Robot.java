package ca.warp7.robot;

import ca.warp7.robot.autonomous.TestAutonomous;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ChandlerDefault;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intakes;
import ca.warp7.robot.subsystems.Shooter;
import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import edu.wpi.first.wpilibj.*;

public class Warp7Robot extends SampleRobot {

    public static XboxController driver;   // set to ID 1 in DriverStation
    public static XboxController operator; // set to ID 2 in DriverStation
    public static ADXRS453Gyro gyro;
    public static DigitalInput photosensor;
    public static int wantedRPM;
    int session;
    Image frame;
    private double degrees;
    private boolean increased;
    private boolean changed;

    private boolean changed2;
    private int count;
    private boolean changed3;
    private boolean changed4;
    private boolean changed5;
	private Shooter shooter;
    public Warp7Robot() {
        count = 0;
        changed3 = false;
        driver = new XboxController(0, new ChandlerDefault());
        operator = new XboxController(1, new ChandlerDefault());
        changed4 = false;
        changed5 = false;
        initRobot();

        degrees = 0.0;
        increased = false;
        changed2 = false;
    }

    private void controls() {
        shooter.prepareToFire(wantedRPM * -1);

        shooter.setHood(-0.25);

        if (driver.getAbutton()) {
            if (!changed2) {
                Intakes.moveAdjustingArm();
                changed2 = true;
            }
        } else {
            changed2 = false;
        }

        if (driver.getDPad() == 0) {
            if (!increased) {
                wantedRPM += 200;
                if (wantedRPM <= 1000 && wantedRPM >= 0) wantedRPM = 1000;
                increased = true;
            }
        }

        if (driver.getDPad() == 180) {
            if (!increased) {
                wantedRPM -= 200;
                if (wantedRPM >= -1000 && wantedRPM <= 0) wantedRPM = -1000;
                increased = true;
            }
        }

        if (driver.getDPad() == 90) {
            if (!increased) {
                degrees += 2;
                //if(degrees <= 0.15)degrees = 0.15;
                increased = true;
            }
        }

        if (driver.getDPad() == 270) {
            if (!increased) {
                degrees -= 2;
                //if(degrees >= -0.15)degrees = -0.15;
                increased = true;
            }
        }

        if (driver.getDPad() == -1) {
            increased = false;
        }

        if (driver.getLeftTrigger() >= 0.5) {
            if (driver.getRightBumperbutton()) {
                Intakes.intake(false);
            } else {
                Intakes.intake(photosensor.get());
            }
        } else {
            Intakes.stopIntake();
        }

        if (driver.getBbutton()) {
            Intakes.outake();
        }

        wantedRPM = Math.max(-6000, Math.min(6000, wantedRPM));
        degrees = Math.max(0, Math.min(90, degrees));

        if (driver.getRightTrigger() <= 0.5) wantedRPM = 0;
        if (driver.getStartButton()) degrees = 0.0;

        if (wantedRPM != 0 && count >= 10) {
            System.out.println("\n \n" + "Wanted rpm = " + wantedRPM);
            System.out.println("Current rpm  = " + (shooter.getSpeed() * -1) + "\n");
            System.out.println("Hood Degrees = " + degrees);
        }

        if (count == 10) count = 0;
        count++;

        if (degrees != 0) {
            System.out.println(degrees);
        }
        shooter.setHood(degrees);

        if (driver.getRightStickButton()) {
            if (!changed) {
                Drive.changeDirection();
                changed = true;
            }
        } else {
            changed = false;
        }

        if (driver.getLeftStickButton()) {
            if (!changed3) {
                System.out.println("pressed");
                Drive.changeGear();
                changed3 = true;
            }
        } else {
            changed3 = false;
        }
        if (driver.getXbutton()) {
            if (!changed4) {
                System.out.println("pressed");
                Drive.changePTO();
                changed4 = true;
            }
        } else {
            changed4 = false;
        }

        if (driver.getYbutton()) {
            if (!changed5) {
                System.out.println("pressed");
                Intakes.moveInitialArm();
                changed5 = true;
            }
        } else {
            changed5 = false;
        }


        if (operator.getRightBumperbutton()) {
            System.out.println("getAngle " + gyro.getAngle());
        }

        if (operator.getLeftBumperbutton()) {
            System.out.println(gyro.getStatus());
        }
        if (operator.getStartButton()) {
            gyro.calibrate();
        }
        if (operator.getBackButton()) {
            gyro.stopCalibrating();
        }
        if (operator.getAbutton()) {
            if (gyro.isCalibrating()) {
                System.out.println("Gyro is calibrating");
            } else {
                System.out.println("Gyro is not calibrating");
            }
        }
    }

    public void operatorControl() {
        while (isOperatorControl() && isEnabled()) {
            cameraLoop();
            controls();
            Drive.cheesyDrive();
            Timer.delay(0.005);
        }
    }

    public void autonomous() {
        double distance = 0.0;
        while (isAutonomous() && !isOperatorControl() && isEnabled()) {
            cameraLoop();
            distance = TestAutonomous.sinAuto(distance);
        }
    }

    public void disabled() {
        while (!isEnabled()) {
            shooter.stop();
            Drive.stop();
            Intakes.stop();
            cameraLoop();
            //System.out.println("Robot Disabled!!!!!");
        }
    }

    private void initRobot() {
        wantedRPM = 0;
        try {
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

            // the camera name (ex "cam0") can be found through the roborio web interface
            session = NIVision.IMAQdxOpenCamera("cam1",
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(session);
            NIVision.IMAQdxStartAcquisition(session);
        } catch (Exception e) {
        }

        shooter = new Shooter(new CANTalon(Constants.SHOOTER_CAN_ID), new Encoder(Constants.FLY_ENC_A, Constants.FLY_ENC_B),
                new GearBox(Constants.FLY_WHEEL_PIN, Constants.FLY_WHEEL_MOTOR_TYPE));
        Drive.init(new GearBox(Constants.RIGHT_DRIVE_MOTOR_PINS, Constants.RIGHT_DRIVE_MOTOR_TYPES),
                new GearBox(Constants.LEFT_DRIVE_MOTOR_PINS, Constants.LEFT_DRIVE_MOTOR_TYPES),
                new Solenoid(Constants.GEAR_CHANGE), new Solenoid(Constants.PTO));
        Intakes.init(new GearBox(Constants.INTAKE_MOTOR, Constants.INTAKE_MOTOR_TYPES),
                new Solenoid(Constants.INTAKE_PISTON_A), new Solenoid(Constants.INTAKE_PISTON_B));

        photosensor = new DigitalInput(Constants.INTAKE_PHOTOSENSOR);

        gyro = new ADXRS453Gyro();
        gyro.startThread();
    }

    private void cameraLoop() {
        try {
            NIVision.IMAQdxGrab(session, frame, 1);
            CameraServer.getInstance().setImage(frame);
        } catch (Exception e) {
        }
    }
}