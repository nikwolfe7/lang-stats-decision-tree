import java.util.ArrayList;
import java.util.List;


public abstract class AbstractQuestion implements Question {
  
  private List<String> positives = new ArrayList<String>();
  private List<String> negatives = new ArrayList<String>();

  @Override
  public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
    String curr = history.get(currentPOSIndex);
    boolean result = doAskQuestion(history, currentPOSIndex);
    if (result) {
      positives.add(curr);
    } else {
      negatives.add(curr);
    }
    return result;
  }
  
  protected abstract boolean doAskQuestion(List<String> history, Integer currentPOSIndex);

  public abstract String getDescription();

  @Override
  public List<String> getPositives() {
   return positives;
  }

  @Override
  public List<String> getNegatives() {
    return negatives;
  }

}
