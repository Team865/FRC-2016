package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import static ca.warp7.robot.Warp7Robot.drive;

import java.io.File;


public class SwagDrive extends AutonomousBase {

	public SwagDrive() {
		Waypoint[] points = new Waypoint[] {
		    new Waypoint(-0.5, -0.5, 0), // Waypoint @ x=-2, y=-2, exit angle=0 radians
		    new Waypoint(0, 0, 0) // Waypoint @ x=0, y=0,   exit angle=0 radians
		};

		// Create the Trajectory Configuration
		//
		// Arguments:
		// Fit Method:          HERMITE_CUBIC or HERMITE_QUINTIC
		// Sample Count:        SAMPLES_HIGH (100 000)
//		                      SAMPLES_LOW  (10 000)
//		                      SAMPLES_FAST (1 000)
		// Time Step:           1/50 Seconds
		// Max Velocity:        0.1 m/s
		// Max Acceleration:    2.0 m/s/s
		// Max Jerk:            60.0 m/s/s/s
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 1/50, 0.1, 2.0, 60.0);

		// Generate the trajectory
		Trajectory trajectory = Pathfinder.generate(points, config);
		
		Pathfinder.writeToCSV(new File("/home/lvuser/traj.csv"), trajectory);
		//Trajectory trajectory = Pathfinder.readFromCSV(new File("/home/lvuser/traj.csv"));
		System.out.println("Trajectories loaded.");
		drive.setTrajectory(trajectory);
	}
	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		drive.followPath(); // pls no bork
		//_pool.logDouble("heading", drive.getDesiredHeading());
	}

}
