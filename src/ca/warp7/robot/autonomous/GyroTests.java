package ca.warp7.robot.autonomous;

import ca.warp7.robot.autonomous.autoModuals.BasicModules;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class GyroTests extends AutonomousBase {

	private int step;

	
	public GyroTests(Drive drive, Shooter shooter, Intake intake) {
		super(drive, shooter, intake);
		step = 1;
		//drive.gyro.free();
		//drive.gyro = new ADXRS450_Gyro();
	}

	
	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {	
		switch(step){
		case 1:
			if(BasicModules.relativeTurn(90, drive))step++;
			break;
		default:
			System.out.println("YAY!!! :)");
			break;
		}
	}
}
