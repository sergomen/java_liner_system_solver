package sample.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import sample.Main;
import sample.equation_solvers.BaseSolver;
import sample.utils.State;
import sample.utils.StateKeep;

public class PlotsController {

	private Stage mainStage;
	private LineChart<Number, Number> velocity_plot;
	private XYChart.Series<Number, Number> velocity;
	private XYChart.Series<Number, Number> velocity_noised;

	private LineChart<Number, Number> acceleration_plot;
	private XYChart.Series<Number, Number> acceleration;

	private LineChart<Number, Number> noise_plot;
	private XYChart.Series<Number, Number> noise;

	private final NumberAxis timeAxis = new NumberAxis();
	private final NumberAxis velocityAxis = new NumberAxis();
	private final NumberAxis accelerationAxis = new NumberAxis();
	private final NumberAxis noiseAxis = new NumberAxis();

	private BaseSolver baseSolver = null;
	private final int solveTime = 5400; // TODO:set with GUT
	private final int timeDelta = 1; // TODO:set with GUI
	private final double frequency = 1.0 / solveTime;

	@FXML
	private VBox graphs;

	public void initialize(Stage mainStage) {
		this.mainStage = mainStage;

		this.timeAxis.setLabel("time, s");
		this.velocityAxis.setLabel("velocity, m/s");
		this.accelerationAxis.setLabel("acceleration, rad/s**2");
		this.noiseAxis.setLabel("noise");

		this.velocity = new XYChart.Series<>();
		this.velocity.setName("velocity");
		this.velocity_noised = new XYChart.Series<>();
		this.velocity_noised.setName("noised velocity");

		this.acceleration = new XYChart.Series<>();
		this.acceleration.setName("acceleration");

		this.noise = new XYChart.Series<>();
		this.noise.setName("noise");

		this.velocity_plot = new LineChart<>(timeAxis, velocityAxis);
		this.acceleration_plot = new LineChart<>(timeAxis, accelerationAxis);
		this.noise_plot = new LineChart<>(timeAxis, noiseAxis);

		// velocity noised append first for first plotting on grid
		this.velocity_plot.getData().add(velocity_noised);
		this.velocity_plot.getData().add(velocity);
		this.acceleration_plot.getData().add(acceleration);
		this.noise_plot.getData().add(noise);

		this.velocity_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, velocityAxis));
		this.acceleration_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, accelerationAxis));
		this.noise_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, noiseAxis));

		this.velocity_plot.setPrefWidth(900);
		this.acceleration_plot.setPrefWidth(900);
		this.noise_plot.setPrefWidth(900);

		this.velocity_plot.setCreateSymbols(false);
		this.acceleration_plot.setCreateSymbols(false);
		this.noise_plot.setCreateSymbols(false);
	}

	public void close() {
		this.mainStage.close();
	}

	public void solve() {
		Main.log(PlotsController.class.getName(), "solve");

		// add plots to graph pane
		if (!graphs.getChildren().contains(velocity_plot)) {
			graphs.getChildren().add(velocity_plot);
		}
		if (!graphs.getChildren().contains(acceleration_plot)) {
			graphs.getChildren().add(acceleration_plot);
		}
		if (!graphs.getChildren().contains(noise_plot)) {
			graphs.getChildren().add(noise_plot);
		}

		// is it first start
		baseSolver = new BaseSolver(0, 0, 0, frequency);

		// If StateKeep is empty try to deserialize or fill and serialize it.
		if (StateKeep.getStates() == null) {
			if (!StateKeep.deserialize()) {
				for (int i = 0; i < solveTime; i++)
					StateKeep.addState(baseSolver.next(timeDelta));
				StateKeep.serialize();
			}
		}

		// Load states from StateKeep to plots
		for (State state : StateKeep.getStates()) {
			velocity.getData().add(new XYChart.Data(state.time, state.velocity));
			velocity_noised.getData().add(new XYChart.Data(state.time, state.velocity + state.velocity_noise));
			acceleration.getData().add(new XYChart.Data(state.time, state.acceleration));
			noise.getData().add(new XYChart.Data(state.time, state.noise));
		}
	}


	public void resolve() {
	}


	/**
	 * Clear next plots: velocity, noiced velocity, acceleration, noise
	 */
	public void clear() {
		Main.log(PlotsController.class.getName(), "clear");
		velocity.getData().clear();
		velocity_noised.getData().clear();
		acceleration.getData().clear();
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
