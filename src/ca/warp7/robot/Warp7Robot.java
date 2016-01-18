package ca.warp7.robot;

import ca.warp7.robot.autonomous.TestAutonomous;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.subSystems.Drive;
import ca.warp7.robot.subSystems.Intakes;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class Warp7Robot extends SampleRobot {
    
    private double speed;
    private double speed2;
    private boolean increased;
    
    
    public Warp7Robot(){
    	initRobot();

    	speed = 0.0;
		speed2 = 0.0;
    	increased = false;
    }
	
	private void controls(){
		if(driver.getRightBumperbutton()){
			piston.set(true);
		}else{
			piston.set(false);
		}

		if(driver.getLeftBumperbutton()){
			System.out.println("getAngle " + gyro.getAngle());
			System.out.println(gyro.getID());
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
		
		if(driver.getYbutton()) speed = 1.0;
		
		speed = Math.max(-1, Math.min(1, speed));
		
		if(driver.getRightTrigger() <= 0.5) speed = 0.0;
		
		System.out.println(speed);
		
		motorA.set(speed);
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
    		speed = TestAutonomous.sinAuto(motorA, speed);
    	}
    }

	public void disabled(){
    	while(!isEnabled()){
    		motorA.set(0);
    		motorB.set(0);
    		rightGearBox.set(0);
    		leftGearBox.set(0);
    		intakes.stop();
    	}
    }
	
	public static XboxController driver;   // set to ID 1 in DriverStation
	public static XboxController operator; // set to ID 2 in DriverStation
    Victor motorA;
    Victor motorB;
    GearBox rightGearBox;
    GearBox leftGearBox;
    Intakes intakes;
    Solenoid piston;
    ADXRS453Gyro gyro;
	
	private void initRobot(){
		driver = new XboxController(0);
        operator = new XboxController(1);
		
		motorA = new Victor(Constants.SHOOTER_FLY_WHEEL);
    	motorB = new Victor(Constants.SHOOTER_HOOD_MOTOR);    
    	
    	leftGearBox = new GearBox(Constants.LEFT_DRIVE_MOTORS, Constants.LEFT_DRIVE_MOTOR_TYPES);
    	rightGearBox = new GearBox(Constants.RIGHT_DRIVE_MOTORS, new char[]{Constants.TALON, Constants.TALON});
    	
    	intakes = new Intakes(new Victor(Constants.INTAKE_MOTOR));
    	
    	piston = new Solenoid(0);
    	
    	gyro = new ADXRS453Gyro();
    	gyro.startThread();
	}
}