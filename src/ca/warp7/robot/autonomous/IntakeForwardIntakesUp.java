package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class IntakeForwardIntakesUp extends AutonomousBase {

	private int count;

	public IntakeForwardIntakesUp(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		intake.raisePortculus(true);
		// drive.setGear(true);
		// intake.adjustedArmRetracted();
		// Timer.delay(7);
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if (count <= 400) {
			count++;
			drive.moveRamped(0.8, 0.9);
		} else {
			drive.overrideMotors(0);
			shooter.setHood(0.3);
		}

		/*
		 * TODO if(move){ defence(defence); move(x); if(shoot){
		 * shoot(direction); // do more if you want to go back etc. }
		 * if(hardstop){ curve(x, y); shoot(direction); } }
		 */
	}
}