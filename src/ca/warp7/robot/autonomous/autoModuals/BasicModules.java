package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;

public class BasicModules{
	
	private double zero;
	
	
	/**
	 * @param distance
	 *            in centimeters
	 */
	public static boolean move(double distance, Drive drive) {
		return false;
	}

	/**
	 * @param degrees
	 *            relative (0 is where you are)
	 *            Positive is to the right
	 */
	private static double errorOld = 0.0;
	public static boolean relativeTurn(double degrees, Drive drive) {
		double angle = drive.gyro.getAngle();
		double kp = 25.0;
		double kd = 70.0;
		
		double error = degrees-angle;
		
		double speed = (kp*error/360) + ((error-errorOld)*kd/360);
		if(Math.abs(error) < 5)speed = 0;
		
		
		speed = Math.max(-1, Math.min(1, speed));
		drive.autoMove(-speed, speed);
		errorOld = error;
		return false;
	}
}
