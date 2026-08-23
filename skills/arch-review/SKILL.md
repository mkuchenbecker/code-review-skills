---
name: arch-review
description: Architecture review of a diff, PR, branch, or module in a JVM codebase. Judges module boundaries, dependency direction, interface contracts, ownership of decisions, and error design against the principles in PRINCIPLES.md, and reports findings diagnosed at the structural cause rather than the surface pattern. Use this whenever the user asks for an architecture review, a design review, a check of boundaries, coupling, layering, or API shape, or a review of error/exception handling in Java or other JVM code — error handling questions are architecture questions and belong here.
---

# Architecture Review

Review code as a systems designer: the subject is decomposition — module topology,
interface contracts, ownership of decisions, and the failure channel. Diagnose every
finding at its structural cause (a boundary, a contract, a seam), never at the
surface pattern (a catch block). A catch block is evidence; the finding is the
misplaced boundary that made someone write it.

## Target

JVM codebases. The principles in `PRINCIPLES.md` are language-neutral; the mechanics
they are matched against are their Java instantiation, and orientation reads JVM
build files.

## Invocation

The target is the current diff by default. Also accepts a PR number, a branch (review
its diff against the default branch), or a module path (review the module as it
stands).

## Procedure

1. **Orient.** Read the build/module graph — `settings.gradle` or Maven `pom.xml`s,
   module build files, package structure, imports — to establish two facts before
   judging anything:
   - What kind of code each changed file is: domain logic, a boundary interface, or
     a translation layer at an external edge. Severity depends on this — the same
     construct can be correct in an edge translation layer and a defect in interior
     logic.
   - Which modules are allowed to depend on which. The module graph is the ground
     truth for every dependency-direction judgment. Roles are inferred from
     structure; nothing is configured.
2. **Read the diff with enough surrounding context to judge**: the callers of
   changed code, the contract being changed, and the module's existing conventions.
3. **Judge against `PRINCIPLES.md`.** Read it in full before reporting. Every
   finding names a concrete failure scenario — specific inputs or state leading to a
   specific wrong outcome. A concern that cannot be stated as a failure scenario is
   not reported.
4. **Report findings** in the contract below, as a structured chat report, ordered
   most severe first.

## Findings contract

Each finding carries:

- **location** — `file:line`
- **principle** — the principle from `PRINCIPLES.md`, cited by name
- **claim** — one sentence stating the defect
- **failure scenario** — concrete inputs or state leading to a concrete wrong outcome
- **severity** — `blocker` | `suggestion` | `nit`
- **confidence** — `confirmed` (verified against the code and its callers) |
  `probable` (consistent with everything read, not fully traced) | `speculative`
  (depends on unverified assumptions)
- **reviewer** — `arch-review`

A report with zero findings states that explicitly, with a one-line note of what was
examined.

## Posture

- Scope is deliberately narrow: the diff and the contracts it changes. Hold new API
  surface to the standard; do not demand refactors of pre-existing style. Within
  that scope, report everything true, with evidence, regardless of expected
  pushback — severity and confidence are carried in each finding so consumers can
  make their own policy decisions. A low-confidence finding is reported with its
  confidence marked low, not suppressed.
- Severity is location-dependent. Judge each construct against the role of the code
  it sits in (from the orientation step), not against a universal rule.
- Anti-noise commitments — violating either of these is itself a defective review:
  - Never recommend consolidating duplicated types that sit on opposite sides of a
    module boundary. That duplication is deliberate (see *Intentional duplication*
    in `PRINCIPLES.md`).
  - Never demand translation layers, DTO mapping, or defensive wrapping between
    components inside the same module. Interior code is trusted (see *Trust
    interiors*).
