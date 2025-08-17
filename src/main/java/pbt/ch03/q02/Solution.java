package pbt.ch03.q02;

import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;

public class Solution {
  public static <L extends Comparable<L>, R> List<Pair<L, R>> keysort(List<Pair<L, R>> pairs) {
    return pairs.stream().sorted(Comparator.comparing(pair -> pair.getLeft())).toList();
  }
}
