import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class NgramQuestionFactory implements CategoryQuestionFactory {

  private List<String> posTags;

  private List<String> context;

  private List<String> nGramList;

  private List<Question> questions;

  private Integer resultLimit = 20;

  public NgramQuestionFactory(Unigram model) {
    this.posTags = new ArrayList<String>();
    this.questions = new ArrayList<Question>();
    this.context = new ArrayList<String>();
    for (String line : model.getCorpus()) {
      line += " ";
      String[] words = line.split(" ");
      for (String word : words) {
        context.add(word);
      }
    }
    File file = new File("hw6-WSJ-TRAIN.tags.model");
    try {
      Scanner scn = new Scanner(file);
      while (scn.hasNextLine()) {
        String line = scn.nextLine();
        posTags.add(line.split("\t")[0]);
      }
      scn.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    nGramList = buildNgrams(this.context);
    for (String POS : posTags) {
      for(String ngram : nGramList) {
        questions.add(new NgramQuestion(POS, ngram));
      }
    }
  }

  private List<String> buildNgrams(List<String> corpus) {
    Counter<String, Integer> unigram = new Counter<String, Integer>();
    Counter<String, Integer> bigram = new Counter<String, Integer>();
    Counter<String, Integer> trigram = new Counter<String, Integer>();
    for (int i = 0; i < corpus.size() - 3; i++) {
      String ugram = corpus.get(i);
      String bgram = corpus.get(i) + "_" + corpus.get(i + 1);
      String tgram = corpus.get(i) + "_" + corpus.get(i + 1) + "_" + corpus.get(i + 2);
      unigram.add(ugram);
      bigram.add(bgram);
      trigram.add(tgram);
    }
    String last_bigram = corpus.get(corpus.size() - 2) + "_" + corpus.get(corpus.size() - 1);
    bigram.add(last_bigram);
    unigram.add(corpus.get(corpus.size() - 1));
    unigram.add(corpus.get(corpus.size() - 2));
    List<Map.Entry<String, Integer>> ngrams = new ArrayList<Map.Entry<String, Integer>>();
    ngrams.addAll(unigram.getSortedNgrams().subList(0, resultLimit));
    ngrams.addAll(bigram.getSortedNgrams().subList(0, resultLimit));
    ngrams.addAll(trigram.getSortedNgrams().subList(0, resultLimit));
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

  private class NgramQuestion implements Question {

    private String POSTag;

    private String ngram;

    private Integer n;

    public NgramQuestion(String POSTag, String ngram) {
      this.POSTag = POSTag;
      this.ngram = ngram;
      this.n = ngram.split("_").length;
    }

    @Override
    public boolean askQuestion(List<String> history, Integer currentPOSIndex) {
      if (history.get(currentPOSIndex) != POSTag) {
        return false;
      } else if (currentPOSIndex < n) {
        return false;
      } else {
        List<String> prev = history.subList(currentPOSIndex - n, currentPOSIndex - 1);
        String joinedString = prev.stream().map(i -> i.toString()).collect(Collectors.joining("_"));
        return ngram == joinedString;
      }
    }

    @Override
    public String getDescription() {
      return "Given the previous " + n + " elements are " + ngram + ", is the next tag " + POSTag + "?";
    }

  }

  @Override
  public List<Question> getQuestions() {
    return questions;
  }

}
