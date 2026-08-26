# Rule catalog

Each finding cites one stable identifier from this catalog. Report only when the evidence column applies and the guard column does not.

## Flow and composition

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-FLOW-001` | A changed `for` loop performs a collection transformation, filter, flatten, grouping, aggregation, lookup, or side-effect traversal that a stream API or equivalent declarative construct expresses clearly and correctly. | The loop has essential short-circuit, ordering, checked-failure, performance, or state-machine semantics that the declarative form would obscure or break. | Replace it with `map`, `filter`, `flatMap`, `reduce`, a collector, comprehension, iterator adapter, or typed fold. |
| `PL-FLOW-002` | A mutable collection, counter, flag, or accumulator manually implements a standard transformation or reduction. | Mutation is confined to a purpose-built mutable algorithm whose performance or semantics require it. | Use a collector, reduction, immutable transformation, or domain accumulator with an explicit contract. |
| `PL-FLOW-003` | `break`, `continue`, nested branches, or exception-based control flow interrupt a transformation that can be represented as stages or explicit outcomes. | The construct implements a clear terminal search or parser state machine and a pipeline would hide the exit semantics. | Use filtering, partitioning, `takeWhile`, `findFirst`, `Optional`, a result type, or separated stages. |
| `PL-FLOW-004` | A new helper has one call site and moves readable linear logic out of view without naming an invariant, effect boundary, reusable operation, or independent concept. | The helper marks a transaction, retry, authorization, effect, or other boundary whose name is part of the contract. | Inline the helper or expand its contract so it owns a meaningful boundary. |
| `PL-FLOW-005` | Interior guard clauses or early returns fragment one composable operation into scattered exits. | The return rejects external input at the boundary or handles a genuinely terminal case more clearly than a pipeline. | Parse at the boundary or model the branch as a filter, option, result, or exhaustive match. |

## Null and absence

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-NULL-001` | A nullable external value crosses the first project-owned boundary without conversion. | The changed line is the adapter performing the conversion. | Wrap or parse it immediately into a required value, `Optional`, empty collection when semantically valid, result, or domain type. |
| `PL-NULL-002` | An internal parameter, field, return value, or collection element is nullable even though the project owns the contract. | A framework requires the nullable shape at an adapter and it does not escape that adapter. | Make the value required or represent absence explicitly. |
| `PL-NULL-003` | Empty string, zero, negative number, empty object, or magic enum value stands for absence without domain meaning. | The domain specification defines that exact sentinel and the type documents it. | Use `Optional`, a result variant, or a named domain state. |
| `PL-NULL-004` | Code permits a null `Optional`, calls `Optional.get()` without proof, or wraps and unwraps `Optional` only to recreate null. | Exhaustive presence handling proves the access safe at the same location. | Keep the value in the optional pipeline or match both cases explicitly. |

## Failure contracts

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-FAIL-001` | Changed code creates, extends, throws, deliberately propagates, or declares as a contract `RuntimeException` or any subclass, including `IllegalArgumentException`, `IllegalStateException`, and custom runtime exceptions. | Generated or inherited framework code is unchangeable and a project-owned adapter immediately translates the failure. | Replace the path with a declared checked domain exception, explicit result, or type that makes the invalid state unrepresentable. |
| `PL-FAIL-002` | A recoverable domain, validation, service, dependency, or API failure is absent from the method contract. | The operation is total because its inputs and dependencies make failure unrepresentable. | Declare a checked exception or return an explicit result type. |
| `PL-FAIL-003` | An unchecked exception from a parser, framework, or vendor library escapes its immediate adapter. | The external exception is caught at that adapter and translated once. | Catch the exact external type, preserve the cause, and convert it to the module-owned checked failure or result. |
| `PL-FAIL-004` | Code uses Lombok `@SneakyThrows`, generic rethrow tricks, reflection wrappers, or another mechanism that hides a checked failure. | There is no exemption from reporting. An inherited incompatible interface only lowers the required fix to isolation and debt documentation. | Change the local interface, return a result, or isolate the sneaky throw in the smallest inherited-interface adapter. |
| `PL-FAIL-005` | Code catches broadly, swallows, logs and continues, logs and rethrows, returns success-shaped fallback data, loses the cause, or maps distinct failures to one generic outcome. | A true process entry point contains an unknown failure only to report and terminate or isolate one callback from siblings. | Catch exact types, translate once, preserve the cause and context, and return control to the caller through the declared contract. |

## Input boundaries

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-INPUT-001` | External input is merely checked and the raw DTO, map, string, or nullable representation continues inward. | The boundary constructs a valid internal type that cannot represent the rejected state. | Parse once into a required domain value or return a typed failure. |
| `PL-INPUT-002` | The same null, range, format, or presence check repeats in internal layers for one value. | Each check protects a distinct boundary with a distinct contract. | Move judgment to the first boundary and narrow downstream signatures. |

## Effects

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-EFFECT-001` | I/O, RPC, database, filesystem, clock, randomness, environment, or framework work appears inside an otherwise pure transformation or stream stage. | The stage is explicitly the effect boundary and its contract declares failure. | Load or write at the edge, then pass values through pure stages. |
| `PL-EFFECT-002` | A query, getter, validator, converter, or predicate performs a hidden write, callback, log-as-policy, session change, or other effect. | The name and contract explicitly state the effect. | Separate command from query or rename and retype the operation so the effect is visible. |

## Boundaries and contracts

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-BOUNDARY-001` | HTTP, servlet, RPC, ORM, serializer, database, or framework types appear in a domain or module-owned API. | The signature belongs to the adapter that translates that technology. | Introduce a small adapter and map to a module-owned type. |
| `PL-BOUNDARY-002` | Vendor DTOs, clients, exceptions, null rules, or lifecycle semantics leak through a project-owned boundary. | The module is itself the vendor adapter and callers depend only on its own contract. | Wrap the vendor API and translate values and failures once. |
| `PL-CONTRACT-001` | A public signature hides legitimate absence, expected failure, mutation, ordering, or effect semantics. | The outcome is impossible by construction and callers can verify that from the type. | Add an explicit option, checked failure, result variant, semantic return type, or effect-bearing name. |
| `PL-CONTRACT-002` | Boolean flags, selector strings, loosely related primitives, or undocumented maps force callers to know argument positions or internal modes. | The values form a natural mathematical or platform primitive whose meaning is obvious at the call site. | Introduce a semantic type, separate named operation, strategy, enum with owned behavior, or parameter object. |

## Names and cohesion

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-NAME-001` | A changed name is abbreviated, generic, unsearchable, unpronounceable, or fails to state its domain role, unit, or effect. | A short name follows a strong language convention in a tiny scope and carries no ambiguity. | Spell out the accurate domain meaning and unit. Use a repository convention only when it is semantically correct. |
| `PL-NAME-002` | A name lies about collection type, mutability, effect, abstraction level, or uses a different word for an existing concept without a semantic distinction. | The vocabulary difference represents a real domain distinction. | Rename to the accurate abstraction. Preserve an established term only when the term itself is correct. |
| `PL-COHESION-001` | A changed function or class owns unrelated decisions that change for different reasons. | The operations form one transaction, invariant, or domain operation and splitting would scatter ownership. | Separate the unrelated decision at the nearest stable seam. |
| `PL-COHESION-002` | High-level policy and low-level mechanism alternate in one flow, forcing the reader to switch abstraction levels. | The low-level expression is the domain operation and an adapter would add only indirection. | Move the mechanism behind a named boundary or inline fragmented policy into one readable flow. |

## Duplication and complexity

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-DUP-001` | Two changed or directly affected locations independently encode one business rule, conversion, validation, or constant and can drift. | The similar code represents different ownership, different boundary vocabularies, or self-contained test readability. | Give the knowledge one owner and call or map through it. |
| `PL-DUP-002` | A proposed or implemented cleanup shares types or helpers across boundaries and couples their evolution only to remove visual duplication. | The shared concept has one genuine owner and both callers depend on that owner semantically. | Restore boundary-owned types or local code and keep an explicit mapping. |
| `PL-COMPLEXITY-001` | Clever expression, nested callback, generic abstraction, reflection, builder layer, or indirection costs more understanding than the behavior warrants. | The abstraction removes verified variation or enforces a real invariant. | Use the smallest direct typed form that keeps the complete flow visible. |
| `PL-COMPLEXITY-002` | Changed code leaves dead branches, unused values, unreachable catches, commented-out alternatives, or compatibility paths with no caller. | A documented rollout or compatibility contract still exercises the path. | Remove the dead path or connect it to the explicit rollout contract. |

## Values, types, and encapsulation

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-MAGIC-001` | A literal encodes a domain threshold, unit, protocol value, retry policy, timeout, or special state without a name and owner. | The literal is conventional syntax or clearer inline, such as zero in an arithmetic identity. | Introduce a named typed constant or configuration value at the layer that knows its meaning and unit. |
| `PL-TYPE-001` | Raw types, unchecked casts, warning suppression, `Object`, untyped maps, reflection, or string parsing bypass compile-time guarantees. | A boundary must erase a type and immediately restores and verifies it in one place. | Use a generic, sealed type, semantic wrapper, exact parser, or isolated checked adapter. |
| `PL-TYPE-002` | Strings, integers, or booleans encode states or variants that the type system could enumerate. | The value is an external wire representation being parsed at the boundary. | Introduce an enum, sealed hierarchy, tagged result, or domain value type. |
| `PL-ENCAPSULATION-001` | Code reaches through another object, reconstructs its rule from exposed fields, returns mutable internals, or places behavior away from the data and invariant it owns. | The caller is an adapter whose job is projection between owned representations. | Move the behavior to the owner, expose a narrow query, or return an immutable view. |

## Comments

| Rule | Report when | Do not report when | Smallest fix |
|---|---|---|---|
| `PL-COMMENT-001` | A changed comment is obsolete, redundant, historical, negative, vague, or explains an absent alternative instead of the present contract. | The comment states a current invariant, external constraint, domain formula, compatibility rule, or non-obvious effect. | Delete it or rewrite it in present-tense positive terms. |
| `PL-COMMENT-002` | Non-obvious concurrency, performance, protocol, compatibility, or external-system behavior has no local rationale and the code cannot express why it is required. | A name, type, or cited specification makes the reason evident. | Add one concise comment explaining the invariant or external constraint with a clickable source when available. |
| `PL-COMMENT-003` | Changed code adds commented-out code, journal entries, authorship notes, closing-brace comments, or section banners used to navigate an oversized file. | A required legal header follows repository policy. | Delete the history or dead code; use source structure and version control. |

## Severity and grouping

Rule identifiers classify violations, not severity. Grade each instance by consequence:

- `blocker`: the changed contract hides expected failure or absence, leaks a boundary, produces wrong behavior, or creates a failure path callers cannot handle.
- `suggestion`: the change is correct today but introduces avoidable mutation, coupling, indirection, or drift risk.
- `nit`: a local clarity defect with no meaningful behavioral or maintenance consequence.

Do not combine separate locations merely because they share a rule identifier. Merge only a contiguous instance with one cause and one smallest fix.
