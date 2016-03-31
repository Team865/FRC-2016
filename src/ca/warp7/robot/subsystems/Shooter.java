package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.*;

import ca.warp7.robot.subsystems.shooterComponents.FlyWheel;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.VictorSP;

public class Shooter {

	private VictorSP hood;
	private FlyWheel flyWheel;

	public Shooter() {
		flyWheel = new FlyWheel(new CANTalon(SHOOTER_CAN_ID));
		hood = new VictorSP(HOOD_PIN);

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

	public void periodic() {
		// wew
	}

	public void set(double speed) {
		// flyWheel.set(speed);
	}

	public void stop() {
		flyWheel.coast();
	}

	public void setHood(double power) {
		hood.set(power);
	}

	public void slowPeriodic() {
		flyWheel.slowPeriodic();
	}

	public void spinUp() {
		flyWheel.spinUp(HARDSTOP_RPM);
	}
}
