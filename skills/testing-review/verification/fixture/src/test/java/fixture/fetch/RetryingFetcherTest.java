package fixture.fetch;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import org.junit.jupiter.api.Test;

class RetryingFetcherTest {

  // Fault injection: a timeout after the request was sent cannot be produced
  // deterministically against a real transport, so the transport is mocked to
  // throw it. The contract says the outcome is unknown and must not be
  // retried.
  @Test
  void timeoutAfterSendReportsStateUnknownAndDoesNotRetry() throws Exception {
    RetryingFetcher.Transport transport = mock(RetryingFetcher.Transport.class);
    when(transport.get("u")).thenThrow(new SocketTimeoutException("read timed out"));
    RetryingFetcher fetcher = new RetryingFetcher(transport);

    assertEquals(RetryingFetcher.Result.STATE_UNKNOWN, fetcher.fetch("u"));
    verify(transport, times(1)).get("u");
  }

  // Fault injection: connection refused means the request never left, and the
  // contract says the fetcher retries up to two times before FAILED.
  @Test
  void connectRefusedRetriesTwiceThenFails() throws Exception {
    RetryingFetcher.Transport transport = mock(RetryingFetcher.Transport.class);
    when(transport.get("u")).thenThrow(new ConnectException("refused"));
    RetryingFetcher fetcher = new RetryingFetcher(transport);

    assertEquals(RetryingFetcher.Result.FAILED, fetcher.fetch("u"));
    verify(transport, times(3)).get("u");
  }

  // PIN: the contract defines the ConnectException and SocketTimeoutException
  // channels and says nothing about any other IOException. This records what
  // the code does today, so a change is noticed; a failure here means behavior
  // changed, not that a promise broke.
  @Test
  void pinOtherIoExceptionCurrentlyReportsFailed() throws Exception {
    RetryingFetcher.Transport transport = mock(RetryingFetcher.Transport.class);
    when(transport.get("u")).thenThrow(new java.io.IOException("disk full"));
    assertEquals(RetryingFetcher.Result.FAILED, new RetryingFetcher(transport).fetch("u"));
  }
}
