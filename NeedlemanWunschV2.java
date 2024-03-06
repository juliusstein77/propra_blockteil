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

    //TODO: local alignment, freeshift alignment, global alignment
    //TODO: Backtracking to find the optimal alignment(s)

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

        if (mode == "global") {
            backtracking(P, S, dp, scoringMatrix, gapExtendPenalty);
        } else if (mode == "local") {
            backtrackinglocal(P, S, dp, scoringMatrix, gapExtendPenalty);
        } else if (mode == "freeshift") {
            backtrackingfreeshift(P, S, dp, scoringMatrix, gapExtendPenalty);
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

    public static void backtrackinglocal(String P, String S, int[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
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

        // Print the alignment
        for (String s : alignment) {
            System.out.println(s);
        }
    }

    public static void backtrackingfreeshift(String P, String S, int[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
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

        // Print the alignment
        for (String s : alignment) {
            System.out.println(s);
        }
    }


    /*
    public static void main(String[] args) {
        String P = "TATAAT";
        String S = "TTACGTAAGC";
        int matchScore = 3;
        int mismatchPenalty = -2;
        int gapPenalty = -4;

        int score = needlemanWunsch(P, S, matchScore, mismatchPenalty, gapPenalty);
        System.out.println("Optimal alignment score: " + score);
    }
    */
}

/*
Advantages of Dynamic Programming over Simple Recursive Implementation:

Efficiency: Dynamic programming stores and reuses previously computed results, which avoids redundant computations.
This leads to significant improvements in runtime performance compared to simple recursive implementations, especially for large input sizes.

Memoization: Dynamic programming implicitly uses memoization to store computed values in a table, eliminating the need to recompute them.
This reduces the overall amount of computation required.

Space Complexity: While dynamic programming uses additional memory to store the dynamic programming table,
the amount of memory used is proportional to the size of the input (i.e., the lengths of the input strings), making it memory-efficient for practical input sizes.
In contrast, simple recursive implementations may consume more memory due to the recursive call stack, potentially leading to stack overflow errors for large input sizes.
 */
