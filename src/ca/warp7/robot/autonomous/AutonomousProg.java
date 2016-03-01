package ca.warp7.robot.autonomous;

import ca.warp7.robot.Constants;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class AutonomousProg {

	private int count;
	
	public AutonomousProg(Drive drive, Shooter shooter, Intake intake) {
		count = 0;
		drive.setDirection(Constants.BATTERY);
		//intake.adjustedArmRetracted();
	}

	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if(count <= 2000){
			count++;
			drive.overrideMotors(1.0);
		}else{
			drive.overrideMotors(0);
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
