import java.util.ArrayList;
import java.util.List;

public class EndOfSentenceQuestionFactory implements CategoryQuestionFactory {

  private ArrayList<Question> questions = new ArrayList<Question>();

  public EndOfSentenceQuestionFactory() {
    questions.add(new CheckBeginningOfSentence());
    //questions.add(new CheckEndOfSentence());
  }

  private class CheckBeginningOfSentence extends AbstractQuestion {
    
    public CheckBeginningOfSentence() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public String getDescription() {
      return "Is this the beginning of a sentence?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex == 0) {
        return true;
      } else {
        return history.get(currentPOSIndex - 1).equals(".");
      }
    }
  }

  private class CheckEndOfSentence extends AbstractQuestion {
    
    public CheckEndOfSentence() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public String getDescription() {
      return "Is this the end of a sentence?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      return history.get(currentPOSIndex).equals(".");
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
