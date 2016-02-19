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
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;

public class Warp7Robot extends SampleRobot {
    
    private double speed;
    private boolean increased;
    private boolean changed;
    
    
    public Warp7Robot(){
    	driver = new XboxController(0, new ChandlerDefault());
        operator = new XboxController(1, new ChandlerDefault());
        
    	initRobot();

    	speed = 0.0;
    	increased = false;
    }
	
	private void controls(){
		if(operator.getBbutton()){
			System.out.println(Shooter.getPosition());
		}
		
		if(driver.getRightBumperbutton()){
			piston.set(true);
		}else{
			piston.set(false);
		}

		if(operator.getRightBumperbutton()){
			System.out.println("getAngle " + gyro.getAngle());
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
		
		if(driver.getRightTrigger() <= 0.5) speed = 0.0;
		
		if(speed != 0){
			System.out.println(speed);
		}
		Shooter.set(speed);
		
		if(driver.getRightStickButton()){
			if(!changed){
				Drive.changeDirection();
				changed = true;
			}
		}else{
			changed = false;
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
    Solenoid piston;
    ADXRS453Gyro gyro;
    DigitalInput photosensor;
    public DigitalInput flyWheelSensor;
    Encoder enc;
	
	private void initRobot(){
    	Shooter.init(new CANTalon(Constants.SHOOTER_CAN_ID), enc = new Encoder(Constants.FLY_ENC_A, Constants.FLY_ENC_B));
    	Drive.init(new GearBox(Constants.RIGHT_DRIVE_MOTOR_PINS, Constants.RIGHT_DRIVE_MOTOR_TYPES),
    			   new GearBox(Constants.LEFT_DRIVE_MOTOR_PINS, Constants.LEFT_DRIVE_MOTOR_TYPES));
    	Intakes.init(new GearBox(new int[]{Constants.INTAKE_MOTOR}, new char[]{Constants.VICTOR}));
    	
    	piston = new Solenoid(Constants.PISTON);
    	
    	photosensor = new DigitalInput(Constants.INTAKE_PHOTOSENSOR);
    	
    	gyro = new ADXRS453Gyro();
    	gyro.startThread();
	}
}