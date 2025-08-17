package ch06;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Checkout {
  public int total(List<Item> items, List<String> names) {
    int sum = 0;
    
    Map<String, Item> dict = items.stream().collect(Collectors.toMap(Item::name, Function.identity()));
    Map<String, Integer> counter = names.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.collectingAndThen(Collectors.counting(), Long::intValue)));
    for (Entry<String, Integer> e : counter.entrySet()) {
      String name = e.getKey();
      int count = e.getValue();
      
      Item item = dict.get(name);
      if (item.specialCount() == 0) {
        sum += item.price() * count;
      } else {
        sum += item.specialPrice() * (count / item.specialCount());
        sum += item.price() * (count % item.specialCount());
      }
    }
    return sum;
  }
}
