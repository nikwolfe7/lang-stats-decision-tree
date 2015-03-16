import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VerbPositionQuestionFactory implements CategoryQuestionFactory {

  private String[] posTags = { "VBD", "VB", "VBZ", "VBN", "VBG", "VBP" };

  private ArrayList<String> verbPhraseTags = new ArrayList<String>();

  private ArrayList<Question> questions = new ArrayList<Question>();

  public VerbPositionQuestionFactory() {
    for (String POS : posTags) {
      verbPhraseTags.add(POS);
    }
    questions.add(new CheckBeforeVerb());
    questions.add(new CheckAfterVerb());
  }

  private class CheckAfterVerb extends AbstractQuestion {
    
    public CheckAfterVerb() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public String getDescription() {
      return "Are we after a verb?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex == 0) {
        return false;
      } else {
        int check = currentPOSIndex - 1;
        while(check > 0) { // history after a period
          if(history.get(check).equals(".")) {
            break;
          } else {
            check--;
          }
        }
        Set<String> prev = new HashSet<String>(history.subList(check, currentPOSIndex));
        Set<String> verbs = new HashSet<String>(verbPhraseTags);
        prev.retainAll(verbs);
        return prev.size() > 0;
      }
    }
  }

  private class CheckBeforeVerb extends CheckAfterVerb {

    public CheckBeforeVerb() {
      super();
    }

    @Override
    public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
      return !super.askQuestion(history, currentPOSIndex);
    }

    @Override
    public String getDescription() {
      return "Are we before a verb?";
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
