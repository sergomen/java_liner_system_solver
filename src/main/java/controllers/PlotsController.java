package controllers;


import DAO.RowState;
import DAO.State;
import Settings.Parameters;
import equation_solvers.BaseSolver;
import equation_solvers.KalmansFilter;
import equation_solvers.ReverseKalmansFilter;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Main;
import utils.StateKeep;

public class PlotsController {

	private Stage mainStage;

	private final NumberAxis timeAxis = new NumberAxis();
	private final NumberAxis velocityAxis = new NumberAxis();
	private final NumberAxis angleAxis = new NumberAxis();
	private final NumberAxis noiseAxis = new NumberAxis();

	private LineChart<Number, Number> velocity_plot;
	private LineChart<Number, Number> angle_plot;
	private LineChart<Number, Number> noise_plot;

	private XYChart.Series<Number, Number> velocity;
	private XYChart.Series<Number, Number> angle;
	private XYChart.Series<Number, Number> noise;
	private XYChart.Series<Number, Number> noised_velocity;

	private XYChart.Series<Number, Number> filtered_velocity;
	private XYChart.Series<Number, Number> filtered_angle;
	private XYChart.Series<Number, Number> filtered_noise;

	private XYChart.Series<Number, Number> reverse_filtered_velocity;
	private XYChart.Series<Number, Number> reverse_filtered_angle;
	private XYChart.Series<Number, Number> reverse_filtered_noise;

	private XYChart.Series<Number, Number> unshifted_filtered_noise;

	private final int solveTime = (int) Parameters.solve_time.getValue(); // TODO:set with GUT
	private final int timeDelta = (int) Parameters.time_delta.getValue();
	private final double frequency = 1.0 / solveTime;

	@FXML
	private VBox graphs;

	public void initialize(Stage mainStage) {
		this.mainStage = mainStage;

		// set labels
		this.timeAxis.setLabel("time, s");
		this.velocityAxis.setLabel("velocity, m/s");
		this.angleAxis.setLabel("angle, rad");
		this.noiseAxis.setLabel("drift");

		// make empty charts to append it to plots
		this.velocity = new XYChart.Series<>();
		this.velocity.setName("velocity");
		this.noised_velocity = new XYChart.Series<>();
		this.noised_velocity.setName("noised velocity");
		this.filtered_velocity = new XYChart.Series<>();
		this.filtered_velocity.setName("filtered velocity");
		this.reverse_filtered_velocity = new XYChart.Series<>();
		this.reverse_filtered_velocity.setName("reverse filtered velocity");

		this.angle = new XYChart.Series<>();
		this.angle.setName("angle");
		this.filtered_angle = new XYChart.Series<>();
		this.filtered_angle.setName("filtrate angle");
		this.reverse_filtered_angle = new XYChart.Series<>();
		this.reverse_filtered_angle.setName("filtrate angle");

		this.noise = new XYChart.Series<>();
		this.noise.setName("noise");
		this.filtered_noise = new XYChart.Series<>();
		this.filtered_noise.setName("filtrate noise");
		this.reverse_filtered_noise = new XYChart.Series<>();
		this.reverse_filtered_noise.setName("filtrate noise");

		this.unshifted_filtered_noise = new XYChart.Series<>();
		this.unshifted_filtered_noise.setName("unshifted noise");

		this.velocity_plot = new LineChart<>(timeAxis, velocityAxis);
		this.angle_plot = new LineChart<>(timeAxis, angleAxis);
		this.noise_plot = new LineChart<>(timeAxis, noiseAxis);

		// velocity noised append first for first plotting on grid
		this.velocity_plot.getData().add(noised_velocity);
		this.velocity_plot.getData().add(filtered_velocity);
		this.velocity_plot.getData().add(reverse_filtered_velocity);
		this.velocity_plot.getData().add(velocity);

		this.angle_plot.getData().add(angle);
		this.angle_plot.getData().add(filtered_angle);
		this.angle_plot.getData().add(reverse_filtered_angle);

		this.noise_plot.getData().add(noise);
		this.noise_plot.getData().add(filtered_noise);
		this.noise_plot.getData().add(reverse_filtered_noise);
		this.noise_plot.getData().add(unshifted_filtered_noise);

		this.velocity_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, velocityAxis));
		this.angle_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, angleAxis));
		this.noise_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, noiseAxis));

		// static set width, sad but true
		this.velocity_plot.setPrefWidth(900);
		this.angle_plot.setPrefWidth(900);
		this.noise_plot.setPrefWidth(900);

		// don't create points to plots
		this.velocity_plot.setCreateSymbols(false);
		this.angle_plot.setCreateSymbols(false);
		this.noise_plot.setCreateSymbols(false);
	}

	public void close() {
		this.mainStage.close();
	}

	public void solve() {
		Main.log(PlotsController.class.getName(), "solve");
		clear();

		// add plots to graph pane
		if (!graphs.getChildren().contains(velocity_plot)) {
			graphs.getChildren().add(velocity_plot);
		}
		if (!graphs.getChildren().contains(angle_plot)) {
			graphs.getChildren().add(angle_plot);
		}
		if (!graphs.getChildren().contains(noise_plot)) {
			graphs.getChildren().add(noise_plot);
		}

		// Make generator for iteration solve
		BaseSolver baseSolver = new BaseSolver(0, 0, 0, frequency);

		// If StateKeep is empty try to deserialize or fill and serialize it.
		if (StateKeep.getRowStates() == null) {
			if (!StateKeep.deserialize()) {
				for (int i = 0; i < solveTime; i++)
					StateKeep.addState(baseSolver.next(timeDelta));
				StateKeep.serialize();
			}
		}

		// Load states from StateKeep to plots
		for (RowState rowState : StateKeep.getRowStates()) {
			velocity.getData().add(new XYChart.Data(rowState.time, rowState.velocity));
			noised_velocity.getData().add(new XYChart.Data(rowState.time, rowState.velocity + rowState.velocity_noise));
			angle.getData().add(new XYChart.Data(rowState.time, rowState.angle));
			noise.getData().add(new XYChart.Data(rowState.time, rowState.noise));
		}
	}

	public void filtrate_reverse() {
		Main.log(PlotsController.class.getName(), "run reversed Kalman's filter");

		reverse_filtered_velocity.getData().clear();
		reverse_filtered_angle.getData().clear();
		reverse_filtered_noise.getData().clear();

		if (StateKeep.getRowStates() == null) {
			Main.log(PlotsController.class.getName(), "there is no states to filtrate");
			return;
		} else if (!graphs.getChildren().contains(velocity_plot) ||
			!graphs.getChildren().contains(angle_plot) ||
			!graphs.getChildren().contains(noise_plot)) {
			Main.log(PlotsController.class.getName(), "there is no plots to draw");
			return;
		}

		// permanently define R
		final double R = Parameters.velocity_interval_noise.getValue(); // TODO: variate instead set absolute true

		// variate params for find optimal Q value
		double Q_optimal = 0.0;

		int minimal_degree = 10;
		int degree_step = 1;
		int optimum_find_steps = (int) Parameters.optimum_find_steps.getValue();

		// minimal difference between all raw data and all filtrate data
		double minimal_error = 0.0;

		// Make generator for iteration filtrate
		for (int i = 0; i < optimum_find_steps; i++) {
			// average_error is middle error between all row and filtrate data
			double average_error = 1;
			int summary_numbers = StateKeep.getRowStates().size();

			double Q = Math.pow(10, -1 * (minimal_degree + i * degree_step));
			ReverseKalmansFilter filterSolver = new ReverseKalmansFilter(R, Q);

			for (int j = (summary_numbers - 1); j > 0; j--) {
				RowState rowState = StateKeep.getRowStates().get(j);
				State state = filterSolver.next(rowState);

				summary_numbers++;
				average_error += (rowState.noise - state.noise) * (rowState.noise - state.noise);
			}

			average_error = Math.sqrt(average_error);
			average_error /= summary_numbers;

			if (average_error < minimal_error || minimal_error == 0.0) {
				minimal_error = average_error;
				Q_optimal = Q;

				Main.err(PlotsController.class.getName(), "minimal result error:" + average_error + "\tQ:" + Q);
			} else {
				Main.err(PlotsController.class.getName(), "not minimal result error:" + average_error + "\tQ:" + Q);
			}
		}

		Main.err(PlotsController.class.getName(), "minimal error:" + minimal_error + "\toptimal Q:" + Q_optimal);
		ReverseKalmansFilter filterSolver = new ReverseKalmansFilter(R, Q_optimal);

		//Load states from StateKeep to plots
		for (int j = (StateKeep.getRowStates().size() - 1); j > 0; j--) {
			RowState rowState = StateKeep.getRowStates().get(j);
			State state = filterSolver.next(rowState);

			reverse_filtered_velocity.getData().add(new XYChart.Data(rowState.time, state.velocity));
			reverse_filtered_angle.getData().add(new XYChart.Data(rowState.time, state.angle));
			reverse_filtered_noise.getData().add(new XYChart.Data(rowState.time, state.noise));
		}

		/* Q_reverse_optimal need to find filtrate & reverse filtrate
		* plots shift from original
		* */
		Parameters.Q_reverse_optimal.setValue(Q_optimal);
	}

	public void filtrate() {
		Main.log(PlotsController.class.getName(), "run Kalman's filter");

		filtered_velocity.getData().clear();
		filtered_angle.getData().clear();
		filtered_noise.getData().clear();

		if (StateKeep.getRowStates() == null) {
			Main.log(PlotsController.class.getName(), "there is no states to filtrate");
			return;
		} else if (!graphs.getChildren().contains(velocity_plot) ||
			!graphs.getChildren().contains(angle_plot) ||
			!graphs.getChildren().contains(noise_plot)) {
			Main.log(PlotsController.class.getName(), "there is no plots to draw");
			return;
		}

		// permanently define R
		final double R = Parameters.velocity_interval_noise.getValue(); // TODO: variate instead set true

		// variate params for find optimal Q value
		double Q_optimal = 0.0;

		int minimal_degree = 10;
		int degree_step = 1;
		int optimum_find_steps = (int) Parameters.optimum_find_steps.getValue();

		double minimal_error = 0.0;

		// Make generator for iteration filtrate
		for (int i = 0; i < optimum_find_steps; i++) {
			double average_error = 0;
			int summary_numbers = 0;

			double Q = Math.pow(10, -1 * (minimal_degree + i * degree_step));
			KalmansFilter filterSolver = new KalmansFilter(R, Q);

			for (RowState rowState : StateKeep.getRowStates()) {
				State state = filterSolver.next(rowState);

				summary_numbers++;
				average_error += (rowState.noise - state.noise) * (rowState.noise - state.noise);
			}

			average_error = Math.sqrt(average_error);
			average_error /= summary_numbers;

			if (average_error < minimal_error || minimal_error == 0.0) {
				minimal_error = average_error;
				Q_optimal = Q;

				Main.err(PlotsController.class.getName(), "minimal result error:" + average_error + "\tQ:" + Q);
			} else {
				Main.err(PlotsController.class.getName(), "not minimal result error:" + average_error + "\tQ:" + Q);
			}
		}

		Main.err(PlotsController.class.getName(), "minimal error:" + minimal_error + "\toptimal Q:" + Q_optimal);
		KalmansFilter filterSolver = new KalmansFilter(R, Q_optimal);

		// Load states from StateKeep to plots
		for (RowState rowState : StateKeep.getRowStates()) {
			State state = filterSolver.next(rowState);
			filtered_velocity.getData().add(new XYChart.Data(rowState.time, state.velocity));
			filtered_angle.getData().add(new XYChart.Data(rowState.time, state.angle));
			filtered_noise.getData().add(new XYChart.Data(rowState.time, state.noise));
		}

		/* Q_reverse_optimal need to find filtrate & reverse filtrate
		* plots shift from original
		* */
		Parameters.Q_direct_optimal.setValue(Q_optimal);
	}

	/**
	 * the goal: find time shift between direct and
	 * reverse filtrate plot, the half of that shift is
	 * shift between each filtrate plot and
	 * original noise plot
	 */
	public void find_shift() {

		if (Parameters.Q_direct_optimal.getValue() == 0 || Parameters.Q_reverse_optimal.getValue() == 0) {
			Main.log(PlotsController.class.getName(), "Filtrate in both directions before");
			return;
		}

		double[] direct_noise = new double[StateKeep.getRowStates().size()];
		double[] reverse_noise = new double[StateKeep.getRowStates().size()];

		ReverseKalmansFilter reverseKalmansFilter =
			new ReverseKalmansFilter(Parameters.velocity_interval_noise.getValue(), Parameters.Q_reverse_optimal.getValue());
		KalmansFilter kalmansFilter =
			new KalmansFilter(Parameters.velocity_interval_noise.getValue(), Parameters.Q_direct_optimal.getValue());

		// fill direct and reverse noise arrays
		for (int j = (StateKeep.getRowStates().size() - 1); j > 0; j--) {
			RowState rowState = StateKeep.getRowStates().get(j);
			State state = reverseKalmansFilter.next(rowState);
			reverse_noise[j] = state.noise;
		}
		for (int i = 0; i < StateKeep.getRowStates().size(); i++) {
			RowState rowState = StateKeep.getRowStates().get(i);
			State state = kalmansFilter.next(rowState);
			direct_noise[i] = state.noise;
		}

		/*
		* reverse_filtrate_interval_sum is sum of noise value in
		* interval of second quarter of all solved period;
		*
		* direct_filtrate_interval_sum is sum of noise value in
		* interval with unknown shift from original and reverse filtrates;
		*
		* the goal: find an interval on direct filtrated plot where
		* the difference between reverse_filtrate_interval_sum and
		* direct_filtrate_interval_sum is minimal, the
		* interval between start direct_filtrate_interval_sum and start
		* reverse_filtrate_interval_sum arrays is double time-shift
		* filtrate plots from original
		*/
		double reverse_filtrate_interval_sum;
		double direct_filtrate_interval_sum;

		int reverse_plot_interval_start = (int) (Parameters.solve_time.getValue() / 4);
		int direct_plot_interval_start = 0;
		int interval_delta = (int) (Parameters.solve_time.getValue() / 3);

		Double minimal_shift = null;

		reverse_filtrate_interval_sum = 0;
		for (int i = reverse_plot_interval_start; i < reverse_plot_interval_start + interval_delta; i++) {
			reverse_filtrate_interval_sum += reverse_noise[i];
		}

		for (int i = reverse_plot_interval_start; i < reverse_plot_interval_start + interval_delta; i++) {
			direct_filtrate_interval_sum = 0.0;
			for (int j = i; j < i + interval_delta; j++) {
				direct_filtrate_interval_sum += direct_noise[j];
			}

			double difference = (direct_filtrate_interval_sum - reverse_filtrate_interval_sum > 0) ?
				direct_filtrate_interval_sum - reverse_filtrate_interval_sum :
				(direct_filtrate_interval_sum - reverse_filtrate_interval_sum) * -1.0;

			if (minimal_shift == null || minimal_shift > difference) {
				minimal_shift = difference;
				direct_plot_interval_start = i;
			}
		}

		Parameters.shift_between_direct_and_reverse_filtrate_plots.setValue(
			direct_plot_interval_start - reverse_plot_interval_start
		);

		Main.log(PlotsController.class.getName(),
			"find shift value:" + (direct_plot_interval_start - reverse_plot_interval_start));


		/* Reverse and direct noise filters has shifts from original plot;
		* The main idea is find shift between reverse and direct filter plots,
		* then find shift between each filter plot and original plot as half of shift
		* between filter plots, then count average in interval [start+half_shift, end-half_shift]
		* and draw on this interval reversed original plot. Reversed plot not fully equal to
		* original plot, but very near.
		*/
		unshifted_filtered_noise.getData().clear();

		int shift = (int) Parameters.shift_between_direct_and_reverse_filtrate_plots.getValue();
		int half_shift = shift / 2;

		for (int time = half_shift; time < StateKeep.getRowStates().size() - half_shift; time++) {
			double average_noise = reverse_noise[time - half_shift] + direct_noise[time + half_shift];
			average_noise /= 2;
			unshifted_filtered_noise.getData().add(new XYChart.Data(time, average_noise));
		}

	}

	/**
	 * Clear next plots: velocity, noiced velocity, angle, noise
	 */
	public void clear() {
		Main.log(PlotsController.class.getName(), "clear");
		filtered_velocity.getData().clear();
		filtered_angle.getData().clear();
		filtered_noise.getData().clear();

		reverse_filtered_velocity.getData().clear();
		reverse_filtered_angle.getData().clear();
		reverse_filtered_noise.getData().clear();

		unshifted_filtered_noise.getData().clear();

		velocity.getData().clear();
		noised_velocity.getData().clear();
		angle.getData().clear();
		noise.getData().clear();
	}

	/**
	 * Remove states-serialized file
	 */
	public void drop() {
		StateKeep.drop();
	}

	/**
	 * show mouse-on-plot coordinates on window title
	 *
	 * @param event is mouse event
	 * @param axis  is number axis (y axis usually)
	 */
	private void mouseEvent(MouseEvent event, NumberAxis axis) {
		Point2D mouseSceneCoordinates = new Point2D(event.getSceneX(), event.getSceneY());
		double x = timeAxis.sceneToLocal(mouseSceneCoordinates).getX();
		double y = axis.sceneToLocal(mouseSceneCoordinates).getY();

		mainStage.setTitle(String.format("Time: %1$d Value: %2$,.8f",
			timeAxis.getValueForDisplay(x).intValue(), axis.getValueForDisplay(y).doubleValue()));
	}

}
