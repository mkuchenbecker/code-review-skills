---
name: testing-review
description: Testing skill with two modes. Evaluate mode reviews the tests, and the tested-ness of code, changed in a diff, PR, branch, or module, and ends in a verdict with named actions. Write mode plans the tests for a change and ends in a concrete test list with level, type, and oracle per claim. Both modes derive the tests that should exist, find the tests that do exist, and diff the two sets, judging against PRINCIPLES.md with the catalog in TEST-TYPES.md. Use this whenever the user asks to review tests, judge coverage or test quality, investigate flaky tests, or decide what tests to write for a change, in any language.
---

# Testing review

A test is a question that can prove a claim wrong; findings are the suite's
product; a green run reports only that old answers still hold. This skill
turns that into decisions. It derives the set of tests that should exist,
finds the set that does exist, and diffs them; every step ends in a choice
from a closed set, and every finding carries the action it demands.

## Target

Any codebase. The principles are language-agnostic, and the verification
fixture that exercises them is Java. The unit of work is a body of code
however selected: the current diff (the default), a PR number, a branch, or a
module path.

## Phase 0, both modes: build the two maps

Good tests require understanding both the contract and the control flow,
because they answer different questions and neither substitutes for the other.

| Map | Built from | It decides |
|---|---|---|
| Contract inventory | Governing specifications, explicit requirements, approved contracts and designs, then the definitions the change touches | What is actually promised, and therefore what tests assert |
| Control-flow map | The code: branch points, error channels, state transitions, concurrency points, and where paths provably converge | Which cases exist (one per reachable partition), which cells are vacuous (a stated convergence proof), and each case's home level |

One discipline keeps the flow map from corrupting the tests: the flow map
chooses inputs and placement, never assertions. An assertion derived from the
flow pins the implementation, and only the pin label makes that legitimate.
Coverage is the empirical check that the two maps agree: a branch no test
traverses is either a partition the contract questions missed, or a path the
contract never needed, meaning dead code or an error branch that should have
been an assert on an unrepresentable state.

The source order matters. Changed signatures, documentation, tests, and existing
behavior are proposed or observed behavior. They do not ratify themselves when they
conflict with a governing specification, explicit requirement, approved contract,
or reviewer charter. Repository precedent can reveal compatibility constraints, but
it cannot prove the changed behavior is correct.

## Evaluate mode

The output is a verdict (ship, ship after named tests, or the instrument
lies) built on an explicit two-set diff.

1. **Derive the SHOULD set.** From the two maps, enumerate the tests that
   should exist: each gating claim crossed with each reachable partition,
   each row carrying a type from TEST-TYPES.md, a level, and an oracle. A
   claim gates shipping if its falsification would change the ship decision;
   non-gating claims produce no findings but remain in the disposition ledger with
   the reason they do not gate. The SHOULD set is a concrete table in the report:
   it is the answer key the rest of the review grades against.
2. **Find the DOES set.** Search the whole suite, not only the diff, for
   tests attacking the touched claims: references to the changed symbols,
   fixtures over the changed surface, case ids in generated matrices. A claim
   covered by an existing test elsewhere is covered; demanding it again is
   the review failing its own inventory.
3. **Diff the sets, three ways.**

   | Population | Meaning | Decision |
   |---|---|---|
   | SHOULD minus DOES | Gaps | Write the named test; the SHOULD row already specifies it |
   | DOES minus SHOULD | Tests attacking no claim | Relabel as a pin when it records undefined behavior; delete when it records nothing; or the SHOULD map missed a claim, a step 1 defect fixed in the open |
   | Intersection | Claim met by a test | Judge the instrument: could it fail; cheapest falsifying level; strongest free oracle. Keep, fix, move, or strengthen |

4. **Deliver the verdict.** Ship; or ship after the named tests; or the
   instrument lies (vacuous passes, retried flakes, order dependence), which
   blocks regardless of coverage because a lying instrument voids every
   green. The report's centerpiece is the map itself, one row per claim and
   partition: the SHOULD entry, the DOES entry found, and the verdict.
   Findings rank by the defect that would ship uncaught.

## Write mode

The output is the SHOULD map with each row marked "write" or "already covered
by X", plus the open decisions surfaced for a human.

1. **Enumerate the claims** from the governing sources and approved definitions,
   never from the implementation as written. Only new or altered claims need new
   questions; a discovered gap
   in old claims is surfaced separately, not silently absorbed.
2. **Run the DOES search first**, so no test is authored that already exists.
3. **Push each claim down the ladder before writing anything.** Stop at the
   first level that can falsify it: a type that makes the violation
   unrepresentable ends the story with no test; an assert deletes an
   untestable branch; a pure function owns an algorithm claim. Claims that
   survive the descent earn runtime tests at the level where they stopped.
   This decision controls all cost, and most candidate tests should die here.
4. **Choose each case set from both maps.** Cases enumerate from the claim,
   one per reachable partition; axes cross only where an independence claim
   exists to attack; vacuous cells are pruned with the convergence argument
   stated in the open.
5. **Choose each oracle by taking the strongest that is free.** An
   independence claim makes a differential oracle free; an unstatable
   absolute makes the delta relation the default; an invariant makes a
   property; hand-written examples are the floor.
6. **Choose the doubles, then close.** Real dependency wherever embeddable; a
   mock only for questions reality cannot answer on demand, injected faults
   and forced interleavings, each named as such. Deferred questions are
   written down and surfaced, never skipped silently. Exit check: make each
   new test fail once; a test never seen red is an uncalibrated instrument.

## Test types

TEST-TYPES.md is the catalog both modes choose from. Every demanded or
planned test names its type, so "add a test" is never the finding; "add a
fault-injection test at the port for the state-unknown timeout" is.

## Findings contract

Each finding carries the fields in this table; the decision field makes the
report a set of resolved choices rather than observations. Both modes write
their report per the writing rules at
https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md; if that
reference is unreachable, proceed without it, since it is a style dependency
and not a correctness one.

| Field | Content |
|---|---|
| location | File and line, or test id for generated cases |
| principle | The principle from PRINCIPLES.md, by name; `none (internal consistency)` when no principle covers a true defect |
| claim | One sentence stating the defect |
| evidence | The quoted test or code supporting the claim |
| failure scenario | The defect that would ship uncaught, or the false confidence created |
| decision | The action taken or demanded: the named test to write (type, level, oracle), or keep, fix, move, relabel, delete |
| severity | `blocker`: a real defect class passes silently, or the suite lies. `suggestion`: a gating claim weakly attacked. `nit`: polish |
| confidence | `confirmed`, `probable`, or `speculative` |
| reviewer | `testing-review` |

Both modes also emit a disposition ledger for every candidate claim, partition,
test concern, or case that is accepted, pruned, exempted, waived, tabled,
delegated, or classified as non-gating. Each row includes the location or claim id,
the proposed concern, the disposition, the convergence or contract evidence, and
the governing reason. Existing tests or existing behavior alone cannot justify a
non-finding disposition.

## Posture

- Report everything true, with evidence. A low-confidence finding is reported
  with its confidence marked `probable` or `speculative`, not suppressed.
- Treat existing tests and implementation patterns as observations. They establish
  coverage and compatibility facts, not the correctness of the contract they encode.
- Coverage findings name the missed partition or the dead path, never a
  percentage. No numeric coverage target is used or recommended.
- Anti-noise commitments (violating any is itself a defective review):
  - Never flag duplication that keeps each test readable on its own; that
    duplication is load-bearing.
  - Never demand a test above its claim's cheapest falsifying level, and
    never demand the same claim tested at a second level.
  - Never demand a pin become a contract; demand only that pins are labeled,
    so their failures read as "behavior changed", not "promise broke".
  - Never flag a mock that exists for fault injection or a forced
    interleaving; those are the legitimate mocks.
