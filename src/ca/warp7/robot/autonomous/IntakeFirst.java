package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class IntakeFirst extends AutonomousBase {

	private int count;

	public IntakeFirst(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		intake.raisePortculus(false);
		// drive.setGear(true);
		// intake.adjustedArmRetracted();
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if (count <= 400) {
			count++;
			drive.moveRamped(0.8, 0.9);
		} else {
			drive.stop();
			shooter.setHood(0.3);
		}

		/*
		 * TODO if(move){ defence(defence); move(x); if(shoot){
		 * shoot(direction); // do more if you want to go back etc. }
		 * if(hardstop){ curve(x, y); shoot(direction); } }
		 */
	}
}