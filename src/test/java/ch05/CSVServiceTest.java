package ch05;

import static org.assertj.core.api.Assertions.*;
import java.io.IOException;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

class CSVServiceTest {
  @Property
  public void fromCSVのテスト(@ForAll(supplier = Employees.class) List<Employee> employees) {
    StringWriter writer = new StringWriter();
    try (CSVPrinter printer = new CSVPrinter(writer, CSVFormat.RFC4180)) {
      printer.printRecord("last_name", "first_name", "day_of_birth", "email");
      for (Employee employee : employees) {
        printer.printRecord(employee.lastName(), employee.firstName(), DateTimeFormatter.ofPattern("yyyy/MM/dd").format(employee.dayOfBirth()), employee.email());
      }
      assertThat(new CSVService().fromCSV(writer.toString())).isEqualTo(employees);
    } catch (IOException e) {
      fail(e);
    }
  }
}
