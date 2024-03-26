package book.service.controller.request;

import book.service.entity.Author;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class Request {
  public static record RequestToUpdateBook(String title) {}

  public static class RequestToCreateBook {
    @NotNull(message = "Author`s firstName can`t be null")
    private String firstName;

    @NotNull(message = "Author`s lastName can`t be null")
    private String lastName;

    @NotNull(message = "Title can`t be null")
    private String title;

    public RequestToCreateBook(String firstName, String lastName, String title) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.title = title;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public String getTitle() {
      return title;
    }
  }

  public static class RequestToCreateOrUpdateAuthor {
    private String firstName;
    private String lastName;

    public RequestToCreateOrUpdateAuthor(String firstName, String lastName) {
      this.firstName = firstName;
      this.lastName = lastName;
    }

    public String getFirstName() {
      return firstName;
    }

    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
  }

  public static class RequestToCreateTag {
    private String text;
    private Long bookId;

    public RequestToCreateTag(String text, Long bookId) {
      this.text = text;
      this.bookId = bookId;
    }

    public String getText() {
      return text;
    }

    public Long getBookId() {
      return bookId;
    }
  }

  public record RequestToUpdateTag(String name) {}
}
