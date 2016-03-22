package ca.warp7.robot;

public class Util {
	public static double limit(double val) {
		return limit(val, 1);
	}

	public static double limit(double val, double lim) {
		return Math.max(-lim, Math.min(val, lim));
	}

	public static double correct_angle(double angle) {
		return angle + 360 * Math.floor(0.5 - angle / 360);
	}
}
