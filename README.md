# code-review-skills

Self-contained code-review skills for Claude Code. Each skill is a complete reviewer:
it defines what it reviews, how it judges, and the contract its findings are reported
in. Skills reference nothing outside their own directory and make no assumptions
about who consumes their findings, so each can be installed and invoked on its own.

## Skills

| Skill | Reviews |
|---|---|
| [`arch-review`](skills/arch-review/SKILL.md) | Decomposition: module topology, interface contracts, ownership of decisions, and error design. Language-agnostic principles with Java examples. Findings are diagnosed at the structural cause (a boundary, a contract, a seam), not the surface pattern. |

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
