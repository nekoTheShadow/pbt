package pbt.ch03.q05;

import static org.assertj.core.api.Assertions.*;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class SolutionTest {

  @Property
  void wordCounting(@ForAll("sentence") String sentence) {
    long count = sentence.chars().filter(ch -> ch == ' ').count();
    assertThat(Solution.wordCount(sentence)).isEqualTo(count + 1);
  }

  @Provide
  private Arbitrary<String> sentence() {
    return Arbitraries.strings().ofMinLength(1).alpha().numeric().list()
        .map(words -> String.join(" ", words));
  }
}
