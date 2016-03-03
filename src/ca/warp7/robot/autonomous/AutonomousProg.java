package ca.warp7.robot.autonomous;

import ca.warp7.robot.Constants;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class AutonomousProg {

	private int count;
	
	public AutonomousProg(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		
		intake.lowerAdjustingArm();
		//intake.adjustedArmRetracted();
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if(count <= 1500){
			count++;
			//GOES INTAKES FORWARD
			drive.overrideMotors(1.0);
			shooter.setHood(-0.5);
		}else{
			drive.overrideMotors(0);
			shooter.setHood(0.3);
		}
		
		/*TODO
		if(move){
			defence(defence);
			move(x);
			if(shoot){
				shoot(direction);
				// do more if you want to go back etc.
			}
			if(hardstop){
				curve(x, y);
				shoot(direction);
			}
		}
		*/
	}

	
}
