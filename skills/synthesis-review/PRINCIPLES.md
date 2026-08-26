# Principles

Experts testify; synthesis commits. A review's experts answer "what is true here?", each inside
one coherent worldview, and their reports are testimony: evidence-backed, severity-graded, and
deliberately unfiltered. Synthesis answers a different question — "what do we ask of this change,
now?" — and everything below derives from keeping those two questions separate. Each principle
is stated with its reason; the log cites principles by name.

## 1. Truth and policy separate

An expert who ranks self-censors: knowing that a finding will be argued down invites not
reporting it, and the testimony degrades. A synthesizer who dismisses specialist evidence as
taste makes the same mistake. So experts report everything true and never tier it, and synthesis
tiers only after verification and the dependent blind audit. Verification checks the frozen governing
sources and target: does the location exist, does the quote match, and does the claim follow from
the specification, explicit requirement, approved contract, charter, and code? A finding is
returned when those checks disprove it, never because it is unwelcome.

## 2. Corroboration is signal

A blind reviewer depends on the SME report, so its pass is a quality gate, not independent domain
corroboration. It proves that the feedback stands on its own and that the SME review achieved its
charter under the governing principles. Independent corroboration occurs when separate SME
branches reach the same defect without seeing each other's work. Deduplication keeps every
source: one comment, all SME sources cited, plus the branch's blind verification. Separate SME
agreement raises confidence. The merge test is same location and same failure; a shared cause
with different symptoms stays separate comments naming the common cause, because each symptom
needs its own fix conversation.

## 3. Tier is a decision about this merge, not a grade of the defect

Severity grades the defect in isolation; the tier answers "must this change wait?" — and that
depends on facts no single expert owns: who calls the code, what flag gates it, what a later
fix costs. The blocking test is a lie test: merging without the fix makes the change lie —
wrong behavior pinned by a green test, a documented claim the code contradicts, silently wrong
answers on the merged surface. A lie compounds: green tests get trusted, documents get relied
on, silent wrongness gets built upon. Everything that does not make the merge a lie is
follow-up, with its trigger named when one exists, because "later, when X" is a commitment
only if X is stated. Posture can move the same defect across the line in either direction,
so the log records which posture fact decided each close call.

## 4. Proportion binds demands to the diff

A review that answers a fifty-line change with a restructuring demand will be ignored, and
deserves to be: the demand's cost lives outside the change under review. Blocking demands
therefore carry a smallest fix proportionate to the diff. When the honest fix is genuinely
large, the smallest honest step blocks — pin the current behavior, correct the document,
guard the entry point — and the large fix follows up with the step as its down payment.
This is the named failure mode of review-by-committee: many true findings, each reasonable,
summing to "rewrite the app"; proportion is what keeps truth actionable.

## 5. Nothing exits silently

Every input finding and non-finding disposition ends as a comment or a log entry. An item
dropped without a trace is a swallowed exception at the review level: the expert's question was
asked and the answer was discarded where no one can see it. The same holds for disagreements:
averaging two positions or quietly picking one erases the information that qualified reviewers
disagreed, which is itself a signal the author deserves. The log states both positions and the
deciding reason; the comment carries the ruling.

## 6. The output is the feedback, not a description of it

A synthesis that ends in "themes" and "recommendations" moves the real work — writing the
comment someone will read at a file and line — to whoever posts it, and that translation is
where precision dies. So synthesis converges: the deliverable is the exact comment set,
tagged `[category][tier]` or `[category][rule-id][tier]` when the expert supplied a stable rule,
one comment per location, each self-contained (what happens now, what should happen and why,
the fix), ordered most severe first, numbered once and never renumbered; revisions append,
because stable numbers are what let a discussion refer to "finding 12" a week later. A rule
identifier is part of the expert's evidence trail and survives verification, merging, and
publication.

Synthesis reads `writing-review` before drafting so structure and sentence quality constrain the
first version. One blind reviewer then judges the completed output using `writing-review`.
Corrections remain part of the same synthesis stage, which resubmits to the same blind reviewer
until it passes. Operator changes return to this same synthesis stage and pass the same blind
review again.

## 7. Physical newlines are output semantics

An exact comment includes its Markdown layout. GitHub review comments render physical source
newlines as visible line breaks, so prose wrapped to an editor column reaches the reader as broken
sentences. Synthesis writes each prose paragraph on one physical line and keeps newlines only where
Markdown structure needs them: paragraph separators, headings, list items, blockquotes, tables,
code blocks, and explicit hard breaks. This normalization happens before the operator gate because
each approved exact comment body and its posted Markdown body field must be byte-for-byte
equivalent.

## 8. Existing behavior does not prove correctness

Current code, tests, documentation, and repository convention can show what callers depend on,
what a migration costs, and where compatibility breaks. They cannot prove that repeating the
same behavior is correct. Governing specifications, explicit requirements, approved contracts,
and the reviewer charter decide correctness. A non-finding disposition based only on precedent
fails verification and must be adjudicated again.

## 9. Blind review follows the SME review

Every selected charter runs as a dependency chain: the SME review completes its findings and
disposition ledger, then the blind reviewer audits that completed work against the same frozen
bundle. The blind reviewer must see the SME output because its job is to decide whether the
feedback makes sense and stands alone under `writing-review`, then decide whether it achieves the
charter under the governing principles. It validates every claim, citation, and consequence
against the target. Source inspection supports those judgments; it is not a second line-by-line
specialty pass. A substantive omission or invalid disposition returns the report to the SME for
correction. The blind reviewer validates the revision, emits the passed branch packet, and
synthesis depends on that packet rather than on the raw SME report. Synthesis does not duplicate
that claim-validation pass unless it materially changes the fact.

## Symptoms

| Symptom | Causing principle |
|---|---|
| A finding present in a report and absent from both comments and log | 5 |
| Two comments at one location saying the same thing with different words | 2 |
| A comment citing one reviewer for a defect two reports contain | 2 |
| A blocking comment whose fix touches more code than the diff did | 4 |
| A nit tiered blocking, or a silent-wrongness defect tiered follow-up on a live surface | 3 |
| A ruling with no recorded opposing position | 5 |
| A "themes and recommendations" section in place of postable comments | 6 |
| An expert's severity passed through as the tier with no posture reasoning | 1, 3 |
| A follow-up with a real trigger ("before enabling") not naming it | 3 |
| Renumbered findings between revisions of the same review | 6 |
| A synthesized comment omits the expert's stable rule identifier | 6 |
| A posted sentence breaks at the source file's editor-wrapping column | 7 |
| A sentence-leading or sentence-continuing link renders on its own line | 7 |
| A finding or non-finding is justified only because nearby code uses the same pattern | 8 |
| A blind review starts before the SME report and disposition ledger are complete | 9 |
| Synthesis consumes a raw SME report before its blind audit completes | 9 |
| A blind reviewer reruns the specialty pass but never checks whether the report stands alone | 9 |
| A blind review identifies a feedback defect that remains unresolved in the branch packet | 5, 9 |
| Synthesis drafts before loading `writing-review` | 6 |
| Synthesis reaches the operator gate without passing its one blind `writing-review` | 6 |
| A separate re-verification stage repeats claim validation already owned by blind review | 9 |
| An operator change bypasses synthesis or the blind writing review | 6 |
