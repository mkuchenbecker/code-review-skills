# Verifying testing-review

The suite passes when all three gates in the table below pass. The Recall
gate carries `report_requirements` from `expected.json`; no other gate does. A recall miss or a restraint
violation is a skill defect. A deliberate change to the skill updates
`expected.json` in the same change; the gates read their entry counts from
`expected.json`, never from this file.

All paths in this file are relative to the repository root.
`skills/testing-review/verification/fixture/` holds a small Java module with
two documented contracts and their two test files: one seeded with defects,
one a restraint control containing constructs the skill must leave alone.
`skills/testing-review/verification/expected.json` is the answer key.

## Procedure

The procedure has two steps, run by two separate agents so the review stays
blind.

**1. Blind run.** Give an agent `skills/testing-review/SKILL.md` (which loads
`PRINCIPLES.md` and `TEST-TYPES.md` beside it) and this instruction,
withholding `expected.json` and this file:

> Follow the skill at skills/testing-review/SKILL.md in evaluate mode. Review
> target: the module at skills/testing-review/verification/fixture/, all newly
> added in the change under review. Review it as source; it is not built, so
> its build tooling is out of scope. Write the report, in the skill's findings
> contract and including the SHOULD/DOES map, to a file.

The reviewer must not see `expected.json` or this file. A review that knows
the answer key measures nothing.

**2. Grade.** Give a second agent the report and `expected.json`, with the
gates in this table; recall carries the suite, and the other two keep it
honest.

| Gate | Rule | Pass |
|---|---|---|
| Recall | Every `must_find` entry matched by a finding in its file citing one of the entry's principles, with `detail` disambiguating; `report_requirements` also hold | All entries matched and requirements met |
| Restraint | No finding makes the demand a `must_not_flag` entry's `detail` forbids | Zero violations |
| Precision | A finding counts against precision unless it matches a `must_find` entry, or sits at an `allowed_extras` location AND reports what that entry's `detail` describes | Zero unmatched findings |
