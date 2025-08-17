package ch05;

import static org.assertj.core.api.Assertions.*;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class MailTest {

  @Property
  void bodyのテスト(@ForAll("firstName") String firstName) {
    assertThat(new Mail(firstName).body()).isEqualTo("Happy birthday, dear " + firstName);
  }

  @Provide
  Arbitrary<String> firstName() {
    return Arbitraries.strings().alpha().ofMinLength(1);
  }
}
