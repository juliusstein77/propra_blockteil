import java.io.IOException;

public class Gotoh {
    public static int run(String P, String S, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode) throws IOException {
        int m = P.length();
        int n = S.length();
        int[][] dp = new int[m + 1][n + 1];




        return dp[m][n];
    }

    public static void main(String[] args) {
        String P = "TATAAT";
        String S = "TTACGTAAGC";
        ScoringMatrix scoring_matrix = null;
        int gap_open_penalty = -11;
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
