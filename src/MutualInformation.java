import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class MutualInformation {

  private Unigram model;

  private LinkedList<String> context;

  private ArrayList<Question> questions;
  
  private List<Map.Entry<Question, Double>> results;

  public MutualInformation(Unigram model, List<Question> questions) {
    this.results = new ArrayList<Map.Entry<Question, Double>>();
    this.questions = new ArrayList<Question>(questions);
    this.context = new LinkedList<String>();
    this.model = model;
    ArrayList<String> corpus = new ArrayList<String>(model.getCorpus());
    for (String line : corpus) {
      line += " ";
      String[] words = line.split(" ");
      for (String word : words) {
        context.add(word);
      }
    }
  }
  
  public void doCalculateMI() {
    int total = context.size();
    HashMap<Question, Double> res = new HashMap<Question, Double>();
    for(Question question : questions) {
      System.out.println("Running question: " + question.getDescription());
      Double posCount = 0.0;
      Double negCount = 0.0;
      List<String> positive = new ArrayList<String>();
      List<String> negative = new ArrayList<String>();
      System.out.println("This may take a while...");
      for(int i  = 0; i < context.size(); i++) {
        if(question.askQuestion(context, i)) {
          posCount++;
          positive.add(context.get(i));
        } else {
          negCount++;
          negative.add(context.get(i));
        }
      }
      Unigram pos = new Unigram(positive);
      Unigram neg = new Unigram(negative);
      Double H = model.calcCrossEntropy(model.getModel());
      Double H1 = pos.calcCrossEntropy(pos.getModel());
      Double w1 = posCount/total;
      Double H2 = neg.calcCrossEntropy(neg.getModel());
      Double w2 = negCount/total;
      /* MI calculation */
      Double MI = H - w1 * H1 - w2 * H2;
      res.put(question, MI);
      System.out.println("Asking question...\n\n" + question.getDescription() + "\n\nInformation gain: " + MI);
    }
    results = new LinkedList<Map.Entry<Question, Double>>(res.entrySet());
    Collections.sort(results, new Comparator<Map.Entry<Question, Double>>() {
      public int compare(Entry<Question, Double> o1, Entry<Question, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
  }

  public List<Map.Entry<Question, Double>> getResults() {
    return results;
  }
  
}
