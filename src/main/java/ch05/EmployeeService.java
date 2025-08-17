package ch05;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class EmployeeService {
  public List<Employee> filterBirthDay(List<Employee> employees, LocalDate today) {
    return employees.stream().filter(employee -> isBirthday(employee.dayOfBirth(), today)).toList();
  }
  
  private boolean isBirthday(LocalDate dayOfBirth, LocalDate today) {
    // うるう年ではない、かつ、2月28日の場合、2月29日生まれの人も誕生日とする
    if (!today.isLeapYear() && today.getMonth() == Month.FEBRUARY && today.getDayOfMonth() == 28) {
      return dayOfBirth.getMonth() == Month.FEBRUARY && (dayOfBirth.getDayOfMonth() == 28 || dayOfBirth.getDayOfMonth() == 29);
    } else {
      return dayOfBirth.getMonth() == today.getMonth() && dayOfBirth.getDayOfMonth() == today.getDayOfMonth();
    }    
  }
}
