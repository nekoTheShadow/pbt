package ch05;

import java.time.LocalDate;


public record Employee(String lastName, String firstName, LocalDate dayOfBirth, String email) {
}
