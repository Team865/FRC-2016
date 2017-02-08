package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import edu.wpi.first.wpilibj.Timer;

public class DefenceModules{

	private static int step = 1;
	
	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean lowbar(Drive drive) {
		// The distance from one bottom of ramp to the other
		final double LOWBAR_WIDTH = 0.0;
		return BasicModules.move(LOWBAR_WIDTH, drive);
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean chival(Drive drive, Intake intake) {
		final double CHIVAL_PRT_1 = 0.0;
		final double CHIVAL_PRT_2 = 0.0;
		switch(step){
		case 1:
			boolean finished = BasicModules.move(CHIVAL_PRT_1, drive);
			if(finished){
				step++;
			}
			return false;
		case 2:
			intake.raisePortculus(false);
			Timer.delay(0.5);
			step++;
			return false;
		case 3:
			boolean finished2 = BasicModules.move(CHIVAL_PRT_2, drive);
			if(finished2){
				step = 1;
			}
			return finished2;
		default:
			return false;
		}
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean portculus(Drive drive, Intake intake) {
		final double PORTCULUS_WIDTH = 0.0;
		switch(step){
		case 1:
			intake.raisePortculus(false);
			Timer.delay(0.5);
			step++;
			return false;
		case 2:
			boolean finished = BasicModules.move(PORTCULUS_WIDTH, drive);
			if(finished){
				step = 1;
			}
			return finished;
			default:
				return false;
		}
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean roughTerrain(Drive drive) {
		final double ROUGH_TERRAIN_WIDTH = 0.0;
		return BasicModules.move(ROUGH_TERRAIN_WIDTH, drive);
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean ramparts(Drive drive) {
		final double RAMPARTS_WIDTH = 0.0;
		return BasicModules.move(RAMPARTS_WIDTH, drive);
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean moat(Drive drive) {
		final double MOAT_WIDTH = 0.0;
		return BasicModules.move(MOAT_WIDTH, drive);

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static boolean rockWall(Drive drive) {
		final double ROCK_WALL_WIDTH = 0.0;
		return BasicModules.move(ROCK_WALL_WIDTH, drive);

	}
}
