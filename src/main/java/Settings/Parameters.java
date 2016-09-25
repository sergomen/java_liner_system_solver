package Settings;

/**
 * Created by Georg Mayur on 23.09.16.
 * Container for solver settings.
 */
public enum Parameters {
    velocity_interval_noise(1),
    gravity_const(9.8),
    earth_radius(6371000),
    filter_value_interval(1.0 * Math.pow(10, -4));

    private double value;

    Parameters(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }
}