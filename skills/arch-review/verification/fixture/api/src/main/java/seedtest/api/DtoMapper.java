package seedtest.api;

import seedtest.core.Table;

/** Maps between the wire representation and the domain representation. */
public final class DtoMapper {

  public static TableDto toDto(Table table) {
    TableDto dto = new TableDto();
    dto.database = table.database();
    dto.name = table.name();
    dto.sizeBytes = table.sizeBytes();
    return dto;
  }

  public static Table fromDto(TableDto dto) {
    return new Table(dto.database, dto.name, dto.sizeBytes);
  }

  private DtoMapper() {}
}
