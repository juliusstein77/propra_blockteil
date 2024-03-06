import utils.Helpers;

import java.io.IOException;

public class Gotoh {
    private static void initializeMatrices (String mode, int[][] a, int[][] d, int[][] i, Integer go, Integer ge){
        switch (mode) {
            case "global":
                a[0][0] = 0;
                for (int j = 0; j < a[0].length; j++) {
                    a[0][j] = go + j * ge;
                    //a[j][0] = go + j * ge;
                }
                for (int i1 = 0; i1 < a.length; i1++) {
                    a[i1][0] = go + i1 * ge;
                }


                // --- i(0,j) = -inf ---
                for (int col = 0; col < i[0].length; col++) {
                    i[0][col] = Integer.MIN_VALUE;
                }
                i[0][0] = 0;

                // --- d(i,0) = -inf ---
                for (int row = 0; row < d.length; row++) {
                    d[row][0] = Integer.MIN_VALUE;
                }
                d[0][0] = 0;
                break;
            default:
                break;
        }
    }
    public static int run(String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode) throws IOException {
        int m = P.length();
        int n = S.length();
        int[][] mx_d = new int[m + 1][n + 1];
        int[][] mx_i = new int[m + 1][n + 1];
        int[][] mx_a = new int[m + 1][n + 1];
        initializeMatrices(mode, mx_a, mx_d, mx_i, gap_open_penalty, gap_extend_penalty);
        Helpers.printMatrix(mx_d, "D");
        Helpers.printMatrix(mx_i, "I");
        Helpers.printMatrix(mx_a, "A");

        return 0;
    }

    public static void main(String[] args) {
        String P = "GCATGCCAT";
        String S = "CATGCATCGAC";
        ScoringMatrix scoring_matrix = null;
        int gap_open_penalty = -2;
        int gap_extend_penalty = -1;
        String mode = "global";
        int score = 0;
        try {
            score = run(P, S, scoring_matrix, gap_open_penalty, gap_extend_penalty, mode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Optimal alignment score: " + score);
    }
}
