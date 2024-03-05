import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import utils.CmdParserV2;
import utils.PairsReader;
import utils.SeqLibReader;

public class Runner2 {

    public static void main(String[] args) {
        CmdParserV2 parser = new CmdParserV2();
        parser.addOption("go", false);
        parser.addOption("ge", false);
        parser.addOption("m", true);
        parser.addOption("pairs", true);
        parser.addOption("seqlib", true);
        parser.addOption("mode", true);

        try {
            parser.parse(args);
            String goStr = parser.getOptionValue("go");
            String geStr = parser.getOptionValue("ge");
            String mFile = parser.getOptionValue("m");
            String pairsFile = parser.getOptionValue("pairs");
            String seqlibFile = parser.getOptionValue("seqlib");
            String mode = parser.getOptionValue("mode");

            // Convert string arguments to appropriate data types
            int go = goStr != null ? Integer.parseInt(goStr) : -12; // Default gap open penalty
            int ge = geStr != null ? Integer.parseInt(geStr) : -1; // Default gap extend penalty

            // Read scoring matrix file and handle it
            ScoringMatrix scoringMatrix = new ScoringMatrix(mFile);

            // Read sequence library file and store sequences in a HashMap
            SeqLibReader seqLibReader = new SeqLibReader(seqlibFile);
            HashMap<String, String> sequenceLibrary = seqLibReader.readSequenceLibrary(seqlibFile);

            // Read pairs file
            PairsReader pairsReader = new PairsReader(pairsFile);

            // For each pair of sequences, perform alignment and print results
            for (String[] pair : pairsReader.readPairs(pairsFile)) {
                String seq1 = sequenceLibrary.get(pair[0]);
                String seq2 = sequenceLibrary.get(pair[1]);
                // Perform alignment using seq1 and seq2, match score (match), gap penalties (go, ge)
                int score = 0;
                score = NeedlemanWunschV2.needlemanWunsch(seq1, seq2, scoringMatrix, go, ge);
                // Print alignment results
                System.out.println("Alignment score for sequences " + pair[0] + " and " + pair[1] + ":");
                System.out.println(score);
            }
        } catch (IOException e) {
            System.out.println("Error reading input files: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
