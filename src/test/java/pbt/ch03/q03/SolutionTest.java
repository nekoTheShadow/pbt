package pbt.ch03.q03;

import static org.assertj.core.api.Assertions.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

public class SolutionTest {

  @Property
  public void _セットの和集合(@ForAll List<Integer> listA, @ForAll List<Integer> listB) {
    Set<Integer> setA = new HashSet<>(listA);
    Set<Integer> setB = new HashSet<>(listB);
    
    // 誤り
    // List<Integer> modelUnion = Stream.concat(listA.stream(), listB.stream()).sorted().toList();

    // 正解
    List<Integer> modelUnion =
        Stream.concat(listA.stream(), listB.stream()).distinct().sorted().toList();
    
    assertThat(Solution.unionAndSort(setA, setB)).isEqualTo(modelUnion);
  }
}
