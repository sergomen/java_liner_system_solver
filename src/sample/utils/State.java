package sample.utils;

import java.io.Serializable;

/**
 * Class include step and solution of linear equation;
 * Each step include velocity, acceleration & noise as
 * solution result, and velocity_noise as velocity error
 * fiction.
 */
public class State implements Serializable {
	public double velocity;
	public double acceleration;
	public double noise;
	public long time;
	public double velocity_noise;

	public State(double[][] state, long time, double velocity_noise) {
		this.velocity = state[0][0];
		this.acceleration = state[1][0];
		this.noise = state[2][0];

		this.time = time;
		this.velocity_noise = velocity_noise;
	}
}
