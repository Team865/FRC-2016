package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class BatteryFirst extends AutonomousBase {

	private int count;

	public BatteryFirst(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		intake.raisePortculus(false);
		// drive.setGear(true);
		// intake.adjustedArmRetracted();
		Timer.delay(5);
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if (count <= 56*1.5) { // 1.125 seconds
			count++;
			drive.autoMove(-1, -1);
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
