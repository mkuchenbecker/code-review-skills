# Principles

Testing is falsification. A test is a question that can prove a claim wrong; a
suite is worth the claims it can falsify; findings are the product, and green
is the report that old answers still hold. Every principle below derives from
taking that seriously. Each is stated with its reason; findings cite
principles by name. The principles are language-agnostic; the examples that
name a language name Java.

## 0. Two maps before any question

The contract inventory supplies assertions; the control-flow map supplies
input partitions and placement. Neither substitutes for the other: contract
alone clusters cases on one path while a second reachable path ships
untested; flow alone derives assertions from the implementation, which is
circular. The flow map also does the negative work: where paths provably
converge, a cell is vacuous and the convergence argument licenses pruning it
in the open. Coverage is the empirical check that the two maps agree, and a
hole names its own diagnosis: a missed partition, or a path the contract
never needed (dead code, or an error branch that should have been an assert
on an unrepresentable state). The flow map never supplies an assertion; an
assertion from the flow is a pin, and only the label makes it legitimate.

## 1. Claims before tests

A question needs a stated claim to falsify, so every test derives from one.
The sources, strongest first; the source determines what a failure means.

| Claim source | Test kind | A failure means |
|---|---|---|
| Defined behavior (signatures, error contracts, docs) | Contract test | A promise broke |
| Independence assumption ("X shouldn't affect Y") | Crossed axis | The assumption was false |
| Invariant (algebraic property, race freedom, data integrity) | Property, race check, production assert | The invariant does not hold |
| Bare observation (no definition exists) | Pin | Behavior changed; someone should look |

A test derived from the implementation is circular: it verifies that the code
does what the code does, inheriting the blind spots it exists to catch. The
audit runs both ways. Defined behavior with no test is an unverified promise.
A test asserting behavior nothing defines is either a pin mislabeled as a
contract, or the tester authoring a contract nobody committed to.

## 2. The cheapest falsifying level

Every claim has one home: the cheapest level that can falsify it. Placement
above the home wastes the multiplication budget, because case counts multiply
as axes cross; placement below the home tests nothing, because the property
is not present there. The ladder, with what is real at each rung:

| Level | What is real | Claims that live here |
|---|---|---|
| Types | Nothing runs; the compiler is the test | States that cannot be represented |
| Interior | The module's interior only | Algorithms and invariants of pure, total code |
| Contract | The module; dependencies faked at ports | Every enumerated outcome at a surface, failure modes included |
| Composition | Adjacent modules wired together | The boundary translations: total, distinction-preserving |
| System | Everything, embedded and hermetic | The product surface crossed over its axes; interactions; discoveries |
| Production | Reality | Invariants the code relies on: asserts, canaries, reconciliation |

No level re-asks a lower level's questions; duplicated coverage is duplicated
ownership, and when the bug comes, two levels fail and neither names it. The
pyramid's shape (many low, few high) is a consequence of this rule, not a
rule itself. The hierarchy extends into production deliberately: an invariant
assert is a test that runs forever against the only fully real environment.

Tests that live at no level are the common defects: the unit test that mocks
the module's own interior and asserts on the mock; the integration test that
re-derives arithmetic; the end-to-end test asserting an internal call
happened.

## 3. Oracle strength

Where the expected answer comes from is a dimension independent of level, and
each assertion should sit as high on it as its property allows.

| Strength | The expected answer comes from |
|---|---|
| Example | The author wrote it down; only as right as the author was |
| Property | An invariant over generated inputs; explores what the author did not think of |
| Metamorphic relation | A relation between outputs when no absolute is checkable; the delta assertion (assert the change, never the absolute state) is the workhorse, and it is what lets one authored operation compose across arbitrary substrates |
| Differential | A second, independent computation of the same result |

Crossing an axis under a claimed independence manufactures a differential
oracle for free: run the same operations across two file formats, or a stub
and a real service, and each leg becomes the other's reference
implementation. Divergence-zero is a correctness result no hand-written
expectation provides.

## 4. The instrument cannot lie

A vacuous pass is worse than a failure, because it manufactures the
appearance of knowledge and is undetectable from the outside. The suite
defends itself:

- A test proves it ran. Seed steps assert the seed took; negative tests
  assert the exact exception type, and no-throw is a failure; the runner
  fails on zero cases or on everything skipped.
- The harness is stateless even when the subject is stateful. Each case owns
  a fresh world and tears it down; no order dependence, no shared mutable
  fixtures; time and randomness enter through seams. Stateful testing is
  legitimate exactly when the contract is a state machine: sequences derive
  from the defined lifecycle, and the productive crossings follow state
  flows (setup, then a destroyer, then a consumer of what was destroyed).
- A flaky test is a detection that already fired, never a candidate for
  retry. Retry only errors positively identified as transient; a bare
  IOException is not assumed transient; when in doubt, an error is terminal.
- Races get a three-tier response, best first: make the race unrepresentable
  (immutability, single-writer, confinement); force the interleaving
  deterministically at a seam; stress with invariant assertions, never with
  examples, as the last resort.

Coverage belongs to this principle: code no test exercises is a question
never asked, and the code is either untested or dead. Both demand action.
This is also why an assert on an unrepresentable state beats elaborate
handling of a state that cannot occur: the handling is a branch coverage can
never reach, while the assert deletes the branch and marks the remainder as
deliberately unreachable, so the coverage report tells the truth.

## 5. Fakes answer for the wrong system

A question asked of a fake produces knowledge about the fake. Embed the real
dependency wherever it can be embedded; where a fake is unavoidable, it must
be checked against the real thing at some higher level, or it is an
unverified model. A mock is legitimate for exactly the questions reality
cannot be made to answer on demand: injected faults (the timeout after the
request was sent, the mid-stream reset) and forced interleavings. Both are
boundary devices, and both exist to make a production-only failure
deterministic.

## 6. Answered questions become tripwires

A suite is the residue of falsification, and its bookkeeping preserves the
knowledge. Pins flip when behavior changes and are updated, not silenced. A
known product bug is tagged as a skip with its reason, visible in every run,
rather than deleted or retried into passing. No question is dropped silently:
deferring a test is fine, done in the open with the decision surfaced;
pruning coverage silently is the suite-level swallowed exception. Estimates
inflated by vacuous cells are corrected in the open, because the count is a
claim too.

## Symptoms

Concrete patterns worth flagging, with the causing principle; each finding
names the principle, not just the pattern.

| Symptom | Causing principle |
|---|---|
| A case set clustered on one path while a reachable branch has no case through it | 0 |
| A pruned or absent cell with no stated convergence argument | 0, 6 |
| A test asserting on the mock it configured | 4, 5 |
| Interaction verification where an outcome was observable | 1, 5 |
| Shared fixtures, order dependence, wall-clock or unseeded randomness | 4 |
| Sleep-and-retry around a flaky assertion | 4 |
| An end-to-end case asserting a property computable in a pure function | 2 |
| Absolute-state assertions inside composed or parameterized tests | 3 |
| A negative test asserting only that something threw | 4 |
| A rejected-input test presented as a contract with no defining document | 1 |
| A numeric coverage gate with no named unasked questions | 4, 6 |
| Coverage added by calling code without asserting anything about it | 4 |

## The shape the principles produce

In miniature: a harness where each case is a typed pipeline over a declared
schema, composed from a preparation prefix and a headless operation suffix,
so the case set is substrates crossed with operations and each operation is
authored once. Every step asserts a delta against observed pre-state. Each
case owns a fresh table and drops it. Format is a per-case parameter because
format-independence is a hypothesis the suite verifies rather than assumes,
and the crossing doubles as a differential oracle. Known bugs are tagged
skips with reasons. The runner fails on zero cases. The shape is what makes
the suite an instrument: one authored operation asks a question of every
substrate, no case can pass without having run, and a crossed axis answers
its own independence claim.
