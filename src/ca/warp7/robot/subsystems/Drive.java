package ca.warp7.robot.subsystems;

import ca.warp7.robot.Util;
import ca.warp7.robot.hardware.GearBox;
import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;

public class Drive {

	// https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
	private static int direction;
	private static GearBox rightGearBox;
	private static GearBox leftGearBox;
	private static Solenoid PTO;
	private static Solenoid gearChange;
	private ADXRS450_Gyro gyro;
	private DataPool pool;

	double quickstop_accumulator = 0f;
	double old_wheel = 0f;
	double sensitivity = .9f;
	//public PIDController pid = new PIDController(2, 0.5, 0.03125, new PIDSource() {
	public PIDController pid = new PIDController(0.04, 0.00004, 0.065, new PIDSource() {

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			gyro.setPIDSourceType(pidSource);
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			return gyro.getPIDSourceType();
		}

		@Override
		public double pidGet() {
			return Util.correct_angle(gyro.getAngle());
		}
		
	}, new PIDOutput(){
		@Override
		public void pidWrite(double output) {
			move(-output, output);
		}
	});

	public Drive(GearBox right, GearBox left, Solenoid PTO_, Solenoid gearChange_, Compressor comp) {
		rightGearBox = right;
		leftGearBox = left;
		direction = 1;
		PTO = PTO_;
		gearChange = gearChange_;
		PTO.set(false);
		gearChange.set(false);
		gyro = new ADXRS450_Gyro();
		pool = new DataPool("Drive");
		pid.setAbsoluteTolerance(1);
	}

	public void changeDirection() {
		direction *= -1;
	}

	public void setGear(boolean gear) {
		PTO.set(gear); // TODO gear pto swap
	}

	public void setDirection(int direction_) {
		direction = direction_;
	}

	public void tankDrive(double left, double right) {
		left *= direction;
		right *= direction;

		left = createDeadband(left);
		right = createDeadband(right);

		move(left, right);
	}

	public void cheesyDrive(double wheel, double throttle, boolean quickturn) {
		/*
		 * Poofs! :param wheel: The speed that the robot should turn in the X
		 * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
		 * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
		 * :param quickturn: If the robot should drive arcade-drive style
		 */

		throttle *= direction;
		wheel *= -direction;
		double right_pwm;
		double left_pwm;
		double neg_inertia_scalar;
		double neg_inertia = wheel - old_wheel;
		old_wheel = wheel;
		wheel = sin_scale(wheel, 0.8f, 3); // TODO implement this gyy

		if (wheel * neg_inertia > 0) {
			neg_inertia_scalar = 2.5f;
		} else {
			if (Math.abs(wheel) > .65) {
				neg_inertia_scalar = 5;
			} else {
				neg_inertia_scalar = 3;
			}
		}

		double neg_inertia_accumulator = neg_inertia * neg_inertia_scalar;

		wheel += neg_inertia_accumulator;

		double over_power, angular_power;
		if (quickturn) {
			if (Math.abs(throttle) < 0.2) {
				double alpha = .1f;
				quickstop_accumulator = (1 - alpha) * quickstop_accumulator + alpha * Util.limit(wheel, 1.0) * 5;
			}
			over_power = 1;
			angular_power = -wheel * direction * .85;
		} else {
			over_power = 0;
			angular_power = throttle * wheel * sensitivity - quickstop_accumulator;
			quickstop_accumulator = wrap_accumulator(quickstop_accumulator);
		}
		right_pwm = left_pwm = throttle;

		left_pwm += angular_power;
		right_pwm -= angular_power;

		if (left_pwm > 1) {
			right_pwm -= over_power * (left_pwm - 1);
			left_pwm = 1;
		} else if (right_pwm > 1) {
			left_pwm -= over_power * (right_pwm - 1);
			right_pwm = 1;
		} else if (left_pwm < -1) {
			right_pwm += over_power * (-1 - left_pwm);
			left_pwm = -1;
		} else if (right_pwm < -1) {
			left_pwm += over_power * (-1 - right_pwm);
			right_pwm = -1;
		}
		// SET MOTORS to left_pwm and right_pwm
		move(left_pwm, right_pwm);
	}

	public void move(double left, double right) {
		// right *= 0.94;
		right *= 0.94;
		rightGearBox.set(right * (-1));
		leftGearBox.set((left));
	}

	private static double createDeadband(double num) {
		if (0.13 >= Math.abs(num)) {
			num = 0;
		}

		num = Math.pow(num, 3);

		return num;
	}

	public void stop() {
		rightGearBox.set(0);
		leftGearBox.set(0);
	}

	public void changeGear() {
		PTO.set(!(PTO.get()));
	}

	public void changePTO() {
		gearChange.set(!(gearChange.get()));
	}

	public void overrideMotors(double d) {
		move(d, d);
	}

	public double getRotation() {
		return gyro.getAngle();
	}

	public void slowPeriodic() {
		pool.logDouble("gyro angle", getRotation());
	}

	static double sin_scale(double val, double non_linearity, int passes) {
		/*
		 * recursive sin scaling! :D
		 * 
		 * :param val: input :param non_linearity: :param passes: how many times
		 * to recurse :return: scaled val
		 */
		double scaled = Math.sin(Math.PI / 2 * non_linearity * val) / Math.sin(Math.PI / 2 * non_linearity);
		if (passes == 1) {
			return scaled;
		} else {
			return sin_scale(scaled, non_linearity, passes - 1);
		}

	}

	static double wrap_accumulator(double acc) {
		if (acc > 1) {
			acc -= 1;
		} else if (acc < -1) {
			acc += 1;
		} else {
			acc = 0;
		}
		return acc;
	}
}
