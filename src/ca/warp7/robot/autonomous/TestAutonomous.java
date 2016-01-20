package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Timer;

public class TestAutonomous {

	public static double sinAuto(double distance) {
		
    		distance = Math.cos(Timer.getFPGATimestamp()/5);
        	Shooter.prepareToFire(distance);
        	
        	return distance;
    	
	}

	
	
}
