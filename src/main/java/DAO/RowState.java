package DAO;

import java.io.Serializable;

/**
 * Class contain solution of linear equations;
 * Each step include velocity, angle & noise as
 * solution result, and velocity_noise as random
 * velocity error.
 */
public class RowState extends State implements Serializable {
    public long time;
    public double velocity_noise;

    public RowState(double[][] state, long time, double velocity_noise) {
        super(state);
        this.time = time;
        this.velocity_noise = velocity_noise;
    }
}
