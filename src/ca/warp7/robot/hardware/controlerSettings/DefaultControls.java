package ca.warp7.robot.hardware.controlerSettings;


import static ca.warp7.robot.Warp7Robot.drive;
import static ca.warp7.robot.Warp7Robot.intake;
import static ca.warp7.robot.Warp7Robot.shooter;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.XboxController.RumbleType;
import edu.wpi.first.wpilibj.Compressor;

public class DefaultControls extends ControllerSettings {
	private static boolean changedA;
	private static boolean changedRS;
	private static boolean O_ChangedLB;
	private static boolean O_ChangedBack;
	private static boolean O_ChangedStart;
	private static boolean O_ChangedX;
	private static boolean O_ChangedB;

	private static boolean O_DPadLeft;
	private static boolean O_DPadRight;
	
	private static double hoodSpeed;
	private static boolean firing;
	private XboxController driver;
	private XboxController operator;
	private Compressor compressor;
	boolean disableHood;

	public DefaultControls(XboxController driver, XboxController operator, Compressor compressor) {
		this.driver = driver;
		this.operator = operator;
		this.compressor = compressor;
	}
	
	public void reset() {
		changedA = false;
		changedRS = false;
		O_ChangedLB = false;
		O_ChangedBack = false;
		O_ChangedStart = false;
		O_ChangedB = false;
		O_ChangedX = false;
		O_DPadLeft = false;
		O_DPadRight = false;
		
		hoodSpeed = 0.0;
		firing = false;
		disableHood = false;
		drive.setDrivetrainReversed(false);
	}

	@Override
	public void periodic() {
		/*
        if(!driverStation.isFMSAttached()) { // if we're not fms controlled
            if (operator.getLeftTrigger() < 0.5) {
                shooter.stop();
                drive.stop();
                intake.stopIntake();
                return; // stop doing thing if our deadman's switch isn't held.
            }
        }*/

        if (driver.getLeftTrigger() >= 0.5) {
            if (!firing) {
                intake.intake();
            } else {
                intake.fireBall();
            }
        } else if (driver.getRightTrigger() >= 0.5) { // outtake
            intake.outake();
        } else {
            intake.stopIntake();
        }

		// Toggles the long piston
		if (driver.getAbutton()) {
			if (!changedA) {
				if(!operator.getBbutton()){
					intake.toggleAdjustingArm();
				}
				intake.initialBackAdjustingForward(false);
				changedA = true;
			}
		} else {
			if (changedA)
				changedA = false;
		}
		
		if(operator.getBbutton()){
			if(!O_ChangedB){
				intake.initialBackAdjustingForward(true);
				O_ChangedB = true;
			}
		}else {
            if (O_ChangedB){
                O_ChangedB = false;
            intake.initialBackAdjustingForward(false);
        }
		}

		// hold to change gears for driving let go and it goes back
		drive.setGear(driver.getRightBumperbutton());

		// press to toggle which direction is front
		if (driver.getRightStickButton()) {
			if (!changedRS) {
				drive.setDrivetrainReversed(!drive.isDrivetrainReversed());
				changedRS = true;
			}
		} else {
			if (changedRS)
				changedRS = false;
		}
		hoodSpeed = 0.2; // drive up by default

		firing = false;
		if (driver.getBbutton()) {
			shooter.spinUp(Constants.LARGE_RPM);
			hoodSpeed = 0.3;
			if (shooter.atTargetRPM()) {
				firing = true;
				driver.setRumble(RumbleType.kRightRumble, 0.5f);
				driver.setRumble(RumbleType.kLeftRumble, 0.5f);
			} else {
				driver.setRumble(RumbleType.kRightRumble, 0.0f);
				driver.setRumble(RumbleType.kLeftRumble, 0.0f);
			}
		}else if(driver.getBbutton()){ 
			shooter.spinUp(Constants.SMALL_RPM);
			hoodSpeed = 0.3;
			if (shooter.atTargetRPM()) {
				firing = true;
				driver.setRumble(RumbleType.kRightRumble, 0.5f);
				driver.setRumble(RumbleType.kLeftRumble, 0.5f);
			} else {
				driver.setRumble(RumbleType.kRightRumble, 0.0f);
				driver.setRumble(RumbleType.kLeftRumble, 0.0f);
			}
		}else {
		
			shooter.stop();
			driver.setRumble(RumbleType.kRightRumble, 0.0f);
			driver.setRumble(RumbleType.kLeftRumble, 0.0f);
		}
		
		// Climber controls start
		// pto
		if(operator.getRightBumperbutton()){
			drive.setPTO(true);
		}else{
			drive.setPTO(false);
		}
		//stage 1
		if(operator.getDPad() == -90){
			if(!O_DPadLeft){
				intake.toggleT1();
				O_DPadLeft = true;
			}
		}else{
			if(O_DPadLeft){
				O_DPadLeft = false;
			}
		}
		//stage 2
		if(operator.getDPad() == 90){
			if(!O_DPadRight){
				intake.toggleT2();
				O_DPadRight = true;
			}
		}else{
			if(O_DPadRight){
				O_DPadRight = false;
			}
		}
		// Climber controls end
		
		if(operator.getXbutton()){
			if(!O_ChangedX){
				shooter.setLight(true);
				O_ChangedX = true;
			}
		}else{
			//if(O_ChangedX){
				shooter.setLight(false);
				O_ChangedX = false;
			//}
		}
		
		if (operator.getAbutton()) {
			hoodSpeed = -0.2; // a to down
		}

		if (operator.getLeftBumperbutton()) {
			if (!O_ChangedLB) {
				intake.raisePortculus(true);
				O_ChangedLB = true;
			}
		} else {
			if (O_ChangedLB) {
				intake.raisePortculus(false);
				O_ChangedLB = false;
			}
		}
		if(operator.getStartButton()) {
			if (!O_ChangedStart) {
				disableHood = !disableHood;
				O_ChangedStart = true;
			}
		} else {
			if(O_ChangedStart) {
				O_ChangedStart = false;
			}
		}
		if(disableHood) {
			hoodSpeed = 0;
		}
		 shooter.setHood(hoodSpeed);

		if (operator.getBackButton()) {
			if (!O_ChangedBack) {
				compressor.setClosedLoopControl(!compressor.getClosedLoopControl());
				O_ChangedBack = true;
			}
		} else {
			if (O_ChangedBack)
				O_ChangedBack = false;
		}

		drive.cheesyDrive(driver.getRightX(), driver.getLeftY(), driver.getLeftBumperbutton());
//		drive.tankDrive(driver.getLeftY(), driver.getRightY());
	}
    @Override
    public void disabled() {
        driver.setRumble(RumbleType.kLeftRumble, 0);
        driver.setRumble(RumbleType.kRightRumble, 0);
    }
}
