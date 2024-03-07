import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import utils.CmdParser;
import utils.Helpers;
import utils.PairsReader;
import utils.SeqLibReader;

public class Runner {

    public static void main(String[] args) {
        CmdParser parser = new CmdParser();
        parser.addOption("go", false);
        parser.addOption("ge", false);
        parser.addOption("m", true);
        parser.addOption("pairs", true);
        parser.addOption("seqlib", true);
        parser.addOption("mode", true);
        parser.addSwitch("nw");
        parser.addOption("format", true);
        //parser.addOption("dpmatrices", true);
        //parser.addSwitch("check");

        try {
            parser.parse(args);
            String goStr = parser.getOptionValue("go");
            String geStr = parser.getOptionValue("ge");
            String mFile = parser.getOptionValue("m");
            String pairsFile = parser.getOptionValue("pairs");
            String seqlibFile = parser.getOptionValue("seqlib");
            String mode = parser.getOptionValue("mode");
            boolean nw = parser.getSwitchValue("nw");
            String format = parser.getOptionValue("format");
            //String dpMatrices = parser.getOptionValue("dpmatrices");
            //boolean check = parser.getSwitchValue("check");

            //TODO: Parse all necessary command line arguments (-m with only 1 hyphen)
            //TODO: modify the output accordingly
            //TODO: print help text if no/wrong arguments are provided
            // necessary command line arguments: --pairs, --seqlib, -m, --go, --ge, --mode, --nw, --format, --dpmatrices, --check
            //already implemented: --pairs, --seqlib, -m, --go, --ge, --mode, --nw, --format,

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
                double score = 0;
                if (nw) {
                    score = NeedlemanWunsch.needlemanWunsch(seq1, seq2, scoringMatrix, ge, mode);
                    BigDecimal bd = new BigDecimal(score);
                    bd = bd.setScale(4, RoundingMode.HALF_UP);
                    score = bd.doubleValue();

                    // Print alignment results
                    if (format.equals("scores")) {
                        System.out.println(pair[0] + " " + pair[1] + " " + String.format("%.4f", score).replace(",", "."));
                    } else if (format.equals("ali")) {
                        System.out.println(">" + pair[0] + " " + pair[1] + " " + String.format("%.3f", score).replace(",", "."));
                        for (int i = 0; i < NeedlemanWunsch.getAlignment().size(); i++) {
                            System.out.println(pair[i] + ": " + NeedlemanWunsch.getAlignment().get(i));
                        }
                    } else if (format.equals("html")) {

                    }
                } else {
                    HashMap<String, Double> matrix = Helpers.read_in_matrix(mFile);
                    String output = "";

                    if (mode == "global"){
                        GlobalGotoh alignment = new GlobalGotoh(seq1, seq2, matrix, go, ge);
                        if (format.equals("scores")) {
                            output += pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                        } else if (format.equals("ali")) {
                            String[] stringsAligned = alignment.alignment.split("\n");
                            output += ">" + pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                            output += pair[0] + ": " + stringsAligned[0] + "\n";
                            output += pair[1] + ": " + stringsAligned[1] + "\n";
                        } else if (format.equals("html")) {

                        }
                    } else if (mode == "local") {
                        LocalGotoh alignment = new LocalGotoh(seq1, seq2, matrix, go, ge);
                        if (format.equals("scores")) {
                            output += pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                        } else if (format.equals("ali")) {
                            String[] stringsAligned = alignment.alignment.split("\n");
                            output += ">" + pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                            output += pair[0] + ": " + stringsAligned[0] + "\n";
                            output += pair[1] + ": " + stringsAligned[1] + "\n";
                        } else if (format.equals("html")) {

                        }
                    } else if (mode == "freeshift") {
                        FreeshiftGotoh alignment = new FreeshiftGotoh(seq1, seq2, matrix, go, ge);
                        if (format.equals("scores")) {
                            output += pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                        } else if (format.equals("ali")) {
                            String[] stringsAligned = alignment.alignment.split("\n");
                            output += ">" + pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                            output += pair[0] + ": " + stringsAligned[0] + "\n";
                            output += pair[1] + ": " + stringsAligned[1] + "\n";
                        } else if (format.equals("html")) {

                        }
                    }

                    // Print alignment results

                    System.out.println(output);

                }

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
