package ch05;

import java.time.LocalDate;
import java.util.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ArbitrarySupplier;
import net.jqwik.api.Combinators;
import net.jqwik.time.api.Dates;

public class Employees implements ArbitrarySupplier<List<Employee>> {
  @Override
  public Arbitrary<List<Employee>> get() {
    Arbitrary<String> firstName = Arbitraries.strings().alpha();
    Arbitrary<String> lastName = Arbitraries.strings().alpha();
    Arbitrary<LocalDate> dayOfBirth = Dates.dates();
    Arbitrary<String> email =
        Combinators.combine(Arbitraries.strings().alpha(), Arbitraries.strings().alpha())
            .as((s1, s2) -> s1 + "@" + s2);
    return Combinators.combine(firstName, lastName, dayOfBirth, email).as(Employee::new).list().ofMinSize(1);
  }

}
