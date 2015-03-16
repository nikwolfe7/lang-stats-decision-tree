import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NgramQuestionFactory implements CategoryQuestionFactory {

  private List<String> corpus;

  private List<String> nGramList;

  private List<Question> questions;

  private Integer resultLimit = 20;

  public NgramQuestionFactory(Unigram model) {
    this.questions = new ArrayList<Question>();
    this.corpus = new ArrayList<String>();
    for (String line : model.getCorpus()) {
      line += " ";
      String[] words = line.split(" ");
      for (String word : words) {
        corpus.add(word);
      }
    }
    nGramList = buildNgrams();
    for (String ngram : nGramList) {
      questions.add(new NgramQuestion(ngram));
    }
  }

  private List<String> buildNgrams() {
    Counter<String, Integer> bigram = new Counter<String, Integer>();
    Counter<String, Integer> trigram = new Counter<String, Integer>();
    Counter<String, Integer> quadgram = new Counter<String, Integer>();
    for (int i = 0; i < corpus.size() - 4; i++) {
      String bgram = corpus.get(i) + "_" + corpus.get(i + 1);
      String tgram = corpus.get(i) + "_" + corpus.get(i + 1) + "_" + corpus.get(i + 2);
      String qgram = corpus.get(i) + "_" + corpus.get(i + 1) + "_" + corpus.get(i + 2) + "_"
              + corpus.get(i + 3);
      bigram.add(bgram);
      trigram.add(tgram);
      quadgram.add(qgram);
    }
    String last_trigram = corpus.get(corpus.size() - 3) + "_" + corpus.get(corpus.size() - 2) + "_"
            + corpus.get(corpus.size() - 1);
    String last_bigram = corpus.get(corpus.size() - 2) + "_" + corpus.get(corpus.size() - 1);
    String second_to_last_bigram = corpus.get(corpus.size() - 3) + "_"
            + corpus.get(corpus.size() - 2);
    trigram.add(last_trigram);
    bigram.add(last_bigram);
    bigram.add(second_to_last_bigram);
    List<Map.Entry<String, Integer>> ngrams = new ArrayList<Map.Entry<String, Integer>>();
    ngrams.addAll(bigram.getSortedNgrams().subList(0, resultLimit));
    ngrams.addAll(trigram.getSortedNgrams().subList(0, resultLimit));
    ngrams.addAll(quadgram.getSortedNgrams().subList(0, resultLimit));
    ArrayList<String> retList = new ArrayList<String>();
    for (Map.Entry<String, Integer> elem : ngrams) {
      retList.add(elem.getKey());
    }
    return retList;
  }

  private class Counter<K, V> extends HashMap<K, Integer> {
    private static final long serialVersionUID = 1L;

    public void add(K k) {
      if (this.containsKey(k)) {
        put(k, get(k) + 1);
      } else {
        put(k, 1);
      }
    }

    public List<Map.Entry<K, Integer>> getSortedNgrams() {
      List<Map.Entry<K, Integer>> list = new LinkedList<Map.Entry<K, Integer>>(entrySet());
      Collections.sort(list, new Comparator<Map.Entry<K, Integer>>() {
        public int compare(Map.Entry<K, Integer> o1, Map.Entry<K, Integer> o2) {
          return o2.getValue().compareTo(o1.getValue());
        }
      });
      return list;
    }
  }

  private class NgramQuestion extends AbstractQuestion {

    private String ngram;

    private Integer n;

    public NgramQuestion(String ngram) {
      this.ngram = ngram;
      this.n = ngram.split("_").length;
    }

    @Override
    public String getDescription() {
      return "Are the previous " + n + " elements " + ngram + "?";
    }

    @Override
    protected boolean doAskQuestion(List<String> history, Integer currentPOSIndex) {
      if (currentPOSIndex < n) {
        return false;
      } else {
        List<String> prev = history.subList(currentPOSIndex - n, currentPOSIndex);
        String joinedString = prev.stream().map(i -> i.toString()).collect(Collectors.joining("_"));
        return ngram.equals(joinedString);
      }
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
