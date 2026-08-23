package seedtest.core;

/** Internal helper computing the effective size of a table. */
final class SizeParser {
  long effectiveSize(Table table) {
    return Math.max(0L, table.sizeBytes());
  }
}
