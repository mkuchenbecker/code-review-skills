package seedtest.core;

import java.util.Optional;

/** Core-owned storage interface. Implementations live in adapter modules. */
public interface TableStore {
  Optional<Table> load(TableName name);

  void save(Table table);
}
