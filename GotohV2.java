import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
//TODO: delete hamming relative
//TODO: why -1000000?

public class GotohV2 {
    public static int[] findMaxIndex(double[][] a) {
        double maxVal = -1000000;
        int[] answerArray = new int[2];
        for (int row = 0; row < a.length; row++) {
            for (int col = 0; col < a[row].length; col++) {
                if (a[row][col] > maxVal) {
                    maxVal = a[row][col];
                    answerArray[0] = row;
                    answerArray[1] = col;
                }
            }
        }
        return answerArray;
    }

    public static int[] findMaxIndexLastRowCol(double[][] a) {
        double maxValRow = -1000000;
        double maxValCol = -1000000;
        int[] maxPosRow = new int[2];
        int[] maxPosCol = new int[2];
        int rowLength = a.length;
        int colLength = a[0].length;
        for (int i = 0; i < colLength; i++) {
            if (a[rowLength - 1][i] >= maxValRow) {
                maxValRow = a[rowLength - 1][i];
                maxPosRow[0] = rowLength - 1;
                maxPosRow[1] = i;
            }
        }
        for (int j = 0; j < rowLength; j++) {
            if (a[j][colLength - 1] >= maxValCol) {
                maxValCol = a[j][colLength - 1];
                maxPosCol[0] = j;
                maxPosCol[1] = colLength - 1;
            }
        }

        if (a[maxPosRow[0]][maxPosRow[1]] > a[maxPosCol[0]][maxPosCol[1]]) {
            return maxPosRow;
        } else return maxPosCol;
    }


    public double bestScore;
    public double hammingRelative;
    public final String sequenceA;
    public final String sequenceB;
    public final int go;
    public final int ge;
    ScoringMatrix quasar;
    public double[][] matrixI;
    public double[][] matrixD;
    public double[][] matrixA;
    public String alignedStrings;
    public String outputAlignment;
    public ArrayList<int[]> backtrackList;

    public double matching(int i, int j) {
        return score(sequenceA.charAt(i - 1), sequenceB.charAt(j - 1)) + matrixA[i - 1][j - 1];
    }


    public static void initializeMatrices(String s, String t, double[][] mx_a, double[][] mx_i, double[][] mx_d, int gap_open_penalty, int gap_extend_penalty, ScoringMatrix sm, String mode) {
        switch (mode) {
            case "local":
                for (int i = 1; i < s.length() + 1; i++) {
                    mx_d[i][0] =  -1000000;
                }
                for (int j = 1; j < t.length() + 1; j++) {
                    mx_i[0][j] =  -1000000;
                }
                for (int i = 1; i < s.length() + 1; i++) {
                    for (int j = 1; j < t.length() + 1; j++) {
                        mx_i[i][j] = Math.max(mx_a[i - 1][j] + gap_open_penalty + gap_extend_penalty, mx_i[i - 1][j] + gap_extend_penalty);
                        mx_d[i][j] = Math.max(mx_a[i][j - 1] + gap_open_penalty + gap_extend_penalty, mx_d[i][j - 1] + gap_extend_penalty);
                        mx_a[i][j] = Math.max(0, Math.max(mx_a[i-1][j] + sm.getScore(s.charAt(i-1), t.charAt(j-1)), Math.max(mx_d[i][j], mx_i[i][j])));
                    }
                }
                break;
            case "global":
                for (int i = 1; i < s.length() + 1; i++) {
                    mx_d[i][0] = Double.NEGATIVE_INFINITY;
                }
                for (int j = 1; j < t.length() + 1; j++) {
                    mx_i[0][j] = Double.NEGATIVE_INFINITY;
                }
                mx_a[0][0] = 0;
                for (int j = 1; j < t.length() + 1; j++) {
                    int aCost = gap_extend_penalty + j * gap_open_penalty;
                    mx_a[0][j] = aCost;
                }
                for (int i = 1; i < s.length() + 1; i++) {
                    int aCost = gap_open_penalty + i * gap_extend_penalty;
                    mx_a[i][0] = aCost;
                }

                for (int i = 1; i < s.length() + 1; i++) {
                    for (int j = 1; j < t.length() + 1; j++) {
                        mx_i[i][j] = Math.max(mx_a[i - 1][j] + gap_open_penalty + gap_extend_penalty, mx_i[i - 1][j] + gap_extend_penalty);
                        mx_d[i][j] = Math.max(mx_a[i][j - 1] + gap_open_penalty + gap_extend_penalty, mx_d[i][j - 1] + gap_extend_penalty);
                        mx_a[i][j] = Math.max(mx_a[i - 1][j - 1] + sm.getScore(s.charAt(i - 1), t.charAt(j - 1)), Math.max(mx_d[i][j], mx_i[i][j]));
                    }
                }
                break;
        }

    }

    public static ArrayList<int[]> backtrack(String s, String t, double[][] mx_a, double[][] mx_i, double[][] mx_d, int gap_open_penalty, int gap_extend_penalty, ScoringMatrix sm, String mode) {
        ArrayList<int[]> backtrackArray = new ArrayList<int[]>();
        int i;
        int j;
        switch (mode){
            case "local":
                int[] starter_indices = new int[2];
                double maxVal = Double.NEGATIVE_INFINITY;
                for (int row = 0; row < mx_a.length; row++) {
                    for (int col = 0; col < mx_a[row].length; col++) {
                        if (mx_a[row][col] > maxVal) {
                            maxVal = mx_a[row][col];
                            starter_indices[0] = row;
                            starter_indices[1] = col;
                        }
                    }
                }
                i = starter_indices[0];
                j = starter_indices[1];
                while (mx_a[i][j] != 0) {
                    if (i > 0 && j > 0 && (Math.abs(mx_a[i][j] - (sm.getScore(s.charAt(i - 1), t.charAt(j - 1)) + mx_a[i - 1][j - 1])) < 0.0001)) {
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        --i;
                        --j;
                    } else if (Math.abs(mx_a[i][j] - mx_i[i][j]) < 0.0001) {
                        int k = 1;
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        while (k < i) {
                            if (Math.abs((mx_a[i - k][j] + gap_open_penalty + gap_extend_penalty * k) - mx_a[i][j]) < 0.0001) {
                                break;
                            }
                            ++k;
                        }
                        i -= k;
                    } else if (Math.abs(mx_a[i][j] - mx_d[i][j]) < 0.0001) {
                        int k = 1;
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        while (k < j) {
                            if (Math.abs((mx_a[i][j - k] + gap_open_penalty + gap_extend_penalty * k) - mx_a[i][j]) < 0.0001) {
                                break;
                            }
                            ++k;
                        }
                        j -= k;
                    }

                }
                backtrackArray.add(new int[]{i, j});
                break;

            case "global":
                i = s.length();
                j = t.length();
                while (i > 0 || j > 0) {
                    if (i > 0 && j > 0 && Math.abs(mx_a[i][j] - (sm.getScore(s.charAt(i - 1), t.charAt(j - 1)) + mx_a[i - 1][j - 1])) < 0.0001) {
                        //System.out.println("Matching: " + sequenceA.charAt(i-1) + " " + sequenceB.charAt(j-1));
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        --i;
                        --j;
                    } else if (Math.abs(mx_a[i][j] - mx_i[i][j]) < 0.0001) {
                        //System.out.println("Insertion: " + sequenceA.charAt(i-1) + " -");
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        int k = 1;
                        while (k < i) {
                            if (Math.abs((mx_a[i - k][j] + gap_open_penalty + gap_extend_penalty * k) - mx_a[i][j]) < 0.0001) {
                                break;
                            }
                            ++k;
                        }
                        i -= k;
                    } else if (Math.abs(mx_a[i][j] - mx_d[i][j]) < 0.0001) {
                        //System.out.println("Deletion: - " + sequenceB.charAt(j-1));
                        int[] position = {i, j};
                        backtrackArray.add(position);
                        int k = 1;
                        while (k < j) {
                            if (Math.abs((mx_a[i][j - k] + gap_open_penalty + gap_extend_penalty * k) - mx_a[i][j]) < 0.00001) {
                                break;
                            }
                            ++k;
                        }
                        j -= k;
                    } else if (i == 0 || j == 0) {
                        int[] positionVor = {i, j};
                        int[] position = {0, 0};
                        backtrackArray.add(positionVor);
                        backtrackArray.add(position);
                        break;
                    }
                }
                if (backtrackArray.get(backtrackArray.size() - 1)[0] == 0 && backtrackArray.get(backtrackArray.size() - 1)[1] == 0) {

                } else {
                    int[] position = {0, 0};
                    backtrackArray.add(position);
                }
                break;
            default:
                break;
        }

        return backtrackArray;
    }

    public static String generate_alignment(String s, String t, ArrayList<int[]> bts) {
        StringBuilder alignA = new StringBuilder();
        StringBuilder alignB = new StringBuilder();
        for (int listpointer = 0; listpointer < bts.size() - 1; listpointer++) {
            int i = bts.get(listpointer)[0];
            int j = bts.get(listpointer)[1];
            int x = bts.get(listpointer + 1)[0];
            int y = bts.get(listpointer + 1)[1];
            if (i == x + 1 && j == y + 1) {
                alignA.insert(0, s.charAt(i - 1));
                alignB.insert(0, t.charAt(j - 1));
            } else if (i == x && j > y) {
                int gap = j - y;
                int dummyCount = 1;
                while (gap > 0) {
                    alignA.insert(0, "-");
                    alignB.insert(0, t.charAt(j - dummyCount));
                    --gap;
                    ++dummyCount;
                }
            } else if (i > x && j == y) {
                int gap = i - x;
                int dummyCount = 1;
                while (gap > 0) {
                    alignA.insert(0, s.charAt(i - dummyCount));
                    alignB.insert(0, "-");
                    --gap;
                    ++dummyCount;
                }
            }
        }
        return alignA.toString() + "\n" + alignB.toString();

    }

    public static HashMap<Double, String> run(String s, String t, ScoringMatrix scoring_matrix, int gap_open_penalty, int gap_extend_penalty, String mode) throws IOException {
        double[][] mx_d = new double[s.length() + 1][t.length() + 1];
        double[][] mx_i = new double[s.length() + 1][t.length() + 1];
        double[][] mx_a = new double[s.length() + 1][t.length() + 1];
        initializeMatrices(s, t, mx_a, mx_i, mx_d, gap_open_penalty, gap_extend_penalty, scoring_matrix, mode);
        ArrayList<int[]> backtrackList = backtrack(s, t, mx_a, mx_i, mx_d, gap_open_penalty, gap_extend_penalty, scoring_matrix, mode);
        switch (mode) {
            case "local":
                int[] maxPosition = findMaxIndex(mx_a);
                int[] minPosition = findMaxIndexLastRowCol(mx_a);
                int maxI = maxPosition[0];
                int maxJ = maxPosition[1];
                int minI = minPosition[0];
                int minJ = minPosition[1];
                String alignment = generate_alignment(s, t, backtrackList);
                Double alignment_score = mx_a[maxI][maxJ];


            case "global":

                break;
            default:
                break;
        }
        String alignment = generate_alignment(s, t, backtrackList);
        Double alignment_score = mx_a[s.length()][t.length()];

        return new HashMap<Double, String>() {{
            put(alignment_score, alignment);
        }};
    }

    public GotohV2(String sequenceA, String sequenceB, ScoringMatrix quasar, int go, int ge, String mode) {
        this.sequenceA = sequenceA;
        this.sequenceB = sequenceB;
        this.quasar = quasar;
        this.go = go;
        this.ge = ge;
        switch (mode) {
            case "global": {
                matrixA = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixD = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixI = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                this.computeGotoGlobal();
                this.computeGotoGlobalBacktrack();
                this.alignmentFromBacktrack();
                this.outputAlignment = alignedStrings;
                this.bestScore = matrixA[sequenceA.length()][sequenceB.length()];
                this.calculateHammingRelative();
                break;
            }
            case "local": {
                matrixA = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixD = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixI = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                this.gotohLocalAlign();
                this.computeGotoLocalBacktrack();
                this.alignmentFromBacktrack();
                int[] maxPosition = backtrackList.get(0);
                int[] minPosition = backtrackList.get(backtrackList.size() - 1);
                int maxI = maxPosition[0];
                int maxJ = maxPosition[1];
                int minI = minPosition[0];
                int minJ = minPosition[1];
                this.bestScore = matrixA[maxI][maxJ];
                this.outputAlignment = gotoGetOutputAlign(maxI, maxJ, minI, minJ);
                this.calculateHammingRelative();
                break;
            }
            case "freeshift": {
                matrixA = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixD = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                matrixI = new double[sequenceA.length() + 1][sequenceB.length() + 1];
                this.gotohFreeshiftAlign();
                this.computeGotoFreeshiftBacktrack();
                this.alignmentFromBacktrack();
                int[] maxPosition = backtrackList.get(0);
                int[] minPosition = backtrackList.get(backtrackList.size() - 1);
                int maxI = maxPosition[0];
                int maxJ = maxPosition[1];
                int minI = minPosition[0];
                int minJ = minPosition[1];
                this.bestScore = matrixA[maxI][maxJ];
                this.outputAlignment = gotoGetOutputAlign(maxI, maxJ, minI, minJ);
                this.calculateHammingRelative();
                break;
            }
        }
    }

    /**
     * Getting Score from QASAR HashMap
     *
     * @param a character from Sequence A
     * @param b character from Sequence b
     * @return the double score of this matching
     */
    public double score(char a, char b) {
        return quasar.getScore(a, b);
    }

    /**
     * Compute the matrices with the Goto Algo and fill it.
     */
    public void computeGotoGlobal() {
        //Initializing state matrixD
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            matrixD[i][0] = Double.NEGATIVE_INFINITY;
        }
        //Initializing state matrixI
        for (int j = 1; j < sequenceB.length() + 1; j++) {
            matrixI[0][j] = Double.NEGATIVE_INFINITY;
        }
        //Initializing state matrixA
        matrixA[0][0] = 0;
        for (int j = 1; j < sequenceB.length() + 1; j++) {
            int aCost = go + j * ge;
            matrixA[0][j] = aCost;
        }
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            int aCost = go + i * ge;
            matrixA[i][0] = aCost;
        }

        for (int i = 1; i < sequenceA.length() + 1; i++) {
            for (int j = 1; j < sequenceB.length() + 1; j++) {
                char charSeqA = sequenceA.charAt(i - 1);
                char charSeqB = sequenceB.charAt(j - 1);
                matrixI[i][j] = Math.max(matrixA[i - 1][j] + go + ge, matrixI[i - 1][j] + ge);
                matrixD[i][j] = Math.max(matrixA[i][j - 1] + go + ge, matrixD[i][j - 1] + ge);
                matrixA[i][j] = Math.max(matrixA[i - 1][j - 1] + score(charSeqA, charSeqB), Math.max(matrixD[i][j], matrixI[i][j]));
            }
        }
        this.bestScore = matrixA[sequenceA.length()][sequenceB.length()];
    }

    public void gotohLocalAlign() {
        //Initializing state matrixD
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            matrixD[i][0] = -1000000;
        }
        //Initializing state matrixI
        for (int j = 1; j < sequenceB.length() + 1; j++) {
            matrixI[0][j] = -1000000;
        }
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            for (int j = 1; j < sequenceB.length() + 1; j++) {
                char charSeqA = sequenceA.charAt(i - 1);
                char charSeqB = sequenceB.charAt(j - 1);
                matrixI[i][j] = Math.max(matrixA[i - 1][j] + go + ge, matrixI[i - 1][j] + ge);
                matrixD[i][j] = Math.max(matrixA[i][j - 1] + go + ge, matrixD[i][j - 1] + ge);
                matrixA[i][j] = Math.max(0, Math.max(matrixA[i - 1][j - 1] + score(charSeqA, charSeqB), Math.max(matrixD[i][j], matrixI[i][j])));
            }
        }
        int[] maxPosition = findMaxIndex(matrixA);
        this.bestScore = matrixA[maxPosition[0]][maxPosition[1]];
    }

    public void gotohFreeshiftAlign() {
        //Initializing state matrixD
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            matrixD[i][0] = -1000000;
        }
        //Initializing state matrixI
        for (int j = 1; j < sequenceB.length() + 1; j++) {
            matrixI[0][j] = -1000000;
        }
        for (int i = 1; i < sequenceA.length() + 1; i++) {
            for (int j = 1; j < sequenceB.length() + 1; j++) {
                char charSeqA = sequenceA.charAt(i - 1);
                char charSeqB = sequenceB.charAt(j - 1);
                matrixI[i][j] = Math.max(matrixA[i - 1][j] + go + ge, matrixI[i - 1][j] + ge);
                matrixD[i][j] = Math.max(matrixA[i][j - 1] + go + ge, matrixD[i][j - 1] + ge);
                matrixA[i][j] = Math.max(matrixA[i - 1][j - 1] + score(charSeqA, charSeqB), Math.max(matrixD[i][j], matrixI[i][j]));
            }
        }
        int[] maxPosition = findMaxIndexLastRowCol(matrixA);
        this.bestScore = matrixA[maxPosition[0]][maxPosition[1]];
    }

    /**
     * Comnpute the backtracing path of Alignment
     *
     * @return ArrayList with backtracing path
     */
    public void computeGotoGlobalBacktrack() {
        ArrayList<int[]> backtrackArray = new ArrayList<int[]>();
        int i = sequenceA.length();
        int j = sequenceB.length();
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && Math.abs(matrixA[i][j] - matching(i, j)) < 0.0001) {
                //System.out.println("Matching: " + sequenceA.charAt(i-1) + " " + sequenceB.charAt(j-1));
                int[] position = {i, j};
                backtrackArray.add(position);
                --i;
                --j;
            } else if (Math.abs(matrixA[i][j] - matrixI[i][j]) < 0.0001) {
                //System.out.println("Insertion: " + sequenceA.charAt(i-1) + " -");
                int[] position = {i, j};
                backtrackArray.add(position);
                int k = 1;
                while (k < i) {
                    if (Math.abs((matrixA[i - k][j] + go + ge * k) - matrixA[i][j]) < 0.0001) {
                        break;
                    }
                    ++k;
                }
                i -= k;
            } else if (Math.abs(matrixA[i][j] - matrixD[i][j]) < 0.0001) {
                //System.out.println("Deletion: - " + sequenceB.charAt(j-1));
                int[] position = {i, j};
                backtrackArray.add(position);
                int k = 1;
                while (k < j) {
                    if (Math.abs((matrixA[i][j - k] + go + ge * k) - matrixA[i][j]) < 0.00001) {
                        break;
                    }
                    ++k;
                }
                j -= k;
            } else if (i == 0 || j == 0) {
                int[] positionVor = {i, j};
                int[] position = {0, 0};
                backtrackArray.add(positionVor);
                backtrackArray.add(position);
                break;
            }
        }
        if (backtrackArray.get(backtrackArray.size() - 1)[0] == 0 && backtrackArray.get(backtrackArray.size() - 1)[1] == 0) {

        } else {
            int[] position = {0, 0};
            backtrackArray.add(position);
        }
        this.backtrackList = backtrackArray;
    }

    public void computeGotoLocalBacktrack() {
        ArrayList<int[]> backtrackArray = new ArrayList<int[]>();
        int[] maxPosition = findMaxIndex(matrixA);
        int i = maxPosition[0];
        int j = maxPosition[1];
        while (matrixA[i][j] != 0) {
            if (i > 0 && j > 0 && (Math.abs(matrixA[i][j] - matching(i, j)) < 0.0001)) {
                int[] position = {i, j};
                backtrackArray.add(position);
                --i;
                --j;
            } else if (Math.abs(matrixA[i][j] - matrixI[i][j]) < 0.0001) {
                int k = 1;
                int[] position = {i, j};
                backtrackArray.add(position);
                while (k < i) {
                    if (Math.abs((matrixA[i - k][j] + go + ge * k) - matrixA[i][j]) < 0.0001) {
                        break;
                    }
                    ++k;
                }
                i -= k;
            } else if (Math.abs(matrixA[i][j] - matrixD[i][j]) < 0.0001) {
                int k = 1;
                int[] position = {i, j};
                backtrackArray.add(position);
                while (k < j) {
                    if (Math.abs((matrixA[i][j - k] + go + ge * k) - matrixA[i][j]) < 0.0001) {
                        break;
                    }
                    ++k;
                }
                j -= k;
            }

        }
        int[] position = {i, j};
        backtrackArray.add(position);
        this.backtrackList = backtrackArray;
    }

    public void computeGotoFreeshiftBacktrack() {
        ArrayList<int[]> backtrackArray = new ArrayList<int[]>();
        int[] maxPosition = findMaxIndexLastRowCol(matrixA);
        int i = maxPosition[0];
        int j = maxPosition[1];
        while (i > 0 && j > 0) {
            if (Math.abs(matrixA[i][j] - matching(i, j)) < 0.0001) {
                int[] position = {i, j};
                backtrackArray.add(position);
                --i;
                --j;
            } else if (Math.abs(matrixA[i][j] - matrixI[i][j]) < 0.0001) {
                int k = 1;
                int[] position = {i, j};
                backtrackArray.add(position);
                while (k < i) {
                    if (Math.abs((matrixA[i - k][j] + go + ge * k) - matrixA[i][j]) < 0.0001) {
                        break;
                    }
                    ++k;
                }
                i -= k;
            } else if (Math.abs(matrixA[i][j] - matrixD[i][j]) < 0.0001) {
                int k = 1;
                int[] position = {i, j};
                backtrackArray.add(position);
                while (k < j) {
                    if (Math.abs((matrixA[i][j - k] + go + ge * k) - matrixA[i][j]) < 0.0001) {
                        break;
                    }
                    ++k;
                }
                j -= k;
            }
        }
        int[] position = {i, j};
        backtrackArray.add(position);
        this.backtrackList = backtrackArray;
    }

    public String gotoGetOutputAlign(int maxI, int maxJ, int minI, int minJ) {
        StringBuilder alignA = new StringBuilder();
        StringBuilder alignB = new StringBuilder();
        if (alignedStrings.equals("\n")) {
            int bLength = this.sequenceB.length();
            int aLength = this.sequenceA.length();
            String repStr = "-";
            String repeatedGapB = repStr.repeat(aLength);
            String repeatedGapA = repStr.repeat(bLength);
            alignA.append(sequenceA);
            alignA.append(repeatedGapA);
            alignB.append(repeatedGapB);
            alignB.append(sequenceB);
        } else {
            String[] strings = this.alignedStrings.split("\n");
            alignA.append(strings[0]);
            alignB.append(strings[1]);
            int bLength = this.sequenceB.length();
            int aLength = this.sequenceA.length();
            alignA.insert(0, this.sequenceA.substring(0, minI));
            for (int i = 0; i < minI; i++) {
                alignB.insert(0, "-");
            }
            alignB.insert(0, this.sequenceB.substring(0, minJ));
            for (int j = 0; j < minJ; j++) {
                alignA.insert(0, "-");
            }
            alignA.append(this.sequenceA.substring(maxI, aLength));
            alignB.append("-".repeat(Math.max(0, aLength - maxI)));
            alignB.append(this.sequenceB.substring(maxJ, bLength));
            alignA.append("-".repeat(Math.max(0, bLength - maxJ)));
        }
        return alignA.toString() + "\n" + alignB.toString();
    }

    public void alignmentFromBacktrack() {
        StringBuilder alignA = new StringBuilder();
        StringBuilder alignB = new StringBuilder();
        for (int listpointer = 0; listpointer < backtrackList.size() - 1; listpointer++) {
            int i = backtrackList.get(listpointer)[0];
            int j = backtrackList.get(listpointer)[1];
            int x = backtrackList.get(listpointer + 1)[0];
            int y = backtrackList.get(listpointer + 1)[1];
            if (i == x + 1 && j == y + 1) {
                alignA.insert(0, sequenceA.charAt(i - 1));
                alignB.insert(0, sequenceB.charAt(j - 1));
            } else if (i == x && j > y) {
                int gap = j - y;
                int dummyCount = 1;
                while (gap > 0) {
                    alignA.insert(0, "-");
                    alignB.insert(0, sequenceB.charAt(j - dummyCount));
                    --gap;
                    ++dummyCount;
                }
            } else if (i > x && j == y) {
                int gap = i - x;
                int dummyCount = 1;
                while (gap > 0) {
                    alignA.insert(0, sequenceA.charAt(i - dummyCount));
                    alignB.insert(0, "-");
                    --gap;
                    ++dummyCount;
                }
            }
        }
        this.alignedStrings = alignA.toString() + "\n" + alignB.toString();
    }

    public double checkScore() {
        String[] sepStrings = alignedStrings.split("\n");
        String stringA = sepStrings[0];
        String stringB = sepStrings[1];
        double score = 0;
        for (int i = 0; i < stringA.length(); i++) {
            if (stringA.charAt(i) == '-') {
                if (i == 0 || stringA.charAt(i - 1) != '-') {
                    score += go + ge;
                } else {
                    score += ge;
                }
            } else if (stringB.charAt(i) == '-') {
                if (i == 0 || stringB.charAt(i - 1) != '-') {
                    score += go + ge;
                } else {
                    score += ge;
                }
            } else {
                double value = this.score(stringA.charAt(i), stringB.charAt(i));
                score += value;
                score = Math.round(score * 10) / 10.0;
            }
        }
        return score;
    }

    /**
     * Calculating the hammingscore as relative number
     */
    public void calculateHammingRelative() {
        char[] seq1;
        char[] seq2;
        String[] alginedStrings = this.outputAlignment.split("\n");
        int score = 0;
        if (alginedStrings[0].length() > alginedStrings[1].length()) {
            seq1 = alginedStrings[1].toCharArray();
            seq2 = alginedStrings[0].toCharArray();
        } else {
            seq1 = alginedStrings[0].toCharArray();
            seq2 = alginedStrings[1].toCharArray();
        }

        for (int i = 0; i < seq1.length; i++) {
            if (seq1[i] != seq2[i]) {
                ++score;
            }
        }
        this.hammingRelative = 1 - ((double) score / (double) seq1.length * 1.0);
    }

    public static void main(String[] args) {
        try {
            String P = "WTHGQACVELSIW";
            String S = "WTHAVSLW";
            ScoringMatrix scoringMatrix = new ScoringMatrix("C:\\Users\\smith\\Downloads\\blosum62.mat");
            int go = -10;
            int ge = -2;
            String mode = "global";
            //double gotohScore = Gotoh.run(P, S, scoringMatrix, go, ge, mode, true);
            GotohV2 alignment = new GotohV2(P, S, scoringMatrix, go, ge, mode);
            //System.out.println("Optimal alignment score:\n" + "> Valid:\t" + alignment.bestScore + "\n" + "> Test*:\t" + gotohScore);
            //System.out.println("Alignments:\n" + "> Valid:\n" + alignment.outputAlignment + "\n" + "> Test*:\n" + "???");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
