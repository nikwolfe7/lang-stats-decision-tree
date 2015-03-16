import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PrecedingElementQuestionFactory implements CategoryQuestionFactory {

  private List<String> posTags;

  private List<Question> questions;

  public PrecedingElementQuestionFactory() {
    this.questions = new ArrayList<Question>();
    this.posTags = new ArrayList<String>();
    File file = new File("hw6-WSJ-TRAIN.tags.model");
    try {
      Scanner scn = new Scanner(file);
      while (scn.hasNextLine()) {
        String line = scn.nextLine();
        posTags.add(line.split("\t")[0]);
      }
      scn.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    for (String POS : posTags) {
      questions.add(new NgramQuestion(POS));
    }
  }

  private class NgramQuestion extends AbstractQuestion {

    private String POSTag;

    public NgramQuestion(String POSTag) {
      this.POSTag = POSTag;
    }

    @Override
    public String getDescription() {
      return "Is the previous element "+ POSTag +"?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex == 0) {
        return false;
      } else {
        return POSTag.equals(history.get(currentPOSIndex - 1));
      }
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
