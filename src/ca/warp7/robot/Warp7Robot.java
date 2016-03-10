package ca.warp7.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;

import ca.warp7.robot.autonomous.BatteryFirst;
import ca.warp7.robot.autonomous.AutonomousBase;
import ca.warp7.robot.autonomous.IntakeFirst;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ControllerSettings;
import ca.warp7.robot.hardware.controlerSettings.Default;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.networking.GUITableListener;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Warp7Robot extends SampleRobot {

	private static XboxController driver;   // set to ID 1 in DriverStation
    private static XboxController operator; // set to ID 2 in DriverStation
    private static ADXRS453Gyro gyro;
    private static DigitalInput photosensor;
    private static ControllerSettings controls;
    private static Compressor compressor;
    int camera_session;
    Image camera_frame;
	private Shooter shooter;
	private Intake intake;
	private Drive drive;
	private Climber climber;
	private AutonomousBase auto;
	
	
	private static NetworkTable autonTable;
    private static String messageBuffer = "";
    private static String warningBuffer = "";
    private static int robotMode = 0;
    private static boolean visionTablesUpdated = false;
    private static boolean updateRobotTables = false;
    private static NetworkTable robotTable;
    private static NetworkTable visionTable;
    public static double autonID;
    
    
    public Warp7Robot(){
        driver = new XboxController(0);
        operator = new XboxController(1);
        controls = new Default();
        initRobot();
    }

    public void operatorControl() {
    	controls.init(drive);
    	intake.reset();
    	compressor.setClosedLoopControl(false);
        while (isOperatorControl() && isEnabled()) {
            controls.periodic(driver, operator, gyro, shooter, intake, drive, photosensor, climber, compressor);
            controls.drive(driver, operator);
            allEnabledLoop();
            allLoop();
            Timer.delay(0.005);
        }
    }

    public void autonomous() {
    	//auto = new IntakeFirst(drive, shooter, intake);
    	auto = new BatteryFirst(drive, shooter, intake);
    	
    	
        while(isAutonomous() && !isOperatorControl() && isEnabled()){
        	auto.periodic(drive, shooter, intake);
            allEnabledLoop();
            allLoop();
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

   private void allEnabledLoop(){
   		shooter.periodic(controls.getWantedRPM(), controls.isFiring(), drive);
   }
   
   private void allLoop() {
       try {
           NIVision.IMAQdxGrab(camera_session, camera_frame, 1);
           CameraServer.getInstance().setImage(camera_frame);
       } catch (Exception e) {}
       if(updateRobotTables) {
           if(!messageBuffer.isEmpty()) robotTable.putString("messages", messageBuffer);
           if(!warningBuffer.isEmpty()) robotTable.putString("warnings", Timer.getFPGATimestamp() + ": " + warningBuffer);
           robotTable.putNumber("mode", (double) robotMode);
           updateRobotTables = false;
           DataPool.collectAllData();
       }
       
       controls.logs(shooter);
   }
   
    private void initRobot(){
        try {
            camera_frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);

            // the camera name (ex "cam0") can be found through the roborio web interface
            camera_session = NIVision.IMAQdxOpenCamera("cam1",
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            NIVision.IMAQdxConfigureGrab(camera_session);
            NIVision.IMAQdxStartAcquisition(camera_session);
        } catch (Exception e) {
        }
        autonTable = NetworkTable.getTable("autonSelect");
        autonID = 0;
        autonTable.addTableListener(new GUITableListener());
        visionTable = NetworkTable.getTable("vision");
        robotTable = NetworkTable.getTable("status");

        compressor = new Compressor(Constants.COMPRESSOR_PIN);
        compressor.setClosedLoopControl(true);
    
        shooter = new Shooter(new CANTalon(Constants.SHOOTER_CAN_ID), new Encoder(Constants.FLY_ENC_A, Constants.FLY_ENC_B),
                new GearBox(Constants.FLY_WHEEL_PIN, Constants.FLY_WHEEL_MOTOR_TYPE));
        drive = new Drive(new GearBox(Constants.RIGHT_DRIVE_MOTOR_PINS, Constants.RIGHT_DRIVE_MOTOR_TYPES),
                new GearBox(Constants.LEFT_DRIVE_MOTOR_PINS, Constants.LEFT_DRIVE_MOTOR_TYPES),
                new Solenoid(Constants.GEAR_CHANGE), new Solenoid(Constants.PTO), compressor);
        intake = new Intake(new GearBox(Constants.INTAKE_MOTOR, Constants.INTAKE_MOTOR_TYPES),
                new Solenoid(Constants.INTAKE_PISTON_A), new Solenoid(Constants.INTAKE_PISTON_B));
        climber = new Climber();
        
        
        photosensor = new DigitalInput(Constants.INTAKE_PHOTOSENSOR);

        gyro = new ADXRS453Gyro();
        gyro.startThread();

    }

    public static void logMessage(String msg) {
        messageBuffer += msg;
        updateRobotTables = true;
    }

    public static void logWarning(String msg) {
        warningBuffer += msg;
        updateRobotTables = true;
    }

    public static void changeMode(Mode mode) {
        robotMode = mode.code;
        updateRobotTables = true;
    }
}