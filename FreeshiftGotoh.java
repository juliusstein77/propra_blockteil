import java.util.ArrayList;
import java.util.HashMap;

public class FreeshiftGotoh extends Gotoh_V2 {

    public void initialize() {
        for (int i = 1; i < s.length() + 1; i++) {
            mx_d[i][0] = Double.NEGATIVE_INFINITY;
        }
        //Initializing state mx_i
        for (int j = 1; j < t.length() + 1; j++) {
            mx_i[0][j] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 1; i < s.length() + 1; i++) {
            for (int j = 1; j < t.length() + 1; j++) {
                mx_i[i][j] = Math.max(mx_a[i - 1][j] + gap_open_penalty + gap_extend_penalty, mx_i[i - 1][j] + gap_extend_penalty);
                mx_d[i][j] = Math.max(mx_a[i][j - 1] + gap_open_penalty + gap_extend_penalty, mx_d[i][j - 1] + gap_extend_penalty);
                mx_a[i][j] = Math.max(mx_a[i - 1][j - 1] + get_score(s.charAt(i - 1), t.charAt(j - 1)), Math.max(mx_d[i][j], mx_i[i][j]));
            }
        }
        int[] max = get_max_last(mx_a);
        this.score = mx_a[max[0]][max[1]];
    }

    public void backtrack() {
        ArrayList<int[]> local_bts = new ArrayList<int[]>();
        int[] max = get_max_last(mx_a);
        int i = max[0];
        int j = max[1];
        // -- start from the maximum value in the matrix and go to the border of the matrix
        while (i > 0 && j > 0) {
            // --- diagonal --- >>> match/mismatch
            if (mx_a[i][j] == (get_score(s.charAt(i - 1), t.charAt(j - 1)) + mx_a[i - 1][j - 1])) {
                local_bts.add(new int[]{i, j});
                --i;
                --j;
            }
            // --- up --- >>> gap in t
            else if (mx_a[i][j] == mx_i[i][j]) {
                local_bts.add(new int[]{i, j});
                int counter = 1;
                while (counter < i) {
                    if ((mx_a[i - counter][j] + gap_open_penalty + gap_extend_penalty * counter) == mx_a[i][j]) {
                        break;
                    }
                    ++counter;
                }
                i -= counter; // continue after the gap
            }
            // -- left --- >>> gap in s
            else if (mx_a[i][j] == mx_d[i][j]) {
                local_bts.add(new int[]{i, j});
                int counter = 1;
                while (counter < j) {
                    if ((mx_a[i][j - counter] + gap_open_penalty + gap_extend_penalty * counter) == mx_a[i][j]) {
                        break;
                    }
                    ++counter;
                }
                j -= counter; // continue after the gap
            }
        }
        // >>> add the first position
        local_bts.add(new int[]{i, j});
        this.bts = local_bts;
    }


    public FreeshiftGotoh(String s, String t, HashMap<String, Double> scoring_matrix, int gap_open_penalty, int gap_extend_penalty) {
        super(s, t, scoring_matrix, gap_open_penalty, gap_extend_penalty);
        initialize();
        backtrack();
        calculate_alignment();
        this.score = mx_a[bts.get(0)[0]][bts.get(0)[1]];
        // >>> pull the sole alignment into the whole sequence
        this.alignment = calculate_final_alignment(bts.get(0)[0], bts.get(bts.size() - 1)[0], bts.get(0)[1], bts.get(bts.size() - 1)[1]);
    }
}