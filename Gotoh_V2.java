import java.util.ArrayList;
import java.util.HashMap;

public class Gotoh_V2 {
    public double score;
    public final String s;
    public final String t;
    public final int gap_open_penalty;
    public final int gap_extend_penalty;
    public final HashMap<String, Double> scoring_matrix;
    public double[][] mx_a;
    public double[][] mx_d;
    public double[][] mx_i;
    public ArrayList<int[]> bts;
    public String alignment;

    public Gotoh_V2(String s, String t, HashMap<String, Double> scoring_matrix, int gap_open_penalty, int gap_extend_penalty) {
        this.s = s;
        this.t = t;
        this.scoring_matrix = scoring_matrix;
        this.gap_open_penalty = gap_open_penalty;
        this.gap_extend_penalty = gap_extend_penalty;
        this.mx_a = new double[s.length() + 1][t.length() + 1];
        this.mx_d = new double[s.length() + 1][t.length() + 1];
        this.mx_i = new double[s.length() + 1][t.length() + 1];
        this.bts = new ArrayList<int[]>();
        this.alignment = "";
    }

    public void calculate_alignment() {
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        for (int l = 0; l < bts.size() - 1; l++) {
            int i = bts.get(l)[0];
            int j = bts.get(l)[1];
            int x = bts.get(l + 1)[0];
            int y = bts.get(l + 1)[1];
            // --- diagonal --- >>> match/mismatch
            if (not_gap(i, j, x, y)) {
                a.insert(0, s.charAt(i - 1));
                b.insert(0, t.charAt(j - 1));
            }
            // --- left --- >>> gap in s
            else if (gap_in_s(i, j, x, y)) {
                HashMap<StringBuilder, StringBuilder> gap = add_gap(i, j, x, y, a, b, s, "-");
                a = gap.keySet().iterator().next();
                b = gap.values().iterator().next();
            }
            // --- up --- >>> gap in t
            else if (gap_in_t(i, j, x, y)) {
                HashMap<StringBuilder, StringBuilder> gap = add_gap(j, i, y, x, b, a, t, "-");
                b = gap.keySet().iterator().next();
                a = gap.values().iterator().next();
            }

        }
        this.alignment = a.toString() + "\n" + b.toString();
    }

    private boolean not_gap(int i, int j, int x, int y) {
        return i == x + 1 && j == y + 1;
    }

    private boolean gap_in_t(int i, int j, int x, int y) {
        return i == x && j > y;
    }

    private boolean gap_in_s(int i, int j, int x, int y) {
        return i > x && j == y;
    }

    private HashMap<StringBuilder, StringBuilder> add_gap(int q, int w, int e, int r, StringBuilder a, StringBuilder b, String seq, String placeholder) {
        int gap = q - e;
        int count = 1;
        while (gap > 0) {
            a.insert(0, seq.charAt(q - count));
            b.insert(0, placeholder);
            --gap;
            ++count;
        }
        return new HashMap<StringBuilder, StringBuilder>() {{
            put(a, b);
        }};
    }

    public String calculate_final_alignment(int index_A, int index_a, int index_B, int index_b) {
        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();

        // --- if the alignment is not empty, return the sequences with the alignment ---
        if (!alignment.equals("\n")) {
            String[] aligned_sequences = alignment.split("\n");
            a.append(aligned_sequences[0]);
            b.append(aligned_sequences[1]);
            a.insert(0, s.substring(0, index_a));
            for (int i = 0; i < index_a; i++) {
                b.insert(0, "-");
            }
            b.insert(0, t.substring(0, index_b));
            for (int j = 0; j < index_b; j++) {
                a.insert(0, "-");
            }
            a.append(s.substring(index_A, s.length()));
            b.append("-".repeat(Math.max(0, s.length() - index_A)));
            b.append(t.substring(index_B, t.length()));
            a.append("-".repeat(Math.max(0, t.length() - index_B)));
        }
        // --- if the alignment is empty, return the sequences followed by gaps placeholders with the length of the sequences ---
        else {
            a.append(s);
            a.append("-".repeat(s.length()));
            b.append(t);
            b.append("-".repeat(t.length()));
        }
        return a.toString() + "\n" + b.toString();
    }

    public double get_score(char a, char b) {
        String ab = Character.toString(a) + Character.toString(b);
        if (scoring_matrix.containsKey(ab)) {
            return scoring_matrix.get(ab);
        } else return scoring_matrix.get(Character.toString(b) + Character.toString(a));
    }

    public double checkScore() {
        String[] sepStrings = alignment.split("\n");
        String stringA = sepStrings[0];
        String stringB = sepStrings[1];
        double score = 0;
        for (int i = 0; i < stringA.length(); i++) {
            if (stringA.charAt(i) == '-') {
                if (i == 0 || stringA.charAt(i - 1) != '-') {
                    score += gap_open_penalty + gap_extend_penalty;
                } else {
                    score += gap_extend_penalty;
                }
            } else if (stringB.charAt(i) == '-') {
                if (i == 0 || stringB.charAt(i - 1) != '-') {
                    score += gap_open_penalty + gap_extend_penalty;
                } else {
                    score += gap_extend_penalty;
                }
            } else {
                double value = this.get_score(stringA.charAt(i), stringB.charAt(i));
                score += value;
                score = Math.round(score * 10) / 10.0;
            }
        }
        return score;
    }

    public int[] get_max(double[][] mx) {
        double bin = Double.NEGATIVE_INFINITY;
        int r_max = -1;
        int c_max = -1;

        for (int r = 0; r < mx.length; r++) {
            for (int c = 0; c < mx[r].length; c++) {
                if (mx[r][c] > bin) {
                    bin = mx[r][c];
                    r_max = r;
                    c_max = c;
                }
            }
        }

        return new int[]{r_max, c_max};
    }

    public int[] get_max_last(double[][] mx) {
        double r_bin = Double.NEGATIVE_INFINITY;
        double c_bin = Double.NEGATIVE_INFINITY;
        int[] index_r = new int[2];
        int[] index_c = new int[2];
        for (int i = 0; i < mx[0].length; i++) {
            if (mx[mx.length - 1][i] >= r_bin) {
                r_bin = mx[mx.length - 1][i];
                index_r[0] = mx.length - 1;
                index_r[1] = i;
            }
        }
        for (int j = 0; j < mx.length; j++) {
            if (mx[j][mx[0].length - 1] >= c_bin) {
                c_bin = mx[j][mx[0].length - 1];
                index_c[0] = j;
                index_c[1] = mx[0].length - 1;
            }
        }

        if (mx[index_r[0]][index_r[1]] > mx[index_c[0]][index_c[1]]) {
            return index_r;
        } else return index_c;
    }


}
