import java.util.ArrayList;
import java.util.List;

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
    
    List<CategoryQuestionFactory> questionFactories = new ArrayList<CategoryQuestionFactory>();
    questionFactories.add(new NounPhraseQuestionFactory());
    questionFactories.add(new PrecedingElementQuestionFactory());
    questionFactories.add(new NgramQuestionFactory(train));
    questionFactories.add(new EndOfSentenceQuestionFactory());
    questionFactories.add(new VerbPositionQuestionFactory());
    
    TreeBuilder tree = new TreeBuilder(train, questionFactories);
    tree.testUnigram(test);
    
  }

}
