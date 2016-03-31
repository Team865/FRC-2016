package ca.warp7.robot;

import static ca.warp7.robot.Constants.COMPRESSOR_PIN;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import ca.warp7.robot.autonomous.AutonomousBase;
import ca.warp7.robot.autonomous.SwagDrive;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ControllerSettings;
import ca.warp7.robot.hardware.controlerSettings.DefaultControls;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Warp7Robot extends SampleRobot {

    private ControllerSettings controls;
    private Compressor compressor;
    private PowerDistributionPanel pdp;
    private int camera_session;
    private Image camera_frame;
    public static Shooter shooter;
    public static Intake intake;
    public static Drive drive;
    public static Climber climber;
    public static DriverStation driverStation;
    private AutonomousBase auto;
    private int counter;
    private DataPool _pool;

    public void robotInit() {
        System.out.println("hello i am robit");
        _pool = new DataPool("Robot");
        NetworkTable.setUpdateRate(0.050);
        driverStation = DriverStation.getInstance();
        try {
            camera_frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

            // the camera name (ex "cam0") can be found through the roborio web
            // interface
            camera_session = NIVision.IMAQdxOpenCamera("cam0",
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(camera_session);
            NIVision.IMAQdxStartAcquisition(camera_session);
        } catch (Exception ignored) {
        }

        compressor = new Compressor(COMPRESSOR_PIN);


        shooter = new Shooter();
        drive = new Drive();
        intake = new Intake();
        climber = new Climber();

        pdp = new PowerDistributionPanel();

        XboxController driver = new XboxController(0);
        XboxController operator = new XboxController(1);
        controls = new DefaultControls(driver, operator, compressor);

        // auto = new IntakeFirst(drive, shooter, intake);
//		auto = new BatteryFirst(drive, shooter, intake);
        auto = new SwagDrive();
        // auto = new IntakeForwardIntakesUp(drive, shooter, intake);
//		 auto = new Rotato(drive, shooter, intake);
        // auto = new SpybotHardstop(drive, shooter, intake);

    }

    public void operatorControl() {
        controls.reset();
        intake.reset();
        if(driverStation.isFMSAttached()) {
            compressor.setClosedLoopControl(false);
        } else {
            compressor.setClosedLoopControl(true);
        }
        while (isOperatorControl() && isEnabled()) {
            controls.periodic();
            allEnabledLoop();
            slowLoop();
            Timer.delay(0.005);
        }
    }

    public void autonomous() {
        while (isAutonomous() && isEnabled()) {
            auto.periodic(drive, shooter, intake);
            allEnabledLoop();
            slowLoop();
            Timer.delay(1 / 50); // 1/50 second delay
        }
    }

    public void disabled() {
        while (!isEnabled()) {
            shooter.stop();
            drive.stop();
            climber.stop();
            intake.stop(); // TODO Investigate these, seem pointless
            slowLoop();
            Timer.delay(0.005);
        }
    }

    private void allEnabledLoop() {
        shooter.periodic();
        intake.periodic();
    }

    private void slowLoop() {
        if (counter++ >= 10) {
            try {
                NIVision.IMAQdxGrab(camera_session, camera_frame, 1);
                CameraServer.getInstance().setImage(camera_frame);
            } catch (Exception ignored) {
            }

            counter = 0;
            intake.slowPeriodic();
            drive.slowPeriodic();
            shooter.slowPeriodic();
            _pool.logBoolean("compressor", compressor.getClosedLoopControl());
            _pool.logBoolean("direction", drive.isDrivetrainReversed());
            _pool.logData("hotbot", pdp.getTemperature());
            DataPool.collectAllData();
        }
    }
}