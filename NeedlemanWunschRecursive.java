public class NeedlemanWunschRecursive {

    private static int needlemanWunschRecursiveHelper(String P, String S, int i, int j, int matchScore, int mismatchPenalty, int gapPenalty) {
        // Base cases
        if (i == 0 && j == 0) {
            return 0;
        }
        if (i == 0) {
            return j * gapPenalty;
        }
        if (j == 0) {
            return i * gapPenalty;
        }

        // Recursive cases
        int matchMismatchScore = (P.charAt(i - 1) == S.charAt(j - 1)) ? matchScore : mismatchPenalty;
        int diagonal = needlemanWunschRecursiveHelper(P, S, i - 1, j - 1, matchScore, mismatchPenalty, gapPenalty) + matchMismatchScore;
        int left = needlemanWunschRecursiveHelper(P, S, i, j - 1, matchScore, mismatchPenalty, gapPenalty) + gapPenalty;
        int up = needlemanWunschRecursiveHelper(P, S, i - 1, j, matchScore, mismatchPenalty, gapPenalty) + gapPenalty;

        return Math.max(Math.max(diagonal, left), up);
    }

    public static void main(String[] args) {
        String P = "TATAAT";
        String S = "TTACGTAAGC";
        int matchScore = 3;
        int mismatchPenalty = -2;
        int gapPenalty = -4;

        int score = needlemanWunschRecursiveHelper(P, S, P.length(), S.length(), matchScore, mismatchPenalty, gapPenalty);
        System.out.println("Optimal alignment score: " + score);
    }
}
