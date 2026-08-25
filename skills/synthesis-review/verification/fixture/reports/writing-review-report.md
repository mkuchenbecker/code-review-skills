# writing-review findings — RateLimiter

Target: `target/RateLimiter.java` (its class javadoc is the document under review). Findings most
severe first.

## D1 — The usage example calls a method that does not exist

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` line 16 |
| principle | The document stands on its own |
| claim | The javadoc example calls `limiter.acquirePermit()`, but the class's only public method is `tryAcquire()`; a reader who copies the example gets code that does not compile. |
| evidence | Line 16: `if (limiter.acquirePermit()) {` — no such method is declared anywhere in the class. |
| failure scenario | The first thing every new caller copies is broken, and the fix they guess at may not be the method the author intended. |
| fix | Change the example to `limiter.tryAcquire()`. |
| severity | suggestion |
| confidence | confirmed |
| reviewer | `writing-review` |

## D2 — The no-throw sentence is wrong and should be corrected to state the actual behavior

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 9-10; behavior at line 30 |
| principle | State the present |
| claim | The sentence "This class never throws; a non-positive capacity or refillPerSecond is clamped to 1" describes behavior the code does not have; the document should be corrected to state what the code does: the constructor throws `IllegalArgumentException` for non-positive capacity, and `refillPerSecond` is accepted unvalidated. |
| evidence | Line 30 throws; no clamping code exists. |
| failure scenario | A reader designs error handling around clamping that never happens. |
| fix | Rewrite the sentence: delete the never-throws claim, document the `IllegalArgumentException`, and state that `refillPerSecond` is not validated. |
| severity | blocker |
| confidence | confirmed |
| reviewer | `writing-review` |

## D3 — The contract paragraph buries the thread-safety statement

| Field | Content |
|---|---|
| location | `target/RateLimiter.java` lines 6-10 |
| principle | Conclusion on top |
| claim | The thread-safety statement, the strongest promise in the document, sits mid-paragraph after the refill mechanics; promises should lead the paragraph. |
| evidence | Lines 6-10: return contract, then refill, then thread-safety, then the throw clause. |
| failure scenario | None beyond scanning cost. |
| fix | Reorder: guarantees first, mechanics second. |
| severity | nit |
| confidence | confirmed |
| reviewer | `writing-review` |
