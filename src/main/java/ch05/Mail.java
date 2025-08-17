package ch05;

public record Mail(String firstName) {
  public String body() {
    return "Happy birthday, dear %s".formatted(firstName);
  }
}
