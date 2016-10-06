package DAO;

import java.io.Serializable;

/**
 * Created by Georg Mayur on 24.09.16.
 * Filtered system state
 */
public class State implements Serializable {
	public double velocity;
	public double angle;
	public double noise;

	public State(double[][] state) {
		this.velocity = state[0][0];
		this.angle = state[1][0];
		this.noise = state[2][0];
	}
}
