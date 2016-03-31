package ca.warp7.robot.autonomous;

import ca.warp7.robot.Constants;
import ca.warp7.robot.autonomous.autoModuals.DefenceModules;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public abstract class AutonomousBase {

	protected boolean moving;
	protected int defence;
	protected boolean hardstop;
	protected boolean rangedShooting;
	protected boolean shooting;
	protected boolean twoBall;

	protected final int NO_DEFENCE = 0;
	protected final int LOWBAR = 1;
	protected final int CHIVAL = 2;
	protected final int PORTCULUS = 3;
	protected final int ROUGH_TERRAIN = 4;
	protected final int RAMPARTS = 5;
	protected final int MOAT = 6;
	protected final int ROCK_WALL = 7;

	protected AutonomousBase() {
		moving = false;
		defence = NO_DEFENCE;
		hardstop = false;
		rangedShooting = false;
		shooting = false;
		twoBall = false;

		updateModularInfo();
	}

	public abstract void periodic(Drive drive, Shooter shooter, Intake intake);

	public double getWantedRPM() {
		return 0.0;
	}

	public boolean isFiring() {
		return false;
	}

	private void updateModularInfo() {
		// TODO make this interact with some human interface
		moving = true;
		defence = 0;
		hardstop = true;
		rangedShooting = false;
		shooting = true;
		twoBall = false;
	}

	protected void doDefence(Drive drive, Intake intake) {
		switch (defence) {
		case LOWBAR:
			DefenceModules.lowbar(drive);
			break;
		case CHIVAL:
			DefenceModules.chival(drive, intake);
			break;
		case PORTCULUS:
			DefenceModules.portculus(drive, intake);
			break;
		case ROUGH_TERRAIN:
			DefenceModules.roughTerrain(drive);
			break;
		case RAMPARTS:
			DefenceModules.ramparts(drive);
			break;
		case MOAT:
			DefenceModules.moat(drive);
			break;
		case ROCK_WALL:
			DefenceModules.rockWall(drive);
			break;
		}
	}

	protected double getDefenceDirection() {
		switch (defence) {
		case LOWBAR:
			return Constants.BATTERY;
		case CHIVAL:
			return Constants.INTAKE;
		case PORTCULUS:
			return Constants.INTAKE;
		case ROUGH_TERRAIN:
			return Constants.BATTERY;
		case RAMPARTS:
			return Constants.BATTERY;
		case MOAT:
			return Constants.BATTERY;
		case ROCK_WALL:
			return Constants.BATTERY;
		default:
			return Constants.BATTERY;
		}
	}
}