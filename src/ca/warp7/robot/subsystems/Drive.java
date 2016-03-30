package ca.warp7.robot.subsystems;

import ca.warp7.robot.Util;
import ca.warp7.robot.hardware.MotorGroup;
import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.followers.EncoderFollower;

import static ca.warp7.robot.Constants.*;

public class Drive {

	// https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
	private static int direction;
	private static MotorGroup rightDrive;
	private static MotorGroup leftDrive;
	private Encoder leftEncoder;
	private Encoder rightEncoder;

	private static Solenoid shifter;
	private static Solenoid PTO;
	private ADXRS450_Gyro gyro;
	private DataPool pool;

	double quickstop_accumulator = 0f;
	double old_wheel = 0f;
	double sensitivity = .9f;
	// public PIDController pid = new PIDController(2, 0.5, 0.03125, new
	// PIDSource() {
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

	}, new PIDOutput() {
		@Override
		public void pidWrite(double output) {
			move(-output, output);
		}
	});
	private EncoderFollower leftEncoderFollower;
	private EncoderFollower rightEncoderFollower;

	public Drive(Compressor comp) {
		pool = new DataPool("Drive");

		// Hardware components
		rightDrive = new MotorGroup(RIGHT_DRIVE_MOTOR_PINS, VictorSP.class);
		rightDrive.setInverted(true);
		leftDrive = new MotorGroup(LEFT_DRIVE_MOTOR_PINS, VictorSP.class);
		direction = -1;
		shifter = new Solenoid(GEAR_CHANGE); // actually ear change
		PTO = new Solenoid(PTO_SOLENOID); // actually pto
		shifter.set(false);
		PTO.set(false);
		
		leftEncoder =  new Encoder(LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B);;
		rightEncoder = new Encoder(RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B);
		gyro = new ADXRS450_Gyro();
		
		pid.setAbsoluteTolerance(1);

	}

	public void changeDirection() {
		direction *= -1;
	}

	public void setGear(boolean gear) {
		shifter.set(gear); // TODO gear pto swap
	}

	public void setDirection(int direction_) {
		direction = direction_;
	}

	public void tankDrive(double left, double right) {
		left *= direction;
		right *= direction;

		left = Util.deadband(left);
		right = Util.deadband(right);

		move(left, right);
	}

	public void cheesyDrive(double wheel, double throttle, boolean quickturn) {
		/*
		 * Poofs! :param wheel: The speed that the robot should turn in the X
		 * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
		 * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
		 * :param quickturn: If the robot should drive arcade-drive style
		 */
		
		throttle = Util.deadband(throttle * direction);
		wheel = Util.deadband(wheel * direction);
		double right_pwm;
		double left_pwm;
		double neg_inertia_scalar;
		double neg_inertia = wheel - old_wheel;
		old_wheel = wheel;
		wheel = Util.sinScale(wheel, 0.8f, 3);

		if (wheel * neg_inertia > 0) {
			neg_inertia_scalar = 2.5f;
		} else {
			if (Math.abs(wheel) > .65) {
				neg_inertia_scalar = 6;
			} else {
				neg_inertia_scalar = 4;
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
			quickstop_accumulator = Util.wrap_accumulator(quickstop_accumulator);
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
		//right *= 0.94;

		/*
		 * double currentVoltage = pdp.getVoltage(); double speedLimit = 1;
		 * if(currentVoltage <= 8.5){ speedLimit = 0.6; }
		 * 
		 * right = Math.max(-speedLimit, Math.min(speedLimit, right)); left =
		 * Math.max(-speedLimit, Math.min(speedLimit, left));
		 */

		rightDrive.set(right);
		leftDrive.set(left);
	}

	public void stop() {
		rightDrive.set(0);
		leftDrive.set(0);
	}

	public void changeGear() {
		shifter.set(!(shifter.get()));
	}

	public void changePTO() {
		PTO.set(!(PTO.get()));
	}

	public void overrideMotors(double d) {
		move(d, d);
	}

	public double getRotation() {
		return gyro.getAngle();
	}

	public void slowPeriodic() {
		pool.logDouble("gyro angle", getRotation());
		pool.logDouble("left_enc", leftEncoder.getDistance());
		pool.logDouble("right_enc", rightEncoder.getDistance());
	}

	public boolean getDirection() {
		return direction == 1;
	}
	
	public void followPath() {
		if(leftEncoderFollower == null || rightEncoderFollower == null) {
			return; //bmlep
		}
		double leftOutput = leftEncoderFollower.calculate(leftEncoder.get());
		double rightOutput = rightEncoderFollower.calculate(rightEncoder.get());
		double gyro_heading = gyro.getAngle();    // Assuming the gyro is giving a value in degrees
		double desired_heading = Pathfinder.r2d(leftEncoderFollower.getHeading());  // Should also be in degrees

		double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
		double turn = 0.8 * (-1.0/80.0) * angleDifference;

		move(leftOutput + turn, rightOutput - turn);
	}

	public void setEncoderFollowers(EncoderFollower left, EncoderFollower right) {
		leftEncoderFollower = left;
		rightEncoderFollower = right;
		
		leftEncoderFollower.configureEncoder(leftEncoder.get(), DRIVE_TICKS_PER_REV, WHEEL_DIAMETER);
		rightEncoderFollower.configureEncoder(rightEncoder.get(), DRIVE_TICKS_PER_REV, WHEEL_DIAMETER);
		
		// The first argument is the proportional gain. Usually this will be quite high
		// The second argument is the integral gain. This is unused for motion profiling
		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the 
		//     trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
		leftEncoderFollower.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
		rightEncoderFollower.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
	}
}
