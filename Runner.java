import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import utils.CmdParserV2;

public class Runner {

    public static void main(String[] args) {
        CmdParserV2 parser = new CmdParserV2();
        parser.addOption("go", false);
        parser.addOption("ge", false);
        parser.addOption("match", true);
        parser.addOption("mismatch", true);
        parser.addOption("pairs", true);
        parser.addOption("seqlib", true);
        parser.addSwitch("nw");

        try {
            parser.parse(args);
            String goStr = parser.getOptionValue("go");
            String geStr = parser.getOptionValue("ge");
            String matchStr = parser.getOptionValue("match");
            String mismatchStr = parser.getOptionValue("mismatch");
            String pairsFile = parser.getOptionValue("pairs");
            String seqlibFile = parser.getOptionValue("seqlib");
            boolean nw = parser.getSwitchValue("nw");

            // Convert string arguments to appropriate data types
            int go = goStr != null ? Integer.parseInt(goStr) : -12; // Default gap open penalty
            int match = Integer.parseInt(matchStr);
            int mismatch = Integer.parseInt(mismatchStr);
            int ge = geStr != null ? Integer.parseInt(geStr) : -1; // Default gap extend penalty

            // Read sequence library file and store sequences in a HashMap
            HashMap<String, String> sequenceLibrary = readSequenceLibrary(seqlibFile);

            // Read pairs file
            BufferedReader br = new BufferedReader(new FileReader(pairsFile));
            String line;
            while ((line = br.readLine()) != null) {
                String[] pair = line.split("\\s+"); // Split by whitespace
                if (pair.length >= 2) {
                    String seq1 = sequenceLibrary.get(pair[0]);
                    String seq2 = sequenceLibrary.get(pair[1]);
                    // Perform alignment using seq1 and seq2, match score (match), gap penalties (go, ge)
                    int score = 0;
                    if (nw) {
                        score = NeedlemanWunsch.needlemanWunsch(seq1, seq2, match, mismatch, go);
                        System.out.println("Used NeedlemanWunsch Dynamic Programming");
                    } else {
                        score = NeedlemanWunschRecursive.needlemanWunschRecursiveHelper(seq1, seq2, seq1.length(), seq2.length(), match, mismatch, go);
                        System.out.println("Used NeedlemanWunsch Recursive");
                    }
                    // Print alignment results
                    System.out.println("Alignment score for sequences " + pair[0] + " and " + pair[1] + ":");
                    System.out.println(score);
                    // Example: System.out.println(alignmentResult);
                }
            }
            br.close();
        } catch (IOException e) {
            System.out.println("Error reading input files: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to read sequence library file and store sequences in a HashMap
    private static HashMap<String, String> readSequenceLibrary(String seqlibFile) throws IOException {
        HashMap<String, String> sequenceLibrary = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(seqlibFile));
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(":");
            if (parts.length >= 2) {
                sequenceLibrary.put(parts[0], parts[1]);
            }
        }
        br.close();
        return sequenceLibrary;
    }
}
