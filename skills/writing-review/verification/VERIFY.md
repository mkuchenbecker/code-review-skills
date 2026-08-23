# Verifying writing-review

The suite passes when all three gates in the table below pass. A recall miss or
a restraint violation is a skill defect. A deliberate change to the skill or
the writing reference updates `expected.json` in the same change; the gates
read their entry counts from `expected.json`, never from this file.

All paths in this file are relative to the repository root.
`skills/writing-review/verification/fixture/` contains two design documents
treated as newly added in the change under review: one seeded with structural
defects and one structurally sound restraint control.
`skills/writing-review/verification/expected.json` is the answer key.
Everything needed is in this repository except the external writing reference
the skill fetches; when that is unreachable, the skill's own fallback applies
and the run is still gradable, because every `must_find` entry is a structural
finding.

## Procedure

The procedure has two steps, run by two separate agents so the review stays
blind.

**1. Blind run.** Give an agent `skills/writing-review/SKILL.md` (which loads
`DESIGN-DOCS.md` beside it and the external writing reference) and this
instruction, withholding `expected.json` and this file:

> Follow the skill at skills/writing-review/SKILL.md. Review target: the
> documents under skills/writing-review/verification/fixture/, all newly added
> in the change under review. Write the findings report, in the skill's
> findings contract, to a file.

The reviewer must not see `expected.json` or this file. A review that knows the
answer key measures nothing.

**2. Grade.** Give a second agent the report and `expected.json`, with the
gates in this table; recall is the gate that carries the suite, and the other
two keep it honest.

| Gate | Rule | Pass |
|---|---|---|
| Recall | Every `must_find` entry is matched by a finding in its file citing one of the entry's principles; match by file and principle name, with `detail` disambiguating | All `must_find` entries matched |
| Restraint | The control file named by `must_not_flag` draws no findings | Zero findings on it |
| Precision | Findings matching neither `must_find` nor `allowed_extras` count against precision | Zero unmatched findings |
