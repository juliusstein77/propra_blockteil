import java.io.IOException;
import java.util.ArrayList;

public class TrainerGOR_III extends TrainerGOR_I {
    public TrainerGOR_III(String pathToDb, int gorType) throws IOException {
        super(pathToDb, gorType);
    }

    @Override
    public void train(String pathToModelFile) throws IOException {
        SearchWindow searchWindow = this.getSearchWindow();
        ArrayList<Sequence> trainingSequences = this.getTrainingSequences();
        // define main loop that goes over the sequences in training sequences
        // for each entry, init
        for (Sequence sequence: trainingSequences) {
            // get entry content in readable vars
            String pdbId = sequence.getId();
            String aaSequence = sequence.getAaSequence();
            String ssSequence = sequence.getSsSequence();
            this.getSearchWindow().trainGORIII(aaSequence, ssSequence, pdbId);
        }
        searchWindow.writeToFile(pathToModelFile, this.getGorType());
    }
}
