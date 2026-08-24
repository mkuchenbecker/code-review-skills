# Test types

A catalog of test types. Reach for a type when its trigger holds; a demanded
or planned test names the type it is. The middle columns decide; the last
says what a failure of that test means, which is what the responder reads.

| Type | Reach for it when | Claim source; level | A failure means |
|---|---|---|---|
| Type-level constraint | The invalid state can be made unrepresentable | Invariant; types | It did not compile; the cheapest catch that exists |
| Production assert | The branch is unreachable by construction but the invariant is load-bearing | Invariant; production | The model of the world broke in the field |
| Unit (example) | A pure interior function, and no stronger oracle is free | Invariant or contract; interior | The logic is wrong |
| Property-based | The claim is an invariant over an input space bigger than the author's imagination | Invariant; interior | The invariant fails for a generated input, minimized |
| Fuzz | The code parses external input at a boundary | Defined behavior at the parse seam; contract | Malformed input reached past the seam |
| Contract (provider) | A module surface promises enumerated outcomes, failure modes included | Defined behavior; contract | The promise broke |
| Fault-injection | A dependency fails in production in ways not deterministically reproducible | Defined behavior of the failure channel; contract | The failure path mishandles the fault, for example a state-unknown timeout retried |
| Composition | Two owned modules meet at a boundary with a translation | The mapping; composition | The translation is wrong: not total, or a distinction lost |
| System matrix | The product surface crosses substrates and interactions are the question | Product surface definition; system | Wiring, interaction, or a discovery |
| Differential | Two computations of the same result exist, or an independence claim makes one free | Independence hypothesis; any level | The legs diverge; the independence claim is false |
| Metamorphic (delta) | The absolute state is unstatable across substrates but the change is statable | Contract as a relation; any level | The operation's delta is wrong |
| Stateful lifecycle | The contract is a state machine | Defined lifecycle; contract or system | A transition, or a consumer of destroyed state, broke |
| Interleaving (forced) | Shared state exists and a specific race is conjectured or was seen flaking | Invariant; composition or system | The race is real and now reproducible |
| Stress with invariants | The interleaving cannot be forced | Invariant; system | An invariant broke under contention; the assertion is never an example |
| Pin | Behavior exists that no document defines | Observation; any level | Behavior changed; decide whether to promote or update |
| Smoke | Assembly itself is the question | Wiring; system | The system does not come up |
| Golden or snapshot | The output artifact is the contract, byte for byte | Defined behavior; contract | The artifact changed. Caution: without that definition this is a pin wearing a contract's label |
| Mutation | The suite itself is under evaluation | The instrument; meta | A seeded defect survived the suite; the mechanical form of "see each test fail once" |

Benchmarks and load tests answer performance claims and belong to a
performance review, not this one; they appear here only so their absence
reads as a scope statement rather than an omission.
