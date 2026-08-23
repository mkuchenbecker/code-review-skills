# Verifying writing-review

`fixture/` contains two design documents to be treated as newly added in the
change under review: one seeded with structural defects and one structurally
sound restraint control. `expected.json` is the answer key. Everything needed is
in this repository, except the external writing reference the skill fetches; if
that is unreachable the skill's own fallback applies and the run is still valid.

## Procedure

Two steps, run by two separate agents so the review stays blind.

**1. Blind run.** Give an agent `../SKILL.md` (which loads `../DESIGN-DOCS.md`
and the external writing reference) and this instruction, withholding
`expected.json` and this file:

> Follow the skill at skills/writing-review/SKILL.md. Review target: the
> documents under skills/writing-review/verification/fixture/, all newly added
> in the change under review. Write the findings report, in the skill's findings
> contract, to a file.

**2. Grade.** Give a second agent the report and `expected.json`:

- **Recall**: every `must_find` entry matched by a finding in its file citing
  one of its principles (match by file and principle name; `detail`
  disambiguates). Pass: 7/7.
- **Restraint**: zero findings on `fixture/docs/design/id-format.md`.
- **Precision**: findings matching neither `must_find` nor `allowed_extras`
  count against precision. Pass: 0 unmatched.

The suite passes when all three pass. A miss or a restraint violation is a
skill defect; after deliberate changes to the skill or the writing reference,
update `expected.json` in the same change.
