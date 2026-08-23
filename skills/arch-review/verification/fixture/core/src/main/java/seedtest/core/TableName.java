package seedtest.core;

/** A parsed "db.table" identifier. */
public final class TableName {
  private final String database;
  private final String table;

  private TableName(String database, String table) {
    this.database = database;
    this.table = table;
  }

  /** Parses a user-supplied identifier of the form "db.table". */
  public static TableName parse(String raw) {
    String[] parts = raw.split("\\.");
    if (parts.length != 2 || parts[0].isEmpty() || parts[1].isEmpty()) {
      throw new IllegalArgumentException("malformed table name: " + raw);
    }
    return new TableName(parts[0], parts[1]);
  }

  public String database() {
    return database;
  }

  public String table() {
    return table;
  }
}
