import java.io.IOException;
import java.util.ArrayList;

public class NeedlemanWunschV2 {

    public static void printMatrix(int[][] matrix) {
        System.out.println("Dynamic Programming Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static int needlemanWunsch(String P, String S, ScoringMatrix scoringMatrix, int gapOpenPenalty, int gapExtendPenalty, String mode) throws IOException {
        int m = P.length();
        int n = S.length();

        // Initialize the dynamic programming matrix
        int[][] dp = new int[m + 1][n + 1];

        if (mode.equals("global")) {
            // Initialize the first row and first column
            for (int i = 1; i <= m; i++) {
                dp[i][0] = i * gapExtendPenalty;
            }
            for (int j = 1; j <= n; j++) {
                dp[0][j] = j * gapExtendPenalty;
            }
        } else if (mode.equals("local") || mode.equals("freeshift")) {
            // Initialize the first row and first column
            for (int i = 1; i <= m; i++) {
                dp[i][0] = 0;
            }
            for (int j = 1; j <= n; j++) {
                dp[0][j] = 0;
            }
        }

        // Fill in the dynamic programming matrix
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int diagonal = dp[i - 1][j - 1] + scoringMatrix.getScore(P.charAt(i - 1), S.charAt(j - 1));
                int left = dp[i][j - 1] + gapExtendPenalty;
                int up = dp[i - 1][j] + gapExtendPenalty;

                int max = Math.max(Math.max(diagonal, left), up);
                int maxZero = Math.max(0, max);

                if (mode.equals("local")) {
                    dp[i][j] = maxZero;
                } else if (mode.equals("freeshift")) {
                    if (i == 1 || j == 1)
                        dp[i][j] = maxZero;
                    else
                        dp[i][j] = max;
                    dp[i][j] = max;
                } else if (mode.equals("global")) {
                    dp[i][j] = max;
                }
            }
        }

        ArrayList<String> alignment = new ArrayList<>();
        if (mode.equals("global")) {
            alignment = backtracking(P, S, dp, scoringMatrix, gapExtendPenalty);
        } else if (mode.equals("local")) {
            alignment = backtrackinglocal(P, S, dp, scoringMatrix, gapExtendPenalty);
        } else if (mode.equals("freeshift")) {
            alignment = backtrackingfreeshift(P, S, dp, scoringMatrix, gapExtendPenalty);
        }

        for (String s : alignment) {
            System.out.println(s);
        }


        // Return the score at the bottom-right corner of the matrix (optimal alignment score)
        printMatrix(dp);
        return dp[m][n];
    }

    public static ArrayList<String> backtracking(String P, String S, int[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
        //backtracking
        char[] PArray = P.toCharArray();
        char[] SArray = S.toCharArray();
        ArrayList<String> alignment = new ArrayList<>();
        int k = PArray.length;
        int l = SArray.length;
        StringBuilder alignedP = new StringBuilder();
        StringBuilder alignedS = new StringBuilder();

        while (k > 0 || l > 0) {
            if (k > 0 && l > 0 && m[k][l] == m[k - 1][l - 1] + scoringMatrix.getScore(PArray[k - 1], SArray[l - 1])) {
                alignedP.append(PArray[k - 1]);
                alignedS.append(SArray[l - 1]);
                k--;
                l--;
            } else if (k > 0 && m[k][l] == m[k - 1][l] + gapExtendPenalty) {
                alignedP.append(PArray[k - 1]);
                alignedS.append("-");
                k--;
            } else {
                alignedP.append("-");
                alignedS.append(SArray[l - 1]);
                l--;
            }
        }

        alignment.add(alignedP.reverse().toString());
        alignment.add(alignedS.reverse().toString());
        return alignment;
        /*
        for (String s : alignment) {
            System.out.println(s);
        }
         */
    }

    public static ArrayList<String> backtrackinglocal(String P, String S, int[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
        // Backtracking
        char[] PArray = P.toCharArray();
        char[] SArray = S.toCharArray();
        ArrayList<String> alignment = new ArrayList<>();
        StringBuilder alignedP = new StringBuilder();
        StringBuilder alignedS = new StringBuilder();

        // Find the cell with the highest score
        int maxScore = Integer.MIN_VALUE;
        int maxI = 0;
        int maxJ = 0;
        for (int i = 0; i < m.length; i++) {
            for (int j = 0; j < m[0].length; j++) {
                if (m[i][j] > maxScore) {
                    maxScore = m[i][j];
                    maxI = i;
                    maxJ = j;
                }
            }
        }

        // Start backtracking from the cell with the highest score
        int k = maxI;
        int l = maxJ;

        while (k > 0 && l > 0 && m[k][l] != 0) {
            if (m[k][l] == m[k - 1][l - 1] + scoringMatrix.getScore(PArray[k - 1], SArray[l - 1])) {
                alignedP.append(PArray[k - 1]);
                alignedS.append(SArray[l - 1]);
                k--;
                l--;
            } else if (m[k][l] == m[k - 1][l] + gapExtendPenalty) {
                alignedP.append(PArray[k - 1]);
                alignedS.append("-");
                k--;
            } else {
                alignedP.append("-");
                alignedS.append(SArray[l - 1]);
                l--;
            }
        }



        // Reverse the alignment strings
        String alignedPString = alignedP.reverse().toString();
        String alignedSString = alignedS.reverse().toString();

        // Add the alignment strings to the list
        alignment.add(alignedPString);
        alignment.add(alignedSString);
        return alignment;
        /*
        // Print the alignment
        for (String s : alignment) {
            System.out.println(s);
        }
         */
    }

    public static ArrayList<String> backtrackingfreeshift(String P, String S, int[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
        // Backtracking
        char[] PArray = P.toCharArray();
        char[] SArray = S.toCharArray();
        ArrayList<String> alignment = new ArrayList<>();
        StringBuilder alignedP = new StringBuilder();
        StringBuilder alignedS = new StringBuilder();

        // Find the cell with the highest score in the last row
        int maxScoreLastRow = Integer.MIN_VALUE;
        int maxJLastRow = 0;
        for (int j = 0; j < m[0].length; j++) {
            if (m[m.length - 1][j] > maxScoreLastRow) {
                maxScoreLastRow = m[m.length - 1][j];
                maxJLastRow = j;
            }
        }

        // Find the cell with the highest score in the last column
        int maxScoreLastCol = Integer.MIN_VALUE;
        int maxILastCol = 0;
        for (int i = 0; i < m.length; i++) {
            if (m[i][m[0].length - 1] > maxScoreLastCol) {
                maxScoreLastCol = m[i][m[0].length - 1];
                maxILastCol = i;
            }
        }

        // Start backtracking from the cell with the highest score in the last row or column
        int k;
        int l;
        if (maxScoreLastRow >= maxScoreLastCol) {
            k = m.length - 1;
            l = maxJLastRow;
        } else {
            k = maxILastCol;
            l = m[0].length - 1;
        }

        while (k > 0 && l > 0) {
            if (m[k][l] == 0) {
                break;
            }
            if (m[k][l] == m[k - 1][l - 1] + scoringMatrix.getScore(PArray[k - 1], SArray[l - 1])) {
                alignedP.append(PArray[k - 1]);
                alignedS.append(SArray[l - 1]);
                k--;
                l--;
            } else if (m[k][l] == m[k - 1][l] + gapExtendPenalty) {
                alignedP.append(PArray[k - 1]);
                alignedS.append("-");
                k--;
            } else {
                alignedP.append("-");
                alignedS.append(SArray[l - 1]);
                l--;
            }
        }

        // Reverse the alignment strings
        String alignedPString = alignedP.reverse().toString();
        String alignedSString = alignedS.reverse().toString();

        // Add the alignment strings to the list
        alignment.add(alignedPString);
        alignment.add(alignedSString);
        return alignment;
        /*
        // Print the alignment
        for (String s : alignment) {
            System.out.println(s);
        }
         */
    }

}
