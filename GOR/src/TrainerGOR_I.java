import java.io.IOException;
import java.util.ArrayList;

public class TrainerGOR_I {
    private final SearchWindow searchWindow;
    private final ArrayList<Sequence> trainingSequences;
    private int gorType;
    public TrainerGOR_I(String pathToDBfile, int gorType) throws IOException {
        this.gorType = gorType;
        searchWindow = new SearchWindow(gorType);
        this.trainingSequences = SecLibFileReader.readSecLibFile(pathToDBfile);
    }

    public void train(String pathToModelFile) throws IOException {
        // define main loop that goes over the sequences in training sequences
        // for each entry, init
        for (Sequence sequence: this.trainingSequences) {
            // get entry content in readable vars
            String pdbId = sequence.getId();
            String aaSequence = sequence.getAaSequence();
            String ssSequence = sequence.getSsSequence();
            searchWindow.trainGORI(aaSequence, ssSequence, pdbId);
        }
        searchWindow.writeToFile(pathToModelFile, this.getGorType());
    }

    // getters and setters
    public SearchWindow getSearchWindow() {
        return searchWindow;
    }

    public ArrayList<Sequence> getTrainingSequences() {
        return trainingSequences;
    }

    public int getGorType() {
        return gorType;
    }
}
