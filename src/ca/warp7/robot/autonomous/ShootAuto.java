package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class ShootAuto extends AutonomousBase{

	public ShootAuto(){
		Timer.delay(0.5);
	}
	
	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		shooter.spinUp();
		shooter.setHood(0.3);
		if (shooter.atTargetRPM()) {
			intake.fireBall();
		}
	}

}
