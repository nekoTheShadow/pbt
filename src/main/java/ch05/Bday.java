package ch05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

public class Bday {
  public static void main(String[] args) throws IOException {
    String csv = Files.readString(Paths.get("db.csv"));
    List<Employee> allEmployees = new CSVService().fromCSV(csv);
    List<Employee> filteredEmployees = new EmployeeService().filterBirthDay(allEmployees, LocalDate.now());
    for (Employee employee : filteredEmployees) {
      System.out.printf("send birthday email to %s ... %s%n", employee.email(), new Mail(employee.firstName()).body());
    }
  }


  

}
