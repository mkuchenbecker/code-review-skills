---
name: synthesis-review
description: Synthesis skill that turns expert review reports into the final, post-ready feedback for a code or document review. Takes a target (diff, PR, branch, module, or document), its deployment posture, and one or more expert findings reports; verifies every finding against the target, merges duplicates, adjudicates disagreements, tiers each surviving finding as blocking or follow-up, and converges on the exact tagged comments to post plus an adjudication log. Use this whenever the user asks to synthesize, triage, merge, rank, or finalize review findings from one or more reviewers into the feedback that will actually be delivered.
---

# Synthesis review

Experts testify; synthesis commits. An expert report is testimony about what is true in the
target; this skill decides what to do about it, and its output is not a report about the
feedback — it is the feedback: the exact comments that will be posted, each tagged, tiered,
and self-contained, plus a log of every judgment call and every finding that did not survive.
The rest of this file is the procedure; the judgment criteria live in PRINCIPLES.md beside it.

## Inputs

| Input | Content | If absent |
|---|---|---|
| Target | The code or document reviewed: a diff, PR, branch, module path, or document | Required; stop and ask |
| Posture | Deployment reality: who calls this, what flags gate it, what a mistake costs to fix later | Assume the riskiest posture (live callers, no flag), and say so in the log |
| Reports | One or more expert reports whose findings carry location, claim, evidence, severity, confidence, and reviewer | Required; a report missing fields is used for what it has, with the gaps noted in the log |

Severity in a report is the expert's testimony about the defect. It is never overruled as
testimony and never passes through as the decision: the tier assigned here is the decision.

## Procedure

1. **Verify every finding against the target.** Check the location exists, the quoted evidence
   matches the file, and the claim is consistent with the surrounding code or text. A finding
   that fails any check goes to the adjudication log as *returned* — with what failed — and
   produces no comment. Silence is the one forbidden outcome: every input finding ends up
   either in a comment or in the log.
2. **Merge duplicates.** Two findings with the same location and the same failure are one
   comment citing every reviewer that found it; independent rediscovery raises confidence and
   rank, never comment count. Findings that share a cause but not a location stay separate
   comments that name the shared cause.
3. **Adjudicate disagreements.** When findings conflict — two incompatible fixes, or one
   expert's claim contradicting another's — the log states both positions and the deciding
   reason, and the surviving comment carries the ruling. Never present one side as if the
   other did not exist, and never post both sides as separate comments.
4. **Tier each surviving finding: blocking or follow-up.** A finding is *blocking* when
   merging without the fix makes the change lie: behavior a green test pins that is wrong,
   a documented claim the code contradicts, or silently wrong answers on the merged surface.
   Everything else is *follow-up*, carrying its named trigger when one exists ("before
   enabling the flag", "at the next dependency upgrade"). Posture is what turns severity
   into tier: the same defect blocks on a live surface and follows up behind a dark flag,
   and the log states which posture fact decided each close call.
5. **Bound every blocking demand by the diff.** A blocking comment carries a smallest fix
   proportionate to the change under review. When the true fix is large, the blocking demand
   is the smallest honest step — pin the behavior, correct the document, guard the entry —
   and the large fix is a follow-up. A restructuring demand never blocks a small change.
6. **Converge to the exact comments.** Each comment is what will be posted, verbatim:
   a `[category][tier]` prefix (category is the reviewer that found it; a merged comment
   lists each), the location, and self-contained text stating what happens now, what should
   happen and why, and the fix. One comment per location: findings that share an anchor are
   merged or explicitly cross-referenced. Comments are ordered most severe first, numbered
   once, and never renumbered in a later revision — new findings append.

## Output contract

The output has three parts, in this order. All prose follows the writing rules at
https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md; if that reference is
unreachable, proceed without it, since it is a style dependency and not a correctness one.

| Part | Content |
|---|---|
| Review body | One short body for the whole target: the verdict in plain sentences, the tier counts, and an attribution line naming the criteria the review was generated from, so a reader can inspect what was being judged |
| Comment set | The numbered, tagged comments exactly as they will be posted, most severe first |
| Adjudication log | Every returned finding with what failed; every merge with its sources; every disagreement with both positions and the deciding reason; the posture facts (or the stated assumption) that decided tiering |

## Posture

- Every input finding is accounted for: comment or log entry, never silence.
- Truth is not re-litigated: a verified finding's claim stands even when inconvenient; a
  finding is returned for failed verification, not for being unwelcome.
- Report what the synthesis did not do: dimensions no report covered are named in the review
  body as unreviewed, not silently absent.
- Anti-noise commitments (violating any is itself a defective synthesis):
  - Never let a rewrite-scale demand through as blocking.
  - Never inflate a nit's tier to blocking; polish follows up.
  - Never emit two comments where one location carries both.
  - Never drop, soften, or average a disagreement instead of ruling on it.
