package ca.warp7.robot.autonomous;

import ca.warp7.robot.autonomous.autoModuals.BasicModules;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class EncoderDrive extends AutonomousBase {

	private int step;

	public EncoderDrive(Drive drive, Shooter shooter, Intake intake) {
		step = 1;
		intake.raisePortculus(false);
		// drive.setGear(true);
		// intake.adjustedArmRetracted();
		Timer.delay(3);
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		switch(step){
		case 1:
			boolean finished = BasicModules.move(100, drive);
			if(finished)step++;
			break;
		default:
			drive.stop();
			shooter.setHood(0.3);
			break;
		}

		/*
		 * TODO if(move){ defence(defence); move(x); if(shoot){
		 * shoot(direction); // do more if you want to go back etc. }
		 * if(hardstop){ curve(x, y); shoot(direction); } }
		 */
	}

}
