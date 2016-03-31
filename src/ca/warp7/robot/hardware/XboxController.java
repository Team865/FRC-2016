package ca.warp7.robot.hardware;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary;
import edu.wpi.first.wpilibj.communication.FRCNetworkCommunicationsLibrary.tResourceType;
import edu.wpi.first.wpilibj.communication.UsageReporting;

/**
 * Handle input from standard Xbox controllers connected to the Driver Station.
 * This class handles standard input that comes from the Driver Station. Each
 * time a value is requested the most recent value is returned. There is a
 * single class instance for each controller and the mapping of ports to
 * hardware buttons depends on the code in the driver station.
 */
public class XboxController extends GenericHID {

	static final byte LEFTSTICKY = 0;
	static final byte LEFTSTICKX = 1;
	static final byte RIGHTSTICKY = 4;
	static final byte RIGHTSTICKX = 5;
	static final byte LEFTTRIGGER = 2;
	static final byte RIGHTTRIGGER = 3;
	static final int A = 1;
	static final int B = 2;
	static final int X = 3;
	static final int Y = 4;
	static final int LEFTBUMPER = 5;
	static final int RIGHTBUMPER = 6;
	static final int BACK = 7;
	static final int START = 8;
	static final int LEFTSTICKPRESS = 9;
	static final int RIGHTSTICKPRESS = 10;
	private final int m_port;
	private final byte[] m_axes;
	private final byte[] m_buttons;
	private DriverStation m_ds;
	private int m_outputs;
	private short m_leftRumble;
	private short m_rightRumble;

	/**
	 * Construct an instance of a controller. The controller index is the usb
	 * port on the drivers station.
	 *
	 * @param port
	 *            The port on the driver station that the controller is plugged
	 *            into.
	 */
	public XboxController(final int port) {
		this(port, AxisType.NumAxis.value, ButtonType.NumButton.value);

		m_axes[AxisType.LX.value] = LEFTSTICKY;
		m_axes[AxisType.LY.value] = LEFTSTICKX;
		m_axes[AxisType.RX.value] = RIGHTSTICKY;
		m_axes[AxisType.RY.value] = RIGHTSTICKX;
		m_axes[AxisType.LTRIGGER.value] = LEFTTRIGGER;
		m_axes[AxisType.RTRIGGER.value] = RIGHTTRIGGER;

		m_buttons[ButtonType.BA.value] = A;
		m_buttons[ButtonType.BB.value] = B;
		m_buttons[ButtonType.BX.value] = X;
		m_buttons[ButtonType.BY.value] = Y;
		m_buttons[ButtonType.BLBUMPER.value] = LEFTBUMPER;
		m_buttons[ButtonType.BRBUMPER.value] = RIGHTBUMPER;
		m_buttons[ButtonType.BBACK.value] = BACK;
		m_buttons[ButtonType.BSTART.value] = START;
		m_buttons[ButtonType.BLEFTSTICKPRESS.value] = LEFTSTICKPRESS;
		m_buttons[ButtonType.BRIGHTSTICKPRESS.value] = RIGHTSTICKPRESS;

		UsageReporting.report(tResourceType.kResourceType_Joystick, port);

		// TODO go here
	}

	/**
	 * Protected version of the constructor to be called by sub-classes.
	 * <p>
	 * This constructor allows the subclass to configure the number of constants
	 * for axes and buttons.
	 *
	 * @param port
	 *            The port on the driver station that the controller is plugged
	 *            into.
	 * @param numAxisTypes
	 *            The number of axis types in the enum.
	 * @param numButtonTypes
	 *            The number of button types in the enum.
	 */
	protected XboxController(int port, int numAxisTypes, int numButtonTypes) {
		m_ds = DriverStation.getInstance();
		m_axes = new byte[numAxisTypes];
		m_buttons = new byte[numButtonTypes];
		m_port = port;
	}

	/**
	 * Get the left X value of the stick. This depends on the mapping of the
	 * controller connected to the current port.
	 *
	 * @return The left X value of the stick.
	 */
	public double getLeftX() {
		return getRawAxis(m_axes[AxisType.LX.value]);
	}

	/**
	 * Get the left Y value of the stick. This depends on the mapping of the
	 * controller connected to the current port.
	 *
	 * @return The left Y value of the stick.
	 */
	public double getLeftY() {
		return getRawAxis(m_axes[AxisType.LY.value]);
	}

	/**
	 * Get the right Y value of the stick. This depends on the mapping of the
	 * controller connected to the current port.
	 *
	 * @return The right Y value of the stick.
	 */
	public double getRightY() {
		return getRawAxis(m_axes[AxisType.RY.value]);
	}

	/**
	 * Get the right X value of the stick. This depends on the mapping of the
	 * controller connected to the current port.
	 *
	 * @return The right X value of the stick.
	 */
	public double getRightX() {
		return getRawAxis(m_axes[AxisType.RX.value]);
	}

	/**
	 * Get the left trigger value of the controller. This depends on the mapping
	 * of the controller connected to the current port.
	 *
	 * @return The left trigger value of the controller.
	 */
	public double getLeftTrigger() {
		return getRawAxis(m_axes[AxisType.LTRIGGER.value]);
	}

	/**
	 * Get the right trigger value of the controller. This depends on the
	 * mapping of the controller connected to the current port.
	 *
	 * @return The right trigger value of the controller.
	 */
	public double getRightTrigger() {
		return getRawAxis(m_axes[AxisType.RTRIGGER.value]);
	}

	/**
	 * Get the value of the axis.
	 *
	 * @return The value of the axis.
	 */
	public double getRawAxis(final int axis) {
		return m_ds.getStickAxis(m_port, axis);
	}

	/**
	 * For the current controller, return the number of axis
	 */
	public int getAxisCount() {
		return m_ds.getStickAxisCount(m_port);
	}

	/**
	 * Read the state of the 'a' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'a' button and read its
	 * state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getAbutton() {
		return getRawButton(m_buttons[ButtonType.BA.value]);
	}

	/**
	 * Read the state of the 'b' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'b' button and read its
	 * state.
	 *
	 * * @return The state of the a button.
	 */
	public boolean getBbutton() {
		return getRawButton(m_buttons[ButtonType.BB.value]);
	}

	/**
	 * Read the state of the 'x' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'x' button and read its
	 * state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getXbutton() {
		return getRawButton(m_buttons[ButtonType.BX.value]);
	}

	/**
	 * Read the state of the 'y' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'y' button and read its
	 * state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getYbutton() {
		return getRawButton(m_buttons[ButtonType.BY.value]);
	}

	/**
	 * Read the state of the left bumper on the controller.
	 * <p>
	 * Look up which button has been assigned to the left bumper and read its
	 * state.
	 * 
	 * @return The state of the a button.
	 */
	public boolean getLeftBumperbutton() {
		return getRawButton(m_buttons[ButtonType.BLBUMPER.value]);
	}

	/**
	 * Read the state of the right bumper on the controller.
	 * <p>
	 * Look up which button has been assigned to the right bumper and read its
	 * state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getRightBumperbutton() {
		return getRawButton(m_buttons[ButtonType.BRBUMPER.value]);
	}

	/**
	 * Read the state of the 'back' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'back' button and read its
	 * state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getBackButton() {
		return getRawButton(m_buttons[ButtonType.BBACK.value]);
	}

	/**
	 * Read the state of the 'start' button on the controller.
	 * <p>
	 * Look up which button has been assigned to the 'start' button and read its
	 * state.
	 * 
	 * @return The state of the a button.
	 */
	public boolean getStartButton() {
		return getRawButton(m_buttons[ButtonType.BSTART.value]);
	}

	/**
	 * Read the state of the left stick button on the controller.
	 * <p>
	 * Look up which button has been assigned to the left stick button and read
	 * its state.
	 * 
	 * @return The state of the a button.
	 */
	public boolean getLeftStickButton() {
		return getRawButton(m_buttons[ButtonType.BLEFTSTICKPRESS.value]);
	}

	/**
	 * Read the state of the right stick button on the controller.
	 * <p>
	 * Look up which button has been assigned to the right stick button and read
	 * its state.
	 *
	 * @return The state of the a button.
	 */
	public boolean getRightStickButton() {
		return getRawButton(m_buttons[ButtonType.BRIGHTSTICKPRESS.value]);
	}

	/**
	 * Get the button value (starting at button 1)
	 * <p>
	 * The appropriate button is returned as a boolean value.
	 *
	 * @param button
	 *            The button number to be read (starting at 1).
	 * @return The state of the button.
	 */
	public boolean getRawButton(final int button) {
		return m_ds.getStickButton(m_port, (byte) button);
	}

	/**
	 * For the current controller, return the number of buttons
	 */
	public int getButtonCount() {
		return m_ds.getStickButtonCount(m_port);
	}

	/**
	 * Get the state of a DPad on the controller.
	 * <p>
	 * NOTE: 90 is right -90 is left
	 *
	 * @return the angle of the DPad in degrees, or -1 if the POV is not
	 *         pressed.
	 */
	public int getDPad() {
		return m_ds.getStickPOV(m_port, 0);
	}

	/**
	 * For the current controller, return the number of POVs
	 */
	public int getPOVCount() {
		return m_ds.getStickPOVCount(m_port);
	}

	/**
	 * Get the magnitude of the direction vector formed by the controller's
	 * current position relative to its origin
	 *
	 * @return The magnitude of the direction vector
	 */
	public double getMagnitude() {
		return Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
	}

	/**
	 * Get the direction of the vector formed by the controller and its origin
	 * in radians
	 *
	 * @return The direction of the vector in radians
	 */
	public double getDirectionRadians() {
		return Math.atan2(getX(), -getY());
	}

	/**
	 * Get the direction of the vector formed by the controller and its origin
	 * in degrees
	 * <p>
	 * uses acos(-1) to represent Pi due to absence of readily accessable Pi
	 * constant in C++
	 *
	 * @return The direction of the vector in degrees
	 */
	public double getDirectionDegrees() {
		return Math.toDegrees(getDirectionRadians());
	}

	/**
	 * Get the channel currently associated with the specified axis.
	 *
	 * @param axis
	 *            The axis to look up the channel for.
	 * @return The channel fr the axis.
	 */
	public int getAxisChannel(AxisType axis) {
		return m_axes[axis.value];
	}

	/**
	 * Set the channel associated with a specified axis.
	 *
	 * @param axis
	 *            The axis to set the channel for.
	 * @param channel
	 *            The channel to set the axis to.
	 */
	public void setAxisChannel(AxisType axis, int channel) {
		m_axes[axis.value] = (byte) channel;
	}

	/**
	 * Get the value of isXbox for the current controller. $
	 *
	 * @return A boolean that is true if the controller is an xbox controller.
	 */
	public boolean getIsXbox() {
		return m_ds.getJoystickIsXbox(m_port);
	}

	/**
	 * Get the HID type of the current controller. $
	 *
	 * @return The HID type value of the current controller.
	 */
	public int getType() {
		return m_ds.getJoystickType(m_port);
	}

	/**
	 * Get the name of the current controller. $
	 *
	 * @return The name of the current controller.
	 */
	public String getName() {
		return m_ds.getJoystickName(m_port);
	}

	/**
	 * Set a single HID output value for the controller. $
	 *
	 * @param outputNumber
	 *            The index of the output to set (1-32)
	 * @param value
	 *            The value to set the output to
	 */

	public void setOutput(int outputNumber, boolean value) {
		m_outputs = (m_outputs & ~(1 << (outputNumber - 1))) | ((value ? 1 : 0) << (outputNumber - 1));
		FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
	}

	/**
	 * Set all HID output values for the controller. $
	 *
	 * @param value
	 *            The 32 bit output value (1 bit for each output)
	 */
	public void setOutputs(int value) {
		m_outputs = value;
		FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
	}

	/**
	 * Set the rumble output for the controller. The DS currently supports 2
	 * rumble values, left rumble and right rumble $
	 *
	 * @param type
	 *            Which rumble value to set
	 * @param value
	 *            The normalized value (0 to 1) to set the rumble to
	 */
	public void setRumble(RumbleType type, float value) {
		if (value < 0)
			value = 0;
		else if (value > 1)
			value = 1;
		if (type.value == RumbleType.kLeftRumble_val)
			m_leftRumble = (short) (value * 65535);
		else
			m_rightRumble = (short) (value * 65535);
		FRCNetworkCommunicationsLibrary.HALSetJoystickOutputs((byte) m_port, m_outputs, m_leftRumble, m_rightRumble);
	}

	@Override
	public double getZ(Hand hand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTwist() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getThrottle() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getTrigger(Hand hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getTop(Hand hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean getBumper(Hand hand) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public double getX(Hand hand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getY(Hand hand) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPOV(int pov) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void runCommands() {
		if (getAbutton()) {
			// TODO put repeating command here
		}

	}

	/**
	 * Represents an analog axis on a controller.
	 */
	public static class AxisType {

		static final int LEFTSTICKY_val = 0;
		/**
		 * axis: left y-axis
		 */
		public static final AxisType LY = new AxisType(LEFTSTICKY_val);
		static final int LEFTSTICKX_val = 1;
		/**
		 * axis: left x-axis
		 */
		public static final AxisType LX = new AxisType(LEFTSTICKX_val);
		static final int RIGHTSTICKY_val = 4;
		/**
		 * axis: right y-axis
		 */
		public static final AxisType RY = new AxisType(RIGHTSTICKY_val);
		static final int RIGHTSTICKX_val = 5;
		/**
		 * axis: right x-axis
		 */
		public static final AxisType RX = new AxisType(RIGHTSTICKX_val);
		static final int LEFTTRIGGER_val = 2;
		/**
		 * axis: left trigger
		 */
		public static final AxisType LTRIGGER = new AxisType(LEFTTRIGGER_val);
		static final int RIGHTTRIGGER_val = 3;
		/**
		 * axis: right trigger
		 */
		public static final AxisType RTRIGGER = new AxisType(RIGHTTRIGGER_val);
		static final int NumAxis_val = 6;
		/**
		 * axis: number of axis
		 */
		public static final AxisType NumAxis = new AxisType(NumAxis_val);
		/**
		 * The integer value representing this enumeration
		 */
		public final int value;

		private AxisType(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents a digital button on the controller
	 */
	public static class ButtonType {

		static final int A_val = 0;
		/**
		 * button: a
		 */
		public static final ButtonType BA = new ButtonType((A_val));
		static final int B_val = 1;
		/**
		 * button: b
		 */
		public static final ButtonType BB = new ButtonType(B_val);
		static final int X_val = 2;
		/**
		 * button: x
		 */
		public static final ButtonType BX = new ButtonType(X_val);
		static final int Y_val = 3;
		/**
		 * button: y
		 */
		public static final ButtonType BY = new ButtonType(Y_val);
		static final int LEFTBUMPER_val = 4;
		/**
		 * button: left bumper
		 */
		public static final ButtonType BLBUMPER = new ButtonType(LEFTBUMPER_val);
		static final int RIGHTBUMPER_val = 5;
		/**
		 * button: right bumper
		 */
		public static final ButtonType BRBUMPER = new ButtonType(RIGHTBUMPER_val);
		static final int BACK_val = 6;
		/**
		 * button: back
		 */
		public static final ButtonType BBACK = new ButtonType(BACK_val);
		static final int START_val = 7;
		/**
		 * button: start
		 */
		public static final ButtonType BSTART = new ButtonType(START_val);
		static final int LEFTSTICKPRESS_val = 8;
		/**
		 * button: left stick press
		 */
		public static final ButtonType BLEFTSTICKPRESS = new ButtonType(LEFTSTICKPRESS_val);
		static final int RIGHTSTICKPRESS_val = 9;
		/**
		 * button: right stick press
		 */
		public static final ButtonType BRIGHTSTICKPRESS = new ButtonType(RIGHTSTICKPRESS_val);
		static final int NumButton_val = 10;
		/**
		 * button: num button types
		 */
		public static final ButtonType NumButton = new ButtonType((NumButton_val));
		/**
		 * The integer value representing this enumeration
		 */
		public final int value;

		private ButtonType(int value) {
			this.value = value;
		}
	}

	/**
	 * Represents a rumble output on the controller
	 */
	public static class RumbleType {

		static final int kLeftRumble_val = 0;
		/**
		 * Left Rumble
		 */
		public static final RumbleType kLeftRumble = new RumbleType((kLeftRumble_val));
		static final int kRightRumble_val = 1;
		/**
		 * Right Rumble
		 */
		public static final RumbleType kRightRumble = new RumbleType(kRightRumble_val);
		/**
		 * The integer value representing this enumeration
		 */
		public final int value;

		private RumbleType(int value) {
			this.value = value;
		}
	}
}
