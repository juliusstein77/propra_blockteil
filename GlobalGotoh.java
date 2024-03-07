import java.util.ArrayList;
import java.util.HashMap;

public class GlobalGotoh extends Gotoh_V2 {
    public void initialize() {
        for (int i = 1; i < s.length() + 1; i++) {
            mx_d[i][0] = Double.NEGATIVE_INFINITY;
        }
        for (int j = 1; j < t.length() + 1; j++) {
            mx_i[0][j] = Double.NEGATIVE_INFINITY;
        }

        mx_a[0][0] = 0;
        for (int j = 1; j < t.length() + 1; j++) {
            mx_a[0][j] = gap_open_penalty + j * gap_extend_penalty;
        }
        for (int i = 1; i < s.length() + 1; i++) {
            mx_a[i][0] = gap_open_penalty + i * gap_extend_penalty;
        }

        for (int i = 1; i < s.length() + 1; i++) {
            for (int j = 1; j < t.length() + 1; j++) {
                mx_i[i][j] = Math.max(mx_a[i - 1][j] + gap_open_penalty + gap_extend_penalty, mx_i[i - 1][j] + gap_extend_penalty);
                mx_d[i][j] = Math.max(mx_a[i][j - 1] + gap_open_penalty + gap_extend_penalty, mx_d[i][j - 1] + gap_extend_penalty);
                mx_a[i][j] = Math.max(mx_a[i - 1][j - 1] + get_score(s.charAt(i - 1), t.charAt(j - 1)), Math.max(mx_d[i][j], mx_i[i][j]));
            }
        }
        this.score = mx_a[s.length()][t.length()];
    }

    public void backtrack() {
        ArrayList<int[]> local_bts = new ArrayList<int[]>();
        int i = s.length();
        int j = t.length();
        // start from the left bottom corner and go to the top right corner
        while (i > 0 || j > 0) {
            // --- diagonal --- >>> match/mismatch
            if (i > 0 && j > 0 && Math.abs(mx_a[i][j] - (get_score(s.charAt(i - 1), t.charAt(j - 1)) + mx_a[i - 1][j - 1])) < 0.0001) { // decimal instead of == is used to compare floating point numbers
                local_bts.add(new int[]{i, j});
                --i;
                --j;
            }
            // --- up --- >>> gap in t
            else if (mx_a[i][j] == mx_i[i][j])  {
                local_bts.add(new int[]{i, j});
                int counter = 1;
                while (counter < i) {
                    if (Math.abs((mx_a[i - counter][j] + gap_open_penalty + gap_extend_penalty * counter) - mx_a[i][j]) < 0.0001) {
                        break;
                    }
                    ++counter;
                }
                i -= counter;
            }
            // -- left --- >>> gap in s
            else if (Math.abs(mx_a[i][j] - mx_d[i][j]) < 0.0001) {
                local_bts.add(new int[]{i, j});
                int counter = 1;
                while (counter < j) {
                    if (Math.abs((mx_a[i][j - counter] + gap_open_penalty + gap_extend_penalty * counter) - mx_a[i][j]) < 0.0001) {
                        break;
                    }
                    ++counter;
                }
                j -= counter;
            }
            // --- reached the end of the matrix --
            else if (i == 0 || j == 0) {
                local_bts.add(new int[]{i, j});
                local_bts.add(new int[]{0, 0});
                break;
            }
        }
        // add the first position
        if (!(local_bts.get(local_bts.size() - 1)[0] == 0 && local_bts.get(local_bts.size() - 1)[1] == 0)) {
            local_bts.add(new int[]{0, 0});
        }
        this.bts = local_bts;
    }

    public GlobalGotoh(String s, String t, HashMap<String, Double> scoring_matrix, int gap_open_penalty, int gap_extend_penalty) {
        super(s, t, scoring_matrix, gap_open_penalty, gap_extend_penalty);
        initialize();
        backtrack();
        calculate_alignment();
        this.score = mx_a[s.length()][t.length()];
    }
}
