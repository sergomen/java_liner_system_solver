package sample.utils;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Class for math operation with matrix
 * Created by Georg Mayur on 10.09.16.
 */
public final class Matrix {

	/**
	 * return result matrix as sum array and other array
	 *
	 * @param array       is matrix[][]
	 * @param other_array is matrix[][]
	 * @return new matrix int[][] or {{0}} if summary arrays dimentions is not equals
	 */
	public static int[][] addition(int[][] array, int[][] other_array) {
		int[][] result;
		if (array.length == other_array.length && array[0].length == other_array[0].length)
			result = new int[array.length][array[0].length];
		else
			return new int[][]{{0}};

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				result[i][j] = array[i][j] + other_array[i][j];
			}
		}

		return result;
	}

	/**
	 * return result matrix as summ array and other array
	 *
	 * @param array       is matrix[][]
	 * @param other_array is matrix[][]
	 * @return new matrix double[][] or {{0}} if summary arrays dimentions is not equals
	 */
	public static double[][] addition(double[][] array, double[][] other_array) {
		double[][] result;
		if (array.length == other_array.length && array[0].length == other_array[0].length)
			result = new double[array.length][array[0].length];
		else
			return new double[][]{{0}};

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[0].length; j++) {
				result[i][j] = array[i][j] + other_array[i][j];
			}
		}

		return result;
	}

	/**
	 * create new matrix with line numbers equal lines array matrix and columns equal columns
	 * in other_array matrix
	 *
	 * @param array       is first matrix
	 * @param other_array is second matrix
	 * @return new double[][] matrix
	 */
	public static double[][] multiplication(double[][] array, double[][] other_array) {
		int weigh = array.length;
		int height = other_array[0].length;
		int iterations;

		if (array[0].length == other_array.length)
			iterations = other_array.length;
		else
			return new double[][]{{0}};

		double[][] final_array = new double[weigh][height];

		for (int lineNum = 0; lineNum < weigh; lineNum++) {
			for (int colNum = 0; colNum < height; colNum++) {
				if (lineNum == 0 && colNum == 0)
					final_array[lineNum][colNum] = 0;
				for (int iter = 0; iter < iterations; iter++)
					final_array[lineNum][colNum] += array[lineNum][iter] * other_array[iter][colNum];
			}
		}

		return final_array;
	}

	/**
	 * create new matrix with line numbers equal lines array matrix and columns equal columns
	 * in other_array matrix
	 *
	 * @param array       is first matrix
	 * @param other_array is not first matrix, maybe second
	 * @return new int[][] matrix
	 */
	public static int[][] multiplication(int[][] array, int[][] other_array) {
		int weigh = array.length;
		int height = other_array[0].length;
		int iterations;

		if (array[0].length == other_array.length)
			iterations = other_array.length;
		else
			return new int[][]{{0}};

		int[][] final_array = new int[weigh][height];

		for (int lineNum = 0; lineNum < weigh; lineNum++) {
			for (int colNum = 0; colNum < height; colNum++) {
				if (lineNum == 0 && colNum == 0)
					final_array[lineNum][colNum] = 0;
				for (int iter = 0; iter < iterations; iter++)
					final_array[lineNum][colNum] += array[lineNum][iter] * other_array[iter][colNum];
			}
		}

		return final_array;
	}

	/**
	 * print matrix double[][]
	 *
	 * @param array is a double[][] matrix
	 */
	public static void printMatrix(double[][] array) {
		for (double[] line : array) {
			for (double value : line) {
				System.out.printf("%s \t", value);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * print int[][] matrix
	 *
	 * @param array is int[][] matrix
	 */
	public static void printMatrix(int[][] array) {
		for (int[] line : array) {
			for (int value : line) {
				System.out.printf("%s \t", value);
			}
			System.out.println();
		}
		System.out.println();
	}

	/**
	 * @param matrix is transposed matrix
	 * @return new matrix[][]
	 */
	public static double[][] transpose(double[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == 0 || columns == 0)
			return new double[][]{{0}};

		double[][] new_matrix = new double[columns][lines];

		for (int line = 0; line < lines; line++)
			for (int column = 0; column < columns; column++)
				new_matrix[column][line] = matrix[line][column];

		return new_matrix;
	}

	/**
	 * @param matrix is transposed matrix
	 * @return new matrix[][]
	 */
	public static int[][] transpose(int[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == 0 || columns == 0)
			return new int[][]{{0}};

		int[][] new_matrix = new int[columns][lines];

		for (int line = 0; line < lines; line++)
			for (int column = 0; column < columns; column++)
				new_matrix[column][line] = matrix[line][column];

		return new_matrix;
	}

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
