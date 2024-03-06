import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

public class ScoringMatrix {
    private int[][] matrix;
    public String matrixName;
    public String Score;
    public int numRow;
    public int numCol;
    public String rowIndex;
    public String colIndex;

    private HashMap<Character, Integer> indexMap;

    public ScoringMatrix(String filePath) throws IOException {
        readMatrixFromFile(filePath);
    }

    private void readMatrixFromFile(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int row = 0;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("NAME")) {
                matrixName = line.split("\\s+")[1];
            } else if (line.startsWith("SCORE")) {
                Score = line.split("\\s+")[1];
            } else if (line.startsWith("NUMROW")) {
                numRow = Integer.parseInt((line.split("\\s+")[1]));
            } else if (line.startsWith("NUMCOL")) {
                numCol = Integer.parseInt((line.split("\\s+")[1]));
            } else if (line.startsWith("ROWINDEX")) {
                rowIndex = line.split("\\s+")[1];
            } else if (line.startsWith("COLINDEX")) {
                colIndex = line.split("\\s+")[1];
                matrix = new int[rowIndex.length()][colIndex.length()];
            } else if (line.startsWith("MATRIX")) {
                String[] values = line.trim().split("\\s+");
                for (int col = 1; col < values.length; col++) { // Exclude the first column (ROWINDEX)
                    double doubleValue = Double.parseDouble(values[col]);
                    int intValue = (int) Math.round(doubleValue); // Multiply by 3 to convert to integer
                    matrix[row][col - 1] = intValue;
                    matrix[col-1][row] = intValue;
                }
                row++;
            }
        }

        indexMap = new HashMap<>();
        for (int i = 0; i < rowIndex.length(); i++) {
            indexMap.put(rowIndex.charAt(i), i);
        }
        reader.close();
    }

    public int getScore(char a, char b) {
        int indexA = getIndex(a);
        int indexB = getIndex(b);
        return matrix[indexA][indexB];
    }

    private int getIndex(char c) {
        if (!indexMap.containsKey(c)) {
            throw new IllegalArgumentException("Character " + c + " not found in matrix.");
        }
        return indexMap.get(c);
    }

    public void printMatrix() {
        System.out.println("Scoring Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }
}
