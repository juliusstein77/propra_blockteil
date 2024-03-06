import utils.Helpers;

import java.io.IOException;

public class Gotoh {
    private static String backtrack(double[][] d, double[][] i, double[][] a, String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode){
        String alignment = "";
        if (mode == "global") {
            String alignmentP = "";
            String alignmentS = "";
            int r = a.length - 1;
            int c = a[0].length - 1;
            while (r > 0 && c > 0) {
                // --- diagonal --- >>> match/mismatch
                if (a[r][c] == a[r - 1][c - 1] + scoring_matrix.getScore(P.charAt(r - 1), S.charAt(c - 1))) {
                    //System.out.println(P.charAt(r - 1) + " " + S.charAt(c - 1));
                    alignmentP = P.charAt(r - 1) + alignmentP;
                    alignmentS = S.charAt(c - 1) + alignmentS;
                    r--;
                    c--;
                }
                // --- up --- >>> gap in P
                else if (a[r][c] == d[r][c]) {
                    //System.out.println(S.charAt(c - 1) + " -"); // S, gap in P
                    alignmentP = "-" + alignmentP;
                    alignmentS = S.charAt(c - 1) + alignmentS;
                    c--;
                }
                // --- left --- >>> gap in S
                else if (a[r][c] == i[r][c]) {
                    //System.out.println("- " + P.charAt(r - 1)); // gap in S, P
                    alignmentP = P.charAt(r - 1) + alignmentP;
                    alignmentS = "-" + alignmentS;
                    r--;
                }

            }
            alignment = alignmentP + "\n" + alignmentS;
        }
        return alignment;
    }

    private static void compute(double[][] d, double[][] i, double[][] a, String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode) {
        if (mode == "global") {
            for (int r = 1; r < a.length; r++) {
                for (int c = 1; c < a[0].length; c++) {
                    i[r][c] = Math.max(a[r - 1][c] + gap_open_penalty + gap_extend_penalty, i[r - 1][c] + gap_extend_penalty);
                    d[r][c] = Math.max(a[r][c - 1] + gap_open_penalty + gap_extend_penalty, d[r][c - 1] + gap_extend_penalty);
                    a[r][c] = Math.max(a[r - 1][c - 1] + scoring_matrix.getScore(P.charAt(r - 1), S.charAt(c - 1)), Math.max(d[r][c], i[r][c]));
                }
            }
        }
    }


    private static void initializeMatrices(String mode, double[][] a, double[][] d, double[][] i, Integer go, Integer ge) {
        switch (mode) {
            case "global":
                for (int j = 0; j < a[0].length; j++) {
                    a[0][j] = go + j * ge;
                    //a[j][0] = go + j * ge;
                }
                for (int i1 = 0; i1 < a.length; i1++) {
                    a[i1][0] = go + i1 * ge;
                }
                a[0][0] = 0;
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
        compute(mx_d, mx_i, mx_a, P, S, scoring_matrix, gap_open_penalty, gap_extend_penalty, mode);
        if (debug) {
            System.out.println("---||--- Computing GOTOH WITHOUT BACKTRACING ---||---\n\n");
            Helpers.printMatrix(mx_d, "D");
            Helpers.printMatrix(mx_i, "I");
            Helpers.printMatrix(mx_a, "A");
        }
        String alignment = backtrack(mx_d, mx_i, mx_a, P, S, scoring_matrix, gap_open_penalty, gap_extend_penalty, mode);
        System.out.println(alignment);
        return mx_a[m][n];
    }

    public static void main(String[] args) {
        try {
            String P = "WTHA";
            String S = "WTHGQA";
            ScoringMatrix scoringMatrix = new ScoringMatrix("C:\\Users\\smith\\Downloads\\blosum62.mat");
            int go = -10;
            int ge = -2;
            String mode = "global";
            double gotohScore = Gotoh.run(P, S, scoringMatrix, go, ge, mode, true);
            System.out.println("Optimal alignment score: " + gotohScore);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
