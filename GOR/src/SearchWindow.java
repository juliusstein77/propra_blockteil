import constants.Constants;
import utils.FileUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SearchWindow {
    private final HashMap<Character, int[][]> gor1Matrices = new HashMap<>();
    private final HashMap<Character, HashMap<Character, int[][]>> gor3Matrices = new HashMap<>();
    private final HashMap<Integer, Character> INDEX_TO_AA = new HashMap<>();
    private final HashMap<Character, Integer> AA_TO_INDEX = new HashMap<>();
    private final char[] secStructTypes = {'H', 'E', 'C'};

    public SearchWindow(int gorType) {
        initAAHashMaps();
        if (gorType == 1) {
            this.initMatricesGOR1(this.secStructTypes);
        }
        else if (gorType == 3) {
            this.initMatricesGOR3();
        }
    }

    public SearchWindow(int gorType, String pathToModelFile) throws IOException {
        initAAHashMaps();
        if (gorType == 1) {
            this.initMatricesGOR1(this.secStructTypes);
        }
        else if (gorType == 3) {
           this.initMatricesGOR3();
        }
        // read model file to init matrices
        this.readModFile(pathToModelFile);
    }

    public int getWINDOWSIZE() {
        return Constants.WINDOW_SIZE.getValue();
    }

    public String gor1ToString(){
        // std out all matrices :)
        StringBuilder out = new StringBuilder();
        out.append("// Matrix3D\n");

        for (char key : this.gor1Matrices.keySet()){
            out.append("=").append(key).append("=\n\n");
            int[][] currSecMatrix = gor1Matrices.get(key);

            for (int i = 0; i < Constants.AA_SIZE.getValue(); i++) {
                out.append(this.INDEX_TO_AA.get(i) + "\t");

                for (int j = 0; j < Constants.WINDOW_SIZE.getValue(); j++) {
                    out.append(currSecMatrix[i][j]).append("\t");
                }
                out.append("\n");

            }
            out.append("\n");
        }
        return out.toString();
    }

    public String gor3ToString(){
        StringBuilder out = new StringBuilder();
        out.append("// Matrix4D\n");

        for (char aa : this.gor3Matrices.keySet()){
            for (char secType : this.gor3Matrices.get(aa).keySet()) {
                out.append("=" + aa + "," + secType).append("=\n");
                int[][] currSecMatrix = gor3Matrices.get(aa).get(secType);

                for (int i = 0; i < Constants.AA_SIZE.getValue(); i++) {
                    out.append(this.INDEX_TO_AA.get(i) + "\t");

                    for (int j = 0; j < Constants.WINDOW_SIZE.getValue(); j++) {
                        out.append(currSecMatrix[i][j]).append("\t");
                    }
                    out.append("\n");

                }
                out.append("\n");
            }
        }
        return out.toString();

    }

    public void initMatricesGOR1(char[] secStructTypes){
        // init a matrix for each secStructType
        for (char secStruct : secStructTypes){
            this.gor1Matrices.put(secStruct, new int[20][17]);

            // put default value into matrices
            for (int i = 0; i < Constants.AA_SIZE.getValue(); i++) {
                for (int j = 0; j <Constants.WINDOW_SIZE.getValue(); j++) {
                    this.gor1Matrices.get(secStruct)[i][j] = 0;
                }
            }
        }
    }

    public HashMap<Character, int[][]> getGor1Matrices() {
        return this.gor1Matrices;
    }

    public void initAAHashMaps(){
        // maps AA to the row of the 2d matrix
        this.INDEX_TO_AA.put(0, 'A');
        this.INDEX_TO_AA.put(1, 'C');
        this.INDEX_TO_AA.put(2, 'D');
        this.INDEX_TO_AA.put(3, 'E');
        this.INDEX_TO_AA.put(4, 'F');
        this.INDEX_TO_AA.put(5, 'G');
        this.INDEX_TO_AA.put(6, 'H');
        this.INDEX_TO_AA.put(7, 'I');
        this.INDEX_TO_AA.put(8, 'K');
        this.INDEX_TO_AA.put(9, 'L');
        this.INDEX_TO_AA.put(10,'M');
        this.INDEX_TO_AA.put(11,'N');
        this.INDEX_TO_AA.put(12,'P');
        this.INDEX_TO_AA.put(13,'Q');
        this.INDEX_TO_AA.put(14,'R');
        this.INDEX_TO_AA.put(15,'S');
        this.INDEX_TO_AA.put(16,'T');
        this.INDEX_TO_AA.put(17,'V');
        this.INDEX_TO_AA.put(18,'W');
        this.INDEX_TO_AA.put(19,'Y');
        // maps AA to the row of the 2d matrix
        this.AA_TO_INDEX.put('A', 0);
        this.AA_TO_INDEX.put('C', 1);
        this.AA_TO_INDEX.put('D', 2);
        this.AA_TO_INDEX.put('E', 3);
        this.AA_TO_INDEX.put('F', 4);
        this.AA_TO_INDEX.put('G', 5);
        this.AA_TO_INDEX.put('H', 6);
        this.AA_TO_INDEX.put('I', 7);
        this.AA_TO_INDEX.put('K', 8);
        this.AA_TO_INDEX.put('L', 9);
        this.AA_TO_INDEX.put('M', 10);
        this.AA_TO_INDEX.put('N', 11);
        this.AA_TO_INDEX.put('P', 12);
        this.AA_TO_INDEX.put('Q', 13);
        this.AA_TO_INDEX.put('R', 14);
        this.AA_TO_INDEX.put('S', 15);
        this.AA_TO_INDEX.put('T', 16);
        this.AA_TO_INDEX.put('V', 17);
        this.AA_TO_INDEX.put('W', 18);
        this.AA_TO_INDEX.put('Y', 19);
    }

    public HashMap<Integer, Character> getINDEX_TO_AA() {
        return INDEX_TO_AA;
    }

    public void writeToFile(String modelFilePath, int gorType) throws IOException {
        try (BufferedWriter buf = new BufferedWriter(new FileWriter(modelFilePath))) {
            // Get the string representation of your object
            if (gorType == 1) {
                String output = this.gor1ToString();
                buf.write(output);
            } else if (gorType == 3) {
                String output = this.gor3ToString();
                buf.write(output);
            }
            // Write the output to the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readModFile(String pathToModFile) throws IOException {
        File seqLibFile = new File(pathToModFile);
        ArrayList<String> lines = FileUtils.readLines(seqLibFile);

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("=C=")){
                copyLineIntoArray(i, lines, 'C');
            }
            else if (lines.get(i).startsWith("=H=")){
                copyLineIntoArray(i, lines, 'H');
            }
            else if (lines.get(i).startsWith("=E=")){
                copyLineIntoArray(i, lines, 'E');
            }
        }
    }
    public void copyLineIntoArray(int i, ArrayList<String> lines, char secType){
        int relativeMatrixIndex = 0;
        for (int j = i + 2; j <= i + 21; j++) {
            String[] line = lines.get(j).split("\t");
            for (int k = 0; k < line.length - 1; k++) {
                this.gor1Matrices.get(secType)[relativeMatrixIndex][k] = Integer.parseInt(line[k+1]);
            }
            relativeMatrixIndex++;
        }
    }


    /*
    Get all params ready and call addSecondaryCounts and extendSecondarySequence
     */
    public void predictSeq(HashMap<Character, Integer> totalOcc, Sequence sequence, int gor){
        String aaSequence = sequence.getAaSequence();
        if (aaSequence.length() >= this.getWINDOWSIZE()) {
            int windowMid = this.getWINDOWSIZE() / 2; // define mid index
            int start = 0;
            int windowStop  = aaSequence.length() - windowMid ; // define end index of seq (this is the max val of windowMid)

            // enter main loop
            while (windowMid <  windowStop) {
                // get AA at windowMid
                char aaAtWindoMid = aaSequence.charAt(windowMid);

                // now cut out a subsequence of size window
                String windowSequence = aaSequence.substring(start, start + getWINDOWSIZE());

                char predSecStruct = 'C'; // default
                if (AA_TO_INDEX.containsKey(aaAtWindoMid)){
                    if (gor == 1){
                         predSecStruct = predictGorI(windowSequence, totalOcc);
                    }
                }

                sequence.extendSecStruct(predSecStruct);
                windowMid++;
                start++;
            }
        }
        // if the sequence is too short, just give it "-"
        else {
            sequence.setSsSequence(""); // reset "--------" prefix which is in seq per default
            for (int i = 0; i < sequence.getAaSequence().length(); i++) {
                sequence.extendSecStruct('-');
            }
        }
    }

    public char predictGorI(String windowSequence, HashMap<Character, Integer> totalOcc){
        // for every secType, loop over sequence and calculate values
        // these are our scores
        HashMap<Character, Double> scoresPerSeq = new HashMap<>();
        // these are our global final values we will use to determine the max value
        scoresPerSeq.put('H', 0.0);
        scoresPerSeq.put('E', 0.0);
        scoresPerSeq.put('C', 0.0);

        for (char secType: this.getGor1Matrices().keySet())  {
            // loop over sequence
            for (int column = 0; column < windowSequence.length(); column++) {
                char currAAinWindow = windowSequence.charAt(column);
                if (AA_TO_INDEX.containsKey(currAAinWindow)) {

                    // get row and look up value in matrix of curr secType
                    int row = AA_TO_INDEX.get(currAAinWindow);
                    int valueInMatrix = this.getGor1Matrices().get(secType)[row][column];

                    int totalSec = totalOcc.get(secType); // f a|s
                    int totalNotSec = 0; // get the !s and !a|s freqs
                    int notSec = 0;

                    for (char antiSecStruct : getGor1Matrices().keySet()){
                       if (secType != antiSecStruct){
                            totalNotSec += totalOcc.get(antiSecStruct); // f !s
                            notSec += this.getGor1Matrices().get(antiSecStruct)[row][column];// f a|!s
                       }
                    }
                    // sum values into scoresPerSeq
                    double scoreToPutIntoSum = Math.log(1.0 * valueInMatrix / notSec * 1.0 * totalNotSec / totalSec);
                    double currentValue = scoresPerSeq.get(secType);
                    scoresPerSeq.put(secType, currentValue + scoreToPutIntoSum);
                }
            }
        }

        if (scoresPerSeq.get('H') >= scoresPerSeq.get('E') && scoresPerSeq.get('H') >= scoresPerSeq.get('C')) {
            return 'H';
        } else if (scoresPerSeq.get('E') >= scoresPerSeq.get('H') && scoresPerSeq.get('E')  >= scoresPerSeq.get('C')) {
            return 'E';
        } else {
            return 'C';
        }

    }
    /*
    Training model for GOR I
     */
    public void trainGORI(String aaSequence, String ssSequence, String pdbId){
        if (aaSequence.length() >= this.getWINDOWSIZE()) {
            int start = 0; // init start index
            int windowMid = this.getWINDOWSIZE() / 2; // define mid index
            int windowEndPosition  = aaSequence.length() - windowMid ; // define end index of seq (this is the max val of windowMid)

            // enter main loop
            while (windowMid < windowEndPosition) {
                String aaSubSeq = aaSequence.substring(windowMid - this.getWINDOWSIZE() / 2, windowMid + 1 + this.getWINDOWSIZE() / 2);
                String ssSubSeq = ssSequence.substring(windowMid - this.getWINDOWSIZE() / 2, windowMid + 1 + this.getWINDOWSIZE() / 2);

                // in the subSeqs get the corresponding vals
                for (int index = 0; index < aaSubSeq.length(); index++) {
                    char currSS = ssSubSeq.charAt(this.getWINDOWSIZE() / 2) ; // this is the sec struct of the mid AA
                    char currAA = aaSubSeq.charAt(index);
                    if (AA_TO_INDEX.containsKey(currAA)){
                        int indexOfAAinMatrix = this.AA_TO_INDEX.get(currAA);
                        this.getGor1Matrices().get(currSS)[indexOfAAinMatrix][index]++;
                    }
                }
                windowMid++;
            }
        }
    }

    // ---------------------------------------------- GOR III -------------------------------------------------------
    public void initMatricesGOR3(){
        for (char aa : AA_TO_INDEX.keySet()) {
            // TODO: duplicate code for now >:(
            HashMap<Character, int[][]> secStructHashMapForAA = new HashMap<>();
            for (char secStruct : secStructTypes){
                secStructHashMapForAA.put(secStruct, new int[20][17]);
                this.gor3Matrices.put(aa, secStructHashMapForAA);
                // put default value into matrices
                for (int i = 0; i < Constants.AA_SIZE.getValue(); i++) {
                    for (int j = 0; j <Constants.WINDOW_SIZE.getValue(); j++) {
                        this.gor3Matrices.get(aa).get(secStruct)[i][j] = 0;
                    }
                }
            }
        }
    }
    public void trainGORIII(String aaSequence, String ssSequence, String pdbId){
        if (aaSequence.length() >= this.getWINDOWSIZE()) {
            int start = 0; // init start index
            int windowMid = this.getWINDOWSIZE() / 2; // define mid index
            int windowEndPosition  = aaSequence.length() - windowMid ; // define end index of seq (this is the max val of windowMid)
            // enter main loop
            while (windowMid < windowEndPosition) {
                String aaSubSeq = aaSequence.substring(windowMid - this.getWINDOWSIZE() / 2, windowMid + 1 + this.getWINDOWSIZE() / 2);
                String ssSubSeq = ssSequence.substring(windowMid - this.getWINDOWSIZE() / 2, windowMid + 1 + this.getWINDOWSIZE() / 2);

                // in the subSeqs get the corresponding vals
                for (int index = 0; index < aaSubSeq.length(); index++) {

                    // this is the sec struct of the mid AA
                    char aaMid = aaSubSeq.charAt(this.getWINDOWSIZE() / 2);
                    char currSS = ssSubSeq.charAt(this.getWINDOWSIZE() / 2);

                    if (AA_TO_INDEX.containsKey(aaMid)){
                        for (int i = 0; i < aaSubSeq.length(); i++) {
                            // for currSS of midAA
                            char aaToCountInMatrix = aaSubSeq.charAt(i);
                            if (AA_TO_INDEX.containsKey(aaToCountInMatrix)) {
                                int indexOfAAinMatrix = this.AA_TO_INDEX.get(aaToCountInMatrix);
                                this.gor3Matrices.get(aaMid).get(currSS)[indexOfAAinMatrix][index]++;
                                }
                            }
                        }
                    }
                windowMid++;
            }
        }
    }
}