# code-review-skills

Self-contained code-review skills for Claude Code. Each skill is a complete reviewer:
it defines what it reviews, how it judges, and the contract its findings are reported
in. A skill may depend downward on a published external reference (see Writing,
below) but never on its consumers, so each can be installed and invoked on its own.

## Skills

| Skill | Reviews |
|---|---|
| [`arch-review`](skills/arch-review/SKILL.md) | Decomposition: module topology, interface contracts, ownership of decisions, and error design. Language-agnostic principles with Java examples. Findings are diagnosed at the structural cause (a boundary, a contract, a seam), not the surface pattern. |
| [`writing-review`](skills/writing-review/SKILL.md) | Prose changed in a diff: documentation, design docs, comments, commit messages, PR descriptions. Judges document structure first and sentences second, against the external writing reference; design documents are additionally judged against a prescribed structure. |
| [`testing-review`](skills/testing-review/SKILL.md) | Tests, and the tested-ness of code. Two modes: evaluate reviews a change's tests and ends in a verdict with named actions; write plans the tests for a change. Both derive the tests that should exist from the contract and the control flow, find the ones that do, and diff the sets. |
| [`synthesis-review`](skills/synthesis-review/SKILL.md) | Expert reports, into the final feedback. Verifies every finding against the target, merges duplicates, adjudicates disagreements, tiers each survivor as blocking or follow-up against the deployment posture, and converges on the exact tagged comments to post plus an adjudication log. |

## The review protocol

The skills compose into a full review of a change or document. The protocol below is the
orchestration layer: it references the skills; no skill references it back, so each skill still
stands alone.

1. **Grok.** Build the brief before any judgment: what the target is, the commits under review,
   the ground rules, and the deployment posture — who calls this code, what flags gate it, what a
   later fix costs. Posture gathered here is what synthesis uses to tier.
2. **Fan out.** Run the expert skills that fit the change, each independently: `arch-review` for
   structure, `testing-review` for tests and tested-ness, `writing-review` when the diff carries
   prose. A dimension no committed skill covers gets an ad-hoc expert: a charter naming its
   worldview and its authoritative source (a protocol spec, a style guide), reporting in the same
   findings contract. Experts judge truth, report everything, and never rank; every finding
   carries evidence and a verified pointer.
3. **Synthesize.** Run `synthesis-review` over the target, the posture, and the reports. Its
   output is the deliverable: the exact tagged comments, the review body, and the adjudication
   log.
4. **Review the review.** Before a human sees it, the synthesis output is itself reviewed in its
   own genre — `writing-review` for the prose, plus re-verification of every cited location and
   quote against the target. Findings are addressed, not noted.
5. **Operator gate.** A human reviews the finished feedback locally, and their changes fold in
   under stable numbering: findings append, nothing renumbers. Nothing is posted before this
   gate.
6. **Publish.** Comments post exactly as converged, one per location, each prefixed
   `[category][tier]` — category is the reviewer skill that found it, tier is `blocking` or
   `follow-up`. The per-target review body carries an attribution line naming this repository as
   the criteria, so readers can inspect what was being judged.

Two process rules hold at every stage: every agent checkpoints its working state to disk as it
goes, so an interrupted run resumes from notes instead of from zero; and every claim is
re-verified against the source at every hand-off, because pointers drift and quotes rot.

### Signing

Every published artifact declares its generator and its criteria.

| Medium | Signature |
|---|---|
| Review body | An attribution line naming this repository as the criteria, plus the platform's generated-by footer |
| Inline comment | The `[category]` tag, which names the skill whose criteria produced it |
| Commit | The platform's co-author trailer; never a model name inside the artifact itself |

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

Every skill follows the same shape:

```
skills/<name>/
  SKILL.md        # the surface: frontmatter (name, description), invocation,
                  # procedure, posture, and the findings contract
  *.md            # reference documents the skill reads while reviewing
                  # (e.g. arch-review/PRINCIPLES.md)
  verification/   # the skill's self-contained verification suite: a seeded
                  # fixture, a machine-checkable expected.json, and VERIFY.md
                  # as the entry point — re-runnable with no human grading
```

`SKILL.md` is what triggers and drives the review; the reference documents beside it
hold the judgment criteria and are read during the review, not loaded up front.

## Installation

Copy a skill directory into a skills location Claude Code reads:

- Per project: `<repo>/.claude/skills/<name>/`
- Per user: `~/.claude/skills/<name>/`

```sh
cp -r skills/arch-review /path/to/your-repo/.claude/skills/arch-review
```

## Invocation

Invoke by name (`/arch-review`, optionally with a PR number, branch, or module
path), or just ask for the kind of review the skill's description covers; the
description is written so the skill triggers on matching requests.
