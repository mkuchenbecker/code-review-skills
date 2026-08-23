# Principles

Each principle is stated with its reason, in language-independent terms; where a
principle needs something concrete, the example is Java. Findings cite principles
by name.

## Axis 1 — Topology: which components may know which

**Directed dependencies.** Modules form a partial order: lower components never
reference higher ones, and the build graph declares the order. A component that
references its consumers — through an import, a type, or knowledge of their
vocabulary — cannot be tested, reused, or evolved without them. Cycles and upward
references are the finding regardless of which channel carries them.

**Owned vocabulary.** A module's interface, including its failure modes, is expressed
in the module's own vocabulary; translation layers at the edges convert between the
outside world and that vocabulary. Reason: when another technology's types appear in
a module's interface — a storage library's unchecked exception escaping through a
domain-owned interface is the common case — the module depends on that technology
through the type system, often with no import to give it away (only the callers that
handle the foreign exception import the vendor package), and can no longer swap
implementations or test against fakes. (Hexagonal architecture, "ports and adapters",
illustrates this rule and adds an inward direction rule for a single core; it does
not order multiple modules against each other — that is Directed dependencies.)

**No shared types across boundaries.** A type shared across a module boundary couples
the evolution of both sides: adding a variant for one side's needs silently changes
the other side's contract. Shared "common-types"/"common-errors" modules are how this
happens in practice — deduplication hygiene that ends with every module coupled
through one file. Publishing your own types to your direct callers is the contract;
the violation is vocabulary that passes through a module unchanged, coupling that
module's callers to its dependency.

**Intentional duplication.** Near-identical types on the two sides of a boundary (a
wire DTO, a domain type, a storage row) are the firewall that lets each side evolve
alone; consolidating them re-couples what the boundary decouples. Review the mapping
functions between the copies — are they total, tested, and distinction-preserving? —
never the duplication itself.

**Minimal knowledge.** A component can only mishandle what it knows about. Every
field a component consumes is surface it must fear (null? missing? stale?); a
contract narrowed to exactly what is needed makes whole classes of defect
unreachable rather than handled. Prefer designs where the rules above are
unbreakable by construction — where the vocabulary a component would need to violate
them is never in its hands.

**Containment without interpretation.** At inversion-of-control points (callbacks,
listeners, visitor hooks) a lower component legitimately holds a reference upward.
Containment is correct and often required: a dispatch loop — an event bus, a
listener multicaster, an executor — catches broadly around each callback so one
failure cannot kill the loop or starve siblings; dispatch loops are entry points.
What the dispatcher must never do is interpret the failure's type or content to make
domain decisions: that is policy made with vocabulary it does not own. Isolate,
report to an error handler, continue; never inspect-and-branch.

## Axis 2 — Contracts: what a signature promises

**Enumerable outcomes.** The complete set of a call's outcomes — success shapes and
failure modes — is visible in its signature. When outcomes are discoverable only from
the implementation, every caller either over-defends (blanket handling "just in
case") or under-defends (breaks on the case nobody stated); both are symptoms of a
hidden contract. A generic failure (`RuntimeException("failed")`, an untyped error
value) gives the caller two options, swallow everything or handle nothing, which
means the callee has decided for them by erasure.

**Parse, don't validate.** A boundary that merely checks input and passes the
original representation along forces every downstream layer to either trust the
check (nothing enforces it) or re-check (the same test smeared across layers with no
owner). A boundary that converts input into a type that cannot represent invalid
states makes downstream code correct by construction; a re-check becomes visibly
redundant — a null check on a non-nullable type is itself a reportable smell. A
precondition deep in the call stack that external data can still reach means the
parse didn't happen; the finding is at the boundary, upstream of the check.

**Outcomes as values.** A result modeled as a sum type — one value that is either
the success or one of the enumerated failures — makes the failure branch visible at
the call site, where an exception leaves it invisible and optional to think about.
How strongly the language enforces the branch depends on the mechanism, and the
distinction matters. Example (Java): a sealed interface with record variants
consumed by exhaustive `switch` expressions makes the branch a compile-time
obligation — add a variant and every call site fails to compile until it accounts
for it, where a new unchecked exception is noticed only in production. Example
(protobuf): a `oneof success|failure` makes the failure representable and visible
but not compiler-enforced (generated accessors return default instances without a
case check), so it demands runtime case-checking discipline. In any language: a
result that can be silently discarded is a swallowed failure — results must be
consumed (exhaustive matching, or enforcement such as Java's `@CheckReturnValue`).

**Absence is a type.** A null/nil return conflates "legitimately absent" with
"someone forgot" and defers the failure to a dereference far from the cause;
expressing absence in the type — an option type, an empty collection, a
compiler-checked nullability annotation — states it in the signature and forces the
decision at the source. Example (Java): return `Optional` or an empty collection,
never a null collection; a bare `Optional.get()` reintroduces the deferred crash
and reads as an unchecked null dereference; `Optional` in fields or parameters adds
a third state (a null `Optional`) without expressing anything — it earns its place
in return types.

**Narrow contracts, one seam.** When wide data must enter a component, project it at
a single seam: one function that turns the wide representation into exactly what the
component needs, where all judgment about missing or malformed data lives. Judgment
with an address can be reviewed and changed; judgment smeared as repeated checks
across layers has no owner, so every layer defends. Parsed inputs consumed through
narrow contracts make interior functions total — no failure cases left — which is
when control flow composes cleanly in pipelines, comprehensions, and folds.
Example (Java): a `Stream` pipeline stays clean when its stages are total, and a
try/catch inside a stream lambda, or a checked exception wrapped in
`RuntimeException` to get through `map()`, is the sign that IO or an unhandled
boundary was smuggled into interior code.

**Contract evolution is API evolution.** Consumers build policy on a contract's
outcome set — what they retry, surface, ignore. Adding, removing, or reshaping
outcomes breaks consumers exactly as a changed return type would; exception-based
contracts break silently, which is a reason to prefer mechanisms where the compiler
announces the change (Outcomes as values).

## Axis 3 — Ownership: which component decides

**Caller owns policy.** Only the caller knows the context of an operation — whether
the result is critical or best-effort, whether time remains for a retry, what else
has failed. A callee that swallows, logs-and-continues, or retries on its own makes
decisions with information it does not have and removes options from the party that
has them. Libraries are the limiting case: a library is interior code hosted in
someone else's process, so it reports through its published contract and never
logs-and-swallows, retries on its own, or exits.

**Trust interiors.** Inside a module, components share one author's invariants;
there is nothing for them to defend against each other. Defensive machinery between
same-module components — try/catch around an internal call, retries around
deterministic in-process code — points at a defect: either the internal component
signals failure where it should return a value or uphold an invariant (the finding
is that component's contract), or the author distrusts the module's own invariants
(the finding is the misplaced boundary). Aim the finding at the component's
contract, with fixing the component — so the catch can be deleted — enumerated as
the enabling option; the catch itself is the evidence, not the target.

**One boundary, one translation.** Each boundary crossing owns exactly one
translation of what passes through it. Translating twice within one hop adds nesting
without information; a raw low-level type surfacing several layers up identifies
precisely which intermediate boundary failed to do its job — the module order names
it.

**Thin, total protocol surfaces.** An HTTP 404, a gRPC `NOT_FOUND`, a CLI exit code
are each one surface's rendering of an outcome the core defined; the core concept
("record absent") exists independently of all of them. Modeling the core in protocol
terms — a domain exception annotated with an HTTP status, a service method throwing
`ResponseStatusException` — couples the core to one of its surfaces and points the
dependency backwards. Each surface is a total mapping from the core's enumerated
outcomes to its protocol, so that when the core adds an outcome, every surface must
consciously choose a rendering. A deliberate default ("anything unmapped → 500") is
honest; a blanket `catch (Exception) → 500` erases distinctions the core drew.

## Axis 4 — The failure channel

The principles above, applied to failures specifically.

**Failures at boundaries only.** Signaling failure is meaningful where trust changes:
a module's external surface, where inputs may be invalid and dependencies may be
down. Interior code returns values and raises invariant violations; exceptions
appearing in interior logic mean a boundary is missing or misplaced.

**Defined behavior for bad input.** Incorrect input is an expected outcome and
deserves defined behavior stated in the signature. An undeclared throw (or panic)
reachable through expected input means the true contract lives in the
implementation; callers discover it experimentally and grow blanket defensive
wrappers — a caller re-deriving a contract the signature should have stated.
Example (Java): `IllegalArgumentException` on user-reachable input is the canonical
case, and a catch of `IllegalArgumentException` thrown by code the author controls
is proof of the mismodeling — the case was expected, yet declared exceptional.
Exemption: platform and third-party APIs whose only failure channel is such a throw
(`Integer.parseInt` throwing `NumberFormatException`, `Enum.valueOf`,
`UUID.fromString`) — catching at that parse seam and converting to a typed result
is exactly what Parse-don't-validate asks for.

**Fail fast when decidable; halt when not.** If the code can classify what went
wrong and a caller could act on it, detect at the earliest point and return the
typed failure — continuing on bad state moves the eventual break far from its cause.
If an invariant the code relies on is broken, any result would be a lie and there is
no meaningful error to return: raise an invariant violation that no interior code
catches. Example (Java): `checkState`, or an explicit `IllegalStateException` — not
the `assert` keyword, which is disabled at runtime unless `-ea` is set and so
guards nothing in production. The test for any throw site: could this code have
decided something?
Yes → typed failure. Genuinely no → the invariant violation is correct, and if
external input can reach it, the real finding is the missing parse upstream.

**Dependency failure is its own category.** The core sees "a dependency failed", not
`AmazonS3Exception` — which vendor failed is edge knowledge (Owned vocabulary). The
category still carries what the core can act on: retryable or not, state known-failed
or state-unknown (the difference between "safe to retry" and "you may have already
committed"). Two hard rules at the edges: never surface an infrastructure failure as
a domain outcome (a storage timeout reported as "record absent" converts an outage
into a wrong answer that propagates as truth), and never render a dependency failure
as the caller's fault (502/503-family, not 400; vendor detail goes to edge logs, not
the client).

**Failures flow low to high, translated once per boundary.** An error type is a
dependency like any import, so the failure channel respects the module order
(Directed dependencies) — including the couplings the compiler cannot see: matching
on another layer's message strings, shared magic error codes, re-parsing a rendered
error. The cause chain (wrapped exceptions' `cause`) is the legitimate record of the
descent, which is why destroying it — wrapping without the cause, logging only
`getMessage()` — destroys the evidence debugging needs.

## Symptoms

Concrete local patterns worth flagging, given here in their Java form — the same
symptoms exist in every language under different idioms, so translate rather than
skip when reviewing something else. Each finding names the principle, not just the
pattern:

- Empty catch; catch-log-continue; return-null-on-failure — the caller proceeds as
  if the operation succeeded (Enumerable outcomes, Caller owns policy).
- Wrapping without the cause; logging `e.getMessage()` instead of the exception —
  destroys the descent record (Failures flow low to high).
- `catch (Exception)` / `catch (Throwable)` anywhere except a true entry point — a
  request handler, a job runner, a thread's main, a callback dispatch loop — absorbs
  unrelated bugs alongside the anticipated failure (Failures at boundaries only,
  Containment without interpretation).
- Log-and-rethrow — two layers claiming one decision; the log line duplicates at
  every level (Caller owns policy, One boundary one translation).
- try/catch or retries around calls to the same module's internals (Trust
  interiors).
- try/catch inside a stream lambda; checked exceptions wrapped in `RuntimeException`
  to get through `map()` (Narrow contracts, one seam).
- The same null/absence check repeated across layers for the same data (Parse don't
  validate; Narrow contracts, one seam).
- `InterruptedException` caught without restoring the interrupt flag; executor and
  future failures no code observes; cleanup in `finally` that throws and masks the
  original exception (Enumerable outcomes, Caller owns policy).
- Error messages without identifying context — which table, which id, which path —
  leaving the failure record unable to support debugging (Failures flow low to
  high).
- Secrets or internal detail (connection strings, vendor error bodies) leaking
  through a protocol surface (Thin total protocol surfaces, Dependency failure is
  its own category).

## Triage vocabulary

Used when judging whether distinctions survive a boundary: invalid-input, absent,
conflict, dependency-failure, state-unknown, internal-invariant. A lens for review —
"which of these is this, and does the code preserve that across the boundary?" — not
a required shape for any API; no findings for failing to conform to it.

## The shape the principles produce

In miniature: a pure, stateless algorithm module whose core is a stream fold; all IO
and job lifecycle in the scheduling component at its edge; the algorithm's item
contract consuming one field of a wide stats object through a single projection
function; the only throw a builder-misuse invariant violation. A module shaped like
this yields near-zero findings.

An end-to-end example of the stance: Service Weaver (Google's modular-monolith
framework) defined applications as components behind typed interfaces where every
cross-component call is fallible in its signature — because the runtime may execute
any call as an RPC — with transport failure injected as its own distinguishable
error type and HTTP listeners kept as thin edges. It answered the objection that
"you can't hide the network" by hiding only location while surfacing fallibility in
the type: the network's failures became enumerable contract instead of hidden
surprise. The framework is in maintenance mode; the stance is the point.
