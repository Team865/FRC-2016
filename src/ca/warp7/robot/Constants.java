package ca.warp7.robot;

public class Constants {
	//General References
	public static final char TALON = 't';
	public static final char VICTOR = 'v';
	public static final char GEAR_BOX = 'g';
	
	//PWM Pins
	public static final int[] LEFT_DRIVE_MOTOR_PINS = new int[]{0, 1, 2};
	public static final int[] RIGHT_DRIVE_MOTOR_PINS = new int[]{3, 4, 5};
	public static final int FLY_WHEEL_PIN = 6;
	public static final int INTAKE_MOTOR = 7;
	public static final char[] LEFT_DRIVE_MOTOR_TYPES = new char[]{VICTOR, VICTOR, VICTOR};
	public static final char[] RIGHT_DRIVE_MOTOR_TYPES = new char[]{VICTOR, VICTOR, VICTOR};
	
	//CAN ID's
	public static final int SHOOTER_CAN_ID = 0;
	
	//DIG Pins
	public static final int INTAKE_PHOTOSENSOR = 0;
	public static final int FLY_ENC_A = 1;
	public static final int FLY_ENC_B = 2;
	
	//Solinoids
	public static final int PISTON = 0;
	
	// Robot dimesions and stuff
	private static double TARGET_HEIGHT = 3;
	private static double ROBOT_HEIGHT = 2;
	public static double HEIGHT_DIFFERENCE = TARGET_HEIGHT - ROBOT_HEIGHT;
	public static double SHOOTER_ANGLE = 75;
	private static double SHOOTER_WHEEL_RADIUS = 2;
	public static double SHOOTER_WHEEL_CIRCUMFERENCE = SHOOTER_WHEEL_RADIUS * 2 * Math.PI;
}
