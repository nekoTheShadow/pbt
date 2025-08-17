package pbt.ch03.q04;

import static org.assertj.core.api.Assertions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.Pair;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class SolutionTest {

  @Property
  public void 辞書のマージ(@ForAll List<@From("pairs") Pair<String, String>> listA,
      @ForAll List<@From("pairs") Pair<String, String>> listB) {

    // 誤り
    // Map<String, String> merged = Solution.merge(toMap(listA), toMap(listB));
    // assertThat(merged.keySet().stream().sorted().toList()).isEqualTo(Stream
    // .concat(listA.stream(), listB.stream()).map(Pair::getLeft).distinct().sorted().toList());

    Map<String, String> actual = Solution.merge(listA, listB);

    List<Pair<String, String>> expected = distinct(listA);
    for (Pair<String, String> pairB : distinct(listB)) {
      if (expected.stream().noneMatch(pairA -> pairA.getKey().equals(pairB.getKey()))) {
        expected.add(pairB);
      }
    }
    Collections.sort(expected);

    assertThat(
        actual.entrySet().stream().map(e -> Pair.of(e.getKey(), e.getValue())).sorted().toList())
            .isEqualTo(expected);
  }

  private List<Pair<String, String>> distinct(List<Pair<String, String>> pairs) {
    List<Pair<String, String>> ans = new ArrayList<>();
    for (Pair<String, String> pair : pairs) {
      IntStream.range(0, ans.size()).filter(i -> ans.get(i).getKey().equals(pair.getKey()))
          .findFirst().ifPresentOrElse(i -> ans.set(i, pair), () -> ans.add(pair));
    }
    return ans;
  }


  @Provide
  private Arbitrary<Pair<String, String>> pairs() {
    return Combinators.combine(Arbitraries.strings(), Arbitraries.strings()).as(Pair::of);
  }
}
