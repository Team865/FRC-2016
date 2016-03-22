package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public abstract class AutonomousBase {

	public abstract void periodic(Drive drive, Shooter shooter, Intake intake);

}
