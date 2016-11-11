package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class DoNothin extends AutonomousBase{

	public DoNothin(Drive drive, Shooter shooter, Intake intake) {
		super(drive, shooter, intake);
		// nothing
	}

	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		// nothing
	}

}
