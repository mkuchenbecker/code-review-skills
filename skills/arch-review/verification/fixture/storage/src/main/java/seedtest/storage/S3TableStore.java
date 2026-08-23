package seedtest.storage;

import com.fakevendor.s3.S3Client;
import com.fakevendor.s3.S3TimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import seedtest.core.Table;
import seedtest.core.TableName;
import seedtest.core.TableStore;

/** TableStore backed by the vendor object store. */
public final class S3TableStore implements TableStore {

  private final S3Client client;

  public S3TableStore(S3Client client) {
    this.client = client;
  }

  @Override
  public Optional<Table> load(TableName name) {
    try {
      byte[] blob = client.get(name.database() + "/" + name.table());
      return Optional.of(decode(name, blob));
    } catch (S3TimeoutException e) {
      return Optional.empty();
    }
  }

  @Override
  public void save(Table table) {
    client.put(table.database() + "/" + table.name(), encode(table));
  }

  private static Table decode(TableName name, byte[] blob) {
    long size = Long.parseLong(new String(blob, StandardCharsets.UTF_8).trim());
    return new Table(name.database(), name.table(), size);
  }

  private static byte[] encode(Table table) {
    return Long.toString(table.sizeBytes()).getBytes(StandardCharsets.UTF_8);
  }
}
