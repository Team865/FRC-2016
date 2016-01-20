package ca.warp7.robot.autonomous;

import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.Timer;

public class TestAutonomous {

	public static double sinAuto(TalonSRX flyWheel, double speed) {
		
    		speed = Math.cos(Timer.getFPGATimestamp()/5);
        	flyWheel.set(speed);
        	
        	return speed;
    	
	}

	
	
}
