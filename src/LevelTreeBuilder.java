import java.util.List;


public class LevelTreeBuilder {
  
  private TreeBuilder root;
  private Integer stopValue;
  private Unigram test;
  private List<Question> questions;
  
  public LevelTreeBuilder(TreeBuilder builder, Unigram test, Integer stopValue) {
    this.root = builder;
    this.test = test;
    this.stopValue = stopValue;
  }
  
  public void doBuildTree() {
    root.testUnigram(test);
    
    Question nextLevelQuestion = root.getRemainingQuestions().getQuestions().get(0);
    
    System.out.println("\nRIGHT SUBTREE\n");
    TreeBuilder rightTree = new TreeBuilder(root.getRightChild(), nextLevelQuestion);
    rightTree.testUnigram(test);
    
    System.out.println("\nLEFT SUBTREE\n");
    TreeBuilder leftTree = new TreeBuilder(root.getLeftChild(), nextLevelQuestion);
    leftTree.testUnigram(test);
  }
  

}
