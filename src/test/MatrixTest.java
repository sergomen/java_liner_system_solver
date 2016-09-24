import org.junit.Test;
import utils.Matrix;

import static org.junit.Assert.assertEquals;


/**
 * Test Matrix class via extend
 */
public final class MatrixTest extends Matrix {

    @Test
    public void testTranspose() {
        assertEquals(transpose(new double[][]{{1, 2, 3}}), new double[][]{{1}, {2}, {3}});
        assertEquals(transpose(new int[][]{{1, 2, 3}}), new int[][]{{1}, {2}, {3}});
        assertEquals(transpose(new double[][]{{}}), new double[][]{{0}});
        assertEquals(transpose(new int[][]{{}}), new int[][]{{0}});
    }

    @Test
    public void testMultiplication() {
        assertEquals(multiplication(new double[][]{{1, 2}, {1, 2}}, new double[][]{{1}, {2}}), new double[][]{{5}, {5}});
        assertEquals(multiplication(new int[][]{{1, 2}, {1, 2}}, new int[][]{{1}, {2}}), new int[][]{{5}, {5}});
        assertEquals(multiplication(new double[][]{{1, 2}}, new double[][]{{1, 2}}), new double[][]{{0}});
        assertEquals(multiplication(new int[][]{{1, 2}}, new int[][]{{1, 2}}), new int[][]{{0}});
    }

    @Test
    public void testAddition() {
        assertEquals(addition(new double[][]{{1, 2}}, new double[][]{{3, 6}}), new double[][]{{4, 8}});
        assertEquals(addition(new int[][]{{1, 2}}, new int[][]{{3, 6}}), new int[][]{{4, 8}});
        assertEquals(addition(new double[][]{{1, 2}}, new double[][]{{1, 2, 3}}), new double[][]{{0}});
        assertEquals(addition(new int[][]{{1, 2}}, new int[][]{{1, 2, 3}}), new int[][]{{0}});
    }

    @Test
    public void testPrintMatrix() {
        printMatrix(new double[][]{{1, 2}});
        printMatrix(new double[][]{{}});
        printMatrix(new int[][]{{1, 2}});
        printMatrix(new int[][]{{}});
    }
}
