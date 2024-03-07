package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;

public class SeqLibReader {

    public SeqLibReader(String filePath) throws IOException {
        readSequenceLibrary(filePath);
    }

    public static HashMap<String, String> readSequenceLibrary(String seqlibFile) throws IOException {
        HashMap<String, String> sequenceLibrary = new HashMap<>();
        BufferedReader br = new BufferedReader(new java.io.FileReader(seqlibFile));
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
