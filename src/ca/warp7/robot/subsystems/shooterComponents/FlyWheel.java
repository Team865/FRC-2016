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
		_talon.configEncoderCodesPerRev(12*86);
		_talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		_talon.configNominalOutputVoltage(+0.0f, -0.0f);
		_talon.configPeakOutputVoltage(+12.0f, -12.0f);
		_talon.changeControlMode(TalonControlMode.Speed);
		_talon.setProfile(0);
		_talon.setF(0);
		_talon.setP(15);
		_talon.setI(0.00076);
		_talon.setD(868.880005);
		_talon.reverseOutput(false);
		
		/*
		 * practice bot _talon.setF(1.345); _talon.setP(30); _talon.setI(0);
		 * _talon.setD(0);
		 */
		// _talon.setVoltageRampRate(10);

		_pool = new DataPool("FlyWheel");
	}

	public void spinUp(double targetSpeed) {
		_talon.enable();
		_talon.setSetpoint(targetSpeed);
	}

	public void slowPeriodic() {
		if(_talon != null){
			_pool.logDouble("speed", _talon.getSpeed());
	        _pool.logDouble("setpoint", _talon.getSetpoint());
			_pool.logBoolean("readyToFire", atTargetRPM());
			_pool.logDouble("Hot stuff", _talon.getTemperature());
		}
	}

	public double getSpeed() {
		return _talon.getSpeed();
	}

	public boolean atTargetRPM() {
		double speed = _talon.getSpeed();
		double setpoint = _talon.getSetpoint();
		return Math.abs(speed - setpoint) < 35 ;
	}

	public void coast() {
		_talon.disable();
		_talon.set(0);
	}

}
