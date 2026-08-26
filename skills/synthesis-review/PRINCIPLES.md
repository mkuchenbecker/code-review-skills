# Principles

Experts testify; synthesis commits. A review's experts answer "what is true here?", each inside
one coherent worldview, and their reports are testimony: evidence-backed, severity-graded, and
deliberately unfiltered. Synthesis answers a different question — "what do we ask of this change,
now?" — and everything below derives from keeping those two questions separate. Each principle
is stated with its reason; the log cites principles by name.

## 1. Truth and policy separate

An expert who ranks self-censors: knowing that a finding will be argued down invites not
reporting it, and the testimony degrades. A synthesizer who re-litigates truth substitutes a
generalist's opinion for a specialist's evidence. So experts report everything true and never
tier it, and synthesis tiers everything and never overrules a verified claim. The one gate
between them is verification, which checks the finding against the target, not against taste:
does the location exist, does the quote match, is the claim consistent with the code around it?
A finding is returned for failing those checks, never for being unwelcome.

## 2. Corroboration is signal

Two experts reaching the same defect through different worldviews is independent measurement,
and independent agreement is the strongest confidence evidence a review produces. Deduplication
that discards the second report as noise throws that signal away. So merging keeps every
source: one comment, all reviewers cited, confidence and rank raised. The merge test is
same location and same failure; a shared cause with different symptoms stays separate comments
naming the common cause, because each symptom needs its own fix conversation.

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

Every input finding ends as a comment or a log entry. A finding dropped without a trace is a
swallowed exception at the review level: the expert's question was asked and the answer was
discarded where no one can see it. The same holds for disagreements — averaging two positions
or quietly picking one erases the information that qualified reviewers disagreed, which is
itself a signal the author deserves. The log states both positions and the deciding reason;
the comment carries the ruling.

## 6. The output is the feedback, not a description of it

A synthesis that ends in "themes" and "recommendations" moves the real work — writing the
comment someone will read at a file and line — to whoever posts it, and that translation is
where precision dies. So synthesis converges: the deliverable is the exact comment set,
tagged `[category][tier]`, one comment per location, each self-contained (what happens now,
what should happen and why, the fix), ordered most severe first, numbered once and never
renumbered — revisions append, because stable numbers are what let a discussion refer to
"finding 12" a week later.

## 7. Physical newlines are output semantics

An exact comment includes its Markdown layout. GitHub review comments render physical source
newlines as visible line breaks, so prose wrapped to an editor column reaches the reader as broken
sentences. Synthesis writes each prose paragraph on one physical line and keeps newlines only where
Markdown structure needs them: paragraph separators, headings, list items, blockquotes, tables,
code blocks, and explicit hard breaks. This normalization happens before the operator gate because
each approved exact comment body and its posted Markdown body field must be byte-for-byte
equivalent.

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
| A posted sentence breaks at the source file's editor-wrapping column | 7 |
| A sentence-leading or sentence-continuing link renders on its own line | 7 |
