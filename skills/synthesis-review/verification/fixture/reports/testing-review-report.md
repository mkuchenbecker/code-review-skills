# testing-review findings — RateLimiter

Target: `target/RateLimiter.java`. Findings most severe first.

## T1 — Callers polling faster than once per second are starved forever, silently

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 40-42; documented refill at lines 7-8 |
| principle | Claims before tests; the instrument cannot lie |
| claim | `lastRefillNanos` is advanced unconditionally on every call (line 41), before the refill amount is computed from whole elapsed seconds (line 40), so any caller that polls more often than once per second always observes `elapsedSeconds == 0`, refills zero tokens, and — once the initial burst is spent — is starved forever. The documented contract ("tokens refill lazily at refillPerSecond, computed at each call from the time elapsed since the previous call") is not honored, and no test attacks the refill claim at sub-second call spacing. |
| evidence | Line 40: `long elapsedSeconds = (now - lastRefillNanos) / 1_000_000_000L;` line 41: `lastRefillNanos = now;` — the advance is outside the `refilled > 0` guard on line 43. |
| failure scenario | A service calling `tryAcquire()` every 100 ms drains the initial `capacity` tokens and then receives `false` on every call for the life of the process, while the class reports nothing wrong. The wrongness is silent: no exception, no log, plausible-looking early behavior. |
| fix | Advance `lastRefillNanos` only when tokens are credited (move line 41 inside the `refilled > 0` branch), or account fractional elapsed time; add a contract test at sub-second spacing (example oracle: 5 permits/sec, poll every 100 ms for 2 s, assert ≈10 grants after the burst). |
| severity | blocker |
| confidence | confirmed |
| reviewer | `testing-review` |

## T2 — The thread-safety claim has no attacking test and the code is visibly unsynchronized

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 8-9, 38-51 |
| principle | Claims before tests |
| claim | The javadoc's thread-safety sentence (lines 8-9) is a documented claim with no test attacking it, and the implementation it describes is plain unsynchronized mutable state, so the claim is both unverified and false as written. |
| evidence | Lines 8-9 state the claim; `tryAcquire` mutates `tokens` and `lastRefillNanos` (lines 38-51) with no synchronization. |
| failure scenario | Over-issued permits under concurrent load, discovered in production because no test ever asked the question. |
| fix | Decide the claim: either make the class thread-safe and add a forced-interleaving or stress test with an invariant assertion (never more than `capacity` grants per refill window), or delete the sentence and document single-threaded use. |
| severity | blocker |
| confidence | confirmed |
| reviewer | `testing-review` |

## T3 — tryAcquire never returns false for a zero-capacity limiter

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` line 30 |
| principle | Claims before tests |
| claim | For `capacity = 0` the token count starts at zero, yet `tryAcquire` still returns `true` on the first call because the initial-burst branch does not check the configured capacity. |
| evidence | Line 30: `return false;` — reachable only after the burst is spent, so the zero-capacity case never reaches it on the first call. |
| failure scenario | A limiter configured to admit nothing admits one request. |
| fix | Add a contract test for `capacity = 0` asserting the first call returns `false`. |
| severity | suggestion |
| confidence | probable |
| reviewer | `testing-review` |
