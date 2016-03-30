package ca.warp7.robot;

public class Constants {
	// PWM Pins
	public static final int[] LEFT_DRIVE_MOTOR_PINS = { 0, 1, 2 };
	public static final int[] RIGHT_DRIVE_MOTOR_PINS = { 3, 4, 5 };
	public static final int HOOD_PIN = 6;
	public static final int INTAKE_MOTOR_PIN = 7;

	// CAN ID's
	public static final int SHOOTER_CAN_ID = 0;

	// DIG Pins
	public static final int INTAKE_PHOTOSENSOR = 0;
	public static final int LEFT_DRIVE_ENCODER_A = 4; // ding
	public static final int LEFT_DRIVE_ENCODER_B = 3; // ding
	public static final int RIGHT_DRIVE_ENCODER_A = 6; // ding
	public static final int RIGHT_DRIVE_ENCODER_B = 5; // ding

	// Solenoids (manifold ports)
	public static final int GEAR_CHANGE = 0;
	public static final int PTO_SOLENOID = 1;
	public static final int INTAKE_PISTON_A = 3;
	public static final int INTAKE_PISTON_B = 2;

	// Compressor
	public static final int COMPRESSOR_PIN = 0;

	// General References
	public static final int BATTERY = 1;
	public static final int INTAKE = -1;
	public static final boolean LOW_GEAR = false;
	public static final boolean HIGH_GEAR = true;
	public static final double HARDSTOP_RPM = 3700;
	public static final double DISTANCE_TO_DEFENCE_BASE = 0;

	// Robot dimensions and stuff
	public static double SHOOTER_ANGLE = 75;
	private static double TARGET_HEIGHT = 3;
	private static double ROBOT_HEIGHT = 2;
	public static double HEIGHT_DIFFERENCE = TARGET_HEIGHT - ROBOT_HEIGHT;
	private static double SHOOTER_WHEEL_RADIUS = 2;
	public static double SHOOTER_WHEEL_CIRCUMFERENCE = SHOOTER_WHEEL_RADIUS * 2 * Math.PI;
	public static double WHEELBASE_WIDTH = 0.6096; // meters
	public static double WHEEL_DIAMETER = 0.19431; // meters???
	public static int DRIVE_TICKS_PER_REV = 21; // TODO ???!??
	
	public static double MAX_VELOCITY = 3.6576;// m/s
}
