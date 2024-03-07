import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import utils.CmdParserV2;
import utils.PairsReader;
import utils.SeqLibReader;

public class GotohRunner {

    public static void main(String[] args) {
        CmdParserV2 parser = new CmdParserV2();
        parser.addOption("go", true);
        parser.addOption("ge", true);
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

            //TODO: Parse all necessary command line arguments
            //TODO: modify the output accordingly
            //TODO: print help text if no/wrong arguments are provided

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
            String output = "";

            // For each pair of sequences, perform alignment and print results
            for (String[] pair : pairsReader.readPairs(pairsFile)) {
                String seq1 = sequenceLibrary.get(pair[0]);
                String seq2 = sequenceLibrary.get(pair[1]);
                // Perform alignment using seq1 and seq2, match score (match), gap penalties (go, ge)
                int score = 0;
                HashMap<Double, String> res = Gotoh.run(seq1, seq2, scoringMatrix, go, ge, mode, false);
                score = res.keySet().iterator().next().intValue();
                String alignment = res.values().iterator().next();
                String[] stringsAligned = alignment.split("\n");
                output += ">" + pair[0] + " " + pair[1] + " " + (String.format("%.4f", score)).replace(",", ".") + "\n";
                output += pair[0] + ": " + stringsAligned[0] + "\n";
                output += pair[1] + ": " + stringsAligned[1] + "\n";
                // Print alignment results
                System.out.println(output);




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
