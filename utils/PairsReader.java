package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PairsReader {

    public PairsReader(String filePath) throws IOException {
        readPairs(filePath);
    }

    public static ArrayList<String[]> readPairs(String filePath) throws IOException {
        ArrayList<String[]> tuples = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 2) {
                    String[] pair = {parts[0], parts[1]};
                    tuples.add(pair);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tuples;
    }
}
