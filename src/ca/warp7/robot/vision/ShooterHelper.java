package ca.warp7.robot.vision;

import ca.warp7.robot.Constants;

public class ShooterHelper {
	public static double getDesiredRPM(double distance) {
		double desiredVelocity = Math.sqrt(
								(4.9*Math.pow(distance, 2)) /
								((distance * Math.tan(Constants.SHOOTER_ANGLE) - Constants.HEIGHT_DIFFERENCE) * 
										Math.pow(Math.cos(Constants.SHOOTER_ANGLE), 2))
						);
		// Converting linear velocity at edge to RPM
		return desiredVelocity / Constants.SHOOTER_WHEEL_CIRCUMFERENCE * 60;
	}
}
