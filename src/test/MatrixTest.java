import org.junit.Test;
import utils.Matrix;

import static org.junit.Assert.assertEquals;


/**
 * Test Matrix class via extend
 */
public final class MatrixTest extends Matrix {

    @Test
    public void testTranspose() {
        assertEquals(matrix_transpose(new double[][]{{1, 2, 3}}), new double[][]{{1}, {2}, {3}});
        assertEquals(matrix_transpose(new int[][]{{1, 2, 3}}), new int[][]{{1}, {2}, {3}});
        assertEquals(matrix_transpose(new double[][]{{}}), new double[][]{{0}});
        assertEquals(matrix_transpose(new int[][]{{}}), new int[][]{{0}});
    }

    @Test
    public void testMultiplication() {
        assertEquals(matrix_multiplication(new double[][]{{1, 2}, {1, 2}}, new double[][]{{1}, {2}}), new double[][]{{5}, {5}});
        assertEquals(matrix_multiplication(new int[][]{{1, 2}, {1, 2}}, new int[][]{{1}, {2}}), new int[][]{{5}, {5}});
        assertEquals(matrix_multiplication(new double[][]{{1, 2}}, new double[][]{{1, 2}}), new double[][]{{0}});
        assertEquals(matrix_multiplication(new int[][]{{1, 2}}, new int[][]{{1, 2}}), new int[][]{{0}});

        assertEquals(matrix_multiplication(new double[][]{{1, 2}}, 2), new double[][]{{2.0, 4.0}});
        assertEquals(matrix_multiplication(new double[][]{{1}, {2}}, 2), new double[][]{{2.0}, {4.0}});
    }

    @Test
    public void testAddition() {
        assertEquals(matrix_addition(new double[][]{{1, 2}}, new double[][]{{3, 6}}), new double[][]{{4, 8}});
        assertEquals(matrix_addition(new int[][]{{1, 2}}, new int[][]{{3, 6}}), new int[][]{{4, 8}});
        assertEquals(matrix_addition(new double[][]{{1, 2}}, new double[][]{{1, 2, 3}}), new double[][]{{0}});
        assertEquals(matrix_addition(new int[][]{{1, 2}}, new int[][]{{1, 2, 3}}), new int[][]{{0}});
    }

    @Test
    public void testPrintMatrix() {
        matrix_print(new double[][]{{1, 2}});
        matrix_print(new double[][]{{}});
        matrix_print(new int[][]{{1, 2}});
        matrix_print(new int[][]{{}});
    }
}
