package controllers;


import DAO.RowState;
import DAO.State;
import Settings.Parameters;
import equation_solvers.BaseSolver;
import equation_solvers.FilterSolver;
import javafx.application.Application;
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
        this.angleAxis.setLabel("angle, rad/s**2");
        this.noiseAxis.setLabel("noise");

        // make empty charts to append it to plots
        this.velocity = new XYChart.Series<>();
        this.velocity.setName("velocity");
        this.noised_velocity = new XYChart.Series<>();
        this.noised_velocity.setName("noised velocity");
        this.filtered_velocity = new XYChart.Series<>();
        this.filtered_velocity.setName("filtrate velocity");

        this.angle = new XYChart.Series<>();
        this.angle.setName("angle");
        this.filtered_angle = new XYChart.Series<>();
        this.filtered_angle.setName("filtrate angle");

        this.noise = new XYChart.Series<>();
        this.noise.setName("noise");
        this.filtered_noise = new XYChart.Series<>();
        this.filtered_noise.setName("filtrate noise");

        this.velocity_plot = new LineChart<>(timeAxis, velocityAxis);
        this.angle_plot = new LineChart<>(timeAxis, angleAxis);
        this.noise_plot = new LineChart<>(timeAxis, noiseAxis);

        // velocity noised append first for first plotting on grid
        this.velocity_plot.getData().add(noised_velocity);
        this.velocity_plot.getData().add(velocity);
        this.velocity_plot.getData().add(filtered_velocity);

        this.angle_plot.getData().add(angle);
        this.angle_plot.getData().add(filtered_angle);

        this.noise_plot.getData().add(noise);
        this.noise_plot.getData().add(filtered_noise);

        this.velocity_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, velocityAxis));
        this.angle_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, angleAxis));
        this.noise_plot.setOnMouseMoved((MouseEvent event) -> mouseEvent(event, noiseAxis));

        this.velocity_plot.setPrefWidth(900);
        this.angle_plot.setPrefWidth(900);
        this.noise_plot.setPrefWidth(900);

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


    public void filtrate() {
        Main.log(PlotsController.class.getName(), "filtrate");

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

        // Make generator for iteration filtrate
        FilterSolver filterSolver = new FilterSolver();

        // Load states from StateKeep to plots
        for (RowState rowState : StateKeep.getRowStates()) {
            State state = filterSolver.next(rowState);
            filtered_velocity.getData().add(new XYChart.Data(rowState.time, state.velocity));
            filtered_angle.getData().add(new XYChart.Data(rowState.time, state.angle));
            filtered_noise.getData().add(new XYChart.Data(rowState.time, state.noise));
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
