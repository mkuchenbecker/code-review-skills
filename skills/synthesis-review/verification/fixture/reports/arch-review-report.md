# arch-review findings — RateLimiter

Target: `target/RateLimiter.java`. Findings most severe first.

## A1 — Constructor contradicts the documented no-throw contract

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 28-36; contract at lines 9-10 |
| principle | Defined behavior for bad input |
| claim | The javadoc promises "This class never throws; a non-positive capacity or refillPerSecond is clamped to 1", but the constructor throws `IllegalArgumentException` for non-positive capacity and neither validates nor clamps `refillPerSecond`. |
| evidence | Line 30: `throw new IllegalArgumentException("capacity must be positive: " + capacity);` — inside the `capacity <= 0` branch; no branch examines `refillPerSecond`. |
| failure scenario | A caller that trusts the documented clamping passes 0 and crashes; a caller passing `refillPerSecond = 0` gets a limiter that never refills, with no documented or thrown signal. |
| fix | Make the code honor the documented contract: clamp both parameters to 1 and delete the throw. |
| severity | blocker |
| confidence | confirmed |
| reviewer | `arch-review` |

## A2 — Documented thread-safety with unsynchronized mutable state

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 24-25, 38-51; claim at lines 8-9 |
| principle | Defined behavior; owned vocabulary |
| claim | The javadoc states "The limiter is thread-safe: any number of threads may call tryAcquire() concurrently", but `lastRefillNanos` and `tokens` are plain mutable fields read and written with no synchronization, no volatile, and no atomics. |
| evidence | Lines 24-25 declare `private long lastRefillNanos; private int tokens;`; `tryAcquire` (lines 38-51) performs read-modify-write on both. |
| failure scenario | Two threads pass the `tokens > 0` check together and both decrement; the bucket goes negative or over-issues permits; long tearing on `lastRefillNanos` under 32-bit JVMs compounds it. |
| fix | Synchronize `tryAcquire`, or use `AtomicLong`/`AtomicInteger` with a CAS loop. |
| severity | blocker |
| confidence | confirmed |
| reviewer | `arch-review` |

## A3 — The limiter should be decomposed for extensibility

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 20-52 |
| principle | Narrow contracts; one seam |
| claim | Time acquisition, refill policy, and token accounting are fused in one class; the class should be restructured into a `RateLimiter` interface, an injected `Clock` seam, and a refill-policy strategy so alternative policies (sliding window, leaky bucket) and deterministic tests become possible. |
| evidence | `System.nanoTime()` called directly at lines 35 and 39; refill arithmetic inline at lines 40-45. |
| failure scenario | Every future policy variant is a fork of this class, and no test can control time. |
| fix | Extract the interface, inject a clock, move refill math behind a strategy. |
| severity | suggestion |
| confidence | confirmed |
| reviewer | `arch-review` |

## A4 — Field declaration order mixes configuration and state

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 22-25 |
| principle | none (internal consistency) |
| claim | The two immutable configuration fields and the two mutable state fields are interleaved with no grouping comment, which reads as four unrelated fields. |
| evidence | Lines 22-25. |
| failure scenario | None; readability only. |
| fix | Group config above state, or add a one-line comment separating them. |
| severity | nit |
| confidence | confirmed |
| reviewer | `arch-review` |
