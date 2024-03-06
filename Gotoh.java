import utils.Helpers;

import java.io.IOException;

public class Gotoh {
    private static double affine_gap_penalty(int gap_open_penalty, int gap_extend_penalty, int k) {
        return (k * gap_extend_penalty) + gap_open_penalty;
    }
    private static double g(int gap_open_penalty, int gap_extend_penalty, int k) {
        return (k * gap_extend_penalty) + gap_open_penalty;
    }
    private static void compute(double[][] d, double[][] i, double[][] a, String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode, Boolean debug) {
        for (int i1 = 1; i1 < a.length; i1++) { //cols
            for (int j = 1; j < a[0].length; j++) { //rows
                char p = P.charAt(i1 - 1);
                char s = S.charAt(j - 1);
                i[i1][j] = Math.max(a[i1 - 1][j] + gap_open_penalty + gap_extend_penalty, i[i1 - 1][j] + gap_extend_penalty);
                d[i1][j] = Math.max(a[i1][j - 1] + gap_open_penalty + gap_extend_penalty, d[i1][j - 1] + gap_extend_penalty);
                a[i1][j] = Math.max(a[i1 - 1][j - 1] + scoring_matrix.getScore(p, s), Math.max(d[i1][j], i[i1][j]));
            }
        }
    }


    private static void initializeMatrices(String mode, double[][] a, double[][] d, double[][] i, Integer go, Integer ge) {
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

    public static double run(String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode, Boolean debug) throws IOException {
        int m = P.length();
        int n = S.length();
        double[][] mx_d = new double[m + 1][n + 1];
        double[][] mx_i = new double[m + 1][n + 1];
        double[][] mx_a = new double[m + 1][n + 1];

        initializeMatrices(mode, mx_a, mx_d, mx_i, gap_open_penalty, gap_extend_penalty);
        if (debug) {
            System.out.println("---||--- Initializing matrices ---||---\n\n");
            Helpers.printMatrix(mx_d, "D");
            Helpers.printMatrix(mx_i, "I");
            Helpers.printMatrix(mx_a, "A");
        }
        compute(mx_d, mx_i, mx_a, P, S, scoring_matrix, gap_open_penalty, gap_extend_penalty, mode, debug);
        if (debug) {
            System.out.println("---||--- Computing GOTOH WITHOUT BACKTRACING ---||---\n\n");
            Helpers.printMatrix(mx_a, "A");
        }

        return mx_a[m][n];
    }

    public static void main(String[] args) {
        try {
            String P = "WTHGQA";
            String S = "WTHA";
            ScoringMatrix scoringMatrix = new ScoringMatrix("C:\\Users\\smith\\Downloads\\blosum62.mat");
            int go = -2;
            int ge = -1;
            String mode = "global";
            double gotohScore = Gotoh.run(P, S, scoringMatrix, go, ge, mode, true);
            System.out.println("Optimal alignment score: " + gotohScore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
