package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.*;

import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake {
	private static final double INTAKE_SPEED = 1.0;
	private static final double SLOW_INTAKE_SPEED = 0.8;
	private Solenoid initialArm;
	private Solenoid adjustingArm;
	private VictorSP motor;
	int goCounter = 0;
	private DigitalInput photosensor;
	private DataPool _pool;

	public Intake() {
		_pool = new DataPool("Intake");
		photosensor = new DigitalInput(INTAKE_PHOTOSENSOR);
		motor = new VictorSP(INTAKE_MOTOR_PIN);
		initialArm = new Solenoid(INTAKE_PISTON_A);
		adjustingArm = new Solenoid(INTAKE_PISTON_B);
		initialArm.set(false);
		adjustingArm.set(false);
	}

	public void reset() {
		initialArm.set(true);
	}

	public void toggleInitialArm() {
		initialArm.set(!initialArm.get());
	}

	public void toggleAdjustingArm() {
		adjustingArm.set(!adjustingArm.get());
	}

	public void intake() {
		// put ! before sensorReading to fix
		if (photosensor.get()) {
			motor.set(-SLOW_INTAKE_SPEED);
		} else if (goCounter <= 0) {
			motor.set(0);
		}
	}
	public void shoot() {
		motor.set(-INTAKE_SPEED);
	}

	public void outake() {
		motor.set(INTAKE_SPEED);
	}

	public void stop() {
		motor.set(0);
		initialArm.set(false);
		adjustingArm.set(false);
	}

	public void set(double speed) {
		motor.set(speed);
	}

	public boolean adjustedArmRetracted() {
		return adjustingArm.get() == false;
	}

	public void stopIntake() {
		motor.set(0);
	}

	public void raisePortculus(boolean b) {
		adjustingArm.set(!b);
		initialArm.set(!b);
	}

	public void periodic() {
		if (goCounter > 0) {
			motor.set(-INTAKE_SPEED);
			goCounter--;
		}
	}

	public void fireBall() {
		goCounter = 150;
	}

	public void slowPeriodic() {
		_pool.logBoolean("photosensor", photosensor.get());
	}
}
