package ca.warp7.robot;

import ca.warp7.robot.autonomous.TestAutonomous;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ChandlerDefault;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intakes;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Warp7Robot extends SampleRobot {
    
    private double speed;
    private double degrees;
    private boolean increased;
    private boolean changed;
    
    
    public Warp7Robot(){
    	driver = new XboxController(0, new ChandlerDefault());
        operator = new XboxController(1, new ChandlerDefault());
        
    	initRobot();

    	speed = 0.0;
    	degrees = 0.0;
    	increased = false;
    }
	
	private void controls(){
		if(driver.getAbutton()){
			Intakes.moveAdjustingArm(false);
		}else{
			if(Intakes.adjustedArmRetracted()){
				//TODO Intakes.moveAdjustingArm(true);
			}
		}
				
		if(driver.getXbutton()){
			speed = 0.4;
		}
		
		if(driver.getDPad() == 0){
			if(!increased){
				speed += 0.01;
				increased = true;
			}
		}
		
		if(driver.getDPad() == 180){
			if(!increased){
				speed -= 0.01;
				increased = true;
			}
		}
		
		if(driver.getDPad() == 90){
			if(!increased){
				degrees += 0.05;
				increased = true;
			}
		}
		
		if(driver.getDPad() == 270){
			if(!increased){
				degrees -= 0.05;
				increased = true;
			}
		}
		
		if(driver.getDPad() == -1){
			increased = false;
		}
		
		if(driver.getLeftTrigger() >= 0.5){
			if(driver.getRightBumperbutton()){
				Intakes.intake(false);
			}else{
				Intakes.intake(photosensor.get());
				//Intakes.intake(false);
			}
		}else{
			Intakes.stop();
		}
		
		if(driver.getBbutton()){
			Intakes.outake();
		}
		
		if(driver.getYbutton()) speed = 0.75;
		
		speed = Math.max(-1, Math.min(1, speed));
		degrees = Math.max(0, Math.min(90, degrees));
		
		if(driver.getRightTrigger() <= 0.5) speed = 0.0;
		if(driver.getStartButton()) degrees = 0.0;
		
		if(speed != 0){
			System.out.println(speed);
		}
		if(degrees != 0){
			System.out.println(degrees);
		}
		Shooter.set(speed);
		Shooter.setHood(degrees);
		
		if(driver.getRightStickButton()){
			if(!changed){
				Drive.changeDirection();
				changed = true;
			}
		}else{
			changed = false;
		}
		
		if(driver.getLeftStickButton()){
			if(!changed){
				Drive.changeGear();
			}
		}
		
		
		
		if(operator.getBbutton()){
			System.out.println(Shooter.getPosition());
		}

		if(operator.getRightBumperbutton()){
			System.out.println("getAngle " + gyro.getAngle());
		}
		
		if(operator.getLeftBumperbutton()){
			System.out.println(gyro.getStatus());
		}
		if(operator.getStartButton()){
			gyro.calibrate();
		}
		if(operator.getBackButton()){
			gyro.stopCalibrating();
		}
		if(operator.getAbutton()){
			if(gyro.isCalibrating()){
				System.out.println("Gyro is calibrating");
			}else{
				System.out.println("Gyro is not calibrating");
			}
		}
	}
	
	
	
	
	
	//============================
	//|| BECAUSE IT LOOKS NICER ||    <------------------- What he said...
	//============================
	
	
	
	
	public void operatorControl() {
		//TODO the moving of the arm is only temperary remove this for comp
		Intakes.moveInitialArm(true);
        while (isOperatorControl() && isEnabled()) {
        	controls();
        	Drive.cheesyDrive();
            Timer.delay(0.005);		// wait for a motor update time
        }
    }
    
    public void autonomous() {
    	double distance = 0.0;
    	while(isAutonomous() && !isOperatorControl() && isEnabled()){
    		distance = TestAutonomous.sinAuto(distance);
    	}
    }

	public void disabled(){
    	while(!isEnabled()){
    		Shooter.stop();
    		Drive.stop();
    		Intakes.stop();
    		
    		//System.out.println("Robot Disabled!!!!!");
    	}
    }
	
	public static XboxController driver;   // set to ID 1 in DriverStation
	public static XboxController operator; // set to ID 2 in DriverStation
    public static ADXRS453Gyro gyro;
    public static DigitalInput photosensor;
    CameraServer server;
	
	private void initRobot(){
		server = CameraServer.getInstance();
        server.setQuality(50);
        //the camera name (ex "cam0") can be found through the roborio web interface
        server.startAutomaticCapture("cam1");
		
    	Shooter.init(new CANTalon(Constants.SHOOTER_CAN_ID), new Encoder(Constants.FLY_ENC_A, Constants.FLY_ENC_B),
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
}