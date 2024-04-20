package author.service.service;

public record BookInfo(String firstName, String lastName, String title) {
  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof BookInfo)) return false;
    BookInfo that = (BookInfo) obj;
    if (that.firstName.equals(this.firstName) && that.lastName.equals(this.lastName) && that.title.equals(this.title)) return true;
    return false;
  }
}
