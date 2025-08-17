
package ch06;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

class CheckoutTest {

  @Property
  void test(@ForAll("inputs") Pair<List<Item>, List<String>> inputs) {
    List<Item> items = inputs.getLeft();
    List<String> names = inputs.getRight();

    int sum = 0;
    for (Item item : items) {
      int count = (int) names.stream().filter(name -> name.equals(item.name())).count();

      if (item.specialCount() > 0 && item.specialCount() <= count) {
        sum += item.price() * (count % item.specialCount())
            + item.specialPrice() * (count / item.specialCount());
      } else {
        sum += item.price() * count;
      }
    }

    assertThat(new Checkout().total(items, names)).isEqualTo(sum);
  }

  @Provide
  Arbitrary<Pair<List<Item>, List<String>>> inputs() {
    Arbitrary<String> nameArb = Arbitraries.strings().alpha().ofLength(10);
    Arbitrary<Integer> priceArb = Arbitraries.integers().greaterOrEqual(1).lessOrEqual(100);
    Arbitrary<Integer> specialCountArb = Arbitraries.integers().greaterOrEqual(0).lessOrEqual(10);
    Arbitrary<Integer> specialPriceArb = Arbitraries.integers().greaterOrEqual(1).lessOrEqual(100);

    Arbitrary<List<Item>> itemsArb =
        Combinators.combine(nameArb, priceArb, specialCountArb, specialPriceArb).as(Item::new)
            .list().ofMinSize(1).uniqueElements(Item::name);
    return itemsArb.flatMap(items -> {
      List<String> availables = items.stream().map(Item::name).toList();
      return Arbitraries.randomValue(random -> availables.get(random.nextInt(availables.size())))
          .list().ofMinSize(1).map(names -> Pair.of(items, names));
    });
  }
}
