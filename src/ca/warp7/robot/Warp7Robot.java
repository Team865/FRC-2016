package ca.warp7.robot;

import java.util.ArrayList;
import java.util.Collection;

import ca.warp7.robot.autonomous.TestAutonomous;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.controlerSettings.ChandlerDefault;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intakes;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

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
		if(driver.getRightBumperbutton()){
			piston.set(true);
		}else{
			piston.set(false);
		}

		if(operator.getRightBumperbutton()){
			System.out.println("getAngle " + gyro.getAngle());
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
			if(driver.getRightTrigger() <= 0.5){
				intakes.intake(false);
			}else{
				intakes.intake(photosensor.get());
			}
		}else{
			intakes.stop();
		}
		
		if(driver.getBbutton()){
			intakes.outake();
		}
		
		if(driver.getXbutton()){
			intakes.stop();
		}
		
		if(driver.getYbutton()) speed = 1.0;
		
		speed = Math.max(-1, Math.min(1, speed));
		
		if(driver.getRightTrigger() <= 0.5) speed = 0.0;
		
		if(speed != 0){
			System.out.println(speed);
		}
		flyWheel.set(speed);
		
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
        	Drive.cheesyDrive(rightGearBox, leftGearBox);
            Timer.delay(0.005);		// wait for a motor update time
        }
    }
    
    public void autonomous() {
    	double speed = 0.0;
    	while(isAutonomous() && !isOperatorControl() && isEnabled()){
    		speed = TestAutonomous.sinAuto(flyWheel, speed);
    	}
    }

	public void disabled(){
    	while(!isEnabled()){
    		flyWheel.set(0);
    		rightGearBox.set(0);
    		leftGearBox.set(0);
    		intakes.stop();
    	}
    }
	
	public static XboxController driver;   // set to ID 1 in DriverStation
	public static XboxController operator; // set to ID 2 in DriverStation
    TalonSRX flyWheel;
    GearBox rightGearBox;
    GearBox leftGearBox;
    Intakes intakes;
    Solenoid piston;
    ADXRS453Gyro gyro;
    DigitalInput photosensor;
	
	private void initRobot(){
		
		flyWheel = new TalonSRX(Constants.SHOOTER_FLY_WHEEL);
    	leftGearBox = new GearBox(Talon.class, Constants.LEFT_DRIVE_MOTORS);
    	rightGearBox = new GearBox(Talon.class, Constants.RIGHT_DRIVE_MOTORS);
    	
    	intakes = new Intakes(new Victor(Constants.INTAKE_MOTOR));
    	
    	piston = new Solenoid(0);
    	
    	photosensor = new DigitalInput(0);
    	
    	gyro = new ADXRS453Gyro();
    	gyro.startThread();
	}
}