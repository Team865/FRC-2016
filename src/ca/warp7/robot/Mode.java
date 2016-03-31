package ca.warp7.robot;

public enum Mode {
	STANDARD(0), BALL_IN_INTAKE(1), FOUND_TARGET(2), AIMED_AT_TARGET(3), READY_TO_FIRE(4);

	public final int code;

	Mode(int i) {
		this.code = i;
	}
}
