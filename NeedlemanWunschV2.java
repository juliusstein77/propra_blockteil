import java.io.IOException;

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

        // Initialize the first row and first column
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i * gapExtendPenalty;
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j * gapExtendPenalty;
        }

        // Fill in the dynamic programming matrix
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int diagonal = dp[i - 1][j - 1] + scoringMatrix.getScore(P.charAt(i - 1), S.charAt(j - 1));
                int left = dp[i][j - 1] + gapExtendPenalty;
                int up = dp[i - 1][j] + gapExtendPenalty;
                dp[i][j] = Math.max(Math.max(diagonal, left), up);
            }
        }

        // Return the score at the bottom-right corner of the matrix (optimal alignment score)
        printMatrix(dp);
        return dp[m][n];
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
