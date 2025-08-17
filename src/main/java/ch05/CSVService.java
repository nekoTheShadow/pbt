package ch05;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CSVService {
  public List<Employee> fromCSV(String csv) throws IOException {
    List<Employee> employees = new ArrayList<>();
    for (CSVRecord csvRecord : CSVParser.parse(csv,
        CSVFormat.RFC4180.builder().setHeader().setSkipHeaderRecord(true).get())) {
      String lastName = csvRecord.get(0).trim();
      String firstName = csvRecord.get(1).trim();
      LocalDate dayOfBirth =
          LocalDate.parse(csvRecord.get(2).trim(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
      String email = csvRecord.get(3).trim();

      Employee employee = new Employee(lastName, firstName, dayOfBirth, email);
      employees.add(employee);
    }
    return employees;
  }
}
