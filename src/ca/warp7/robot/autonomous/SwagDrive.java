package ca.warp7.robot.autonomous;

import static ca.warp7.robot.Warp7Robot.drive;

import java.io.File;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;


public class SwagDrive extends AutonomousBase {

	public SwagDrive() {
		// Imagine the robot starts facing right on the centerline.
		// X+ is forwards
        // Y+ is down (to the right of the robot)
        // X- is backwards
        // Y- is up (to the left of the robot)
        // Positive headings go from forward to right,
        // Negative headings go from forward to left
		Waypoint[] points = new Waypoint[] {
		        new Waypoint(40, 10, 0.244978663),
		        new Waypoint(6.5, 142, Pathfinder.d2r(-90)),
                new Waypoint(6.5, 200, Pathfinder.d2r(-90)),
                new Waypoint(6.5, 120, Pathfinder.d2r(-90))
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
		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.02, 0.1, 2.0, 60.0);

		// Generate the trajectory
		Trajectory trajectory = Pathfinder.generate(points, config);
		
		Pathfinder.writeToCSV(new File("/home/lvuser/traj.csv"), trajectory);

		//Trajectory trajectory = Pathfinder.readFromCSV(new File("/home/lvuser/traj.csv"));
		System.out.println("Trajectories loaded.");
		//drive.setTrajectory(trajectory);
	}
	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		//drive.followPath(); // pls no bork
		//_pool.logDouble("heading", drive.getDesiredHeading());
	}

}
