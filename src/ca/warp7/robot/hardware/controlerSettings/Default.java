package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.Constants;
import ca.warp7.robot.Util;
import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.XboxController.RumbleType;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;

public class Default extends ControllerSettings {
	private static boolean changedA;
	private static boolean changedRS;
	private static boolean O_ChangedLB;
	private static boolean O_ChangedBack;

	private static double hoodSpeed;
	private static boolean firing;

	@Override
	public void init(Drive drive) {
		changedA = false;
		changedRS = false;
		O_ChangedLB = false;
		O_ChangedBack = false;

		hoodSpeed = 0.0;
		firing = false;

		drive.setDirection(Constants.BATTERY);
	}

	@Override
	public void periodic(XboxController driver, XboxController operator, Shooter shooter, Intake intake, Drive drive,
			DigitalInput photosensor, Climber climber, Compressor compressor) {
		driver.setRumble(RumbleType.kRightRumble, 0.0f);

		// Toggles the long piston
		if (driver.getAbutton()) {
			if (!changedA) {
				intake.toggleAdjustingArm();
				changedA = true;
			}
		} else {
			if (changedA)
				changedA = false;
		}

		// hold to change gears for driving let go and it goes back
		drive.setGear(driver.getRightBumperbutton());

		double throttle = driver.getLeftY();

		// press to toggle which direction is front
		if (driver.getRightStickButton()) {
			if (!changedRS) {
				drive.changeDirection();
				changedRS = true;
			}
		} else {
			if (changedRS)
				changedRS = false;
		}
		hoodSpeed = 0.2; // drive up by default

		firing = false;
		if (operator.getRightTrigger() >= 0.5) {
			throttle = Util.limit(throttle, 0.2);
			shooter.spinUp();
			hoodSpeed = 0.5;
			if (shooter.atTargetRPM()) {
				firing = true;
				driver.setRumble(RumbleType.kRightRumble, 0.5f);
			}
		} else {
			shooter.stop();
		}

		if (driver.getLeftTrigger() >= 0.5) {
			if (!firing) {
				intake.intake(photosensor.get());
			} else {
				intake.fireBall();
			}
		} else if (driver.getRightTrigger() >= 0.5) { // outtake
			intake.outake();
		} else {
			intake.stopIntake();
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
		drive.cheesyDrive(driver.getRightX(), throttle, driver.getLeftBumperbutton());
	}
}
