package ca.warp7.robot.hardware.controlerSettings;

import ca.warp7.robot.hardware.XboxController;
import ca.warp7.robot.subsystems.Climber;
import ca.warp7.robot.subsystems.Drive;
import ca.warp7.robot.subsystems.Intake;
import ca.warp7.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;

public abstract class ControllerSettings {

	abstract public void init(Drive drive);

	abstract public void periodic(XboxController driver, XboxController operator, Shooter shooter, Intake intake,
			Drive drive, DigitalInput photosensor, Climber climber, Compressor compressor);
}
