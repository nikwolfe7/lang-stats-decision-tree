import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class NgramQuestionFactory implements CategoryQuestionFactory {

  private ArrayList<String> posTags;
  
  private ArrayList<String> model;
  
  private ArrayList<ArrayList<String>> ngrams;
  
  private List<Question> questions;
  
  public NgramQuestionFactory(Unigram model) {
    this.model = new ArrayList<String>(model.getCorpus());
    this.posTags = new ArrayList<String>();
    this.questions = new ArrayList<Question>();
    File file = new File("hw6-WSJ-TRAIN.tags.model");
    try {
      Scanner scn = new Scanner(file);
      while(scn.hasNextLine()) {
        String line = scn.nextLine();
        posTags.add(line.split("\t")[0]);
      }
      scn.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    for(String POS : posTags) {
      for(int i = 1; i <= 4; i++) {
        questions.add(new NgramQuestion(POS, i));
      }
    }
  }
  
  private ArrayList<ArrayList<String>> buildNgrams(ArrayList<String> corpus) {
    return null;
  }
  
  private class NgramQuestion implements Question {
    
    String POSTag;
    Integer n;
    
    public NgramQuestion(String POSTag, Integer n) {
      this.POSTag = POSTag;
      this.n = n;
    }

    @Override
    public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
      if(currentPOSIndex < n && currentPOSIndex > 1) {
        Set<String> set1 = new HashSet<String>(history.subList(0, currentPOSIndex-1));
        
      }
      
      return false;
    }

    @Override
    public String getDescription() {
      return "Given the previous " + n + " elements, is the next tag " + POSTag + "?";
    }
    
  }
  
  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
