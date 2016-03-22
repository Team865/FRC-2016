package ca.warp7.robot.autonomous;

import ca.warp7.robot.Constants;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class Rotato extends AutonomousBase {
	
	DataPool angle;
	//Used to hit 90 degrees proper
	//private final double MODIFIER = 90/91.5;

	public Rotato(Drive drive, Shooter shooter, Intake intake) {
		drive.pid.reset();
		drive.pid.enable();
		drive.setGear(false);
		drive.pid.setSetpoint(90);
		angle = new DataPool("rotato");
		startTime = Timer.getFPGATimestamp();
	}
	
	boolean done = false;
	double startTime = 0;
	
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		angle.logDouble("setpoint", drive.pid.getSetpoint());
		if(Timer.getFPGATimestamp()-startTime > 3 && !done) {
			drive.pid.setSetpoint(0);
			done = true;
		}
		if(Timer.getFPGATimestamp()-startTime > 6 && done) {
			drive.pid.disable();
		}
	
	}

}
