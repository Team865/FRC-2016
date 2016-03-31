package ca.warp7.robot.hardware.controlerSettings;


import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.hardware.XboxController.RumbleType;
import edu.wpi.first.wpilibj.Compressor;

import static ca.warp7.robot.Warp7Robot.*;

public class DefaultControls extends ControllerSettings {
	private static boolean changedA;
	private static boolean changedRS;
	private static boolean O_ChangedLB;
	private static boolean O_ChangedBack;

	private static double hoodSpeed;
	private static boolean firing;
	private XboxController driver;
	private XboxController operator;
	private Compressor compressor;

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

		hoodSpeed = 0.0;
		firing = false;

		drive.setDrivetrainReversed(false);
	}

	@Override
	public void periodic() {
        if(!driverStation.isFMSAttached()) { // if we're not fms controlled
            if (operator.getLeftTrigger() < 0.5) {
                shooter.stop();
                drive.stop();
                intake.stopIntake();
                return; // stop doing thing if our dead man's switch isn't held.
            }
        }

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
				intake.toggleAdjustingArm();
				changedA = true;
			}
		} else {
			if (changedA)
				changedA = false;
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
		if (operator.getRightTrigger() >= 0.5) {
			shooter.spinUp();
			hoodSpeed = 0.5;
			if (shooter.atTargetRPM()) {
				firing = true;
				driver.setRumble(RumbleType.kRightRumble, 0.5f);
				driver.setRumble(RumbleType.kLeftRumble, 0.5f);
			} else {
				driver.setRumble(RumbleType.kRightRumble, 0.0f);
				driver.setRumble(RumbleType.kLeftRumble, 0.0f);
			}
		} else {
			shooter.stop();
			driver.setRumble(RumbleType.kRightRumble, 0.0f);
			driver.setRumble(RumbleType.kLeftRumble, 0.0f);
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

		drive.cheesyDrive(driver.getRightX(), driver.getLeftY(), driver.getLeftBumperbutton());
//		drive.tankDrive(driver.getLeftY(), driver.getRightY());
	}
}
