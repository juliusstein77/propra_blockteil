package utils;

public class Helpers {
    public static void printMatrix(double[][] matrix, String name) {
        System.out.println(name + "\n" + "_____");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println("\n");
    }
}
