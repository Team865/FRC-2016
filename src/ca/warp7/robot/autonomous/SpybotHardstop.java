package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class SpybotHardstop extends AutonomousBase {

	private int count;

	public SpybotHardstop(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		// intake.raisePortculus(true);
		drive.setGear(true);
		// intake.adjustedArmRetracted();
		// Timer.delay(7);
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		shooter.setHood(0.3);
		if (count <= 250) {
			count++;
			if (count <= 175) {
				drive.overrideMotors(-1.0);
			} else {
				drive.overrideMotors(-0.6);
			}
		} else {
			drive.overrideMotors(0);
			if (shooter.atTargetRPM()) {
				intake.intake(false);
			}
			shooter.spinUp();
		}

		/*
		 * TODO if(move){ defence(defence); move(x); if(shoot){
		 * shoot(direction); // do more if you want to go back etc. }
		 * if(hardstop){ curve(x, y); shoot(direction); } }
		 */
	}

}
