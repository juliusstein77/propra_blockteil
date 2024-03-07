public class Tester {
    public static void main(String[] args) {
        String prefixP = "PPPPPP";
        String prefixS = "ATLAV";
        String suffixP = "ATLV";
        String suffixS = "P";
        int prefixPLength = prefixP.length();
        int prefixSLength = prefixS.length();
        int suffixPLength = suffixP.length();
        int suffixSLength = suffixS.length();
        //System.out.println(prefixP);
        //System.out.println(prefixS);

        System.out.println(suffixP);
        System.out.println(suffixS);

        if (prefixPLength > prefixSLength) {
            for (int i = 0; i < prefixPLength; i++) {
                prefixS = prefixS + "-";
            }
            for (int i = 0; i < prefixSLength; i++) {
                prefixP = "-" + prefixP;
            }
        } else if (prefixSLength > prefixPLength) {
            for (int i = 0; i < prefixSLength ; i++) {
                prefixP = prefixP + "-";
            }
            for (int i = 0; i < prefixPLength; i++) {
                prefixS = "-" + prefixS;
            }
        }

        if (suffixPLength > suffixSLength) {
            for (int i = 0; i < suffixPLength; i++) {
                suffixS = "-" + suffixS;
            }
            for (int i = 0; i < suffixSLength; i++) {
                suffixP = suffixP + "-";
            }
        } else if (suffixSLength > suffixPLength) {
            for (int i = 0; i < suffixSLength ; i++) {
                suffixP = "-" + suffixP;
            }
            for (int i = 0; i < suffixPLength; i++) {
                suffixS = suffixS + "-";
            }
        }


        //System.out.println(prefixP);
        //System.out.println(prefixS);
        System.out.println(suffixP);
        System.out.println(suffixS);
    }
}
