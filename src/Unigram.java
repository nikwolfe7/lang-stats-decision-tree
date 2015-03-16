import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Unigram {

  private String txtFile = "unnamed";
  
  private Double avgLogLikelihood;

  private ArrayList<String> corpus;
  
  private List<Map.Entry<String, Double>> model;
  
  public Unigram(List<String> data) {
    this.corpus = new ArrayList<String>(data);
    this.model = buildUnigram(corpus);
    this.avgLogLikelihood = calculateAverageLogLikelihood();
  }

  public Unigram(String filename) {
    this.txtFile = filename;
    File file = new File(txtFile);
    try {
      Scanner scn = new Scanner(file);
      this.corpus = new ArrayList<String>();
      while (scn.hasNextLine()) {
        String line = scn.nextLine();
        corpus.add(line);
      }
      scn.close();
      this.model = buildUnigram(corpus);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    this.avgLogLikelihood = calculateAverageLogLikelihood();
    printModel();
  }
  
  public Double calcPerplexity(List<Map.Entry<String, Double>> testModel) {
    return Math.pow(2.0d, calcCrossEntropy(testModel));
  }
  
  public Double calcCrossEntropy(List<Map.Entry<String, Double>> testModel) {
    Double entropy = 0.0;
    HashMap<String, Double> test = getHashMap(testModel);
    for(Map.Entry<String, Double> entry : model) {
      Double Px = entry.getValue();
      Double Qx = 0.0;
      if(test.containsKey(entry.getKey())) {
        Qx = test.get(entry.getKey());
      }
      Double wordEntropy = Px * log2(Qx);
      entropy += wordEntropy;
    }
    return -entropy;
  }

  public List<String> getCorpus() {
    return corpus;
  }

  public List<Map.Entry<String, Double>> getModel() {
    return model;
  }
  
  public Double getAvgLogLikelihood() {
    return avgLogLikelihood;
  }
  
  public double log2(double x) {
    return Math.log(x) / Math.log(2.0d);
  }
  
  /* Private Interface */
  
  private List<Entry<String, Double>> buildUnigram(List<String> data) {
    HashMap<String, Double> model = new HashMap<String, Double>();
    ArrayList<String> replaceCorpus = new ArrayList<String>();
    int sum = 0;
    for (String line : data) {
      line += " ";
      for (String word : line.split(" ")) {
        replaceCorpus.add(word);
        if (model.containsKey(word)) {
          model.put(word, model.get(word) + 1);
          sum += 1;
        } else {
          model.put(word, 1.0);
          sum += 1;
        }
      }
    }
    for (String key : model.keySet()) {
      model.put(key, model.get(key) / sum);
    }
    List<Map.Entry<String, Double>> sortedMap = new LinkedList<Map.Entry<String, Double>>(model.entrySet());
    Collections.sort(sortedMap, new Comparator<Map.Entry<String, Double>>() {
      public int compare(Entry<String, Double> o1, Entry<String, Double> o2) {
        return o2.getValue().compareTo(o1.getValue());
      }
    });
    this.corpus = replaceCorpus;
    return sortedMap;
  }
  
  private HashMap<String, Double> getHashMap(List<Map.Entry<String, Double>> mapList) {
    HashMap<String, Double> map = new HashMap<String, Double>();
    for(Map.Entry<String, Double> entry : mapList) {
      map.put(entry.getKey(), entry.getValue());
    }
    return map;
  }
  
  private Double calculateAverageLogLikelihood() {
    Double avgLL = 0.0;
    for (Entry<String, Double> entry : model) {
      Double logLikelihood = log2(entry.getValue());
      avgLL += logLikelihood;
    }
    avgLL = avgLL / model.size();
    return avgLL;
  }
  
  private void printModel() {
    try {
      FileWriter writer = new FileWriter(new File(txtFile + ".model"));
      for (Entry<String, Double> entry : model) {
        writer.write(entry.getKey() + "\t" + entry.getValue() + "\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
