package seedtest.api;

import java.util.Optional;
import java.util.OptionalInt;
import seedtest.core.ErrorCodes;
import seedtest.core.Table;
import seedtest.core.TableName;
import seedtest.core.TableStore;

/** HTTP entry point for table lookups. */
public final class RequestHandler {

  private static final int DEFAULT_LIMIT = 100;

  private final TableStore store;

  public RequestHandler(TableStore store) {
    this.store = store;
  }

  /** Top-level handler invoked by the server for every request. */
  public Response handle(String rawTableName, String rawLimit) {
    try {
      OptionalInt limit = parseLimit(rawLimit);
      if (limit.isEmpty()) {
        return Response.error(400, ErrorCodes.CONFLICT);
      }
      TableName name = TableName.parse(rawTableName);
      Optional<Table> table = store.load(name);
      if (table.isEmpty()) {
        return Response.error(404, ErrorCodes.NOT_FOUND);
      }
      return Response.ok(render(DtoMapper.toDto(table.get()), limit.getAsInt()));
    } catch (Exception e) {
      log(e);
      return Response.error(500, ErrorCodes.STORAGE_DOWN);
    }
  }

  private static OptionalInt parseLimit(String rawLimit) {
    if (rawLimit == null) {
      return OptionalInt.of(DEFAULT_LIMIT);
    }
    try {
      return OptionalInt.of(Integer.parseInt(rawLimit.trim()));
    } catch (NumberFormatException e) {
      return OptionalInt.empty();
    }
  }

  private static String render(TableDto dto, int limit) {
    return dto.database + "." + dto.name + ":" + dto.sizeBytes + " limit=" + limit;
  }

  private static void log(Exception e) {
    System.err.println("request failed: " + e);
  }
}
