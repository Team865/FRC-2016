package ca.warp7.robot.subsystems;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.networking.DataPool;
import ca.warp7.robot.subsystems.shooterComponents.FlyWheel;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Victor;

public class Shooter {

	private Victor hood;
	private FlyWheel flyWheel;

	/**
	 * @param motor
	 *            controller
	 */
	public Shooter(CANTalon flyWheelMotor, Victor motor) {
		flyWheel = new FlyWheel(flyWheelMotor);
		hood = motor;

		// hood.changeControlMode(TalonControlMode.Position);
		// hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Absolute);
		// hood.setPosition(0);
	}

	public double getSpeed() {
		return flyWheel.getSpeed();
	}

	public boolean atTargetRPM() {
		return flyWheel.atTargetRPM();
	}

	public void periodic(double wantedRPM, boolean firing, Drive drive) {
		// wew
	}

	public void set(double speed) {
		// flyWheel.set(speed);
	}

	public void stop() {
		flyWheel.coast();
	}

	public void safety() {
		// TODO if the hood is past some safety distance set the speed to 0
	}

	public void setHood(double degrees) {
		hood.set(degrees);
	}

	public void slowPeriodic() {
		flyWheel.slowPeriodic();
	}

	public void spinUp() {
		flyWheel.spinUp(Constants.HARDSTOP_RPM);
	}
}
