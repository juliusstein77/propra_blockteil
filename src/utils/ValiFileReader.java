package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ValiFileReader {
    public ValiFileReader(String filePath) {
        readValiFile(filePath);
    }

    public static HashMap<String, ArrayList<String>> readValiFile(String filePath) {
        HashMap<String, ArrayList<String>> valiFile = new HashMap<>();
        String currentHeader = null;
        int linesToRead = 4; // Number of lines following the header

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith(">")) {
                    // New header
                    currentHeader = line.substring(0).trim();
                    linesToRead = 4; // Reset lines to read
                    continue;
                }

                if (currentHeader != null && linesToRead > 0) {
                    // Store the line under the current header
                    ArrayList<String> sequences = valiFile.getOrDefault(currentHeader, new ArrayList<>());
                    sequences.add(line);
                    valiFile.put(currentHeader, sequences);
                    linesToRead--;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return valiFile;
    }
}
