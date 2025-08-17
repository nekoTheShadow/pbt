package ch05;

import static org.assertj.core.api.Assertions.*;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import net.jqwik.api.Example;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;

class EmployeeServiceTest {

  @Property
  void filterBirthDayのテスト(@ForAll(supplier = Employees.class) List<Employee> employees,
      @ForAll LocalDate today) {
    assertThat(new EmployeeService().filterBirthDay(employees, today)).allMatch(employee -> {
      LocalDate dayOfBirth = employee.dayOfBirth();
      if (!today.isLeapYear() && today.getMonth() == Month.FEBRUARY && today.getDayOfMonth() == 28) {
        return dayOfBirth.getMonth() == Month.FEBRUARY
            && (dayOfBirth.getDayOfMonth() == 28 || dayOfBirth.getDayOfMonth() == 29);
      } else {
        return dayOfBirth.getMonth() == today.getMonth()
            && dayOfBirth.getDayOfMonth() == today.getDayOfMonth();
      }
    });
  }

  @Example
  void filterBirthDayのテスト_2月29日生まれの従業員について_うるう年ではない年には2月28日を誕生日とする() {
    List<Employee> employees =
        List.of(new Employee("last01", "first01", LocalDate.of(1982, 10, 8), "xxxxx01@example.com"),
            new Employee("last02", "first02", LocalDate.of(1984, 02, 29), "xxxxx02@example.com"),
            new Employee("last03", "first03", LocalDate.of(2002, 03, 28), "xxxxx03@example.com"),
            new Employee("last04", "first04", LocalDate.of(2008, 02, 29), "xxxxx04@example.com"),
            new Employee("last05", "first05", LocalDate.of(1999, 05, 10), "xxxxx05@example.com"));
    assertThat(new EmployeeService().filterBirthDay(employees, LocalDate.of(2015, 2, 28)))
        .containsExactlyInAnyOrder(
            new Employee("last02", "first02", LocalDate.of(1984, 02, 29), "xxxxx02@example.com"),
            new Employee("last04", "first04", LocalDate.of(2008, 02, 29), "xxxxx04@example.com"));
  }
}
