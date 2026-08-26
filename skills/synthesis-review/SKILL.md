---
name: synthesis-review
description: Synthesis skill that turns completed blind-reviewed SME branch packets into the final, post-ready feedback for a code or document review. Takes a frozen review bundle, deployment posture, and one completed branch packet per selected charter; verifies that each blind quality gate passed the SME report for stand-alone writing and charter-principle conformance, merges duplicates, adjudicates cross-branch disagreements, tiers each surviving finding as blocking or follow-up, and converges on the exact tagged comments to post plus an adjudication log. Use this whenever the user asks to synthesize, triage, merge, rank, or finalize review findings into the feedback that will actually be delivered.
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
| Review bundle | Frozen target, base and head identifiers, governing specifications and explicit requirements, approved contracts, reviewer charters, and supporting source | Required; stop and ask |
| Posture | Deployment reality: who calls this, what flags gate it, what a mistake costs to fix later | Assume the riskiest posture (live callers, no flag), and say so in the log |
| Blind-reviewed branch packets | One packet per selected charter. Each packet contains the final SME report and disposition ledger, the dependent `writing-review` result, the charter and principles audit, the correction and verification log, and a blind pass verdict | Required for a complete protocol review. A raw SME report or failed blind gate is not synthesis-ready. |

Severity in a report is the expert's testimony about the defect. It is never overruled as
testimony and never passes through as the decision: the tier assigned here is the decision.

## Procedure

1. **Load the writing criteria before drafting.** Read
   [`writing-review/SKILL.md`](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/writing-review/SKILL.md) in full and load the current
   structure and sentence criteria it requires. Treat those rules as construction constraints
   for the review body, exact comments, and adjudication log, not as cleanup deferred until the
   end.
2. **Verify every branch dependency and quality gate.** Confirm each selected charter has a
   final SME report and a completed blind review that depends on it. Confirm the blind review
   applied `writing-review` to the feedback, audited the report against the charter and governing
   principles, validated every claim and citation, recorded corrections, validated the revision,
   and issued a pass verdict. A raw SME report, failed blind gate, incomplete audit, or blind
   review produced before the SME report was complete is returned for completion.
3. **Preserve blind-validated facts.** Treat each passed branch packet's claims, evidence, and
   dispositions as already validated. Do not repeat the claim-verification pass in synthesis. If
   synthesis materially changes a factual claim, quoted evidence, consequence, or fix, validate
   the changed statement against the frozen bundle and record that validation in the log.
4. **Merge duplicates.** Two findings with the same location and the same failure are one
   comment citing every reviewer that found it; independent rediscovery raises confidence and
   rank, never comment count. Findings that share a cause but not a location stay separate
   comments that name the shared cause.
5. **Adjudicate cross-branch disagreements.** When completed branch packets conflict, either
   through two incompatible fixes or one charter's claim contradicting another's, the log states
   both positions and the deciding reason, and the surviving comment carries the ruling. Never
   present one side as if the other did not exist, and never post both sides as separate
   comments.
6. **Tier each surviving finding: blocking or follow-up.** A finding is *blocking* when
   merging without the fix makes the change lie: behavior a green test pins that is wrong,
   a documented claim the code contradicts, or silently wrong answers on the merged surface.
   Everything else is *follow-up*, carrying its named trigger when one exists ("before
   enabling the flag", "at the next dependency upgrade"). Posture is what turns severity
   into tier: the same defect blocks on a live surface and follows up behind a dark flag,
   and the log states which posture fact decided each close call.
7. **Bound every blocking demand by the diff.** A blocking comment carries a smallest fix
   proportionate to the change under review. When the true fix is large, the blocking demand
   is the smallest honest step — pin the behavior, correct the document, guard the entry —
   and the large fix is a follow-up. A restructuring demand never blocks a small change.
8. **Converge to the exact comments.** Each comment is what will be posted, verbatim.
   Preserve each expert's identity and stable rule identifier in the prefix. Use
   `[category][tier]` when a finding has no rule identifier and
   `[category][rule-id][tier]` when it does, for example
   `[pedantic-linter][PL-FAIL-001][blocking]`. A merged comment retains every source category and
   every supplied rule identifier, grouped after its source category, with the tier last. The
   comment then states the location, what happens now, what should happen and why, and the fix.
   One comment per location: findings that share an anchor are merged or explicitly
   cross-referenced. Comments are ordered most severe first, numbered once, and never renumbered
   in a later revision; new findings append.
9. **Serialize comments for GitHub.** Treat every physical newline in an inline comment as output
   semantics because GitHub renders it as a visible line break. Put each prose paragraph on one
   physical line, including inline links that continue the sentence. Preserve blank lines between
   paragraphs and the structural lines required by headings, lists, blockquotes, tables, fenced or
   indented code, and explicit hard breaks. Run this normalization before the operator gate so the
   approved exact comment body is byte-for-byte equivalent to the Markdown body field that will be
   posted.
10. **Pass one blind review of the synthesis.** Run a blind reviewer over the complete review
    body, exact comment set, and adjudication log using `writing-review` as its criteria. The same
    synthesis stage corrects every finding and resubmits the revision to the same blind reviewer.
    There is no second synthesis stage. The synthesis is not operator-ready until this gate
    passes. This review judges the final writing; it does not repeat source verification already
    completed by each branch blind review. Validate any factual statement materially changed
    during correction.

## Output contract

The output has three parts, in this order. All prose follows the writing rules at
https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md; if that reference is
unreachable, proceed without it, since it is a style dependency and not a correctness one.

| Part | Content |
|---|---|
| Review body | One short body for the whole target: the verdict in plain sentences, the tier counts, and an attribution line naming the criteria the review was generated from, so a reader can inspect what was being judged |
| Comment set | The numbered comments exactly as they will be posted, most severe first. Tags preserve each source category and stable rule identifier. Each prose paragraph occupies one physical line; Markdown structural lines remain intact. |
| Adjudication log | Every incomplete branch dependency or failed blind quality gate; every SME correction required and validated by blind review; every material factual change made during synthesis and its validation; every merge with its sources; every cross-branch disagreement with both positions and the deciding reason; the posture facts (or the stated assumption) that decided tiering |

## Posture

- Every item in every blind-reviewed branch packet is accounted for: comment or log entry, never
  silence.
- A complete protocol review has one completed blind-reviewed branch packet for every selected
  charter. A raw SME report or unresolved blind-review defect never enters synthesis directly.
- Synthesis loads `writing-review` before drafting and passes one blind review using
  `writing-review` before the operator gate.
- Any operator change returns to this same synthesis stage. Apply the change under stable
  numbering, then pass the same blind writing review again.
- Current code, tests, documentation, and repository convention cannot ratify their own defect.
  They are evidence, not governing authority.
- A verified finding is not dismissed because it is inconvenient. It can be rejected only when
  the frozen governing sources or completed branch evidence disproves it, with the reason in the
  log.
- Report what the synthesis did not do: dimensions no report covered are named in the review
  body as unreviewed, not silently absent.
- Anti-noise commitments (violating any is itself a defective synthesis):
  - Never let a rewrite-scale demand through as blocking.
  - Never inflate a nit's tier to blocking; polish follows up.
  - Never emit two comments where one location carries both.
  - Never drop, soften, or average a disagreement instead of ruling on it.
  - Never strip a stable rule identifier supplied by an expert.
  - Never leave editor wrapping inside a prose paragraph in the post-ready comment payload.
