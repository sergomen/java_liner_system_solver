package utils;

/**
 * Class for math operation with matrix
 * Created by Georg Mayur on 10.09.16.
 */
public abstract class Matrix {

	/**
	 * return result matrix as sum array and other array
	 *
	 * @param array       is matrix[][]
	 * @param other_array is matrix[][]
	 * @return new matrix int[][] or {{0}} if summary arrays dimentions is not equals
	 */
	public static int[][] matrix_addition(int[][] array, int[][] other_array) {
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
	public static double[][] matrix_addition(double[][] array, double[][] other_array) {
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
	public static double[][] matrix_multiplication(double[][] array, double[][] other_array) {
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
	public static int[][] matrix_multiplication(int[][] array, int[][] other_array) {
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
	 * provide multiplication twin-dimension array to multiplier
	 *
	 * @param array      is multiplicand matrix
	 * @param multiplier is multiplicand
	 * @return new array[][] with equal array dimensions
	 */
	public static double[][] matrix_multiplication(double[][] array, double multiplier) {
		int weigh = array.length;
		int height = array[0].length;

		if (array[0].length == array.length && array.length == 0)
			return new double[][]{{0}};

		double[][] final_array = new double[weigh][height];

		for (int lineNum = 0; lineNum < weigh; lineNum++) {
			for (int colNum = 0; colNum < height; colNum++) {
				final_array[lineNum][colNum] = array[lineNum][colNum] * multiplier;
			}
		}

		return final_array;
	}

	/**
	 * print matrix double[][]
	 *
	 * @param array is a double[][] matrix
	 */
	public static void matrix_print(double[][] array) {
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
	public static void matrix_print(int[][] array) {
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
	public static double[][] matrix_transpose(double[][] matrix) {

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
	public static int[][] matrix_transpose(int[][] matrix) {

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

	/**
	 * count matrix determinant
	 *
	 * @param matrix is counted matrix
	 * @return double value matrix's determinant
	 */
	public static double matrix_determinant(double[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines != columns)
			return 0.0;
		else if (lines == 1)
			return matrix[0][0];

		// duplicate matrix to simplify code later
		double[][] temp_matrix = new double[lines][columns * 2];
		double matrix_determinant = 0.0;

		// fill new temp matrix
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				temp_matrix[line][column] = matrix[line][column];
				temp_matrix[line][column + columns] = matrix[line][column];
			}
		}

		// count matrix_determinant
		for (int i = 0; i < (columns - 1); i++) {

			double matrix_determinant_increment = 1.0;
			for (int line = 0, column = i; line < lines; line++, column++) {
				matrix_determinant_increment *= temp_matrix[line][column];
			}

			double matrix_determinant_decrement = -1.0;
			for (int line = (lines - 1), column = i; line >= 0; line--, column++) {
				matrix_determinant_decrement *= temp_matrix[line][column];
			}

			matrix_determinant += matrix_determinant_increment;
			matrix_determinant += matrix_determinant_decrement;
		}

		return matrix_determinant;
	}

	/**
	 * count matrix determinant
	 *
	 * @param matrix is counted matrix
	 * @return double value matrix's determinant
	 */
	public static int matrix_determinant(int[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines != columns)
			return 0;
		else if (lines == 1)
			return matrix[0][0];

		// duplicate matrix to simplify code later
		double[][] temp_matrix = new double[lines][columns * 2];
		int matrix_determinant = 0;

		// fill new temp matrix
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				temp_matrix[line][column] = matrix[line][column];
				temp_matrix[line][column + columns] = matrix[line][column];
			}
		}

		// count matrix_determinant
		for (int i = 0; i < (columns - 1); i++) {

			double matrix_determinant_increment = 1;
			for (int line = 0, column = i; line < lines; line++, column++) {
				matrix_determinant_increment *= temp_matrix[line][column];
			}

			double matrix_determinant_decrement = -1;
			for (int line = (lines - 1), column = i; line >= 0; line--, column++) {
				matrix_determinant_decrement *= temp_matrix[line][column];
			}

			matrix_determinant += matrix_determinant_increment;
			matrix_determinant += matrix_determinant_decrement;
		}

		return matrix_determinant;
	}

	/**
	 * make matrix include minors for original matrix
	 *
	 * @param matrix is original matrix
	 * @return double[][] minors matrix
	 */
	public static double[][] matrix_minors(double[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == columns && lines == 1)
			return null;

		int minor_lines = matrix.length - 1;
		int minor_columns = matrix[0].length - 1;

		double[][] temp_minor = new double[minor_lines][minor_columns];
		double[][] minors_matrix = new double[lines][columns];

		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {

				// fill temp minor for each original's matrix element
				for (int minor_line = 0; minor_line < minor_lines; minor_line++) {
					for (int minor_column = 0; minor_column < minor_columns; minor_column++) {

						// there are four case minor filling
						if (minor_line < line && minor_column < column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line][minor_column];
						} else if (minor_line < line && minor_column >= column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line][minor_column + 1];
						} else if (minor_line >= line && minor_column < column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line + 1][minor_column];
						} else if (minor_line >= line && minor_column >= column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line + 1][minor_column + 1];
						}

					}
				}

				// count minor and set determinant as value in minors matrix
				minors_matrix[line][column] = Matrix.matrix_determinant(temp_minor);
			}
		}

		return minors_matrix;
	}

	/**
	 * make matrix include minors for original matrix
	 *
	 * @param matrix is original matrix
	 * @return int[][] minors matrix
	 */
	public static double[][] matrix_minors(int[][] matrix) {

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == columns && lines == 1)
			return null;

		int minor_lines = matrix.length - 1;
		int minor_columns = matrix[0].length - 1;

		int[][] temp_minor = new int[minor_lines][minor_columns];
		double[][] minors_matrix = new double[lines][columns];

		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {

				// fill temp minor for each original's matrix element
				for (int minor_line = 0; minor_line < minor_lines; minor_line++) {
					for (int minor_column = 0; minor_column < minor_columns; minor_column++) {

						// there are four case minor filling
						if (minor_line < line && minor_column < column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line][minor_column];
						} else if (minor_line < line && minor_column >= column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line][minor_column + 1];
						} else if (minor_line >= line && minor_column < column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line + 1][minor_column];
						} else if (minor_line >= line && minor_column >= column) {
							temp_minor[minor_line][minor_column] = matrix[minor_line + 1][minor_column + 1];
						}

					}
				}

				// count minor and set determinant as value in minors matrix
				minors_matrix[line][column] = Matrix.matrix_determinant(temp_minor);
			}
		}

		return minors_matrix;
	}


	/**
	 * solve transpose matrix to matrix n*n, as
	 * (1 / absolute(determinant(matrix)) * transpose_matrix_cofactor
	 *
	 * @param matrix is matrix for transpose
	 * @return transposed matrix or null if matrix isn't square
	 */
	public static double[][] matrix_reverse(double[][] matrix) {

		double matrix_determinant = Matrix.matrix_determinant(matrix);

		double multiplier = (matrix_determinant > 0) ? 1 / matrix_determinant : -1 / matrix_determinant;

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == columns && lines == 1)
			return new double[][]{{1.0 / matrix[0][0]}};
		else if (lines != columns || matrix_determinant == 0)
			return null;

		double minors[][] = Matrix.matrix_minors(matrix);

		// change minors matrix to the cofactor matrix
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				if (line % 2 != column % 2) {
					assert minors != null;
					minors[line][column] *= -1;
				}
			}
		}

		minors = Matrix.matrix_transpose(minors);

		return Matrix.matrix_multiplication(minors, multiplier);
	}

	/**
	 * solve transpose matrix to matrix n*n, as
	 * (1 / absolute(determinant(matrix)) * transpose_matrix_cofactor
	 *
	 * @param matrix is matrix for transpose
	 * @return transposed matrix or null if matrix isn't square
	 */
	public static double[][] matrix_reverse(int[][] matrix) {

		double matrix_determinant = Matrix.matrix_determinant(matrix);

		double multiplier = (matrix_determinant > 0) ? 1 / matrix_determinant : -1 / matrix_determinant;

		int lines = matrix.length;
		int columns = matrix[0].length;

		if (lines == columns && lines == 1)
			return new double[][]{{1.0 / matrix[0][0]}};
		else if (lines != columns || matrix_determinant == 0)
			return null;

		double minors[][] = Matrix.matrix_minors(matrix);

		// change minors matrix to the cofactor matrix
		for (int line = 0; line < lines; line++) {
			for (int column = 0; column < columns; column++) {
				if (line % 2 != column % 2) {
					assert minors != null;
					minors[line][column] *= -1;
				}
			}
		}

		minors = Matrix.matrix_transpose(minors);

		return Matrix.matrix_multiplication(minors, multiplier);
	}
}