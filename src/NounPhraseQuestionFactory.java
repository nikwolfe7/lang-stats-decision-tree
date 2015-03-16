import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NounPhraseQuestionFactory implements CategoryQuestionFactory {

  private String[] posTags = { "NN", "IN", "NNP", "DT", "JJ", "NNS" };

  private ArrayList<String> nounPhrasePOSTags = new ArrayList<String>();

  private ArrayList<Question> questions = new ArrayList<Question>();

  public NounPhraseQuestionFactory() {
    for (String POS : posTags) {
      nounPhrasePOSTags.add(POS);
    }
    questions.add(new PrecedeOneCheckNounPhrase());
    questions.add(new PrecedeNCheckNounPhrase(2));
    questions.add(new PrecedeNCheckNounPhrase(3));
    questions.add(new PrecedeNCheckNounPhrase(4));
    questions.add(new PrecedeNCheckNounPhrase(5));
  }

  private class PrecedeOneCheckNounPhrase extends AbstractQuestion {
    
    public PrecedeOneCheckNounPhrase() {
      // TODO Auto-generated constructor stub
    }

    @Override
    public String getDescription() {
      return "Is the previous element part of a noun phrase?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex == 0) {
        return false;
      } else {
        return nounPhrasePOSTags.contains(history.get(currentPOSIndex - 1));
      }
    }
  }

  private class PrecedeNCheckNounPhrase extends AbstractQuestion {

    private Integer n;

    public PrecedeNCheckNounPhrase(Integer n) {
      this.n = n;
    }

    @Override
    public String getDescription() {
      return "Are the previous " + n + " elements part of a noun phrase?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      Set<String> set2 = new HashSet<String>(nounPhrasePOSTags);
      if (currentPOSIndex <= n) {
        Set<String> set1 = new HashSet<String>(history.subList(0, currentPOSIndex));
        return set2.containsAll(set1);
      } else {
        Set<String> set1 = new HashSet<String>(history.subList(currentPOSIndex - n, currentPOSIndex));
        return set2.containsAll(set1);
      }
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
