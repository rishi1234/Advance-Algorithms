/**
  Homework Assignment #2
 * @ Sukesh Reddy
 * @ Shashank Reddy
 * @ Vandana Rao
 * @ Rishi Reddy

   @version 1.0 2014-03-31
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import Jama.Matrix; // Using Jama jar file to perform calculations 

public class SVD {
	// Converting Row and COlumn Pixels into 2 bytes
	public static String row_column_call(int row_Pixels) {
		String Us = Integer.toBinaryString(row_Pixels);
		StringBuffer su2 = new StringBuffer(Us);
		StringBuffer su3 = su2.reverse();
		while (su3.length() < 16) {
			su3.append('0');
		}
		String su4 = su3.reverse().toString();
		int su_a = Integer.parseInt((su4.substring(0, 8)), 2);
		int su_a1 = Integer.parseInt((su4.substring(8, 16)), 2);
		String out = String.valueOf(su_a) + " " + String.valueOf(su_a1);
		return out;

	}

	// ReConverting Row and Column Pixels into Decimal
	public static int row_column_call_1(String s1, String s22) {
		// System.out.println("hello: " + s1 + " " + s22);
		StringBuffer s2 = new StringBuffer(s1);
		StringBuffer s3 = s2.reverse();
		while (s3.length() < 8) {
			s3.append('0');
		}
		System.out.println("s3:" + s3.reverse());
		StringBuffer s2_1 = new StringBuffer(s22);
		StringBuffer s3_1 = s2_1.reverse();
		while (s3_1.length() < 8) {
			s3_1.append('0');
		}
		// System.out.println("s3_1:" + s3_1.reverse());
		System.out.println(s3.toString() + " " + s3_1.toString());
		String s21 = s3.toString() + s3_1.toString();
		String s12 = String.valueOf(s21);
		int s12_1 = Integer.parseInt(s12, 2);
		return s12_1;

	}

	public static void main(String args[]) throws IOException {

		String a1 = args[0];
		if (a1.equals("1")) { // Will invoke the part 1 of the program
			System.out.println("1_2:" + args[1]);
			First_program(args[1]);
		} else if (a1.equals("2")) { // Will invoke the part 2 of the program
			System.out.println("2_2:" + args[1]);
			Second_program(args[1]);
		} else if (a1.equals("3")) { // Will invoke the part 3 of the program
			System.out.println("3_2:" + args[1]);
			System.out.println("3_3" + args[2]);
			System.out.println("3_4" + args[3]);
			Third_program(args[1], args[2], args[3]);
		} else if (a1.equals("4")) {// Will invoke the part 4 of the program
			System.out.println("4_1:" + args[1]);
			Fourth_program(args[0], args[1]);
		} else {
			System.out.println("Enter the Right Options");
		}
		System.out.println("File Created");

	}

	// Will convert the Ascii values of USV to original form and then multiply
	// the matrices to get the original reduced image
	public static void Fourth_program(String string, String string2)
			throws IOException {
		String s1, s2;
		int row_column;
		FileInputStream f = null;
		int row, column_1, column_2, rank_1, rank_2, column, rank;
		int m, c, d, k, q;
		double sum;
		String inputfile = string2;
		f = new FileInputStream(inputfile);
		String line;
		String[] s;
		System.out.println("*******U*****");
		int i = 0;
		Integer[] U_rc = new Integer[100000];
		Integer[] S_rc = new Integer[100000];
		Integer[] V_rc = new Integer[100000];

		String[] USV_rc_s = new String[100000];
		FileInputStream is = new FileInputStream(inputfile);
		DataInputStream dis = new DataInputStream(is);

		// Re converting the row from 2 bytes to 1 bytes
		int row_1 = dis.read();
		int row_2 = dis.read();
		s1 = Integer.toBinaryString(row_1);
		s2 = Integer.toBinaryString(row_2);
		row_column = row_column_call_4(s1, s2);
		row = row_column;
		// Re converting the rank from 2 bytes to 1 bytes
		rank_1 = dis.read();
		rank_2 = dis.read();
		s1 = Integer.toBinaryString(rank_1);
		s2 = Integer.toBinaryString(rank_2);
		row_column = row_column_call_4(s1, s2);
		rank = row_column;
		// Re converting the column from 2 bytes to 1 bytes
		column_1 = dis.read();
		column_2 = dis.read();
		s1 = Integer.toBinaryString(column_1);
		s2 = Integer.toBinaryString(column_2);
		row_column = row_column_call_4(s1, s2);
		column = row_column;

		System.out.println("row: " + row);
		System.out.println("rank: " + rank);
		System.out.println("column: " + column);
		U_rc[0] = row;
		U_rc[1] = rank;
		S_rc[0] = rank;
		S_rc[1] = rank;
		V_rc[0] = rank;
		V_rc[1] = column;
		double[][] U_svd = new double[U_rc[0]][U_rc[1]];

		String s_u = null;
		// Retrieving the U matrix from Ascii file
		Integer[] U_rc_1 = new Integer[100000];
		for (int i1 = 0; i1 < row; i1++) {

			for (int j1 = 0; j1 < rank; j1++) {
				int n = dis.read();
				if (n > 150) {
					String su_2 = Integer.toBinaryString(n);
					String su_3 = "0" + "0" + su_2.substring(2);
					n = Integer.parseInt(su_3, 2);
					int n1 = dis.read();
					if (n1 < 10) {
						s_u = "-" + n;
						String su_11 = s_u + "." + "0" + n1;
						U_svd[i1][j1] = Double.parseDouble(su_11);
					} else {
						s_u = "-" + n;
						String su_11 = s_u + "." + n1;
						U_svd[i1][j1] = Double.parseDouble(su_11);
					}
				} else {
					int n1 = dis.read();
					if (n1 < 10) {
						String su_11 = n + "." + "0" + n1;
						U_svd[i1][j1] = Double.parseDouble(su_11);
					} else {
						String su_11 = n + "." + n1;
						U_svd[i1][j1] = Double.parseDouble(su_11);
					}
				}
			}
		}
		// Retrieving the S matrix from Ascii file

		double[][] S_svd = new double[S_rc[0]][S_rc[1]];
		Integer[] S_rc_1 = new Integer[100000];

		int l = 0;
		for (int i2 = 0; i2 < rank; i2++) {
			for (int j2 = 0; j2 < rank; j2++) {
				String su_11;
				if (i2 != j2) {
					S_svd[i2][j2] = 0.000;
				} else {
					int s_a = dis.read();
					int s_b = dis.read();
					String s_a1 = Integer.toBinaryString(s_a);
					String s_b1 = Integer.toBinaryString(s_b);
					StringBuffer s2_11 = new StringBuffer(s_a1);
					StringBuffer s3 = s2_11.reverse();
					while (s3.length() < 8) {
						s3.append('0');
					}
					StringBuffer s2_1 = new StringBuffer(s_b1);
					StringBuffer s3_1 = s2_1.reverse();
					while (s3_1.length() < 8) {
						s3_1.append('0');
					}
					String s21 = s3.reverse().toString()
							+ s3_1.reverse().toString();
					String s12 = String.valueOf(s21);
					int s12_1 = Integer.parseInt(s12, 2);
					l++;
					int s12_2 = dis.read();
					System.out.println("A:" + s12_1 + "." + s12_2);
					if (s12_2 < 10) {
						su_11 = s12_1 + "." + "0" + s12_2;
					} else {
						su_11 = s12_1 + "." + s12_2;
					}
					S_svd[i2][j2] = Double.parseDouble(su_11);
				}
			}
		}

		// V^T
		// Retrieving the V^T matrix from Ascii file
		double[][] V_svd = new double[V_rc[0]][V_rc[1]];
		System.out.println("******* V^T *****");
		String su_11;
		for (int i3 = 0; i3 < rank; i3++) {

			for (int j3 = 0; j3 < column; j3++) {
				int n = dis.read();
				if (n > 150) {
					String su_2 = Integer.toBinaryString(n);
					String su_3 = "0" + "0" + su_2.substring(2);
					n = Integer.parseInt(su_3, 2);
					int n1 = dis.read();
					if (n1 < 10) {
						s_u = "-" + n;
						su_11 = s_u + "." + "0" + n1;
					} else {
						su_11 = s_u + "." + n1;
					}
					V_svd[i3][j3] = Double.parseDouble(su_11);
				} else {
					int n1 = dis.read();
					if (n1 < 10) {
						su_11 = n + "." + "0" + n1;
					} else {
						su_11 = n + "." + n1;
					}
					V_svd[i3][j3] = Double.parseDouble(su_11);
				}
			}
		}
		System.out.println("U");
		System.out.println("S");
		System.out.println("V^T");

		// U * S * V^T
		System.out.println("Muliplied Matrix U*S*V^T ");

		sum = 0;

		// Muli
		Matrix U = new Matrix(U_svd, row, rank);
		Matrix S = new Matrix(S_svd, rank, rank);
		Matrix V = new Matrix(V_svd, rank, column);

		Matrix C = U.times(S).times(V);
		double[][] USV_svd = new double[U_rc[0]][V_rc[1]];
		USV_svd = C.getArray();
		String file_name = inputfile.substring(0, inputfile.indexOf("_b"));
		String file_name_1 = file_name + "_k" + ".pgm";
		PrintWriter writer = new PrintWriter(file_name_1);
		System.out.println("Muliplied Matrix U*S ");
		writer.print("P2" + "\n");
		writer.print("# Created by IrfanView" + "\n");
		writer.print(V_rc[1] + " " + U_rc[0] + "\n");
		writer.print("255" + "\n");
		for (c = 0; c < U_rc[0]; c++) {
			for (d = 0; d < V_rc[1]; d++) {
				writer.print((int) USV_svd[c][d] + " ");
			}
			writer.print("\n");
		}
		writer.close();
		System.out.println("File Created");

	}

	private static int row_column_call_4(String s1, String s2) {
		// System.out.println("hello: " + s1 + " " + s2);
		StringBuffer s2_1 = new StringBuffer(s1);
		StringBuffer s3 = s2_1.reverse();
		while (s3.length() < 8) {
			s3.append('0');
		}
		 System.out.println("s3:" + s3.reverse());
		StringBuffer s2_2 = new StringBuffer(s2);
		StringBuffer s3_1 = s2_2.reverse();
		while (s3_1.length() < 8) {
			s3_1.append('0');
		}
		 System.out.println("s3_1:" + s3_1.reverse());
		System.out.println(s3.toString() + " " + s3_1.toString());
		String s21 = s3.toString() + s3_1.toString();
		String s12 = String.valueOf(s21);
		int s12_1 = Integer.parseInt(s12, 2);
		return s12_1;

	}

	private static void Third_program(String string, String string2,
			String string3) throws IOException {
		String header = string;
		String inputFileName = string2;
		String rank_1 = string3;
		int rank = Integer.parseInt(rank_1);
		File f1 = new File(header);
		FileInputStream fis = null;
		Scanner scanner = new Scanner(f1);
		fis = new FileInputStream(f1);
		String s;
		File f2 = new File(inputFileName);
		Scanner scanner_2 = new Scanner(f2);

		int column_Pixels = Integer.parseInt(scanner.next());
		int row_Pixels = Integer.parseInt(scanner.next());
		int grey_scale = Integer.parseInt(scanner.next());

		System.out.println("The Row pixel: " + row_Pixels + " Column Pixel: "
				+ column_Pixels + " grey_scale: " + grey_scale);
		if (rank <= column_Pixels) {
			double U_svd[][] = new double[row_Pixels][row_Pixels];

			for (int i = 0; i < row_Pixels; i++) {
				for (int j = 0; j < row_Pixels; j++) {
					Double b = Double.parseDouble(scanner_2.next());
					s = Double.toString(b);
					if (s.contains("E")) {

						double m1 = (double) Math.round(b * 1000) / 1000;
						s = Double.toString(m1);

					}
					String s1 = null;
					if (s.endsWith("0") && s.length() == 1) {
						// System.out.println("hrllo");
						s1 = "0.00";
					} else {

						if (s.substring(s.indexOf('.') + 1).length() == 1) {
							s1 = s.substring(0, s.indexOf('.'))
									+ "."
									+ s.substring(s.indexOf('.') + 1,
											s.indexOf('.') + 2);
						} else {
							s1 = s.substring(0, s.indexOf('.'))
									+ "."
									+ s.substring(s.indexOf('.') + 1,
											s.indexOf('.') + 3);
						}
					}
					U_svd[i][j] = Double.parseDouble(s1);
				}
			}

			System.out.println("S==");
			double S_svd[][] = new double[row_Pixels][column_Pixels];
			for (int i = 0; i < row_Pixels; i++) {
				for (int j = 0; j < column_Pixels; j++) {
					Double b = Double.parseDouble(scanner_2.next());
					s = Double.toString(b);
					if (s.contains("E")) {
						double m1 = (double) Math.round(b * 1000) / 1000;
						s = Double.toString(m1);

					}
					String s1 = null;

					if (i == j) {
						if (s.endsWith("0") && s.length() == 1) {
							s1 = "0.000";
						} else {
							if (s.substring(s.indexOf('.') + 1).length() == 1) {
								s1 = s.substring(0, s.indexOf('.'))
										+ "."
										+ s.substring(s.indexOf('.') + 1,
												s.indexOf('.') + 2);
							} else {
								s1 = s.substring(0, s.indexOf('.'))
										+ "."
										+ s.substring(s.indexOf('.') + 1,
												s.indexOf('.') + 4);
							}
						}
						S_svd[i][j] = Double.parseDouble(s1);
						System.out.println(S_svd[i][j]);
					}
				}
			}
			System.out.println("V^T");

			double V_svd[][] = new double[column_Pixels][column_Pixels];
			for (int i = 0; i < column_Pixels; i++) {
				for (int j = 0; j < column_Pixels; j++) {
					Double b = Double.parseDouble(scanner_2.next());
					s = Double.toString(b);
					if (s.contains("E")) {

						double m1 = (double) Math.round(b * 1000) / 1000;
						s = Double.toString(m1);

					}
					String s1 = null;
					// System.out.println(s);
					if (s.endsWith("0") && s.length() == 1) {
						s1 = "0.00";
					} else {
						if (s.substring(s.indexOf('.') + 1).length() == 1) {
							s1 = s.substring(0, s.indexOf('.'))
									+ "."
									+ s.substring(s.indexOf('.') + 1,
											s.indexOf('.') + 2);
						} else {
							s1 = s.substring(0, s.indexOf('.'))
									+ "."
									+ s.substring(s.indexOf('.') + 1,
											s.indexOf('.') + 3);
						}
					}
					V_svd[i][j] = Double.parseDouble(s1);
				}
			}

			Matrix V = new Matrix(V_svd, column_Pixels, column_Pixels);
			Matrix V_T = V.transpose();
			double[][] V_T_svd = new double[column_Pixels][column_Pixels];
			V_T_svd = V_T.getArray();
			File filePointer;
			String filename_1 = inputFileName.substring(0,
					inputFileName.indexOf("."))
					+ "_b" + ".pgm.SVD";
			filePointer = new File(filename_1);
			filePointer.createNewFile();
			DataOutputStream output = new DataOutputStream(
					new FileOutputStream(filename_1));
			System.out.println("--U--");

			// Saving U
			System.out.println("*****Saving U******");
			String su1 = row_column_call_3(row_Pixels);
			String su12 = row_column_call_3(rank);
			String su13 = row_column_call_3(column_Pixels);
			String row_11 = su1.substring(0, su1.indexOf(" "));
			String row_12 = su1.substring(su1.indexOf(" ") + 1);
			output.write(Integer.parseInt(row_11));
			output.write(Integer.parseInt(row_12));
			System.out.println(row_11 + " " + row_12);
			String row_21 = su12.substring(0, su12.indexOf(" "));
			String row_22 = su12.substring(su12.indexOf(" ") + 1);
			output.write(Integer.parseInt(row_21));
			output.write(Integer.parseInt(row_22));
			System.out.println(row_21 + " " + row_22);
			String row_31 = su13.substring(0, su13.indexOf(" "));
			String row_32 = su13.substring(su13.indexOf(" ") + 1);
			output.write(Integer.parseInt(row_31));
			output.write(Integer.parseInt(row_32));
			System.out.println(row_31 + " " + row_32);
			for (int i = 0; i < row_Pixels; i++) {
				for (int j = 0; j < rank; j++) {
					String s1 = Double.toString(U_svd[i][j]);
					if (s1.substring(0, 1).equals("-")) {
						s1 = s1.substring(1);
						int b = 0;
						// System.out.println("Hello: " + s1);
						String a1 = Integer.toBinaryString(Integer.parseInt(s1
								.substring(0, s1.indexOf('.'))));
						StringBuffer s2 = new StringBuffer(a1);
						StringBuffer s3 = s2.reverse();
						while (s3.length() < 6) {
							s3.append('0');
						}
						String s4 = "1" + "1" + s3.reverse().toString();
						int a = Integer.parseInt(s4, 2);
						if (s1.substring(s1.indexOf('.') + 1).length() == 1) {
							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 2)
									.concat("0"));
						} else {

							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 3));
						}
						output.write(a);
						output.write(b);
					} else {
						if (s1.substring(s1.indexOf('.') + 1).length() == 1) {

							int a = Integer.parseInt(s1.substring(0,
									s1.indexOf('.')));
							int b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 2)
									.concat("0"));
							output.write(a);
							output.write(b);
						} else {
							int a = Integer.parseInt(s1.substring(0,
									s1.indexOf('.')));

							int b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 3));
							// System.out.println(a + " " + b + " ");
							output.write(a);
							output.write(b);
						}
					}
				}
			}
			System.out.println("****Saving S***");
			for (int i = 0; i < rank; i++) {
				for (int j = 0; j < rank; j++) {
					if (i == j) {
						String s1 = Double.toString(S_svd[i][j]);
						int b = 0;
						s = Integer.toBinaryString(Integer.parseInt(s1
								.substring(0, s1.indexOf('.'))));
						StringBuffer s2 = new StringBuffer(s);
						StringBuffer s3 = s2.reverse();
						while (s3.length() < 16) {
							s3.append('0');
						}
						String s4 = s3.reverse().toString();
						int a = Integer.parseInt((s4.substring(0, 8)), 2);
						int a1 = Integer.parseInt((s4.substring(8, 16)), 2);
						if (s1.substring(s1.indexOf('.') + 1).length() == 1
								|| s1.substring(s1.indexOf('.') + 1).length() == 2) {
							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 2)
									.concat("00"));
						} else {
							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 4));
						}
						System.out.println("a: " + a + " b:" + a1 + " b:" + b
								+ " s:" + s1);
						output.write(a);
						output.write(a1);
						output.write(b);
					}
				}
			}
			// Saving V^T

			System.out.println("****Saving V^T***");

			for (int i = 0; i < rank; i++) {
				for (int j = 0; j < column_Pixels; j++) {
					String s1 = Double.toString(V_T_svd[i][j]);
					if (s1.substring(0, 1).equals("-")) {
						s1 = s1.substring(1);
						int b = 0;
						// System.out.println("Hello: " + s1);
						String a1 = Integer.toBinaryString(Integer.parseInt(s1
								.substring(0, s1.indexOf('.'))));
						StringBuffer s2 = new StringBuffer(a1);
						StringBuffer s3 = s2.reverse();
						while (s3.length() < 6) {
							s3.append('0');
						}
						// System.out.println("rev:" + 1 + s3.reverse());
						String s4 = "1" + "1" + s3.reverse().toString();
						// System.out.println("s4: " + s4);
						int a = Integer.parseInt(s4, 2);
						if (s1.substring(s1.indexOf('.') + 1).length() == 1) {

							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 2)
									.concat("0"));
						} else {

							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 3));
						}
						output.write(a);
						output.write(b);
					} else {
						int b = 0;
						int a = Integer.parseInt(s1.substring(0,
								s1.indexOf('.')));

						if (s1.substring(s1.indexOf('.') + 1).length() == 1) {

							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 2)
									.concat("0"));
						} else {

							b = Integer.parseInt(s1.substring(
									s1.indexOf('.') + 1, s1.indexOf('.') + 3));

						}
						// System.out.print(a + " " + (int) b + " ");
						output.write(a);
						output.write(b);
						// System.out.println(a + " " + b);
					}
				}
			}

			output.close();
			// System.out.println(U_svd[0][11]);
			System.out.println("File Created");
		} else {
			System.out.println("Rank is Greater than Column Pixels");
		}

	}

	private static String row_column_call_3(int row_Pixels) {
		String Us = Integer.toBinaryString(row_Pixels);
		// System.out.println(Us);
		StringBuffer su2 = new StringBuffer(Us);
		StringBuffer su3 = su2.reverse();
		while (su3.length() < 16) {
			su3.append('0');
		}
		String su4 = su3.reverse().toString();
		// System.out.println(su4);
		int su_a = Integer.parseInt((su4.substring(0, 8)), 2);
		int su_a1 = Integer.parseInt((su4.substring(8, 16)), 2);
		String out = String.valueOf(su_a) + " " + String.valueOf(su_a1);
		return out;
	}

	private static void Second_program(String string) throws IOException {
		String args = string;
		Scanner read;
		String s1, s2;
		int max = 0;
		int row = 0, column = 0, row_1, row_2, column_1, column_2, row_column;
		int i, j;
		int matrix[][] = null;
		File filePointer;
		String filename_1 = args.substring(0, args.indexOf("_")) + "2" + ".pgm";
		filePointer = new File(filename_1);
		filePointer.createNewFile();
		PrintWriter writer = new PrintWriter(filename_1);
		FileInputStream is = new FileInputStream(args);
		DataInputStream dis = new DataInputStream(is);
		row_1 = dis.read();
		row_2 = dis.read();
		column_1 = dis.read();
		column_2 = dis.read();
		// max = dis.read();
		s1 = Integer.toBinaryString(row_1);
		s2 = Integer.toBinaryString(row_2);
		row_column = row_column_call_1(s1, s2);
		row = row_column;
		s1 = Integer.toBinaryString(column_1);
		s2 = Integer.toBinaryString(column_2);
		row_column = row_column_call_1(s1, s2);
		System.out.println(row_column);
		column = row_column;
		max = dis.read();
		System.out.println("Row: " + row + " Column: " + column + " max: "
				+ max);
		matrix = new int[row][column];
		writer.print("P2" + "\n");
		writer.print("# Created by IrfanView" + "\n");
		writer.print(row + " " + column + "\n");
		writer.print(max + "\n");
		for (i = 0; i < row; i++) {
			for (j = 0; j < column; j++) {
				writer.print(dis.read() + " ");
			}

		}
		// output.close();
		writer.close();
		dis.close();

	}

	private static void First_program(String args) throws IOException {
		String path = args;
		Scanner read;
		String header = null;
		int max = 0;
		int row = 0, column = 0;
		int i, j;
		int matrix[][] = null;
		try {
			read = new Scanner(new File(path));
			if (read.hasNext("P2")) {
				header += read.nextLine();

			}
			if (read.hasNext("#.*")) {
				header += read.nextLine();

			}
			row = read.nextInt();
			column = read.nextInt();
			max = read.nextInt();
			matrix = new int[row][column];
			for (i = 0; i < row; i++) {
				for (j = 0; j < column; j++) {
					matrix[i][j] = read.nextInt();

				}
			}

		} catch (FileNotFoundException e) {
			System.out.println("filenot found");
			e.printStackTrace();
		}

		int a;
		System.out.println("binary Matrix is");
		String filename = args.substring(0, args.indexOf(".")) + "_b.pgm";
		DataOutputStream output = new DataOutputStream(new FileOutputStream(
				filename));
		System.out.println(row + " " + column + " " + max);
		String row_1 = row_column_call(row);
		String row_11 = row_1.substring(0, row_1.indexOf(" "));
		String row_12 = row_1.substring(row_1.indexOf(" ") + 1);
		System.out.println(row_11 + "_" + row_12);
		output.write(Integer.parseInt(row_11));
		output.write(Integer.parseInt(row_12));
		String column_1 = row_column_call(column);
		String column_11 = column_1.substring(0, column_1.indexOf(" "));
		String column_12 = column_1.substring(column_1.indexOf(" ") + 1);
		System.out.println(column_11 + "_" + column_12);
		output.write(Integer.parseInt(column_11));
		output.write(Integer.parseInt(column_12));
		output.write(max);
		for (i = 0; i < row; i++) {
			for (j = 0; j < column; j++) {

				output.write(matrix[i][j]);
			}
		}

		output.close();
	}

}