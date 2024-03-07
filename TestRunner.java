import java.io.IOException;

public class TestRunner {
    public static void main(String[] args) throws IOException {
        ScoringMatrix scoringMatrix = new ScoringMatrix("/Users/juliusstein/Desktop/Studium/lmu_tum_bioinfo/ws2324/ProPra_ws2324/blockteil/ueb3/MATRICES/dayhoff.mat");
        double score = scoringMatrix.getScore('E', 'E');
        System.out.println("Name of the matrix: " + scoringMatrix.matrixName);
        System.out.println("Score: " + scoringMatrix.Score);
        System.out.println("Number of rows: " + scoringMatrix.numRow);
        System.out.println("Number of columns: " + scoringMatrix.numCol);
        System.out.println("Row index: " + scoringMatrix.rowIndex);
        System.out.println("Column index: " + scoringMatrix.colIndex);
        System.out.println("Score for S, T: " + score);
        scoringMatrix.printMatrix();
    }
}
