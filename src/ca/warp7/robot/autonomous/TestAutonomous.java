package ca.warp7.robot.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;

public class TestAutonomous {

	public static double sinAuto(Victor motorA, double speed) {
		
    		speed = Math.cos(Timer.getFPGATimestamp()/5);
        	motorA.set(speed);
        	
        	return speed;
    	
	}

	
	
}
