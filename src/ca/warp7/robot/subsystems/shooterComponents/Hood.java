package ca.warp7.robot.subsystems.shooterComponents;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CANTalon.FeedbackDevice;
import edu.wpi.first.wpilibj.CANTalon.TalonControlMode;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Hood extends IterativeRobot {

	// Used
	// https://github.com/CrossTheRoadElec/FRC-Examples/blob/master/JAVA_PositionClosedLoop/src/org/usfirst/frc/team469/robot/Robot.java

	CANTalon hood;
	StringBuilder sb;
	int loops;

	public Hood(CANTalon hood) {
		this.hood = hood;
		sb = new StringBuilder();
		loops = 0;

		/*
		 * lets grab the 360 degree position of the MagEncoder's absolute
		 * position
		 */
		int absolutePosition = hood.getPulseWidthPosition()
				& 0xFFF; /*
							 * mask out the bottom12 bits, we don't care about
							 * the wrap arounds
							 */
		/* use the low level API to set the quad encoder signal */
		hood.setEncPosition(absolutePosition);

		/* choose the sensor and sensor direction */
		hood.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
		hood.reverseSensor(false);
		// hood.configEncoderCodesPerRev(XXX), // if using
		// FeedbackDevice.QuadEncoder
		// hood.configPotentiometerTurns(XXX), // if using
		// FeedbackDevice.AnalogEncoder or AnalogPot

		/* set the peak and nominal outputs, 12V means full */
		hood.configNominalOutputVoltage(+0f, -0f);
		hood.configPeakOutputVoltage(+12f, -12f);
		/*
		 * set the allowable closed-loop error, Closed-Loop output will be
		 * neutral within this range. See Table in Section 17.2.1 for native
		 * units per rotation.
		 */
		hood.setAllowableClosedLoopErr(0); /* always servo */
		/* set closed loop gains in slot0 */
		hood.setProfile(0);
		hood.setF(0.0);
		hood.setP(0.01);
		hood.setI(0.0);
		hood.setD(0.0);

		hood.changeControlMode(TalonControlMode.Position);
	}

	public void set(double degrees) {
		double targetPositionRotations = degrees / 360;
		hood.set(targetPositionRotations);
	}
}
