package ca.warp7.robot;


public class Constants {
	public static final int SHOOTER_FLY_WHEEL = 0;
	public static final int[] LEFT_DRIVE_MOTORS = new int[]{2, 3};
	public static final char[] LEFT_DRIVE_MOTOR_TYPES = new char[]{Constants.TALON, Constants.TALON};
	public static final int[] RIGHT_DRIVE_MOTORS = new int[]{4, 5};
	public static final char[] RIGHT_DRIVE_MOTOR_TYPES = new char[]{Constants.TALON, Constants.TALON};
	public static final int INTAKE_MOTOR = 1;
	
	
	public static final char TALON = 't';
	public static final char VICTOR = 'v';
	public static final char GEAR_BOX = 'g';
	
	
	// Robot dimesions and stuff
	private static double TARGET_HEIGHT = 3;
	private static double ROBOT_HEIGHT = 2;
	public static double HEIGHT_DIFFERENCE = TARGET_HEIGHT - ROBOT_HEIGHT;
	public static double SHOOTER_ANGLE = 75;
	private static double SHOOTER_WHEEL_RADIUS = 2;
	public static double SHOOTER_WHEEL_CIRCUMFERENCE = SHOOTER_WHEEL_RADIUS * 2 * Math.PI;
}
