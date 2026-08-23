# Design documents

A design document is judged against this structure, in this order:

1. **Problem statement.** What is broken or missing, and why the document
   exists. Nothing downstream is judgeable without it.
2. **Requirements**, tiered as must / should / won't / out of scope. The
   requirements come before the options so that the recommendation is checkable
   against criteria committed in advance, not rationalized after the choice.
   "Won't" and "out of scope" are load-bearing: they are the document's declared
   non-goals, written down so omissions are not relitigated in review.
3. **Options with a recommendation, as a table.** Options are rows; the
   requirements become the columns; the recommendation is marked. The table's
   accompanying sentence states the recommendation and the deciding criterion.
4. **Sketch.** The chosen option's shape, one screen.
5. **Details.** The chosen option in full. Only the chosen option is elaborated
   in the main line.
6. **Appendix.** Alternative sketches, background, and definitions. The
   alternatives live here as evidence that they were genuinely developed; the
   options table is the verdict, the appendix is the evidence.

## What to flag

| Defect | Why it matters |
|---|---|
| Recommendation absent, buried in details, or stated only at the end | The document's conclusion is its recommendation; readers triage on it |
| Options presented before requirements, or requirements absent | The recommendation cannot be checked against pre-committed criteria; the document reads as advocacy |
| No "won't" or out-of-scope statements | Scope ambiguity survives into review; omissions get relitigated as oversights |
| An option's criteria columns don't match the stated requirements | The evaluation quietly used different criteria than it committed to |
| A rejected alternative elaborated in the main line | The main line is for the chosen path; alternatives are appendix evidence |
| Alternatives absent entirely | A decision with no developed alternatives is a conclusion without a comparison; note it at low severity when the change is small enough not to warrant one |

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
