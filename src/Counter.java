import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Counter<K, V> extends HashMap<K, Integer> {
    private static final long serialVersionUID = 1L;

    public void add(K k) {
      if (this.containsKey(k)) {
        put(k, get(k) + 1);
      } else {
        put(k, 1);
      }
    }

    public List<Map.Entry<K, Integer>> getSortedNgrams() {
      List<Map.Entry<K, Integer>> list = new ArrayList<Map.Entry<K, Integer>>(entrySet());
      Collections.sort(list, new Comparator<Map.Entry<K, Integer>>() {
        public int compare(Map.Entry<K, Integer> o1, Map.Entry<K, Integer> o2) {
          return o2.getValue().compareTo(o1.getValue());
        }
      });
      return list;
    }
  }