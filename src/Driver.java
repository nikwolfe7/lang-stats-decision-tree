import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class Driver {
  
  private static String training = "hw6-WSJ-TRAIN.tags";
  private static String testing = "hw6-WSJ-TEST.tags";
  
  public static void main(String[] args) {
    Unigram train = new Unigram(training);
    Unigram test = new Unigram(testing);
    
    System.out.println("Average Log Likelihood of " + training + ": " + train.getAvgLogLikelihood());
    System.out.println("Average Log Likelihood of " + testing + ": " + test.getAvgLogLikelihood());
    
    System.out.println("Perplexity of " + training + ": " + train.calcPerplexity(train.getModel()));
    System.out.println("Perplexity of " + testing + ": " + train.calcPerplexity(test.getModel()));
    
    List<Map.Entry<Question, Double>> results = new ArrayList<Map.Entry<Question,Double>>();
    
    CategoryQuestionFactory cat1Factory = new NounPhraseQuestionFactory();
    MutualInformation mutualInfoCalculator = new MutualInformation(train, cat1Factory.getQuestions());
    mutualInfoCalculator.doCalculateMI();
    results.addAll(mutualInfoCalculator.getResults());
    
    CategoryQuestionFactory cat2Factory = new PrecedingElementQuestionFactory();
    MutualInformation mutualInfoCalculator2 = new MutualInformation(train, cat2Factory.getQuestions());
    mutualInfoCalculator2.doCalculateMI();
    results.addAll(mutualInfoCalculator2.getResults());
    
    CategoryQuestionFactory cat3Factory = new NgramQuestionFactory(train);
    MutualInformation mutualInfoCalculator3 = new MutualInformation(train, cat3Factory.getQuestions());
    mutualInfoCalculator3.doCalculateMI();
    results.addAll(mutualInfoCalculator3.getResults());
    
    CategoryQuestionFactory cat4Factory = new EndOfSentenceQuestionFactory();
    MutualInformation mutualInfoCalculator4 = new MutualInformation(train, cat4Factory.getQuestions());
    mutualInfoCalculator4.doCalculateMI();
    results.addAll(mutualInfoCalculator4.getResults());
    
    CategoryQuestionFactory cat5Factory = new VerbPositionQuestionFactory();
    MutualInformation mutualInfoCalculator5 = new MutualInformation(train, cat5Factory.getQuestions());
    mutualInfoCalculator5.doCalculateMI();
    results.addAll(mutualInfoCalculator5.getResults());
    
    Collections.sort(results, new Comparator<Map.Entry<Question, Double>>() {
      public int compare(Entry<Question, Double> o1, Entry<Question, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
    System.out.println("Most informative question:");
    System.out.println("Q: " + results.get(0).getKey().getDescription() + "\tInformation Gain: " + results.get(0).getValue());
  }

}
