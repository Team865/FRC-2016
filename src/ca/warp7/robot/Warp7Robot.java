package ca.warp7.robot;

import static ca.warp7.robot.Constants.COMPRESSOR_PIN;


import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import ca.warp7.robot.autonomous.AutonomousBase;
import ca.warp7.robot.autonomous.BatteryFirst;
import ca.warp7.robot.autonomous.SwagDrive;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ControllerSettings;
import ca.warp7.robot.hardware.controlerSettings.DefaultControls;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.networking.GUITableListener;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Warp7Robot extends SampleRobot {

	public XboxController driver; // set to ID 1 in DriverStation
	public XboxController operator; // set to ID 2 in DriverStation
	public ControllerSettings controls;
	public Compressor compressor;
	public PowerDistributionPanel pdp;
	int camera_session;
	Image camera_frame;
	public static Shooter shooter;
	public static Intake intake;
	public static Drive drive;
	public static Climber climber;
	private AutonomousBase auto;
	// private static boolean visionTablesUpdated = false;
	private static NetworkTable visionTable;
	public static double autonID;
	public static double autonDistance;
	public static double autonAngle;
	private int counter;
	private DataPool _pool;

	public void robotInit() {
		System.out.println("hello i am robit");
		_pool = new DataPool("Robot");
		try {
			camera_frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

			// the camera name (ex "cam0") can be found through the roborio web
			// interface
			camera_session = NIVision.IMAQdxOpenCamera("cam0",
					NIVision.IMAQdxCameraControlMode.CameraControlModeController);
			NIVision.IMAQdxConfigureGrab(camera_session);
			NIVision.IMAQdxStartAcquisition(camera_session);
		} catch (Exception e) {
		}

		visionTable = NetworkTable.getTable("vision");
		visionTable.addTableListener(new GUITableListener());

		compressor = new Compressor(COMPRESSOR_PIN);
		compressor.setClosedLoopControl(true);

		shooter = new Shooter();
		drive = new Drive(compressor);
		intake = new Intake();
		climber = new Climber();

		pdp = new PowerDistributionPanel();

		driver = new XboxController(0);
		operator = new XboxController(1);
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
		compressor.setClosedLoopControl(false);
		while (isOperatorControl() && isEnabled()) {
			controls.periodic();
			allEnabledLoop();
			allLoop();
			Timer.delay(0.005);
		}
	}

	public void autonomous() {
		while (isAutonomous() && isEnabled()) {
			auto.periodic(drive, shooter, intake);
			allEnabledLoop();
			allLoop();
			Timer.delay(0.01); // lower dT for auton
		}
	}

	public void disabled() {
		while (!isEnabled()) {
			shooter.stop();
			drive.stop();
			climber.stop();
			intake.stop(); // TODO Investigate these, seem pointless
			allLoop();
			Timer.delay(0.005);
		}
	}

	private void allEnabledLoop() {
		shooter.periodic();
		intake.periodic();
	}

	private void allLoop() {
		if (counter++ >= 10) {
			try {
				NIVision.IMAQdxGrab(camera_session, camera_frame, 1);
				CameraServer.getInstance().setImage(camera_frame);
			} catch (Exception e) {
			}

			counter = 0;
			shooter.slowPeriodic();
			intake.slowPeriodic();
			drive.slowPeriodic();
			_pool.logBoolean("compressor", compressor.getClosedLoopControl());
			_pool.logBoolean("direction", drive.getDirection());
			_pool.logData("hotbot", pdp.getTemperature());
			DataPool.collectAllData();
		}
	}
}