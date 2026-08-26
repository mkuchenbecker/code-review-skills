# code-review-skills

Self-contained code-review skills for Claude Code. Reviewer skills define what they review, how
they judge, and the contract their findings use. `review-orchestrator` composes those reviewers
without replacing their judgment. A skill may depend downward on a published external reference
(see Writing, below) but never on its consumers, so each can be installed and invoked on its own.

## Skills

| Skill | Reviews |
|---|---|
| [`arch-review`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/arch-review/SKILL.md) | Decomposition: module topology, interface contracts, ownership of decisions, and error design. Language-agnostic principles with Java examples. Findings are diagnosed at the structural cause (a boundary, a contract, a seam), not the surface pattern. |
| [`pedantic-linter`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/pedantic-linter/SKILL.md) | Every changed line for concrete cleanliness violations. Audits public signatures, unchecked exceptions, nulls, loops, mutation, effects, boundary types, names, comments, magic values, type safety, and encapsulation. Each violation carries a stable `PL-*` rule identifier and the smallest local fix. |
| [`writing-review`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/writing-review/SKILL.md) | Prose changed in a diff: documentation, design docs, comments, commit messages, PR descriptions. Judges document structure first and sentences second, against the external writing reference; design documents are additionally judged against a prescribed structure. |
| [`testing-review`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/testing-review/SKILL.md) | Tests, and the tested-ness of code. Two modes: evaluate reviews a change's tests and ends in a verdict with named actions; write plans the tests for a change. Both derive the tests that should exist from the contract and the control flow, find the ones that do, and diff the sets. |
| [`synthesis-review`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/synthesis-review/SKILL.md) | Blind-reviewed SME branch packets, into the final feedback. Verifies each completed branch audit against the frozen review bundle, merges duplicates, adjudicates cross-branch disagreements, tiers each survivor as blocking or follow-up against the deployment posture, and converges on the exact tagged comments to post plus an adjudication log. |
| [`review-orchestrator`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/review-orchestrator/SKILL.md) | The end-to-end workflow. Builds the frozen bundle and Graphviz plan, composes SME charters from one or more skills and domain references, enforces dependent blind quality gates, runs synthesis and its final blind writing review, persists local artifacts, and stops at the operator gate before publication. |

## Review orchestration

[`review-orchestrator`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/review-orchestrator/SKILL.md) is the operational authority for a complete review. It owns the frozen review bundle, Graphviz plan, SME charters, dependencies, local artifacts, synthesis, operator feedback loop, and publication boundary.

The README summary is intentionally non-normative:

- An SME branch answers one coherent question and may load multiple skills or authoritative references. An architecture and Iceberg semantics review can be one SME branch.
- Each SME receives the frozen target, governing sources, posture, assigned source context, a written charter, the findings contract, and a required disposition ledger.
- Every SME review is followed by a dependent blind quality gate using `writing-review` plus the SME charter and principles.
- Synthesis consumes only completed branch packets, loads `writing-review` before drafting, and passes one blind writing review.
- The human operator can return feedback to the same synthesis stage. Nothing publishes without explicit approval of the exact payload.

The durable orchestration example is [OpenHouse PR 683](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/review-orchestrator/examples/pr-683-review-dag.dot), where one SME loads both `arch-review` and the Iceberg View Spec.

## Writing

Skills follow the writing rules published at
[mkuchenbecker/humanizer](https://github.com/mkuchenbecker/humanizer): structure
rules in
[STRUCTURE.md](https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md)
and sentence-level patterns in
[SKILL.md](https://github.com/mkuchenbecker/humanizer/blob/main/SKILL.md). The
rules apply both when reviewing prose (`writing-review` fetches them as its
criteria) and when skills write their own reports and PR text. The reference
tracks `main` on purpose: a style guide is policy, and both uses want current
policy. If it is unreachable at
run time, skills say so and proceed; it is a style dependency, not a correctness
one.

This is the repo's one dependency rule in both directions: skills may depend on
published references below them, and nothing in this repository is referenced by
the writing reference in return.

## Layout

Skills use this shape:

```
skills/<name>/
  SKILL.md        # the surface: frontmatter (name, description), invocation,
                  # procedure, posture, and the findings contract
  *.md            # reference documents the skill reads while reviewing
                  # (e.g. arch-review/PRINCIPLES.md)
  verification/   # optional self-contained verification suite when the skill
                  # benefits from executable fixtures and machine grading
```

`SKILL.md` is what triggers and drives the review; the reference documents beside it
hold the judgment criteria and are read during the review, not loaded up front.
`pedantic-linter` intentionally has no verification fixture; its source audit is the
completeness ledger for the imported rule corpus.

## Installation

Copy a skill directory into a skills location Claude Code reads:

- Per project: `<repo>/.claude/skills/<name>/`
- Per user: `~/.claude/skills/<name>/`

```sh
cp -r skills/arch-review /path/to/your-repo/.claude/skills/arch-review
```

## Invocation

Invoke by name (`/review-orchestrator`, `/arch-review`, and the other skill names),
optionally with a PR number, branch, or module path. You can also ask for the kind
of review the skill's description covers; each description is written to trigger
on its matching request.
