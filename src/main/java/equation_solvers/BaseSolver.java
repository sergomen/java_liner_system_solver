package equation_solvers;


import DAO.RowState;
import Settings.StandardMatrix;
import utils.Matrix;

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

    private final double velocity_interval_noise = Settings.Parameters.velocity_interval_noise.getValue();
    private final double gravity_const = Settings.Parameters.gravity_const.getValue();
    private final double earth_radius = Settings.Parameters.earth_radius.getValue();

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
    public RowState next(double time_step) {
        this.step++;

        double velocity_noise = (Math.random() - .5) * velocity_interval_noise;

		/* this matrix is transformation matrix between
        * next and previous state */
        double[][] matrixG = new double[][]{
                {1, -gravity_const * time_step, 0},
                {1 / earth_radius, 1, time_step},
                {0, 0, 1}
        };

        double iteration_coefficient = time_step * frequency;
        double filter_value_interval = Settings.Parameters.filter_value_interval.getValue();
        double filter_value = Math.random() * filter_value_interval - filter_value_interval * .5;

		/* filter add angle velocity noise on each step */
        double[][] filter = null;
        if (step != 1) {
            filter = Matrix.matrix_multiplication(
                    StandardMatrix.G.getMatrix(),
                    (filter_value - state[2][0]) * iteration_coefficient);
        }

        if (step == 1) {
            state = new double[][]{
                    {this.startVelocity},
                    {this.startAcceleration},
                    {this.startNoise}
            };
        } else {
            state = Matrix.matrix_addition(Matrix.matrix_multiplication(matrixG, state), filter);
        }

        return new RowState(state, step, velocity_noise);
    }

}