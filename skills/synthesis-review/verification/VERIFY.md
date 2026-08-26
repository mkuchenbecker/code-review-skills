# Verifying synthesis-review

The suite passes when all three gates in the table below pass. A recall miss or a restraint
violation is a skill defect. A deliberate change to the skill updates `expected.json` in the same
change; the gates read their entry sets from `expected.json`, never from this file.

All paths in this file are relative to the repository root.
`skills/synthesis-review/verification/fixture/` holds the inputs a synthesis run consumes: a
target (`target/RateLimiter.java`, whose class javadoc is its documented contract, plus
`target/CONTEXT.md` stating the deployment posture) and three expert reports under `reports/`
(arch-review, testing-review, writing-review), written in the series findings contract. The
reports seed the phenomena the skill exists to handle: a duplicate found by two reviewers, a
direct disagreement over the right fix, a finding whose evidence does not match the target, a
true-but-rewrite-scale demand, and two nits.
`skills/synthesis-review/verification/expected.json` is the answer key.

## Procedure

The procedure has two steps, run by two separate agents so the run stays blind.

**1. Blind run.** Give an agent `skills/synthesis-review/SKILL.md` (which loads PRINCIPLES.md
beside it) and this instruction, withholding `expected.json` and this file:

> Follow the skill at skills/synthesis-review/SKILL.md. The target is
> skills/synthesis-review/verification/fixture/target/ (RateLimiter.java, all newly added in the
> change under review; CONTEXT.md states the deployment posture). The expert reports are the
> three files in skills/synthesis-review/verification/fixture/reports/. Produce the skill's full
> output — review body, comment set, adjudication log — and write it to a file.

The synthesizer must not see `expected.json` or this file. A run that knows the answer key
measures nothing.

**2. Grade.** Give a second agent the output and `expected.json`, with the gates in this table.

| Gate | Rule | Pass |
|---|---|---|
| Recall | Every `must_produce` entry matched per the key's `matching` text; every `log_requirement` met; every `report_requirement` holds | All matched and met |
| Restraint | No output violates a `must_not` entry's `detail` | Zero violations |
| Precision | Every comment matches a `must_produce` entry or an `allowed_extras` entry (location AND described content); anything else counts against precision | Zero unmatched comments |

Grade the formatting requirement against the raw Markdown output. A prose sentence split across
physical lines fails even when both lines would form the same sentence after whitespace
normalization. Structural Markdown lines and blank paragraph separators remain valid.

## Changing the fixture

A fixture edit changes what a correct synthesis produces: a new documented claim in the target,
a new finding in a report, or a change to CONTEXT.md's posture moves comments across tiers or in
and out of the comment set. Before committing such an edit, ask one question per finding across
the three reports: which `must_produce`, `must_not`, or `allowed_extras` entry accounts for this
finding's fate, and does the posture in CONTEXT.md still justify each entry's tier list? An
unaccounted finding makes a correct synthesis fail Precision or Recall. Run this when the
fixture changes, not when a run comes back.
