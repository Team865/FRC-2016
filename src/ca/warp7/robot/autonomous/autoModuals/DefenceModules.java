package ca.warp7.robot.autonomous.autoModuals;

import ca.warp7.robot.subsystems.Drive;

public class DefenceModules extends ModuleBase{

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void lowbar(Drive drive) {
		// The distance from one bottom of ramp to the other
		final double LOWBAR_WIDTH = 0.0;
		BasicModules.move(LOWBAR_WIDTH, drive);
	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void chival() {

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void portculus() {

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void roughTerrain() {

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void ramparts() {

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void moat() {

	}

	/**
	 * Used to cross this defense from the base of it assuming you are aligned
	 */
	public static void rockWall() {

	}
}
