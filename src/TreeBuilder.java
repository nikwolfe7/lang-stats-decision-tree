import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


public class TreeBuilder {
  
  private Unigram parent;
  private Unigram leftChild;
  private Unigram rightChild;
  private Question mostInformativeQuestion;
  
  public TreeBuilder(Unigram model, List<CategoryQuestionFactory> questionFactories) {
    this.parent = model;
    List<Map.Entry<Question, Double>> results = runMutualInformationQuestions(parent, questionFactories);
    this.mostInformativeQuestion = results.get(0).getKey();
    doBuildTree();
  }
  
  private void doBuildTree() {
    System.out.println("\nParent (Original Model):\n");
    printUnigramStats(getParent());
    leftChild = new Unigram(mostInformativeQuestion.getNegatives());
    System.out.println("\nLeft Child (Negatives):\n");
    printUnigramStats(getLeftChild());
    rightChild = new Unigram(mostInformativeQuestion.getPositives());
    System.out.println("\nRight Child (Positives):\n");
    printUnigramStats(getLeftChild());
  }
  
  private void printUnigramStats(Unigram unigram) {
    System.out.println("Average Log-Likelihood: " + unigram.getAvgLogLikelihood());
    System.out.println("Entropy: " + unigram.calcCrossEntropy(unigram.getModel()));
    System.out.println("Perplexity: " + unigram.calcPerplexity(unigram.getModel()));
  }
  
  private List<Map.Entry<Question, Double>> runMutualInformationQuestions(Unigram unigram, List<CategoryQuestionFactory> questionFactories) {
    List<Map.Entry<Question, Double>> results = new ArrayList<Map.Entry<Question,Double>>();
    List<MutualInformation> mutualInfoQuestions = new ArrayList<MutualInformation>();
    for(CategoryQuestionFactory categoryQuestionFactory : questionFactories) {
      mutualInfoQuestions.add(new MutualInformation(unigram, categoryQuestionFactory.getQuestions()));
    }
    for(MutualInformation mi : mutualInfoQuestions) {
      mi.doCalculateMI();
      results.addAll(mi.getResults());
    }
    Collections.sort(results, new Comparator<Map.Entry<Question, Double>>() {
      public int compare(Entry<Question, Double> o1, Entry<Question, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
    System.out.println("Most informative question:");
    System.out.println("Q: " + results.get(0).getKey().getDescription() + "\tInformation Gain: " + results.get(0).getValue());
    return results;
  }
  
  public void testUnigram(Unigram test) {
    System.out.println("\n==================== TEST RESULTS ====================\n");
    List<CategoryQuestionFactory> qFact = new ArrayList<CategoryQuestionFactory>();
    qFact.add(new CategoryQuestionFactory() {
      public List<Question> getQuestions() {
        List<Question> questions = new ArrayList<Question>();
        questions.add(mostInformativeQuestion);
        return questions;
      }
    });
    List<Map.Entry<Question, Double>> results = runMutualInformationQuestions(test, qFact);
    /* Test stats */
    Question best = results.get(0).getKey();
    System.out.println("\nTest Parent (Original Model):\n");
    printUnigramStats(test);
    System.out.println("\nTest Left Child (Negatives):\n");
    printUnigramStats(new Unigram(best.getNegatives()));
    System.out.println("\nTest Right Child (Positives):\n");
    printUnigramStats(new Unigram(best.getPositives()));
  }
  
  public Unigram getParent() {
    return parent;
  }

  public Unigram getLeftChild() {
    return leftChild;
  }

  public Unigram getRightChild() {
    return rightChild;
  }

}
