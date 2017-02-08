package ca.warp7.robot.subsystems;

import static ca.warp7.robot.Constants.DRIVE_METERS_PER_TICK;
import static ca.warp7.robot.Constants.GEAR_CHANGE;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.LEFT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.LEFT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.Constants.MAX_VELOCITY;
import static ca.warp7.robot.Constants.PTO_SOLENOID;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_A;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_ENCODER_B;
import static ca.warp7.robot.Constants.RIGHT_DRIVE_MOTOR_PINS;
import static ca.warp7.robot.Constants.WHEELBASE_WIDTH;

import ca.warp7.robot.Constants;
import ca.warp7.robot.Util;
import ca.warp7.robot.hardware.MotorGroup;
import ca.warp7.robot.networking.DataPool;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

public class Drive {

	// https://code.google.com/p/3647robotics/source/browse/WCDRobot/src/Robot/DriveTrain.java?r=63
	private static MotorGroup rightDrive;
	private static MotorGroup leftDrive;
	public Encoder leftEncoder;
	public Encoder rightEncoder;

	private static Solenoid shifter;
	private static Solenoid PTO;
	public ADXRS450_Gyro gyro;
	private DataPool pool;
	private double leftRamp = 0.0;
	private double rightRamp = 0.0;

	private double quickstop_accumulator = 0;
	private double old_wheel = 0;
    private EncoderFollower leftFollower;
	private EncoderFollower rightFollower;
    private boolean isDrivetrainReversed = false;
    private int profileStep = 0;

    public Drive() {
		pool = new DataPool("Drive");

		// Hardware components
		rightDrive = new MotorGroup(RIGHT_DRIVE_MOTOR_PINS, VictorSP.class);
		rightDrive.setInverted(true);
		leftDrive = new MotorGroup(LEFT_DRIVE_MOTOR_PINS, VictorSP.class);
//		leftDrive.setInverted(true);

        shifter = new Solenoid(GEAR_CHANGE); // actually ear change
		PTO = new Solenoid(PTO_SOLENOID); // actually pto
		shifter.set(false);
		PTO.set(false);
		
		leftEncoder =  new Encoder(LEFT_DRIVE_ENCODER_A, LEFT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		rightEncoder = new Encoder(RIGHT_DRIVE_ENCODER_A, RIGHT_DRIVE_ENCODER_B, false, EncodingType.k4X);
		
		leftEncoder.setDistancePerPulse(DRIVE_METERS_PER_TICK);
		rightEncoder.setDistancePerPulse(DRIVE_METERS_PER_TICK);
		gyro = new ADXRS450_Gyro();
	}


	public void setGear(boolean gear) {
		shifter.set(gear); // TODO gear pto swap
	}

	public void tankDrive(double left, double right) {
		pool.logDouble("desiredLeft", left);
		pool.logDouble("desiredRight", right);
		moveRamped(left, right);
	}

	public void cheesyDrive(double wheel, double throttle, boolean quickturn) {
		/*
		 * Poofs! :param wheel: The speed that the robot should turn in the X
		 * direction. 1 is right [-1.0..1.0] :param throttle: The speed that the
		 * robot should drive in the Y direction. -1 is forward. [-1.0..1.0]
		 * :param quickturn: If the robot should drive arcade-drive style
		 */
		throttle = Util.deadband(throttle);
		wheel = Util.deadband(wheel);
		if(isDrivetrainReversed){
			wheel*=-1;
		}
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
			angular_power = -wheel * .85;
		} else {
			over_power = 0;
            double sensitivity = .9;
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
        if(isDrivetrainReversed) {
            left_pwm *= -1;
            right_pwm *= -1;
        }
		if(shifter.get()) { // if low gear
			leftDrive.set(left_pwm);
			rightDrive.set(right_pwm);
		} else {
			moveRamped(left_pwm, right_pwm);
		}
	}

	public void moveRamped(double desiredLeft, double desiredRight) {
		double ramp_speed = 6;
		leftRamp += (desiredLeft - leftRamp) / ramp_speed;
		rightRamp += (desiredRight - rightRamp) / ramp_speed;
		leftDrive.set(leftRamp);
		rightDrive.set(rightRamp);
	}

	public void stop() {
		rightDrive.set(0);
		leftDrive.set(0);
	}

	public void autoMove(double left, double right) {
		leftDrive.set(left);
		rightDrive.set(right);
	}

	public double getRotation() {
		return gyro.getAngle();
	}

	public void slowPeriodic() {
		pool.logDouble("gyro_angle", getRotation());
		pool.logDouble("left_enc", leftEncoder.getDistance());
		pool.logDouble("right_enc", rightEncoder.getDistance());
		pool.logDouble("left_ramp", leftRamp);
		pool.logDouble("right_ramp", rightRamp);
		if (leftFollower != null && !leftFollower.isFinished()) {
            Trajectory.Segment seg = leftFollower.getSegment();
            double leftError = seg.position - leftEncoder.get();
            pool.logDouble("left_error", leftError);
		}
	}

	public void followPath() {
		if(leftFollower == null || rightFollower == null) {
			System.out.println("No path follower initialized!!!");
			return; //bmlep
		}
		double leftOutput = leftFollower.calculate(leftEncoder.get());
		double rightOutput = rightFollower.calculate(rightEncoder.get());
        pool.logDouble("leftOutput", leftOutput);
        pool.logDouble("rightOutput", rightOutput);
        if(!leftFollower.isFinished()) {
            pool.logDouble("leftDesired", leftFollower.getSegment().position);
        }
        if (!rightFollower.isFinished()) {
            pool.logDouble("rightDesired", rightFollower.getSegment().position);
        }

		double gyro_heading = gyro.getAngle();    // Assuming the gyro is giving a value in degrees
		 
		double desired_heading = Pathfinder.r2d(leftFollower.getHeading());  // Should also be in degrees

		double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
		double turn = 0.8 * (-1.0/80.0) * angleDifference;

		leftDrive.set(-leftOutput + turn);
		rightDrive.set(-rightOutput - turn);
        profileStep++;
	}

	public void setTrajectory(Trajectory traj) {
		TankModifier modifier = new TankModifier(traj).modify(WHEELBASE_WIDTH);
		Trajectory leftTrajectory = modifier.getLeftTrajectory();
		Trajectory rightTrajectory = modifier.getRightTrajectory();
		leftFollower = new EncoderFollower(leftTrajectory);
		rightFollower = new EncoderFollower(rightTrajectory);
        leftFollower.configureEncoder(leftEncoder.get(), Constants.DRIVE_TICKS_PER_REV, Constants.WHEEL_DIAMETER);
        rightFollower.configureEncoder(rightEncoder.get(), Constants.DRIVE_TICKS_PER_REV, Constants.WHEEL_DIAMETER);

		// The first argument is the proportional gain. Usually this will be quite high
		// The second argument is the integral gain. This is unused for motion profiling
		// The third argument is the derivative gain. Tweak this if you are unhappy with the tracking of the trajectory
		// The fourth argument is the velocity ratio. This is 1 over the maximum velocity you provided in the 
		//     trajectory configuration (it translates m/s to a -1 to 1 scale that your motors can read)
		// The fifth argument is your acceleration gain. Tweak this if you want to get to a higher or lower speed quicker
		leftFollower.configurePIDVA(0.001, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
		rightFollower.configurePIDVA(0.001, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
	}

    public void setDrivetrainReversed(boolean reversed) {
        isDrivetrainReversed = reversed;
    }

    public boolean isDrivetrainReversed() {
        return isDrivetrainReversed;
    }
    
    public boolean isMoving(){
    	return leftDrive.get() != 0.0D || rightDrive.get() != 0.0D;
    }
    
    public void setPTO(boolean on){
    	PTO.set(on);
    }
}
