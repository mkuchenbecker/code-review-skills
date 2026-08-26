# Design documents

A design document is judged against this structure, in this order:

1. **Problem statement.** What is broken or missing, and why the document
   exists. Nothing downstream is judgeable without it.
2. **Requirements**, tiered as must / should / won't / out of scope. The
   requirements come before the options so that the recommendation is checkable
   against criteria committed in advance, not rationalized after the choice.
   "Won't" and "out of scope" are load-bearing proposals, not immunity from
   review. Validate them against governing requirements and reader obligations.
   Once validated, they prevent accidental relitigation of the same omission.
3. **Options with a recommendation, as a table.** Options are rows; the
   requirements become the columns; the recommendation is marked. The table's
   accompanying sentence states the recommendation and the deciding criterion.
4. **Sketch.** The chosen option's shape, one screen.
5. **Details.** The chosen option in full. Only the chosen option is elaborated
   in the main line.
6. **Appendix.** Alternative sketches, background, and definitions. The
   alternatives live here as evidence that they were genuinely developed; the
   options table is the verdict, the appendix is the evidence.

This order is the genre's layering, and it takes precedence over the general
skeleton for section order: within a design document, the options table's
recommendation is the document's conclusion, and a document that follows the
six sections complies with Conclusion on top and Layering at the document
scale. Those rules still govern inside each section (every section leads with
its point). Judge conclusion placement against this order, never against the
general TLDR-first skeleton.

## What to flag

The table lists the genre's defects with the severity each normally carries;
the first two rows dominate, because they decide whether the document can
function as a decision record at all.

| Defect | Why it matters | Severity |
|---|---|---|
| Recommendation absent, buried in details, or stated only at the end | The document's conclusion is its recommendation; readers triage on it | `blocker` |
| Options presented before requirements, or requirements absent | The recommendation cannot be checked against pre-committed criteria; the document reads as advocacy | `blocker` |
| A "won't" or out-of-scope claim conflicts with a governing requirement or has no stated basis | The document tables required work by declaration instead of making a reviewable scope decision | `blocker` when it removes a must; otherwise `suggestion` |
| No "won't" or out-of-scope statements | Scope ambiguity survives into review; omissions get relitigated as oversights | `suggestion` |
| An option's criteria columns don't match the stated requirements | The evaluation quietly used different criteria than it committed to | `suggestion` |
| A rejected alternative elaborated in the main line | The main line is for the chosen path; alternatives are appendix evidence | `suggestion` |
| Alternatives absent entirely | A decision with no developed alternatives is a conclusion without a comparison | `nit` when the change is small enough not to warrant them; otherwise `suggestion` |

## Sections scale with the change

The six sections are the shape, not a quota. A small design may merge the sketch
and the details into one section, or fold background into a sentence instead of
an appendix; the absence of a separate section is not a defect when the content
it would carry is present and sufficient at the change's size. Flag a missing
layer only when a reader is left without something they need (an implementer
with no specifics, a reviewer with no comparison), not because a heading from
this file is absent.

The general structure rules in the writing reference apply on top of this genre
structure; this file adds the design-doc shape, it does not replace them.
