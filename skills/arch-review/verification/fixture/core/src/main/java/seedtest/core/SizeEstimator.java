package seedtest.core;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/** Estimates sizes for tables and manifest files. */
public final class SizeEstimator {

  private final SizeParser parser = new SizeParser();

  /** Returns the effective size of a table, or 0 if estimation fails. */
  public long estimate(Table table) {
    try {
      return parser.effectiveSize(table);
    } catch (RuntimeException e) {
      return 0L;
    }
  }

  /** Reads every manifest file's contents. */
  public List<String> readManifests(List<Path> manifestPaths) {
    return manifestPaths.stream()
        .map(
            path -> {
              try {
                return Files.readString(path);
              } catch (IOException e) {
                throw new UncheckedIOException(e);
              }
            })
        .collect(Collectors.toList());
  }
}
