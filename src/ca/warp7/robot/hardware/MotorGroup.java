package ca.warp7.robot.hardware;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.Talon;

public class MotorGroup {
	private SpeedController[] motors;

	public MotorGroup(int[] pins) {
		motors = new SpeedController[pins.length];
		for(int i=0; i<pins.length; i++) {
			motors[i] = new Talon(pins[i]);
		}
	}
	public void set(double power) {
		for(int i=0; i<motors.length; i++) {
			motors[i].set(power);
		}
	}
}
