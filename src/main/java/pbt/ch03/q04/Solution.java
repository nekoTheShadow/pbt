package pbt.ch03.q04;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;

public class Solution {
  public static Map<String, String> merge(List<Pair<String, String>> list1,
      List<Pair<String, String>> list2) {

    Map<String, String> map1 = list1.stream()
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (oldV, newV) -> newV));
    Map<String, String> map2 = list2.stream()
        .collect(Collectors.toMap(Pair::getKey, Pair::getValue, (oldV, newV) -> newV));
    
   Map<String, String> merged = new HashMap<>(map1);
   map2.entrySet().stream().forEach(e -> merged.merge(e.getKey(), e.getValue(), (oldV, newV) -> oldV));
   return merged;
  }
}
