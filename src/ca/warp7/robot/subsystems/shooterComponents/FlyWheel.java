package ca.warp7.robot.subsystems.shooterComponents;

import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;

public class FlyWheel {

	private CANTalon _talon;
	private DataPool _pool;

	public FlyWheel(CANTalon motor) {
		_talon = motor;
		_talon.configEncoderCodesPerRev(20);
		_talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		_talon.configNominalOutputVoltage(+0.0f, -0.0f);
		_talon.configPeakOutputVoltage(+12.0f, -12.0f);
		_talon.changeControlMode(TalonControlMode.Speed);
		_talon.setProfile(0);

		_talon.setF(0);
		_talon.setP(0);
		_talon.setI(0);
		_talon.setD(0);

		/*
		 * practice bot _talon.setF(1.345); _talon.setP(30); _talon.setI(0);
		 * _talon.setD(0);
		 */
		// _talon.setVoltageRampRate(10);

		_pool = new DataPool("FlyWheel");
	}

	public void spinUp(double targetSpeed) {
		_talon.enable();
		_talon.set(targetSpeed);
	}

	public void slowPeriodic() {
		_pool.logDouble("speed", _talon.getSpeed());
        _pool.logDouble("setpoint", _talon.getSetpoint());
		_pool.logBoolean("readyToFire", atTargetRPM());
	}

	public double getSpeed() {
		return _talon.getSpeed();
	}

	public boolean atTargetRPM() {
		return Math.abs(_talon.getSpeed() - _talon.getSetpoint()) < 50;
	}

	public void coast() {
		_talon.disable();
		_talon.set(0);
	}

}
