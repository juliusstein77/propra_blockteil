import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

public class NeedlemanWunschV2 {

    static ArrayList<String> alignment;

    public static ArrayList<String> getAlignment() {
        return alignment;
    }

    public static void printMatrix(double[][] matrix) {
        System.out.println("Dynamic Programming Matrix:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.print(matrix[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static double needlemanWunsch(String P, String S, ScoringMatrix scoringMatrix, int gapExtendPenalty, String mode) throws IOException {
        int m = P.length();
        int n = S.length();

        // Initialize the dynamic programming matrix
        double[][] dp = new double[m + 1][n + 1];

        if (mode.equals("global")) {
            // Initialize the first row and first column
            for (int i = 1; i <= m; i++) {
                dp[i][0] = (i * gapExtendPenalty);
            }
            for (int j = 1; j <= n; j++) {
                dp[0][j] = (j * gapExtendPenalty);
            }
        } else if (mode.equals("local") || mode.equals("freeshift")) {
            // Initialize the first row and first column
            for (int i = 1; i <= m; i++) {
                dp[i][0] = 0.0;
            }
            for (int j = 1; j <= n; j++) {
                dp[0][j] = 0.0;
            }
        }

        // Fill in the dynamic programming matrix
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                double diagonal = dp[i - 1][j - 1] + scoringMatrix.getScore(P.charAt(i - 1), S.charAt(j - 1));
                double left = dp[i][j - 1] + gapExtendPenalty;
                double up = dp[i - 1][j] + gapExtendPenalty;


                diagonal = new BigDecimal(diagonal).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                left = new BigDecimal(left).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                up = new BigDecimal(up).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();


                double max = Math.max(Math.max(diagonal, left), up);
                double maxZero = Math.max(max, 0.0);

                if (mode.equals("local")) {
                    dp[i][j] = maxZero;
                } else if (mode.equals("freeshift")) {
                    if (i == 1 || j == 1)
                        dp[i][j] = maxZero;
                    else
                        dp[i][j] = max;
                    dp[i][j] = max; //TODO: check if this is correct
                } else if (mode.equals("global")) {
                    dp[i][j] = max;
                }
            }
        }
        //printMatrix(dp);

        double score = 0;
        //ArrayList<String> alignment = new ArrayList<>();
        if (mode.equals("global")) {
            alignment = backtracking(P, S, dp, scoringMatrix, gapExtendPenalty);
            score = dp[m][n];
        } else if (mode.equals("local")) {
            alignment = backtrackinglocal(P, S, dp, scoringMatrix, gapExtendPenalty);
            double maxScore = Integer.MIN_VALUE;
            double maxI = 0;
            double maxJ = 0;
            for (int i = 0; i < dp.length; i++) {
                for (int j = 0; j < dp[0].length; j++) {
                    if (dp[i][j] > maxScore) {
                        maxScore = dp[i][j];
                    }
                }
            }
            score = maxScore;
        } else if (mode.equals("freeshift")) {
            alignment = backtrackingfreeshift(P, S, dp, scoringMatrix, gapExtendPenalty);
            // Find the cell with the highest score in the last row
            double maxScoreLastRow = Integer.MIN_VALUE;
            int maxJLastRow = 0;
            for (int j = 0; j < dp[0].length; j++) {
                if (dp[dp.length - 1][j] > maxScoreLastRow) {
                    maxScoreLastRow = dp[dp.length - 1][j];
                    maxJLastRow = j;
                }
            }

            // Find the cell with the highest score in the last column
            double maxScoreLastCol = Integer.MIN_VALUE;
            int maxILastCol = 0;
            for (int i = 0; i < dp.length; i++) {
                if (dp[i][dp[0].length - 1] > maxScoreLastCol) {
                    maxScoreLastCol = dp[i][dp[0].length - 1];
                    maxILastCol = i;
                }
            }

            // Start backtracking from the cell with the highest score in the last row or column
            int k;
            int l;
            if (maxScoreLastRow >= maxScoreLastCol) {
                k = dp.length - 1;
                l = maxJLastRow;
            } else {
                k = maxILastCol;
                l = dp[0].length - 1;
            }
            score = dp[k][l];
        }

        // Return the score at the bottom-right corner of the matrix (optimal alignment score)
        //printMatrix(dp);
        return score;
    }

    public static ArrayList<String> backtracking(String P, String S, double[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
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
            } else if (l > 0 && m[k][l] == m[k][l - 1] + gapExtendPenalty) {
                alignedP.append("-");
                alignedS.append(SArray[l - 1]);
                l--;
            } else if (k > 0 && m[k][l] == m[k - 1][l] + gapExtendPenalty) {
                alignedP.append(PArray[k - 1]);
                alignedS.append("-");
                k--;

            }
        }

        alignment.add(alignedP.reverse().toString());
        alignment.add(alignedS.reverse().toString());
        return alignment;
    }



    public static ArrayList<String> backtrackinglocal(String P, String S, double[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
        // Backtracking
        char[] PArray = P.toCharArray();
        char[] SArray = S.toCharArray();
        ArrayList<String> alignment = new ArrayList<>();
        StringBuilder alignedP = new StringBuilder();
        StringBuilder alignedS = new StringBuilder();

        // Find the cell with the highest score
        double maxScore = Integer.MIN_VALUE;
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

        while (k > 0 && l > 0 && m[k][l] != 0.0) {
            double mat = new BigDecimal(m[k][l]).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            double c1 = new BigDecimal(m[k - 1][l - 1] + scoringMatrix.getScore(PArray[k - 1], SArray[l - 1])).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double c2 = new BigDecimal(m[k][l - 1] + gapExtendPenalty).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            double c3 = new BigDecimal(m[k - 1][l] + gapExtendPenalty).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();

            System.out.println("value: " + m[k][l]);
            System.out.println("diagonal: " + m[k-1][l-1]);
            System.out.println("left: " + m[k-1][l]);
            System.out.println("up: " + m[k][l-1]);
            System.out.println("aa: " + PArray[k - 1] + " " + SArray[l - 1]);

            if (mat == c1) {
                alignedP.append(PArray[k - 1]);
                alignedS.append(SArray[l - 1]);
                k--;
                l--;
            } else if (mat == c2) {
                alignedP.append("-");
                alignedS.append(SArray[l - 1]);
                l--;
            } else if (mat == c3) {
                alignedP.append(PArray[k - 1]);
                alignedS.append("-");
                k--;
            }
        }

        String prefixP = P.substring(0, k);
        String prefixS = S.substring(0, l);
        String suffixP = P.substring(maxI, P.length());
        String suffixS = S.substring(maxJ, S.length());

        int prefixPLength = prefixP.length();
        int prefixSLength = prefixS.length();
        int suffixPLength = suffixP.length();
        int suffixSLength = suffixS.length();

        if (prefixPLength > prefixSLength) {
            for (int i = 0; i < prefixPLength; i++) {
                prefixS = prefixS + "-";
            }
            for (int i = 0; i < prefixSLength; i++) {
                prefixP = "-" + prefixP ;
            }
        } else if (prefixPLength < prefixSLength) {
            for (int i = 0; i < prefixPLength; i++) {
                prefixS = "-" + prefixS;
            }
            for (int i = 0; i < prefixSLength; i++) {
                prefixP = prefixP + "-";
            }
        }

        if (suffixPLength > suffixSLength) {
            for (int i = 0; i < suffixPLength; i++) {
                suffixS = "-" + suffixS;
            }
            for (int i = 0; i < suffixSLength; i++) {
                suffixP = suffixP + "-";
            }
        } else if (suffixPLength < suffixSLength) {
            for (int i = 0; i < suffixPLength; i++) {
                suffixS = suffixS + "-";
            }
            for (int i = 0; i < suffixSLength; i++) {
                suffixP = "-" + suffixP;
            }
        }


        String alignedPString = alignedP.reverse().toString();
        String alignedSString = alignedS.reverse().toString();
        alignedPString = prefixP + alignedPString + suffixP;
        alignedSString = prefixS + alignedSString + suffixS;

        int i;
        if (alignedPString.length() > alignedSString.length()) {
            for(i = alignedSString.length(); i < alignedPString.length(); ++i) {
                alignedSString = alignedSString + "-";
            }
        } else if (alignedPString.length() < alignedSString.length()) {
            for(i = alignedPString.length(); i < alignedSString.length(); ++i) {
                alignedPString = alignedPString + "-";
            }
        }

        alignment.add(alignedPString);
        alignment.add(alignedSString);
        return alignment;
    }

    public static ArrayList<String> backtrackingfreeshift(String P, String S, double[][] m, ScoringMatrix scoringMatrix, int gapExtendPenalty) {
        // Backtracking
        char[] PArray = P.toCharArray();
        char[] SArray = S.toCharArray();
        ArrayList<String> alignment = new ArrayList<>();
        StringBuilder alignedP = new StringBuilder();
        StringBuilder alignedS = new StringBuilder();

        // Find the cell with the highest score in the last row
        double maxScoreLastRow = Integer.MIN_VALUE;
        int maxJLastRow = 0;
        for (int j = 0; j < m[0].length; j++) {
            if (m[m.length - 1][j] > maxScoreLastRow) {
                maxScoreLastRow = m[m.length - 1][j];
                maxJLastRow = j;
            }
        }

        // Find the cell with the highest score in the last column
        double maxScoreLastCol = Integer.MIN_VALUE;
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

        int maxI = k;
        int maxJ = l;

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

        String prefixP = P.substring(0, k);
        String prefixS = S.substring(0, l);
        String suffixP = P.substring(maxI, P.length());
        String suffixS = S.substring(maxJ, S.length());
        int prefixPLength = prefixP.length();
        int prefixSLength = prefixS.length();
        int suffixPLength = suffixP.length();
        int suffixSLength = suffixS.length();
        for (int i = prefixPLength; i > prefixSLength; i--) {
            prefixS = "-" + prefixS;
        }
        for (int i = prefixSLength; i > prefixPLength; i--) {
            prefixP = "-" + prefixP;
        }
        for (int i = suffixPLength; i > suffixSLength; i--) {
            suffixS = "-" + suffixS;
        }
        for (int i = suffixSLength; i > suffixPLength; i--) {
            suffixP = "-" + suffixP;
        }

        // Reverse the alignment strings
        String alignedPString = alignedP.reverse().toString();
        String alignedSString = alignedS.reverse().toString();

        alignedPString = prefixP + alignedPString + suffixP;
        alignedSString = prefixS + alignedSString + suffixS;

        for (int i = Math.max(maxI, maxJ); i < Math.max(P.length(), S.length()); i++) {
            if (("TACG".indexOf(alignedPString.charAt(i)) != -1) && ("TACG".indexOf(alignedSString.charAt(i)) != -1)) {
                alignedPString = alignedPString.substring(0, i) + "-" + alignedPString.substring(i);
            }
            if (("TACG".indexOf(alignedPString.charAt(i)) != -1) && ("TACG".indexOf(alignedSString.charAt(i)) != -1)) {
                alignedSString = alignedSString.substring(0, i) + "-" + alignedSString.substring(i);
            }
        }

        if (alignedPString.length() > alignedSString.length()) {
            for (int i = alignedSString.length(); i < alignedPString.length(); i++) {
                alignedSString = alignedSString + "-";
            }
        } else if (alignedPString.length() < alignedSString.length()) {
            for (int i = alignedPString.length(); i < alignedSString.length(); i++) {
                alignedPString = alignedPString + "-";
            }
        }

        // Add the alignment strings to the list
        alignment.add(alignedPString);
        alignment.add(alignedSString);
        return alignment;
    }

}
