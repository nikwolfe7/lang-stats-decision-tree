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

  private class PrecedeOneCheckNounPhrase implements Question {

    @Override
    public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex == 0) {
        return nounPhrasePOSTags.contains(history.get(currentPOSIndex));
      } else if (currentPOSIndex >= 1) {
        boolean curr = nounPhrasePOSTags.contains(history.get(currentPOSIndex));
        boolean prev = nounPhrasePOSTags.contains(history.get(currentPOSIndex - 1));
        //System.out.println("Curr POS: " + history.get(currentPOSIndex) + "\t\tPrev POS: " + history.get(currentPOSIndex - 1));
        return (curr && prev);
      }
      return false;
    }

    @Override
    public String getDescription() {
      return "Are preceding and current elements part of a noun phrase?";
    }
  }

  private class PrecedeNCheckNounPhrase implements Question {

    private Integer n;

    public PrecedeNCheckNounPhrase(Integer n) {
      this.n = n;
    }

    @Override
    public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex < n) {
        Set<String> set1 = new HashSet<String>(history.subList(0, currentPOSIndex));
        Set<String> set2 = new HashSet<String>(nounPhrasePOSTags);
        System.out.println("Checking " + set1 + " and " + set2);
        return set2.containsAll(set1);
      } else {
        Set<String> set1 = new HashSet<String>(history.subList(currentPOSIndex - 1 - n,
                currentPOSIndex));
        Set<String> set2 = new HashSet<String>(nounPhrasePOSTags);
        System.out.println("Checking " + set1 + " and " + set2);
        return set2.containsAll(set1);
      }
    }

    @Override
    public String getDescription() {
      return "Are the previous " + n + " elements and the current POS tag part of a noun phrase?";
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
