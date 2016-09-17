package sample.equation_solvers;

import sample.utils.Matrix;
import sample.utils.State;

/**
 * Base solver is class realized generator to set
 * linear system state at first time moment, and
 * return new system state with each next() call
 * Created by Georg Mayur on 11.09.16.
 */
public class BaseSolver {

	private double startVelocity;
	private double startAcceleration;
	private double startNoise;
	private double frequency;

	// constant velocity noise interval: 10^-6 todo: check once
	final double velocity_interval_noice = 0.0001;
	final double gravity_const = 9.8;
	final double earth_radius = 6371000;

	// next() calls counter
	private int step = 0;

	// state is result last solution linear equations system
	private double[][] state;

	public BaseSolver(double startVelocity, double startAcceleration, double startNoise, double frequency) {
		this.startVelocity = startVelocity;
		this.startAcceleration = startAcceleration;
		this.startNoise = startNoise;
		this.frequency = frequency;
	}

	/**
	 * solve and return next linear system state
	 *
	 * @param time_step is solution step
	 * @return new system solution state
	 */
	public State next(double time_step) {
		this.step++;

		double velocity_noise = (Math.random() - .5) * velocity_interval_noice;

		/* this matrix is transformation matrix between
		* next and previous state */
		double[][] matrixG = new double[][]{
				{1, -gravity_const * time_step, 0},
				{1 / earth_radius, 1, time_step},
				{0, 0, 1}
		};

		double iteration_coefficient = time_step * frequency;
		double filter_value_interval = 1.0 * Math.pow(10, -8);
		double filter_value = Math.random() * filter_value_interval - filter_value_interval * .5;

		/* filter add angle velocity noise on each step */
		double[][] filter = null;
		if (step != 1) {
			filter = new double[][]{
					{0},
					{0},
					{(filter_value - state[2][0]) * iteration_coefficient} // fixme: this is shity filter
			};
		}

		if (step == 1) {
			state = new double[][]{
					{this.startVelocity},
					{this.startAcceleration},
					{this.startNoise}
			};
		} else {
			state = Matrix.addition(Matrix.multiplication(matrixG, state), filter);
		}

		return new State(state, step, velocity_noise);
	}

}