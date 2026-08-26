# Source audit

This skill adapts the MIT-licensed [Wondel.ai Clean Code skill](https://github.com/wondelai/skills/tree/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code) at commit [`6bac1534f9f256a56fc2b4dd0e70b9a692758966`](https://github.com/wondelai/skills/commit/6bac1534f9f256a56fc2b4dd0e70b9a692758966). The source is material to audit, not authority.

Dispositions:

| Disposition | Meaning |
|---|---|
| Retain | The useful rule survives with narrower wording and repository-specific guardrails. |
| Rewrite | The topic survives, but the original rule or rationale conflicts with this skill. |
| Delegate | Another reviewer in this repository owns the question. |
| Reject | The rule is excluded because it is mechanical, unverifiable, out of scope, or contrary to the governing principles. |

## `clean-code/SKILL.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/SKILL.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| Core principle | Rewrite | Preserve reader-centered code, but remove the unsupported read-to-write ratio, prose metaphor, and Boy Scout mandate. Use concrete catalog rules. |
| Scoring | Reject | No numeric score or target. Findings are concrete violations with severity and confidence. |
| Meaningful names | Retain | `PL-NAME-001` and `PL-NAME-002` preserve intent, domain vocabulary, searchability, pronunciation, and consistent concepts. |
| Functions | Rewrite | `PL-FLOW-*` and `PL-COHESION-*` prefer composition and semantic cohesion without line, helper, nesting, or argument-count targets. |
| Comments and formatting | Rewrite | `PL-COMMENT-*` keeps present-tense invariant and rationale comments. Repository formatters own whitespace. |
| Error handling | Rewrite | `PL-FAIL-*` requires checked or typed expected failure and explicitly flags `RuntimeException` hierarchies. |
| Unit testing | Delegate | `testing-review` owns tested-ness and strategy. This linter applies ordinary readability rules to changed test code. |
| Code smells and heuristics | Rewrite | Concrete smells survive only with evidence, guardrails, and a smallest local fix in `CATALOG.md`. |
| Common mistakes | Rewrite | Names, broad catches, magic values, comments, and type leaks map to catalog rules. Coverage, test requirements, and blanket refactoring claims are delegated or rejected. |
| Quick diagnostic | Reject | Mechanical questions such as function length, test count, duplication count, and suite duration are not review rules. |
| Further reading | Reject | Bibliography does not define this skill's contract. |
| About the author | Reject | Biographical material is unrelated to the review procedure. |

## `references/naming-conventions.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/naming-conventions.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| Golden rule | Retain | Names state purpose, domain role, unit, and effect under `PL-NAME-001`. |
| Intention-revealing names | Retain | Preserve with repository vocabulary and evidence at the changed location. |
| Avoiding disinformation | Retain | `PL-NAME-002` covers names that lie about type, mutability, effect, or abstraction. |
| Meaningful distinctions | Retain | Noise suffixes and numbered names are findings when the distinction is unclear. |
| Pronounceable names | Retain | Included in `PL-NAME-001`. |
| Searchable names | Rewrite | Preserve searchability without prescribing name length by scope. |
| Constants must be named | Rewrite | `PL-MAGIC-001` applies only to domain and protocol meaning, not every literal. |
| Class names and method names | Rewrite | Follow repository and language conventions; report semantic lies rather than noun-versus-verb formalism alone. |
| Language naming conventions | Delegate | The target repository's formatter and linter own syntax and formatting. Naming conventions are evidence only and do not excuse a misleading semantic name. |
| One word per concept | Retain | `PL-NAME-002` requires one term unless different terms carry different semantics. |
| Solution and problem domain names | Retain | Prefer owned domain vocabulary, with standard technical terms where they are the actual abstraction. |
| Common anti-patterns | Retain | Abbreviations, encodings, mental mapping, and meaningless distinctions map to `PL-NAME-*`. |
| Renaming checklist | Rewrite | Use the evidence and guardrails in `PL-NAME-*`; do not turn the checklist into seven findings for one name. |

## `references/functions-and-methods.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/functions-and-methods.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| First rule of functions | Reject | No function-size, screen-size, argument-count, side-effect, or abstraction checklist applied mechanically. |
| Do one thing | Rewrite | `PL-COHESION-001` asks whether decisions change for different reasons, not whether another helper can be extracted. |
| Function arguments | Rewrite | `PL-CONTRACT-002` judges semantic grouping and call-site clarity without numeric limits. |
| Flag arguments | Rewrite | Flags are findings when they hide modes or force caller knowledge; a clear domain boolean is not automatically wrong. |
| Command-query separation | Retain | Hidden effects are covered by `PL-EFFECT-002`. |
| Side effects | Retain | Effects must be visible in names and contracts. |
| Extract till you drop | Reject | Single-use helper fragmentation is itself reportable under `PL-FLOW-004`. |
| Structured programming | Rewrite | Guard clauses and early returns are tools. `PL-FLOW-005` flags them only when they fragment a composable interior operation. |
| DRY | Rewrite | `PL-DUP-001` flags duplicate knowledge. `PL-DUP-002` protects intentional boundary duplication. |
| Function organization | Rewrite | Keep related code readable together without prescribing newspaper ordering or field placement. |
| Common function anti-patterns | Rewrite | Output mutation, selector modes, temporal coupling, and leaks map to contracts, effects, flow, or boundaries. Switches and helpers are not violations by shape alone. |

## `references/comments-formatting.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/comments-formatting.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| Truth about comments | Rewrite | Comments are judged by accuracy and value, not treated as an inherent failure. |
| Legal comments | Retain | Required headers are exempt under `PL-COMMENT-003`. |
| Informative comments | Rewrite | Keep comments for non-obvious formats or external facts when a type or name cannot carry them. |
| Explanation of intent | Retain | `PL-COMMENT-002` covers present rationale and invariants. |
| Warning of consequences | Retain | Keep when it describes a real non-obvious effect or constraint. |
| TODO comments | Rewrite | A TODO must name concrete unfinished work and follow repository policy; ticket syntax is repository-specific. |
| Amplification | Rewrite | Prefer a positive invariant or named type; retain a comment only when removal would hide a correctness constraint. |
| Mumbling, redundant, misleading, mandated, journal, noise, attribution comments | Retain | These map to `PL-COMMENT-001` and `PL-COMMENT-003`. |
| Commented-out code | Retain | `PL-COMMENT-003` requires deletion. |
| Position markers and closing-brace comments | Rewrite | Report when they compensate for unreadable changed structure, not merely because the syntax exists. |
| Vertical and horizontal formatting | Delegate | The target repository's formatter and linter own whitespace, line length, indentation, and ordering. |
| Team rules and automation | Delegate | Repository tooling, not this line reviewer, owns formatting policy. |
| When comments are necessary | Retain | External constraints, concurrency, performance, protocol behavior, formulas, and public contracts are valid reasons under `PL-COMMENT-002`. |

## `references/error-handling.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/error-handling.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| Core problem | Retain | Failure handling should not obscure the operation, but the fix is an explicit contract and boundary. |
| Use exceptions, not return codes | Rewrite | Use a declared checked exception or typed result. Exceptions are not automatically cleaner than values. |
| Write try-catch-finally first | Reject | Starting from syntax does not define the semantic contract and encourages catch-driven design. |
| Use unchecked exceptions | Rewrite | `PL-FAIL-001` explicitly reverses the advice and flags `RuntimeException` and every subclass. |
| Provide context with exceptions | Retain | Checked failures preserve operation context, identifiers, and the original cause without leaking secrets. |
| Define exception classes for caller needs | Rewrite | Module-owned checked failures or result variants express semantic outcomes without mirroring vendor types. |
| Do not return null | Retain | `PL-NULL-*` converts external null and forbids internal nullable contracts. |
| Do not pass null | Retain | Project-owned internal parameters are required or explicit options. |
| Special Case and Null Object | Rewrite | Use only when the object has honest domain behavior. Do not manufacture default behavior to erase meaningful absence. |
| Empty string over null | Reject | Empty and absent often mean different things. Use an explicit absence type. |
| Error handling patterns summary | Rewrite | Keep boundary translation, context, exact catches, and explicit absence. Reject unchecked hierarchy and generic guard-clause defaults. |
| Catch-and-ignore, broad catch, log-and-rethrow, sentinel returns | Retain | Covered by `PL-FAIL-005` and `PL-NULL-003`. |
| Return codes | Rewrite | Typed result values are encouraged when they enumerate outcomes. Untyped numeric or boolean codes are not. |
| Nested try-catch and checked declaration cascade | Rewrite | Flag hidden or repeated boundaries. Checked propagation is a contract, not a defect by itself. |

## `references/testing-principles.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/testing-principles.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| Why tests matter | Delegate | `testing-review` owns the testing argument. |
| Three laws of TDD | Reject | TDD sequence is a development choice, not a line-level cleanliness rule. |
| Clean tests | Retain | Changed test code must remain readable and explicit under the ordinary catalog. |
| Build-Operate-Check and Arrange-Act-Assert | Rewrite | These are acceptable shapes, not mandatory structures. Report only actual unreadability. |
| Domain-specific testing language | Rewrite | Helpers must improve local readability and avoid hidden defaults; `PL-FLOW-004` still applies. |
| One concept per test | Delegate | `testing-review` judges claims and partitions. No one-assert or one-concept mechanical rule here. |
| F.I.R.S.T. | Delegate | Test speed, independence, repeatability, oracles, and timing belong to `testing-review`. |
| Test naming | Retain | `PL-NAME-*` applies to changed test names. |
| Parameterized tests, fixtures, and builders | Rewrite | Use when they improve clarity. They are not preferred automatically. |
| Testing error paths and boundaries | Delegate | `testing-review` derives required cases from contracts and control flow. |
| Tests as documentation | Delegate | This linter judges code clarity, not test-suite sufficiency. |

## `references/code-smells.md`

Source: [pinned file](https://github.com/wondelai/skills/blob/6bac1534f9f256a56fc2b4dd0e70b9a692758966/clean-code/references/code-smells.md)

| Upstream section | Disposition | Result here |
|---|---|---|
| What is a code smell | Rewrite | A smell starts an audit. It becomes a finding only after evidence and guardrails are checked. |
| Comment smells | Retain | Map to `PL-COMMENT-*`. |
| Environment smells | Delegate | Build and test workflow belong to repository tooling unless the PR directly changes that workflow. |
| Too many arguments | Rewrite | `PL-CONTRACT-002` judges semantic contract quality without a count threshold. |
| Output arguments | Rewrite | Report hidden mutation under `PL-EFFECT-002` or `PL-ENCAPSULATION-001`. |
| Flag arguments | Rewrite | Report hidden mode selection, not every boolean. |
| Dead functions | Retain | `PL-COMPLEXITY-002` covers changed dead code. |
| Multiple languages in one file | Rewrite | Report only when an embedded representation obscures ownership or has a clear adapter boundary. |
| Obvious behavior and boundary behavior | Rewrite | Concrete surprising behavior belongs to a correctness review; hidden contracts map to `PL-CONTRACT-001`. |
| Overridden safeties | Retain | Warning suppression and unchecked casts map to `PL-TYPE-001`. Skipped-test policy belongs to `testing-review`. |
| Duplication | Rewrite | Duplicate knowledge is a defect. Cross-boundary structural duplication can be required for encapsulation. |
| Wrong abstraction level | Retain | `PL-COHESION-002`. |
| Feature envy | Retain | `PL-ENCAPSULATION-001`. |
| Selector arguments | Rewrite | `PL-CONTRACT-002` asks whether the selector is a hidden mode or an honest domain value. |
| Obscured intent | Retain | Names and unnecessary complexity map to `PL-NAME-*` and `PL-COMPLEXITY-001`. |
| Magic numbers | Retain | `PL-MAGIC-001` applies to semantic literals, units, and policy values. |
| Dead code | Retain | `PL-COMPLEXITY-002`. |
| Naming smells | Retain | Map to `PL-NAME-*`. |
| Test smells | Delegate | `testing-review` owns coverage, boundaries, speed, skipped tests, and failure patterns. |
| Smell detection quick reference | Reject | The catalog replaces generic first actions with rule-specific evidence and fixes. |

## Explicit reversals

These upstream positions must never leak back into the skill through examples or summaries:

- Prefer unchecked exceptions.
- Treat `RuntimeException`, `IllegalArgumentException`, or `IllegalStateException` as acceptable expected-failure contracts.
- Wrap checked exceptions in runtime exceptions to fit stream APIs.
- Write try-catch first.
- Prefer exceptions over explicit result values in all cases.
- Extract until functions are tiny.
- Apply function-length, argument-count, nesting-depth, test-count, duplication-count, or suite-duration targets.
- Prefer guard clauses and early returns mechanically.
- Consolidate all duplicated syntax or types.
- Use Null Object, empty string, zero, or another default when absence has distinct meaning.
- Require TDD, one concept per test, tests for every public method, or any skill verification fixture.
- Score code against a ten-point scale.

## Upstream license notice

The adapted source is licensed under the MIT License:

```text
MIT License

Copyright (c) 2025 Wondel.ai sp. z o.o.

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
