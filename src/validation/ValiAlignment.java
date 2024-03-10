package validation;

import java.util.ArrayList;
import java.util.HashMap;

public class ValiAlignment {

    // alignment pairs for predicted and reference alignments
    static ArrayList<AlignmentPairs> predAlignmentPairs;
    static ArrayList<AlignmentPairs> refAlignmentPairs;

    // Initialize the alignment pairs from the predicted and reference alignment sequences (String[])
    public static void initializeAlignment (String[] predictedAlignment, String[] referenceAlignment) {

        // Initialize the alignment pairs
        predAlignmentPairs = new ArrayList<AlignmentPairs>();
        refAlignmentPairs = new ArrayList<AlignmentPairs>();

        String predSeq1 = predictedAlignment[0];
        String predSeq2 = predictedAlignment[1];
        String refSeq1 = referenceAlignment[0];
        String refSeq2 = referenceAlignment[1];

        // Initialize the occurrences of each character in the predicted sequences
        HashMap<Character, Integer> occPred1 = new HashMap<Character, Integer>();
        HashMap<Character, Integer> occPred2 = new HashMap<Character, Integer>();
        for (int i = 0; i < predSeq1.length(); i++) {
            // Count the occurrences of each character in the sequence
            occPred1.put(predSeq1.charAt(i), occPred1.getOrDefault(predSeq1.charAt(i), 0) + 1);
            occPred2.put(predSeq2.charAt(i), occPred2.getOrDefault(predSeq2.charAt(i), 0) + 1);

            if (predSeq1.charAt(i) != '-' && predSeq2.charAt(i) != '-') {
                // calculate the actual position of the character in the sequence by subtracting the number of gaps
                int actualPositionPredSeq1 = Math.max(i-occPred1.getOrDefault('-',0),0);
                int actualPositionPredSeq2 = Math.max(i-occPred2.getOrDefault('-',0),0);
                // Add the alignment pair to the list
                predAlignmentPairs.add(
                        new AlignmentPairs(
                                predSeq1.charAt(i),
                                predSeq2.charAt(i),
                                i,
                                actualPositionPredSeq1,
                                actualPositionPredSeq2,
                                occPred1.get(predSeq1.charAt(i)),
                                occPred2.get(predSeq2.charAt(i))
                        ));
            }
        }

        // Initialize the occurrences of each character in the reference sequences
        HashMap<Character, Integer> occRef1 = new HashMap<Character, Integer>();
        HashMap<Character, Integer> occRef2 = new HashMap<Character, Integer>();
        for (int i = 0; i < refSeq1.length(); i++) {
            // Count the occurrences of each character in the sequence
            occRef1.put(refSeq1.charAt(i), occRef1.getOrDefault(refSeq1.charAt(i), 0) + 1);
            occRef2.put(refSeq2.charAt(i), occRef2.getOrDefault(refSeq2.charAt(i), 0) + 1);

            if (refSeq1.charAt(i) != '-' && refSeq2.charAt(i) != '-') {
                // calculate the actual position of the character in the sequence by subtracting the number of gaps
                int actualPositionRefSeq1 = Math.max(i-occRef1.getOrDefault('-',0),0);
                int actualPositionRefSeq2 = Math.max(i-occRef2.getOrDefault('-',0),0);
                // Add the alignment pair to the list
                refAlignmentPairs.add(
                        new AlignmentPairs(
                                refSeq1.charAt(i),
                                refSeq2.charAt(i),
                                i,
                                actualPositionRefSeq1,
                                actualPositionRefSeq2,
                                occRef1.get(refSeq1.charAt(i)),
                                occRef2.get(refSeq2.charAt(i))
                        ));
            }
        }
    }

    public static double calculateMeanShiftError() {
        ArrayList<Integer> shifts = new ArrayList<>();
        for (AlignmentPairs refali : refAlignmentPairs){
            int refTemplateIndex = refali.actualIndexSeq1;
            int refTargetIndex = refali.actualIndexSeq2;
            for (AlignmentPairs predali : predAlignmentPairs) {
                if (predali.actualIndexSeq2 == refTargetIndex) {
                    int shift = predali.actualIndexSeq1 - refTemplateIndex;
                    shifts.add(Math.abs(shift));
                }
            }
        }
        int shiftSum = 0;
        for (int shift : shifts) {
            shiftSum += shift;
        }
        return (double) shiftSum / shifts.size();
    }

    public static double calculateInverseMeanShiftError() {
        ArrayList<Integer> inverseShifts = new ArrayList<>();
        for (AlignmentPairs refali : refAlignmentPairs){
            int index1 = refali.actualIndexSeq1;
            int index2 = refali.actualIndexSeq2;
            for (AlignmentPairs predali : predAlignmentPairs) {
                if (predali.actualIndexSeq1 == index1) {
                    int shift = predali.actualIndexSeq2 - index2;
                    inverseShifts.add(Math.abs(shift));
                }
            }
        }
        int invShiftSum = 0;
        for (int shift : inverseShifts) {
            invShiftSum += shift;
        }
        return (double) invShiftSum / inverseShifts.size();
    }

    public static double calculateSequenceIdentity(String[] referenceAlignment) {
        String refSeq1 = referenceAlignment[0];
        String refSeq2 = referenceAlignment[1];
        int identicalResidues = 0;
        for (int i = 0; i < refSeq1.length(); i++) {
            if( refSeq1.charAt(i) == refSeq2.charAt(i) && refSeq1.charAt(i) != '-' && refSeq2.charAt(i) != '-') {
                identicalResidues++;
            }
        }
        double seqIdentity = ((double) identicalResidues / (double) (refSeq1.length()));
        return seqIdentity;
    }

    public static double calculateCoverage() {
        int coverageCount = 0;
        for (int i = 0; i < predAlignmentPairs.size(); i++) {
            for (int j = 0; j < refAlignmentPairs.size(); j++) {
                if (isTargetAligned(predAlignmentPairs.get(i), refAlignmentPairs.get(j))) {
                    coverageCount++;
                    break;
                }
            }
        }
        double cov = (double) coverageCount / (double) predAlignmentPairs.size();
        return cov;
    }

    public static double calculateSensitivity() {
        int correctAligned = 0;
        for (int i = 0; i < predAlignmentPairs.size(); i++) {
            for (int j = 0; j < refAlignmentPairs.size(); j++) {
                if (isIdentical(predAlignmentPairs.get(i), refAlignmentPairs.get(j))) {
                    correctAligned++;
                    break;
                }
            }
        }
        double sensitivity = (double) correctAligned / (double) refAlignmentPairs.size();
        return sensitivity;
    }

    public static double calculateSpecificity() {
        int correctAligned = 0;
        for (int i = 0; i < predAlignmentPairs.size(); i++) {
            for (int j = 0; j < refAlignmentPairs.size(); j++) {
                if (isIdentical(predAlignmentPairs.get(i), refAlignmentPairs.get(j))) {
                    correctAligned++;
                    break;
                }
            }
        }
        double specificity = (double) correctAligned / (double) predAlignmentPairs.size();
        return specificity;
    }


    public static boolean isTargetAligned(AlignmentPairs a, AlignmentPairs b) {
        return a.charSeq2 == b.charSeq2 && a.countSeq2 == b.countSeq2;
    }

    public static boolean isIdentical(AlignmentPairs a, AlignmentPairs b) {
        return a.charSeq1 == b.charSeq1 && a.charSeq2 == b.charSeq2 && a.countSeq1 == b.countSeq1 && a.countSeq2 == b.countSeq2;
    }
}
