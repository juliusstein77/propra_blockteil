package validation;
import utils.CmdParser;
import utils.ValiFileReader;

import java.util.ArrayList;
import java.util.HashMap;

public class ValiRunner {
    public static void main(String[] args) {
        CmdParser parserAli = new CmdParser();
        parserAli.addOption("f", true);

        try {
            parserAli.parse(args);
            String filePath = parserAli.getOptionValue("f");
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

                System.out.println(header + " " + String.format("%.4f", sensitivity).replace(",", ".") + " " + String.format("%.4f", specificity).replace(",", ".")
                        + " " + String.format("%.4f", coverage).replace(",", ".") + " " + String.format("%.4f", meanShiftError).replace(",", ".")
                        + " " + String.format("%.4f", inverseMeanShiftError).replace(",", ".") + " " + String.format("%.4f", seqIdentity).replace(",", "."));
                for (String sequence : valiFile.get(header)) {
                    System.out.println(sequence);
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
