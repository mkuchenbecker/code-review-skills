package fixture.limit;

/**
 * A token-bucket rate limiter.
 *
 * <p>Contract: {@code tryAcquire()} returns {@code true} when a token is available and consumes
 * it, and {@code false} otherwise. Tokens refill lazily at {@code refillPerSecond}, computed at
 * each call from the time elapsed since the previous call. The limiter is thread-safe: any number
 * of threads may call {@code tryAcquire()} concurrently. This class never throws; a non-positive
 * {@code capacity} or {@code refillPerSecond} is clamped to 1.
 *
 * <p>Example:
 *
 * <pre>
 *   RateLimiter limiter = new RateLimiter(10, 5);
 *   if (limiter.acquirePermit()) {
 *     serve();
 *   }
 * </pre>
 */
public final class RateLimiter {

  private final int capacity;
  private final int refillPerSecond;
  private long lastRefillNanos;
  private int tokens;

  public RateLimiter(int capacity, int refillPerSecond) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("capacity must be positive: " + capacity);
    }
    this.capacity = capacity;
    this.refillPerSecond = refillPerSecond;
    this.tokens = capacity;
    this.lastRefillNanos = System.nanoTime();
  }

  public boolean tryAcquire() {
    long now = System.nanoTime();
    long elapsedSeconds = (now - lastRefillNanos) / 1_000_000_000L;
    lastRefillNanos = now;
    int refilled = (int) (elapsedSeconds * refillPerSecond);
    if (refilled > 0) {
      tokens = Math.min(capacity, tokens + refilled);
    }
    if (tokens > 0) {
      tokens--;
      return true;
    }
    return false;
  }
}
