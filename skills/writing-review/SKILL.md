---
name: writing-review
description: Review of prose changed in a diff, PR, branch, or named document — design docs, READMEs, comments, commit messages, PR descriptions, and any other writing. Judges document structure (conclusion placement, tables versus lists, caveats, layering, whether the document stands on its own) and sentence-level AI-writing patterns against an external writing reference, and reports findings diagnosed at the structural cause. Design documents are additionally judged against the prescribed design-doc structure in DESIGN-DOCS.md. Use this whenever the user asks for a review of documentation, a design doc, writing quality, clarity, or AI-sounding prose.
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
should apply current policy. If the reference is unreachable at review time, say
so in the report and proceed on the summary below — it is a condensed copy, and
the external files are the source of truth.

Condensed structure rules (fallback only): conclusion on top, at every scale,
stated as a disputable or actionable claim; context-bearing lists become tables,
and a table always comes with the sentence stating what it decides; numbered
items where items will be referenced; the document stands on its own (no prompt
echo, no self-narration, no undefined insider terms); a caveat survives only if
it would change the conclusion, and then as a condition in the main line;
complete sentences in prose; layers ordered TLDR → sketch → details → appendix,
each expanding the one above with no new conclusions downstream.

When the reviewed prose is a design document, also judge it against
[DESIGN-DOCS.md](DESIGN-DOCS.md) in this directory.

## Target

Prose changed in the current diff (the default), a PR number, a branch, or a
named document path. Prose means documentation files, comments and doc-comments,
commit messages, and PR descriptions; code is out of scope (other skills review
code). A diff is a way of selecting the prose under review, not a different kind
of review.

## Procedure

1. Identify the prose in the target and what genre each piece is: a design doc,
   a README, API documentation, a comment, a commit message. Genre sets
   expectations — a design doc is judged against DESIGN-DOCS.md, a code comment
   against far narrower rules.
2. Fetch the criteria (see above); fall back to the condensed copy if
   unreachable, and say so.
3. Judge structure first: conclusion placement, list/table choices, caveat
   inventory, standalone-ness, layering. Then judge sentences against the
   pattern catalog.
4. For structural findings, name the failure scenario in reader terms: who reads
   this, where they stop, what they miss or misjudge because of the defect.
5. Report findings in the contract below, as a structured chat report, ordered
   most severe first. A report with zero findings states that explicitly.

## Findings contract

- **location** — file and line, or section heading for prose without stable lines
- **principle** — the rule from the writing reference (or DESIGN-DOCS.md), by name
- **claim** — one sentence stating the defect
- **failure scenario** — which reader is harmed and how
- **severity** — `blocker` (a reader will act wrongly or the document cannot be
  used without its author) | `suggestion` (a reader pays avoidable cost) |
  `nit` (polish)
- **confidence** — `confirmed` | `probable` | `speculative`
- **reviewer** — `writing-review`

## Posture

- Judge the document that exists, for its actual readers. House style and the
  writer's voice are not defects; the false-positive guardrails in the
  sentence-pattern catalog apply here too.
- Report everything true, with evidence. A low-confidence finding is reported
  with its confidence marked low, not suppressed.
- Never rewrite in the report. The finding names the defect and its cause; the
  writer (or the rewriter skill in the writing reference) applies the fix.
