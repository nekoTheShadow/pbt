package pbt.ch03.q02;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import java.util.stream.IntStream;
import org.apache.commons.lang3.tuple.Pair;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.From;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class SolutionTest {

  @Property
  public void pairKeySortApproach(@ForAll List<@From("pairs") Pair<Integer, String>> pairs) {
    assertThat(isKeyOrdered(Solution.keysort(pairs))).isTrue();
  }

  private <L extends Comparable<L>, R> boolean isKeyOrdered(List<Pair<L, R>> pairs) {
    return IntStream.range(0, pairs.size() - 1)
        .allMatch(i -> pairs.get(i).getLeft().compareTo(pairs.get(i + 1).getLeft()) <= 0);
  }

  @Provide
  private Arbitrary<Pair<Integer, String>> pairs() {
    return Combinators.combine(Arbitraries.integers(), Arbitraries.strings()).as(Pair::of);
  }
}
