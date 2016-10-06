package Settings;

/**
 * Created by Georg Mayur on 23.09.16.
 * Container for solver settings.
 */
public enum Parameters {
	solve_time(5400),
	time_delta(1),

	gravity_const(9.8),
	earth_radius(6371000),

	velocity_interval_noise(1),
	noise_interval_noise(1.0 * Math.pow(10, -4)),

	optimum_find_steps(10),

	shift_between_direct_and_reverse_filtrate_plots(0),

	Q_direct_optimal(0),
	Q_reverse_optimal(0);

	private double value;

	Parameters(double value) {
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
}
