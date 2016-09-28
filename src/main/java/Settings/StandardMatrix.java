package Settings;

/**
 * Created by Georg Mayur on 24.09.16.
 * Contain standard matrix like G and H
 */
public enum StandardMatrix {

    G(new double[][]{
            {0},
            {0},
            {1}
    }),
    H(new double[][]{
            {1, 0, 0},
    });

    private double[][] matrix;

    StandardMatrix(double[][] matrix) {
        this.matrix = matrix;
    }

    public double[][] getMatrix() {
        return matrix;
    }
}
