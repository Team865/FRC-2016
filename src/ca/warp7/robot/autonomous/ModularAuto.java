package ca.warp7.robot.autonomous;

import ca.warp7.robot.Constants;
import ca.warp7.robot.autonomous.autoModuals.AdvancedModules;
import ca.warp7.robot.autonomous.autoModuals.BasicModules;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;

public class ModularAuto extends AutonomousBase {

	protected double wantedRPM;
	protected boolean isFiring;

	public ModularAuto(Drive drive, Shooter shooter, Intake intake) {
		super();
		wantedRPM = 0.0;
		isFiring = false;
	}

	@Override
	public void periodic(Drive drive, Shooter shooter, Intake intake) {
		if (moving) {
			BasicModules.move(Constants.DISTANCE_TO_DEFENCE_BASE * getDefenceDirection());
			if (defence != NO_DEFENCE) {
				doDefence();
				if (hardstop) {
					// AdvancedModules.turnToGoal(direction, drive);
					// BasicModules.move(distance);
					if (shooting) {
						AdvancedModules.hardstopShoot(shooter, intake);
					}
				} else if (rangedShooting) {
					if (shooting) {
						if (twoBall) {

						}
					}
				}
			}
		}
	}

	@Override
	public double getWantedRPM() {
		return wantedRPM;
	}

	@Override
	public boolean isFiring() {
		return isFiring;
	}

}
