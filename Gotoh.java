import java.util.HashMap;

public class Gotoh {
    private String seq1;
    private String seq2;
    private HashMap<String,Double> matrix;
    private int gap_open;
    private int gap_extend;
    public Gotoh(String seq1, String seq2, HashMap<String,Double> matrix, int gap_open, int gap_extend) {
        this.seq1 = seq1;
        this.seq2 = seq2;
        this.matrix = matrix;
        this.gap_open = gap_open;
        this.gap_extend = gap_extend;
    }

}
