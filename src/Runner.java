import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import utils.CmdParser;
import utils.Helpers;
import utils.PairsReader;
import utils.SeqLibReader;

public class Runner {

    public static String resultFile;

    public static ArrayList<double[][]> dpMatrices = new ArrayList<>();
    public static ArrayList<String> matrixNames = new ArrayList<>();

    public static void printMatrixToFile(ArrayList<double[][]> matrices, ArrayList<String> names, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        for (int k = 0; k < matrices.size(); k++) {
            writer.write(">" + names.get(k) + "\n");
            double[][] matrix = matrices.get(k);
            for (int i = 0; i < matrix.length; i++) {
                for (int j = 0; j < matrix[0].length; j++) {
                    writer.write(matrix[i][j] + "\t");
                }
                writer.write("\n");
            }
        }
        writer.close();
    }

    public static void printResults(String result, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(result);
        writer.close();
    }

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
        parser.addOption("dpmatrices", false);
        parser.addSwitch("check");
        parser.addSwitch("val");
        parser.addOption("download", false);

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
            String dpMatricesPath = parser.getOptionValue("dpmatrices");
            boolean check = parser.getSwitchValue("check");
            boolean val = parser.getSwitchValue("val");
            String downloadPath = parser.getOptionValue("download");

            //TODO: Parse all necessary command line arguments (-m with only 1 hyphen)
            //TODO: modify the output accordingly
            // necessary command line arguments: --pairs, --seqlib, -m, --go, --ge, --mode, --nw, --format, --dpmatrices, --check
            //already implemented: --pairs, --seqlib, -m, --go, --ge, --mode, --nw, --format, --dpmatrices

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
                String seq1 = sequenceLibrary.get(pair[0]).replace("-", "");
                String seq2 = sequenceLibrary.get(pair[1]).replace("-", "");
                // Perform alignment using seq1 and seq2, match score (match), gap penalties (go, ge)
                double score = 0;
                // Use Needleman-Wunsch algorithm
                if (nw) {
                    score = NeedlemanWunsch.needlemanWunsch(seq1, seq2, scoringMatrix, ge, mode);
                    BigDecimal bd = new BigDecimal(score);
                    bd = bd.setScale(4, RoundingMode.HALF_UP);
                    score = bd.doubleValue();

                    // Print alignment results
                    if (format.equals("scores")) {
                        System.out.println(pair[0] + " " + pair[1] + " " + String.format("%.4f", score).replace(",", "."));
                    } else if (format.equals("ali")) {
                        String alignment1 = NeedlemanWunsch.getAlignment().get(0);
                        String alignment2 = NeedlemanWunsch.getAlignment().get(1);
                        int matchCount = 0;
                        int mismatchCount = 0;
                        int totalAligned = 0;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < alignment1.length(); i++) {
                            char char1 = alignment1.charAt(i);
                            char char2 = alignment2.charAt(i);

                            if (char1 == char2) {
                                sb.append("|");
                                matchCount++;
                                totalAligned++;
                            } else if (char1 == '-' || char2 == '-') {
                                sb.append("."); // Gap
                            } else {
                                sb.append(":"); // Mismatch
                                mismatchCount++;
                                totalAligned++;
                            }
                        }
                        double percentMatch = (double) matchCount / alignment1.length() * 100;
                        double percentMismatch = (double) mismatchCount / alignment1.length() * 100;
                        int whitespaceCount = pair[1].length();
                        for (int i = 0; i < whitespaceCount; i++) {
                            sb.insert(0, "-");
                        }
                        sb.insert(0, "-");
                        sb.insert(0, "-");
                        String s1 = ">" + pair[0] + " " + pair[1] + " " + String.format("%.3f", score).replace(",", ".") + " " + String.format("%.2f", percentMatch).replace(",", ".") + "% matches" + " " + String.format("%.2f", percentMismatch).replace(",", ".") + "% mismatches" + " " + totalAligned + " " + "total aligned" + "\n";
                        String s2 = pair[0] + ": " + alignment1 + "\n";
                        String s3 = sb.toString() + "\n";
                        String s4 = pair[1] + ": " + alignment2;
                        String result = s1 + s2 + s3 + s4;
                        System.out.println(result);
                        if (downloadPath != null) {
                            resultFile = resultFile + result + "\n";
                        }

                        if (val) {
                            //TODO: remove gap overhangs
                            String refSeq1 = sequenceLibrary.get(pair[0]);
                            String refSeq2 = sequenceLibrary.get(pair[1]);
                            if (refSeq1.length() != refSeq2.length()) {
                                throw new IllegalArgumentException("Sequences are not of the same length");
                            }
                            for (int i = 0; i < refSeq2.length(); i++) {
                                if (refSeq1.charAt(i) == '-' && refSeq2.charAt(i) == '-') {
                                    refSeq1 = refSeq1.substring(0, i) + refSeq1.substring(i + 1);
                                    refSeq2 = refSeq2.substring(0, i) + refSeq2.substring(i + 1);
                                    i++;
                                }
                            }
                            System.out.println(pair[0] + ": " + refSeq1);
                            System.out.println(pair[1] + ": " + refSeq2);
                        }
                    } else if (format.equals("html")) {
                        //TODO: Implement HTML output
                    }

                    if (dpMatricesPath != null) {
                        dpMatrices.add(NeedlemanWunsch.dp);
                        matrixNames.add(pair[0] + "_" + pair[1]);
                    }

                // Use Gotoh algorithm
                } else {
                    HashMap<String, Double> matrix = Helpers.read_in_matrix(mFile);
                    String output = "";
                    Gotoh alignment = new Gotoh(seq1, seq2, matrix, go, ge);

                    if (mode.equals("global")){
                        alignment = new GlobalGotoh(seq1, seq2, matrix, go, ge);
                    } else if (mode.equals("local")) {
                        alignment = new LocalGotoh(seq1, seq2, matrix, go, ge);
                    } else if (mode.equals("freeshift")) {
                        alignment = new FreeshiftGotoh(seq1, seq2, matrix, go, ge);
                    }

                    if (format.equals("scores")) {
                        output += pair[0] + " " + pair[1] + " " + (String.format("%.4f", alignment.score)).replace(",", ".") + "\n";
                    } else if (format.equals("ali")) {
                        String[] stringsAligned = alignment.alignment.split("\n");
                        String alignment1 = stringsAligned[0];
                        String alignment2 = stringsAligned[1];
                        int matchCount = 0;
                        int mismatchCount = 0;
                        int totalAligned = 0;
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < alignment1.length(); i++) {
                            char char1 = alignment1.charAt(i);
                            char char2 = alignment2.charAt(i);

                            if (char1 == char2) {
                                sb.append("|");
                                matchCount++;
                                totalAligned++;
                            } else if (char1 == '-' || char2 == '-') {
                                sb.append("."); // Gap
                            } else {
                                sb.append(":"); // Mismatch
                                mismatchCount++;
                                totalAligned++;
                            }
                        }
                        double percentMatch = (double) matchCount / alignment1.length() * 100;
                        double percentMismatch = (double) mismatchCount / alignment1.length() * 100;
                        int whitespaceCount = pair[1].length();

                        sb.insert(0, "-");
                        sb.insert(0, "-");
                        output += ">" + pair[0] + " " + pair[1] + " " + String.format("%.3f", score).replace(",", ".") + " " + String.format("%.2f", percentMatch).replace(",", ".") + "% matches" + " " + String.format("%.2f", percentMismatch).replace(",", ".") + "% mismatches" + " " + totalAligned + " " + "total aligned" + "\n";
                        output += pair[0] + ": " + stringsAligned[0] + "\n";
                        output += sb.toString() + "\n";
                        output += pair[1] + ": " + stringsAligned[1] + "\n";

                        if (val) {
                            String refSeq1 = sequenceLibrary.get(pair[0]);
                            String refSeq2 = sequenceLibrary.get(pair[1]);
                            if (refSeq1.length() != refSeq2.length()) {
                                throw new IllegalArgumentException("Sequences are not of the same length");
                            }
                            for (int i = 0; i < refSeq2.length(); i++) {
                                if (refSeq1.charAt(i) == '-' && refSeq2.charAt(i) == '-') {
                                    refSeq1 = refSeq1.substring(0, i) + refSeq1.substring(i + 1);
                                    refSeq2 = refSeq2.substring(0, i) + refSeq2.substring(i + 1);
                                    i++;
                                }
                            }
                            output += pair[0] + ": " + refSeq1 + "\n";
                            output += pair[1] + ": " + refSeq2;
                        }

                    } else if (format.equals("html")) {
                        //TODO: Implement HTML output
                    }

                    if (dpMatricesPath != null) {
                        matrixNames.add(pair[0] + "_" + pair[1] + "_matrix_a");
                        dpMatrices.add(alignment.mx_a);
                        matrixNames.add(pair[0] + "_" + pair[1] + "_matrix_i");
                        dpMatrices.add(alignment.mx_i);
                        matrixNames.add(pair[0] + "_" + pair[1] + "_matrix_d");
                        dpMatrices.add(alignment.mx_d);
                    }

                    // Print alignment results
                    System.out.println(output);
                    if (downloadPath != null) {
                        resultFile = resultFile + output + "\n";
                    }
                }

            }
            if (downloadPath != null) {
                printResults(resultFile, downloadPath + "/alignment_results.txt");
            }
            if (dpMatricesPath != null) {
                printMatrixToFile(dpMatrices, matrixNames, dpMatricesPath + "/dpmatrices_result.txt");
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
