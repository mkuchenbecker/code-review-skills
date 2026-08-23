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

## Writing

Skills follow the writing rules published at
[mkuchenbecker/humanizer](https://github.com/mkuchenbecker/humanizer) — structure
rules in
[STRUCTURE.md](https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md),
sentence-level patterns in
[SKILL.md](https://github.com/mkuchenbecker/humanizer/blob/main/SKILL.md) — both
when reviewing prose (`writing-review` fetches them as its criteria) and when
writing their own reports and PR text. The reference tracks `main` on purpose: a
style guide is policy, and both uses want current policy. If it is unreachable at
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
path), or just ask for the kind of review the skill's description covers — the
description is written so the skill triggers on matching requests.
