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
    
    TreeBuilder tree = new TreeBuilder(train, 
            new NounPhraseQuestionFactory(), 
            new PrecedingElementQuestionFactory(), 
            new NgramQuestionFactory(train),
            new EndOfSentenceQuestionFactory(),
            new VerbPositionQuestionFactory());
    
    LevelTreeBuilder builder = new LevelTreeBuilder(tree, test, 3);
    builder.doBuildTree();
    
  }

}
