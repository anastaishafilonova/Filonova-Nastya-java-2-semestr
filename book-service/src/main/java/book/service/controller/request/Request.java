package book.service.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

public class Request {
  public static class RequestToUpdateBook {
    @NotNull(message = "Author can`t be null")
    private String author;

    @NotNull(message = "Author can`t be null")
    private String title;

    public RequestToUpdateBook(String author, String title) {
      this.author = author;
      this.title = title;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }
  }

  public static class RequestToCreateBook {
    @NotNull(message = "Author can`t be null")
    private String author;

    @NotNull(message = "Title can`t be null")
    private String title;

    @NotNull(message = "Tags can`t be null")
    private Set<String> tags;

    public RequestToCreateBook(String author, String title, Set<String> tags) {
      this.author = author;
      this.title = title;
      this.tags = tags;
    }

    public String getAuthor() {
      return author;
    }

    public void setAuthor(String author) {
      this.author = author;
    }

    public String getTitle() {
      return title;
    }

    public void setTitle(String title) {
      this.title = title;
    }

    public Set<String> getTags() {
      return tags;
    }

    public void setTags(Set<String> tags) {
      this.tags = tags;
    }
  }
}
