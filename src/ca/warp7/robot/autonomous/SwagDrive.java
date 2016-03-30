package ca.warp7.robot.autonomous;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import jaci.pathfinder.Pathfinder;
import jaci.pathfinder.Trajectory;
import jaci.pathfinder.Waypoint;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;
import static ca.warp7.robot.Constants.*;
import static ca.warp7.robot.Warp7Robot.drive;


public class SwagDrive extends AutonomousBase {

	public SwagDrive() {
		Waypoint[] points = new Waypoint[] {
			    new Waypoint(-0.5, -0.5, Pathfinder.d2r(35)),      // Waypoint @ x=-4, y=-1, exit angle=-45 degrees
			    new Waypoint(-4.826, 0, Pathfinder.d2r(-90)),   // at start of outerworks, 190in
			    new Waypoint(-7.62, 0, Pathfinder.d2r(-90)),    // 300 in
			    new Waypoint(-4.826, 0, Pathfinder.d2r(-90)), // back out
			};

			Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC, Trajectory.Config.SAMPLES_HIGH, 0.001, 1.7, 2.0, 60.0);
			Trajectory trajectory = Pathfinder.generate(points, config);
			//Pathfinder.writeToCSV(new File("trajectory.csv"), trajectory);
			TankModifier modifier = new TankModifier(trajectory).modify(WHEELBASE_WIDTH);
			
			EncoderFollower left = new EncoderFollower(modifier.getLeftTrajectory());
			EncoderFollower right = new EncoderFollower(modifier.getRightTrajectory());
			drive.setEncoderFollowers(left, right);
	}
	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		//drive.followPath(); // pls no bork
	}

}
