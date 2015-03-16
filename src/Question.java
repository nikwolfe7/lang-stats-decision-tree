import java.util.List;

public interface Question {
  
  public boolean askQuestion(List<String> history, Integer currentPOSIndex);
  
  /**
   * Describe yourself so we can output stuff...
   * @return
   */
  public String getDescription();
  
  public List<String> getPositives();
  
  public List<String> getNegatives();
  
}