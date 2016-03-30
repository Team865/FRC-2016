package deng;

import java.io.File;

import ca.warp7.robot.Constants;
import ca.warp7.robot.hardware.MotorGroup;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;

public class Deng extends SampleRobot {
	private MotorGroup lM, rM;
	private Joystick joy;
	public void robotInit() {
		joy = new Joystick(0);
		lM = new MotorGroup(Constants.LEFT_DRIVE_MOTOR_PINS, VictorSP.class);
		rM = new MotorGroup(Constants.RIGHT_DRIVE_MOTOR_PINS, VictorSP.class);
		// 3 Waypoints
		Waypoint[] points = new Waypoint[] {
		    new Waypoint(-4, -1, Pathfinder.d2r(-45)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
		    new Waypoint(-2, -2, 0),                        // Waypoint @ x=-2, y=-2, exit angle=0 radians
		    new Waypoint(0, 0, 0)                           // Waypoint @ x=0, y=0,   exit angle=0 radians
		};

		// Create the Trajectory Configuration
		//
		// Arguments:
		// Fit Method:          HERMITE_CUBIC or HERMITE_QUINTIC
		// Sample Count:        SAMPLES_HIGH (100 000)
//		                      SAMPLES_LOW  (10 000)
//		                      SAMPLES_FAST (1 000)
		// Time Step:           0.05 Seconds
		// Max Velocity:        1.7 m/s
		// Max Acceleration:    2.0 m/s/s
		// Max Jerk:            60.0 m/s/s/s
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.05, 1.7, 2.0, 60.0);

		// Generate the trajectory
		Trajectory trajectory = Pathfinder.generate(points, config);
		Pathfinder.writeToCSV(new File("/home/lvuser/traj.csv"), trajectory);
		System.out.println("traj generated");
	}
	public void operatorControl() {
		lM.set(-1);
		rM.set(1);
		while(isOperatorControl() && isEnabled()) {
			lM.set(-lM.get());
			rM.set(-lM.get());
			Timer.delay(0.5);
		}
	}
}
