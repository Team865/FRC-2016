package ca.warp7.robot;

public class Constants {
    //PWM Pins
    public static final int[] LEFT_DRIVE_MOTOR_PINS = new int[]{0, 1, 2};
    public static final int[] RIGHT_DRIVE_MOTOR_PINS = new int[]{3, 4, 5};
    public static final int[] FLY_WHEEL_PIN = new int[]{6};
    public static final int[] INTAKE_MOTOR = new int[]{7};

    //CAN ID's
    public static final int SHOOTER_CAN_ID = 0;

    //DIG Pins
    public static final int INTAKE_PHOTOSENSOR = 0;
    public static final int FLY_ENC_A = 3;
    public static final int FLY_ENC_B = 4;

    //Solenoids (manifold ports)
    public static final int GEAR_CHANGE = 0;
    public static final int PTO = 1;
    public static final int INTAKE_PISTON_A = 3;
    public static final int INTAKE_PISTON_B = 2;

    //Compressor
	public static final int COMPRESSOR_PIN = 0;
    
    //General References
    public static final char TALON = 't';
    public static final char VICTOR = 'v';
    public static final char GEAR_BOX = 'g';
	public static final int BATTERY = -1;
	public static final int INTAKE = 1;
	public static final boolean LOW_GEAR = false;
	public static final boolean HIGH_GEAR = true;
	public static final double HARDSTOP_RPM = 4800;
    public static final char[] LEFT_DRIVE_MOTOR_TYPES = new char[]{VICTOR, VICTOR, VICTOR};
    public static final char[] RIGHT_DRIVE_MOTOR_TYPES = new char[]{VICTOR, VICTOR, VICTOR};
    public static final char[] INTAKE_MOTOR_TYPES = new char[]{VICTOR};
    public static final char[] FLY_WHEEL_MOTOR_TYPE = new char[]{VICTOR};
    
    // Robot dimensions and stuff
    public static double SHOOTER_ANGLE = 75;    
    private static double TARGET_HEIGHT = 3;
    private static double ROBOT_HEIGHT = 2;
    public static double HEIGHT_DIFFERENCE = TARGET_HEIGHT - ROBOT_HEIGHT;
    private static double SHOOTER_WHEEL_RADIUS = 2;
    public static double SHOOTER_WHEEL_CIRCUMFERENCE = SHOOTER_WHEEL_RADIUS * 2 * Math.PI;
}
