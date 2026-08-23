# Verifying arch-review

Everything the verification needs is in this directory: `fixture/` is a small
three-module Gradle project seeded with known defects and known must-not-flag
constructs, and `expected.json` is the machine-checkable manifest of both. Running
the verification requires no other code, no external repository, and no human
judgment — a person's role is limited to spot-checking the graded result.

## Procedure

Two steps, run by two separate agents so the review stays blind.

**1. Blind run.** Give an agent `../SKILL.md` (which loads `../PRINCIPLES.md`) and
this instruction, withholding `expected.json` and this file:

> Follow the skill at skills/arch-review/SKILL.md. Review target: the Gradle
> project at skills/arch-review/verification/fixture/ (module-path mode; treat
> every file as newly added). Write the findings report, in the skill's findings
> contract, to a file.

The reviewer must not see `expected.json`, this file, or any statement of what the
fixture contains — a review that knows the answer key measures nothing.

**2. Grade.** Give a second agent (or grade by hand as a spot check) the findings
report and `expected.json`, with these rules:

- **Recall** — every `must_find` entry is matched by at least one finding whose
  location is one of the entry's `locations` and whose cited principle is one of
  the entry's `principles`. Match on file and principle name, not wording; use
  `detail` to disambiguate files that carry more than one seed.
  Pass: 6/6 matched.
- **Restraint** — no finding targets a `must_not_flag` construct (the `detail`
  describes the construct; other findings in the same file are fine).
  Pass: 0 violations.
- **Precision** — findings that match neither `must_find` nor `allowed_extras`
  count against precision. Pass: 0 unmatched findings.

The suite passes when all three pass. A recall miss or a restraint violation is a
defect in the skill (or, after deliberate skill changes, a manifest gone stale —
update `expected.json` in the same change that alters the skill's judgment).
Unmatched extra findings are adjudicated once: a genuine defect in the fixture
joins `allowed_extras` (or `must_find` if it should be mandatory); a false positive
is a skill defect to fix.

## Changing the fixture

The fixture and manifest evolve together: any edit to `fixture/` that adds,
removes, or moves a seeded construct must update `expected.json` in the same
change. The seeds are chosen to exercise distinct principles; when adding one,
prefer a defect class the manifest does not already cover.
