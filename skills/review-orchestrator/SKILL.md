---
name: review-orchestrator
description: Plan and execute a complete multi-reviewer code review. Builds the frozen review bundle and Graphviz dependency graph, composes coherent SME charters from committed skills and domain references, runs a dependent blind quality gate after every SME, synthesizes only completed branch packets, runs one blind writing review over synthesis, persists every artifact locally, and holds publication at the operator gate until the exact payload is approved. Use for comprehensive PR reviews, review DAG planning, multi-agent review coordination, or any request to apply the code-review-skills workflow end to end.
---

# Review orchestrator

The orchestrator is the person or agent executing the review workflow. It owns the plan, dependency graph, reviewer inputs, local artifacts, hand-offs, and publication boundary. It does not replace specialist judgment. SME reviewers establish findings, blind reviewers quality-check each SME report, and `synthesis-review` decides the final feedback.

## Inputs

| Input | Content |
|---|---|
| Target | Repository and immutable base and head identifiers |
| Request | The user's review goals, named experts, exclusions, and output requirements |
| Description | PR title, description, issue links, deployment notes, and changed-file inventory |
| Governing sources | Specifications, explicit user rules, approved contracts, design decisions, and skill criteria |
| Posture | Callers, flags, rollout state, compatibility constraints, deployment order, and cost of a later fix |
| Local destination | Repository `output/` directory unless the user names another location |

If the user requires a plan before code inspection, build the first DAG from the description and changed-file inventory only. Record that planning boundary so no reviewer mistakes the preliminary graph for a completed code judgment.

## Role input boundaries

Give each role the information required for its decision and no downstream output that would anchor it.

| Role | Receives | Must not receive yet |
|---|---|---|
| Orchestrator during planning | Request, description, changed-file inventory, governing references, posture available without code judgment | SME findings or synthesis |
| SME reviewer | Frozen bundle, branch charter, assigned skills and references, assigned changed surface, supporting source context | Other SME reports, branch blind reviews, synthesis, operator reactions |
| Branch blind reviewer | Frozen bundle, branch charter, complete SME report, disposition ledger, `writing-review` | Other branch reports, synthesis, operator reactions |
| Synthesis | Frozen bundle, posture, all completed branch packets, `synthesis-review`, `writing-review` | Operator edits that have not been made yet |
| Blind reviewer of synthesis | Complete synthesis output and `writing-review`; frozen bundle only for a fact synthesis materially changed | Raw SME work not carried into the completed branch packets |
| Operator | Exact local review body, comment set, and adjudication log | Nothing hidden that affects the approval decision |

The orchestrator may use different agent implementations for these roles. Role dependencies and inputs matter; agent product names do not.

## Source hierarchy

Correctness is judged in this order:

1. Explicit user requirements and exclusions.
2. Governing specifications and approved contracts.
3. The assigned skill principles and SME charter.
4. Target behavior, tests, documentation, callers, and repository patterns as evidence.

Existing code is never authority for repeating a defect. It can establish compatibility constraints, migration cost, or blast radius. A small adapter or enabling refactor needed to keep changed code from deepening the defect remains in scope; unrelated cleanup does not.

## Build the review bundle

Create one immutable bundle before launching reviewers:

- base and head identifiers;
- changed-file inventory and diff;
- supporting callers, callees, types, tests, and adapters needed to trace the change;
- PR description and linked requirements;
- governing specifications and explicit user rules;
- deployment posture;
- one charter per SME branch;
- the planned dependency graph.

Persist the Graphviz source as `output/<target>-review-dag.dot`. Return a clickable local link using an absolute `file:///` target or a `~/` path. Every other link in reports and comments must also be clickable.

## Compose SME branches

A branch represents one coherent review question, not one skill name. One SME may load multiple skills or domain references when they support the same judgment and need the same context. For example, an architecture SME reviewing an Iceberg view discriminator can load both `arch-review` and the Iceberg View Spec. Combining them is better than splitting one semantic question across two agents that must reconstruct the same boundary.

Keep branches separate when they require different reading modes, evidence, or output contracts. The line-by-line `pedantic-linter` pass remains separate from architecture synthesis. Testing remains separate because it derives falsifying claims and test placement. Writing remains separate when changed prose itself is under review.

| Branch shape | Reviewer inputs |
|---|---|
| Local cleanliness | Frozen bundle, `pedantic-linter`, full changed hunks, directly affected context |
| Architecture plus domain semantics | Frozen bundle, `arch-review`, one or more governing domain specifications, relevant callers and adapters |
| Testing | Frozen bundle, `testing-review`, contracts, control flow, and the whole relevant test suite |
| Changed prose | Frozen bundle, `writing-review`, changed documentation, comments, PR text, and reader obligations |
| Ad-hoc domain SME | Frozen bundle, a written charter, authoritative references, assigned source surface, and the common findings contract |

Do not combine specialties merely to reduce agent count. Combine them only when they answer one shared question under a coherent worldview.

## Write each SME charter

Every charter states:

- the question the SME must answer;
- the skills and authoritative references to load;
- the files, changed ranges, and supporting context it receives;
- the governing source hierarchy;
- required audit categories;
- explicit exclusions;
- the common findings contract;
- the required disposition ledger.

Each SME reports evidence-backed findings and records every accepted, exempted, waived, tabled, delegated, pruned, non-gating, non-actionable, or accepted-as-conforming candidate in its disposition ledger. Repository precedent alone is never a valid disposition reason.

### Common branch contracts

Committed skills may add fields, but every finding has this minimum shape:

| Field | Content |
|---|---|
| location | Exact file and line, changed range, test id, or section |
| principle | Skill rule, governing specification, or charter criterion |
| claim | One sentence stating the defect |
| evidence | Quoted target evidence and supporting contract fact |
| consequence | Concrete failure scenario, reader harm, or maintenance defect |
| remediation | Smallest fix, required decision, or option set defined by the SME charter |
| severity | `blocker`, `suggestion`, or `nit` as specialist testimony |
| confidence | `confirmed`, `probable`, or `speculative` |
| reviewer | Branch identity and optional stable rule identifier |

Every disposition row has:

| Field | Content |
|---|---|
| location | Candidate location or audit category |
| candidate | Concern considered |
| disposition | Accepted, exempted, waived, tabled, delegated, pruned, non-gating, non-actionable, or accepted as conforming |
| evidence | Target evidence considered |
| governing reason | Specification, explicit requirement, principle, or charter rule supporting the disposition |

## Dependency graph

Each branch follows:

```text
Grok -> SME review -> blind quality gate -> synthesis
```

All SME branches may run in parallel after Grok. A branch blind review starts only after its SME review is complete. Synthesis starts only after every selected blind review passes.

The final path is:

```text
synthesis -> blind review of synthesis -> operator gate -> publish
```

There is one synthesis stage and one blind review after it. The operator has an unconstrained feedback edge back to the same synthesis stage. Any operator change must pass the same blind review again.

## Run the branch blind quality gate

The branch blind reviewer is a separate quality reviewer of the completed SME work, not a second specialty reviewer. It receives the frozen bundle, charter, complete SME report, and disposition ledger.

It applies two lenses:

1. Load `writing-review` and decide whether the feedback makes sense and stands on its own. Each finding must state the defect, evidence, consequence, and smallest fix without requiring the SME to explain it.
2. Decide whether the SME achieved the charter under its governing specifications, skill principles, mandatory audit categories, scope rules, and source hierarchy.

The blind reviewer validates every SME claim and citation: locations exist, quotes match, links resolve, and consequences follow from the governing contract. It second-guesses every non-finding disposition, especially any disposition justified by existing code. It inspects source only as needed for these checks and does not rerun the full specialty pass.

Return defective feedback to the SME for correction. The branch is complete only when the blind reviewer validates the revision and issues a pass verdict.

The completed branch packet contains:

- final SME report and disposition ledger;
- `writing-review` result;
- charter and principles audit;
- correction and validation log;
- blind pass verdict.

## Synthesize

Run `synthesis-review` only on completed branch packets. Before drafting, synthesis loads `writing-review` and its current criteria. Synthesis trusts facts validated by branch blind reviewers unless it materially changes a factual claim, quote, consequence, or fix. It merges duplicates, adjudicates cross-branch disagreements, tiers survivors against posture, preserves stable rule identifiers, and produces:

- the exact review body;
- the exact numbered comment set;
- the adjudication log.

Run one blind review over the complete synthesis using `writing-review`. The same synthesis stage corrects any writing defect and resubmits to that same blind reviewer. There is no second synthesis stage and no separate claim re-verification stage.

## Operator gate and publication

The operator is the human approving the finished feedback. Present the exact local review body and comments. Any operator edit returns to the same synthesis stage through the unconstrained feedback edge, preserves stable numbering, and passes the same blind writing review again.

Do not publish, post, edit a PR, or send feedback externally until the operator explicitly approves the exact payload. Publication uses the approved bytes:

- one comment per location;
- `[category][tier]` or `[category][rule-id][tier]`;
- one physical line per prose paragraph;
- Markdown newlines only where structure requires them;
- attribution naming this repository as the review criteria.

Every published artifact identifies its criteria and generator:

| Medium | Attribution |
|---|---|
| Review body | This repository as the criteria, plus the platform-generated footer |
| Inline comment | Source category and stable rule identifier when one exists |
| Commit containing review artifacts | The platform co-author trailer; never a model name inside the artifact |

## Local state

Give every role a distinct checkpoint path named by target, branch, and role. The SME review must be complete before its branch blind reviewer starts. The branch blind review must be complete before synthesis starts.

Validation has one owner at each boundary:

- branch blind review validates SME claims and dispositions;
- synthesis validates facts it materially changes;
- the final blind review validates synthesis prose;
- the operator approves publication.

## PR 683 example

[OpenHouse PR 683](https://github.com/linkedin/openhouse/pull/683) adds an entity discriminator across schema, persistence, queries, API models, mappings, callers, and tests as groundwork for Iceberg views. Its architecture question and [Iceberg View Spec](https://iceberg.apache.org/view-spec/#overview) question share the same namespace and contract boundary, so one SME loads both `arch-review` and the view specification.

The durable Graphviz example is [PR 683 review graph](https://github.com/mkuchenbecker/code-review-skills/blob/main/skills/review-orchestrator/examples/pr-683-review-dag.dot).

| SME branch | Criteria loaded |
|---|---|
| Pedantic line review | `pedantic-linter` |
| Architecture plus Iceberg view semantics | `arch-review` plus the Iceberg View Spec |
| Testing | `testing-review` |
| Changed prose | `writing-review` |
| HTS persistence and Spring Data | Ad-hoc persistence charter |
| MySQL migration and rollout | Ad-hoc rollout charter |
| API compatibility | Ad-hoc API charter |

The example demonstrates that skills are reviewer inputs, not a fixed one-skill-per-agent topology.
