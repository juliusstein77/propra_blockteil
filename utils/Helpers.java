package utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;


public class Helpers {
    public static final String[] bases = {"A","R","N","D","C","Q","E","G","H","I","L","K","M","F","P","S","T","W","Y","V"};
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

    public static HashMap<String,Double> read_in_matrix(String filepath){
        File file = new File(filepath);
        HashMap<String,Double> matrix = new HashMap<String,Double>();
        try {
            Scanner scanner = new Scanner(file);
            int r = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (!line.isEmpty()){
                    String head = line.substring(0,6);
                    if (head.equals("MATRIX")){
                        String tabuOrspace = line.substring(7,7);
                        if (tabuOrspace.equals("\\s")){
                            String matriceRow = line.substring(11);
                            String[] matriceSplitted = matriceRow.split("\\s{5,7}");
                            int c = 0;
                            while(matriceSplitted.length > c){
                                String key = bases[r] + bases[c];
                                matrix.put(key,Double.parseDouble(matriceSplitted[c]));
                                ++c;
                            }
                            ++r;
                        } else {
                            String matriceRow = line.substring(7);
                            String[] matriceSplitted = matriceRow.split("\t");
                            int c = 0;
                            while(matriceSplitted.length > c){
                                String key = bases[r] + bases[c];
                                matrix.put(key,Double.parseDouble(matriceSplitted[c]));
                                ++c;
                            }
                            ++r;
                        }
                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return matrix;
    }
}
