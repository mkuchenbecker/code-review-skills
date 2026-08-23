---
name: writing-review
description: Review of prose changed in a diff, PR, branch, or named document: design docs, READMEs, comments, commit messages, PR descriptions, and any other writing. Judges document structure (conclusion placement, tables versus lists, caveats, layering, whether the document stands on its own) and sentence-level AI-writing patterns against an external writing reference, and reports findings diagnosed at the structural cause. Design documents are additionally judged against the prescribed design-doc structure in DESIGN-DOCS.md. Use this whenever the user asks for a review of documentation, a design doc, writing quality, clarity, or AI-sounding prose.
---

# Writing review

Review prose the way an editor with a structural eye would: the subject is the
document's shape first (where the conclusion sits, what the lists hide, which
caveats earn their place, whether it stands without its author) and its sentences
second. Diagnose findings at the structural cause; sentence-level tells are
usually symptoms of it.

## Criteria

The judgment criteria live in an external writing reference and are fetched at
review time:

- Structure rules: https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md
- Sentence-level patterns: https://github.com/mkuchenbecker/humanizer/blob/main/SKILL.md

The reference tracks `main` deliberately: a style guide is policy, and reviews
should apply current policy. When the reference is unreachable at review time,
degrade in the open: say so in the report, run the structure pass on the
condensed copy below, and skip the sentence pass, stating the skip in the
report. The condensed copy covers only the structure rules; there is no offline
copy of the sentence-pattern catalog, and improvising one from memory would
apply an unversioned catalog stripped of its false-positive guardrails. The
external files are the source of truth.

Condensed structure rules (fallback only): conclusion on top, at every scale,
stated as a disputable or actionable claim; context-bearing lists become tables,
and a table always comes with the sentence stating what it decides; numbered
items where items will be referenced; the document stands on its own (no prompt
echo, no self-narration, no undefined insider terms); a caveat survives only if
it would change the conclusion, and then as a condition in the main line;
complete sentences in prose; layers ordered TLDR → sketch → details → appendix,
each expanding the one above with no new conclusions downstream; descriptions
state the present and how to verify it, with production history (run counts,
iteration numbers, process narration) left to the commit log and other ledgers.

When the reviewed prose is a design document, also judge it against
[DESIGN-DOCS.md](DESIGN-DOCS.md) in this directory.

## Target

Prose changed in the current diff (the default), a PR number, a branch, or a
named document path. Prose means documentation files, comments and doc-comments,
commit messages, and PR descriptions; code is out of scope (other skills review
code). A diff is a way of selecting the prose under review, not a different kind
of review.

## Genres

Genre decides which rules bind. The table maps each genre to its rules; the
sentence-pattern catalog and its false-positive guardrails apply everywhere,
so the table lists only how the structure rules narrow.

| Genre | Structure rules that bind | Exempt |
|---|---|---|
| Documentation files and design docs | All structure rules; design docs are additionally judged against [DESIGN-DOCS.md](DESIGN-DOCS.md) | Nothing |
| PR descriptions | Conclusion on top; stands on its own; the caveat test; tables where items share fields | The layered skeleton: a PR description is a single layer |
| Commit messages | Conclusion on top (the subject line is the conclusion); stands on its own; the caveat test | Tables and the layered skeleton |
| Comments and doc-comments | Stands on its own; the caveat test; complete sentences | Document-scale rules: conclusion placement, tables, layering |
| Skill, prompt, and agent-instruction files | Prose passages (the rationale attached to rules) are judged as prose; directive passages are judged on precision, density, and duplication, with fragments legitimate inside their scaffold | Complete-sentence and fragment rules for directives and frontmatter; frontmatter is metadata, and its conventions are not prose defects |

## Procedure

The review is multiple passes over the document. Each pass is a separate,
complete read hunting one layer, and every pass has both rule sets in
context: the structure pass needs the sentence catalog to recognize that a pile
of sentence-level tells is evidence for a structural finding, and the sentence
pass needs the structure rules to report a tell at its structural cause instead
of duplicating the symptom. Shape-reading and line-reading are different modes
of attention; a single read does neither well.

1. Identify the prose in the target and what genre each piece is: a design doc,
   a README, API documentation, a comment, a commit message, a skill file. The
   Genres table above states which rules bind each.
2. Fetch the criteria (see above); fall back to the condensed copy if
   unreachable, and say so. Both rule sets stay in context for every pass.
3. **Structure pass.** Read the whole document for shape only: conclusion
   placement, list/table choices, caveat inventory, whether it stands on its
   own, layering, and the genre structure where one applies.
4. **Sentence pass.** Read the whole document again for lines only, against the
   pattern catalog.
5. **Merge.** Deduplicate: a sentence-level finding whose cause is a structural
   finding folds into it as evidence, not a separate entry. For structural
   findings, name the failure scenario in reader terms: who reads this, where
   they stop, what they miss or misjudge because of the defect.
6. Report findings in the contract below, ordered most severe first: a
   structured chat report by default, or a file when the invocation directs.
   A report with zero findings states that explicitly.

## Findings contract

Each finding carries the fields in this table; evidence is what makes a
structural finding checkable rather than asserted.

| Field | Content |
|---|---|
| location | File and line, or section heading for prose without stable lines |
| principle | The rule from the writing reference or DESIGN-DOCS.md, by name; `none (internal consistency)` for a true defect no rule covers |
| claim | One sentence stating the defect |
| evidence | The quoted text, or the folded sentence-level tells, supporting the claim |
| failure scenario | Which reader is harmed and how |
| severity | `blocker`: a reader will act wrongly or the document cannot be used without its author. `suggestion`: a reader pays avoidable cost. `nit`: polish |
| confidence | `confirmed`, `probable`, or `speculative` |
| reviewer | `writing-review` |

## Posture

- Judge the document that exists, for its actual readers. House style and the
  writer's voice are not defects; the false-positive guardrails in the
  sentence-pattern catalog apply here too.
- Report everything true, with evidence. A low-confidence finding is reported
  with its confidence marked `probable` or `speculative`, not suppressed.
- Never rewrite in the report. The finding names the defect and its cause; the
  writer (or the rewriter skill in the writing reference) applies the fix.
