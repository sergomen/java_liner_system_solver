import org.junit.Test;
import utils.Matrix;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


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

	@Test
	public void testDeterminant() {
		assertTrue(matrix_determinant(new double[][]{{1.0, 2.0}, {3.0, 4.0}}) == -2.0);
		assertTrue(matrix_determinant(new double[][]{{1, 2}, {3, 4}}) == -2.0);
		assertTrue(matrix_determinant(new double[][]{{0.0, 0.0}, {3.0, 4.0}}) == 0.0);
		assertTrue(matrix_determinant(new double[][]{{1.0, 1.0}, {1.0, 1.0}}) == 0.0);
		assertTrue(matrix_determinant(new double[][]{{1.0, 1.0}, {1.0, 1.0}, {1.0, 1.0}}) == 0.0);
		assertTrue(matrix_determinant(new double[][]{{1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}, {1.0, 1.0, 1.0}}) == 0.0);
		assertTrue(matrix_determinant(new double[][]{{1.0}}) == 1.0);
	}

	@Test
	public void testMinors() {
		assertEquals(matrix_minors(new double[][]{{1, 2}, {3, 4}}), new double[][]{{4, 3}, {2, 1}});
		assertEquals(matrix_minors(new int[][]{{1}}), null);
	}

	@Test
	public void testReversed() {
		assertEquals(matrix_reverse(new double[][]{{1, 2}, {3, 4}}), new double[][]{{2, -1}, {-1.5, 0.5}});
		assertEquals(matrix_reverse(new int[][]{{1, 2}, {3, 4}}), new double[][]{{2, -1}, {-1.5, 0.5}});
		assertEquals(matrix_reverse(new double[][]{{1}}), new double[][]{{1}});
		assertEquals(matrix_reverse(new double[][]{{1, 2}}), null);
	}
}
