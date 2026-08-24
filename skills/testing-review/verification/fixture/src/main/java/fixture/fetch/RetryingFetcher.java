package fixture.fetch;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Fetches a document over a transport.
 *
 * Defined behavior:
 * - A ConnectException means the request never left; the fetcher retries up
 *   to two times and then reports FAILED.
 * - A SocketTimeoutException after the request was sent means the outcome is
 *   unknown; the fetcher must NOT retry and reports STATE_UNKNOWN.
 * - A successful fetch reports OK.
 * - Any other IOException is outside this contract. The fetcher reports
 *   FAILED, which may report a genuinely unknown outcome as a known failure;
 *   defining this case is open.
 */
public final class RetryingFetcher {

  public enum Result { OK, FAILED, STATE_UNKNOWN }

  private final Transport transport;

  public RetryingFetcher(Transport transport) {
    this.transport = transport;
  }

  public Result fetch(String url) {
    for (int attempt = 0; attempt < 3; attempt++) {
      try {
        transport.get(url);
        return Result.OK;
      } catch (SocketTimeoutException e) {
        return Result.STATE_UNKNOWN;
      } catch (ConnectException e) {
        // request never left; retry
      } catch (IOException e) {
        return Result.FAILED;
      }
    }
    return Result.FAILED;
  }

  public interface Transport {
    String get(String url) throws IOException;
  }
}
