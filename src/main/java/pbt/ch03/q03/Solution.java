package pbt.ch03.q03;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Solution {
  public static List<Integer> unionAndSort(Set<Integer> setA, Set<Integer> setB) {
    Set<Integer> setC = new HashSet<>();
    setC.addAll(setA);
    setC.addAll(setB);
    return setC.stream().sorted().toList();
  }
}
