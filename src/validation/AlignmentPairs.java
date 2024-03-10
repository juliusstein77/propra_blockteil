package validation;

// Class to store alignment pairs
// helpful for maintaining the actual index of the sequence as well as the global index
public class AlignmentPairs {
    // Seq 1 -> template
    // Seq 2 -> target
    public char charSeq1;
    public char charSeq2;

    // global index of the sequence
    int totalIndex;

    // actual index of the sequence
    int actualIndexSeq1;
    int actualIndexSeq2;

    // total count of all characters in the sequence
    int countSeq1;
    int countSeq2;



    public AlignmentPairs(char charSeq1, char charSeq2, int totalIndex, int actualIndexSeq1, int actualIndexSeq2, int countSeq1, int countSeq2) {
        this.charSeq1 = charSeq1;
        this.charSeq2 = charSeq2;
        this.totalIndex = totalIndex;
        this.actualIndexSeq1 = actualIndexSeq1;
        this.actualIndexSeq2 = actualIndexSeq2;
        this.countSeq1 = countSeq1;
        this.countSeq2 = countSeq2;
    }


}
