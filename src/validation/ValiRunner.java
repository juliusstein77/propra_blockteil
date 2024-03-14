package validation;
import utils.CmdParser;
import utils.ValiFileReader;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ValiRunner {

    public static void printResults(String result, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write(result);
        writer.close();
    }

    public static String result;

    public static void main(String[] args) {
        CmdParser parserAli = new CmdParser();
        parserAli.addOption("f", true);
        parserAli.addOption("download", false);

        try {
            parserAli.parse(args);
            String filePath = parserAli.getOptionValue("f");
            String dowloadPath = parserAli.getOptionValue("download");
            ValiFileReader valiFileReader = new ValiFileReader(filePath);
            HashMap<String, ArrayList<String>> valiFile = valiFileReader.readValiFile(filePath);
            for (String header : valiFile.keySet()) {
                String[] predictedAlignment = {valiFile.get(header).get(0).split(":")[1].trim(), valiFile.get(header).get(1).split(":")[1].trim()};
                String[] referenceAlignment = {valiFile.get(header).get(2).split(":")[1].trim(), valiFile.get(header).get(3).split(":")[1].trim()};

                ValiAlignment.initializeAlignment(predictedAlignment, referenceAlignment);
                double specificity = ValiAlignment.calculateSpecificity();
                double sensitivity = ValiAlignment.calculateSensitivity();
                double meanShiftError = ValiAlignment.calculateMeanShiftError();
                double inverseMeanShiftError = ValiAlignment.calculateInverseMeanShiftError();
                double coverage = ValiAlignment.calculateCoverage();
                double seqIdentity = ValiAlignment.calculateSequenceIdentity(referenceAlignment);
                int seqLength = referenceAlignment[0].replace("-", "").length();

                String s1 = header + " " + String.format("%.4f", sensitivity).replace(",", ".") + " " + String.format("%.4f", specificity).replace(",", ".")
                        + " " + String.format("%.4f", coverage).replace(",", ".") + " " + String.format("%.4f", meanShiftError).replace(",", ".")
                        + " " + String.format("%.4f", inverseMeanShiftError).replace(",", ".") + " " + String.format("%.4f", seqIdentity).replace(",", ".") + " " + seqLength;
                System.out.println(s1);
                if (dowloadPath != null) {
                    result = result + s1 + "\n";
                }
                for (String sequence : valiFile.get(header)) {
                    System.out.println(sequence);
                    if (dowloadPath != null) {
                        result = result + sequence + "\n";
                    }
                }

            }
            if (dowloadPath != null) {
                printResults(result, dowloadPath + "/validation_results.txt");
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
