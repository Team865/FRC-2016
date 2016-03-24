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
	
	
	protected AutonomousBase(){
		moving = false;
		defence = NO_DEFENCE;
		hardstop = false;
		rangedShooting = false;
		shooting = false;
		twoBall = false;
		
		getModularInfo();
	}
	
	public abstract void periodic(Drive drive, Shooter shooter, Intake intake);

	public double getWantedRPM(){
		return  0.0;
	}

	public boolean isFiring(){
		return false;
	}
	
	private void getModularInfo(){
		//TODO make this interact with some human interface
		moving = true;
		defence = 0;
		hardstop = true;
		//rangedShooting = true;
		shooting = true;
		//twoBall = true;
	}
	
	protected void doDefence(){
		switch (defence) {
		case LOWBAR:
			DefenceModules.lowbar();
			break;
		case CHIVAL:
			DefenceModules.chival();
			break;
		case PORTCULUS:
			DefenceModules.portculus();
			break;
		case ROUGH_TERRAIN:
			DefenceModules.roughTerrain();
			break;
		case RAMPARTS:
			DefenceModules.ramparts();
			break;
		case MOAT:
			DefenceModules.moat();
			break;
		case ROCK_WALL:
			DefenceModules.rockWall();
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