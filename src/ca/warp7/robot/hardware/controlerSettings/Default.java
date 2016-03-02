package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.ADXRS453Gyro;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.XboxController.RumbleType;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.DigitalInput;

public class Default extends ControllerSettings{
    private static boolean changedA;
    private static boolean changedRT;
    private static boolean changedLT;
    private static boolean changedRB;
    private static boolean changedRS;
    private static boolean changedStart;
    
    private static boolean O_ChangedRT;
    private static boolean O_ChangedLT;
    private static boolean O_ChangedX;
    private static boolean O_ChangedY;
    private static boolean O_ChangedB;
    private static boolean O_ChangedRB;
    private static boolean O_ChangedA;
    private static boolean O_ChangedLB;
    
    private static boolean climbAccessGranted;
    private static double hoodSpeed;
    private static boolean firing;
    
    @Override
    public void init(Drive drive){
    	changedA  = false;
    	changedRT = false;
    	changedLT = false;
    	changedRB = false;
    	changedRS = false;
    	changedStart = false;
    	
    	O_ChangedX = false;
    	O_ChangedY = false;
    	O_ChangedB = false;
    	O_ChangedRT = false;
    	O_ChangedLT = false;
    	O_ChangedRB = false;
    	O_ChangedA = false;
    	O_ChangedLB = false;
    	
    	climbAccessGranted = false;
    	hoodSpeed = 0.0;
    	firing = false;
    	
    	drive.setDirection(Constants.BATTERY);
    }
    
    @Override
	public void periodic(XboxController driver, XboxController operator, ADXRS453Gyro gyro, Shooter shooter,
			Intake intake, Drive drive, DigitalInput photosensor, Climber climber){
		//Toggles the long piston
    	if(driver.getAbutton()){
			if(!changedA){
				intake.toggleAdjustingArm();
				changedA = true;
			}
		}else{
			if(changedA)changedA = false;
		}
		
    	//holding left trigger intakes holding right trigger out-takes
		if(driver.getRightTrigger() >= 0.5 || driver.getLeftTrigger() >= 0.5){
			if(driver.getLeftTrigger() >= 0.5 && driver.getRightTrigger() < 0.5){
					if(!firing){
						intake.intake(photosensor.get());
						changedLT = true;
						changedRT = false;
					}else{
						intake.intake(true);
						changedLT = true;
						changedRT = false;
					}
			}
			if(driver.getRightTrigger() >= 0.5 && driver.getLeftTrigger() < 0.5){
				if(!changedRT){
					intake.outake();
					changedLT = false;
					changedRT = true;
				}
			}
		}else{
			if(changedLT || changedRT){
				intake.stopIntake();
				changedLT = false;
				changedRT = false;
			}
		}
		
		//hold to change gears for driving let go and it goes back
		if(driver.getRightBumperbutton()){
			if(!changedRB){
				drive.changeGear();
				changedRB = true;
			}
		}else{
			if(changedRB){
				drive.changeGear();
				changedRB = false;
			}
		}
		// press to toggle which direction is front
		if(driver.getRightStickButton()){
			if(!changedRS){
				drive.changeDirection();
				changedRS = true;
			}
		}else{
			if(changedRS)changedRS = false;
		}
		
		// hold to allow for climb
		if(driver.getStartButton()){
			if(!changedStart){
				climbAccessGranted = true;
				operator.setRumble(RumbleType.kLeftRumble, 1);
				operator.setRumble(RumbleType.kRightRumble, 1);
				changedStart = true;
			}
		}else{
			if(changedStart){
				operator.setRumble(RumbleType.kLeftRumble, 0);
				operator.setRumble(RumbleType.kRightRumble, 0);
				climbAccessGranted = false;
				changedStart = false;
			}
		}
	
		//=============================================//
		
		// makes the intake button for chandler shooting and only allows for it when the rpm is within 150
		// also it moves forward during this period and moves the hood against the hardstop
		if(operator.getRightTrigger() >= 0.5){
			if(!O_ChangedRT){
			shooter.fireAccessGranted();
			O_ChangedRT = true;
			}
		}else{
			if(O_ChangedRT){
				shooter.fireAccessDenied();
				O_ChangedRT = false;
			}
		}
		
		//hold to be doing hardstop mode
		if(operator.getLeftTrigger() >= 0.5){
			if(!O_ChangedLT){
				shooter.hardStop(true);
				O_ChangedLT = true;
			}
		}else{
			if(O_ChangedLT){
				shooter.hardStop(false);
				O_ChangedLT = false;
			}
		}
		
		//hold to allow rev up
		if(operator.getRightBumperbutton() && shooter.atTargetRPM()){
			if(!O_ChangedRB){
				driver.setRumble(RumbleType.kLeftRumble, 1);
				driver.setRumble(RumbleType.kRightRumble, 1);
				firing = true;
				O_ChangedRB = true;
			}
		}else{
			if(O_ChangedRB){
				shooter.setHood(0);
				driver.setRumble(RumbleType.kLeftRumble, 0);
				driver.setRumble(RumbleType.kRightRumble, 0);
				firing = false;
				O_ChangedRB = false;
			}
		}
		
		//ignore this for now
		//TIME TO START THE END OF TEH ROBIT SO WE NED DA SAFTEY TO MAKE SURE NO IDIOT STARTS CLIMBING
		if(operator.getLeftBumperbutton() && operator.getRightBumperbutton() && climbAccessGranted){
			if(operator.getXbutton()){
				if(!O_ChangedX){
					climber.firstStage();
					O_ChangedX = true;
				}
			}
			
			if(operator.getYbutton()){
				if(!O_ChangedY && O_ChangedX){
					climber.secondStage();
					O_ChangedY = true;
				}
			}
			
			if(operator.getBbutton()){
				if(!O_ChangedB && O_ChangedY && O_ChangedX){
					climber.finalStage();
					O_ChangedB = true;
				}
			}
		}
		
		hoodSpeed = 0.2;
		
		if(operator.getAbutton()){
			hoodSpeed = -0.2;
		}
		
		if(operator.getLeftBumperbutton()){
			hoodSpeed = -0.2;
			if(!O_ChangedLB){
				intake.raisePortculus(true);
				O_ChangedLB = true;
			}
		}else{
			if(O_ChangedLB){
				intake.raisePortculus(false);
				O_ChangedLB = false;
			}
		}
		
		shooter.setHood(hoodSpeed);
	}


	@Override
	public void drive(XboxController driver, XboxController operator) {
			Drive.cheesyDrive(driver.getLeftY(), driver.getRightX(), driver.getLeftBumperbutton());
		}

	@Override
	public void logs(Shooter shooter) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getWantedRPM() {
		//TODO do something along the lines of mathy
		return 0.0;
	}

	@Override
	public boolean isFiring() {
		return firing;
	}
}
