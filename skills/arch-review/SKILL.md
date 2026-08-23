---
name: arch-review
description: Architecture review of a diff, PR, branch, or module. Judges module boundaries, dependency direction, interface contracts, ownership of decisions, and error design against the principles in PRINCIPLES.md, and reports findings diagnosed at the structural cause rather than the surface pattern. The principles are language-agnostic; their examples are given in Java. Use this whenever the user asks for an architecture review, a design review, a check of boundaries, coupling, layering, or API shape, or a review of error/exception handling in any language; error handling questions are architecture questions and belong here.
---

# Architecture review

Review code as a systems designer. The subject is decomposition: module topology,
interface contracts, ownership of decisions, and the failure channel. Diagnose every
finding at its structural cause (a boundary, a contract, a seam), never at the
surface pattern (a catch block). A catch block is evidence; the finding is the
misplaced boundary that made someone write it.

## Target

Any codebase with discernible module structure. The principles in `PRINCIPLES.md`
are language-agnostic; where a principle needs something concrete, its example is
Java, and the symptom catalog gives Java instantiations. When reviewing another
language, translate the symptom to that language's idiom rather than skipping it.

The unit of review is a body of code, however it is selected: the current diff (the
default), a PR number, a branch (its diff against the default branch), or a module
path. A diff is a way of selecting code under review, not a different kind of
review: the same analysis applies, with the diff marking where change is happening.

## Procedure

1. **Orient.** Read the build/module graph in whatever form the build system
   declares it (Gradle `settings.gradle` and module build files, Maven `pom.xml`s,
   a Cargo or Go workspace, package manifests), plus package structure and imports,
   to establish two facts before judging anything:
   - What kind of code each file under review is: domain logic, a boundary
     interface, or a translation layer at an external edge. The same construct can
     be correct in an edge translation layer and a defect in interior logic.
   - Which modules are allowed to depend on which. The module graph is the ground
     truth for every dependency-direction judgment. Roles are inferred from
     structure; nothing is configured.
2. **Read the code under review and its callers.** Caller analysis is not optional
   context: it is how contracts are judged (who actually depends on what) and how
   the feasibility of any restructuring is assessed (what a change to this seam
   would pull in).
3. **Judge against `PRINCIPLES.md`.** Read it in full before reporting. Every
   finding names a concrete failure scenario: specific inputs or state leading to a
   specific wrong outcome. A concern that cannot be stated as a failure scenario is
   not reported.
4. **Where the code fights its surroundings, classify the impedance mismatch.** When
   a change is awkward, contorting to fit the shape of the code it lands in, the
   surrounding architecture is in scope, not just the lines under review. Go one
   level deeper: describe, in a vacuum, the ideal shape of this seam; then use the
   caller analysis to determine what stands between the code and that shape.
5. **Enumerate options before grading.** For each structural finding, put the real
   options on the table before judging severity:
   - accept the code as it stands;
   - a small enabling refactor first, after which the change flows: name the
     refactor and what it touches;
   - the larger restructure: name its blast radius from the caller analysis.
   Grading happens after the options exist, because severity depends on the option
   set: a mismatch resolved by a small enabling refactor is actionable now and
   graded on that basis; one requiring a migration is reported with its cost, not
   silently dropped and not graded as if the cheap fix existed.
6. **Report findings** in the contract below, as a structured chat report, ordered
   most severe first. Write the report itself per the writing rules at
   https://github.com/mkuchenbecker/humanizer/blob/main/STRUCTURE.md (verdict
   first; tables where findings share fields; no hedging that would not change a
   conclusion). If that reference is unreachable, proceed without it; it is a
   style dependency, not a correctness one.

## Findings contract

Each finding carries:

- **location**: `file:line`
- **principle**: the principle from `PRINCIPLES.md`, cited by name
- **claim**: one sentence stating the defect
- **failure scenario**: concrete inputs or state leading to a concrete wrong outcome
- **options**: for structural findings, the enumerated remediation options
  (as-is / enabling refactor / restructure), each with what it touches per the
  caller analysis
- **severity**: `blocker` | `suggestion` | `nit`, graded after the options exist
- **confidence**: `confirmed` (verified against the code and its callers) |
  `probable` (consistent with everything read, not fully traced) | `speculative`
  (depends on unverified assumptions)
- **reviewer**: `arch-review`

A report with zero findings states that explicitly, with a one-line note of what was
examined.

## Posture

- The code under review is the entry point, not the fence. Code that doesn't make
  sense because the architecture around it is already broken is squarely in scope:
  that is when the review earns its name, by classifying the mismatch, describing
  the ideal shape, and enumerating the path there. "Pre-existing" is context that
  shapes the options and the grade, never grounds for silence.
- Report everything true, with evidence, regardless of expected pushback; severity
  and confidence are carried in each finding so consumers can make their own policy
  decisions. A low-confidence finding is reported with its confidence marked low,
  not suppressed.
- Severity is location-dependent. Judge each construct against the role of the code
  it sits in (from the orientation step), not against a universal rule.
- Anti-noise commitments (violating either of these is itself a defective review):
  - Never recommend consolidating duplicated types that sit on opposite sides of a
    module boundary. That duplication is deliberate (see *Intentional duplication*
    in `PRINCIPLES.md`).
  - Never demand translation layers, DTO mapping, or defensive wrapping between
    components inside the same module. Interior code is trusted (see *Trust
    interiors*).
