package seedtest.core;

/** Domain representation of a table. */
public final class Table {
  private final String database;
  private final String name;
  private final long sizeBytes;

  public Table(String database, String name, long sizeBytes) {
    this.database = database;
    this.name = name;
    this.sizeBytes = sizeBytes;
  }

  public String database() {
    return database;
  }

  public String name() {
    return name;
  }

  public long sizeBytes() {
    return sizeBytes;
  }
}
