import java.util.Map;


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
    
    CategoryQuestionFactory cat1Factory = new NounPhraseQuestionFactory();
    MutualInformation mutualInfoCalculator = new MutualInformation(train, cat1Factory.getQuestions());
    mutualInfoCalculator.doCalculateMI();
    mutualInfoCalculator.getResults();
    
    CategoryQuestionFactory cat2Factory = new NgramQuestionFactory(train);
    MutualInformation mutualInfoCalculator2 = new MutualInformation(train, cat2Factory.getQuestions());
    mutualInfoCalculator2.doCalculateMI();
    mutualInfoCalculator2.getResults();
    
  }

}
