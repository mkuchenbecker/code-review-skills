# Principles

The Clean Code corpus supplied useful observations, but its rules do not outrank this file. This reviewer applies the principles below when the two disagree.

## Composition is the default

Collection transformations should read as transformations. Prefer Java stream APIs and the equivalent declarative facilities in other languages: `map`, `filter`, `flatMap`, `reduce`, collectors, comprehensions, iterator adapters, and typed folds.

Every new `for` loop is a warning. Keep it only when a declarative form would hide the operation, require unsafe shared state, change ordering or short-circuit semantics, or impose a material performance cost. The code and context should reveal that reason.

Mutable accumulators, mid-pipeline branches, `break`, `continue`, and manual control-flow escapes receive the same scrutiny. A pipeline whose stages are total and composable is easier to read, reuse, and verify than a loop that manages its own state machine.

Clarity still controls. A dense pipeline with hidden mutation or obscure collector machinery is worse than a direct loop. Prefer the clearest declarative representation, regardless of line count.

## Absence is explicit after the boundary

Null is a representation used by external APIs, serializers, generated models, databases, and legacy contracts. Convert it at the first project-owned boundary to a required value, `Optional`, an empty collection when empty and absent mean the same thing, a result type, or another explicit domain representation.

Internal code does not pass nullable values through several layers and check them later. A public module contract does not make callers guess whether a return value, field, or parameter may be null. `Optional` itself is never null, and a bare `Optional.get()` reintroduces the deferred failure the type was meant to remove.

Empty strings, zero timestamps, negative numbers, empty objects, and magic enum values are not acceptable substitutes for absence unless the domain defines that meaning.

## Expected failure is part of the contract

Expected and recoverable failures must be visible to callers. In Java, use a declared checked domain exception or an explicit result type. In languages without checked exceptions, use the strongest available typed result or declared error mechanism.

`RuntimeException` and every direct or indirect subclass are unchecked exceptions. New explicit runtime exception paths are findings. This includes `IllegalArgumentException`, `IllegalStateException`, custom runtime exception hierarchies, and wrappers that convert checked failures into runtime failures.

Do not use unchecked exceptions because a callback, stream lambda, or existing API makes a checked contract inconvenient. Move the effect or failure boundary, use a result-bearing stage, or change the local contract.

An external library may throw an unchecked exception. Catch its exact type at the immediate adapter and convert it to the module-owned checked failure or result. The external exception does not cross into internal code.

Sneaky throws are permitted only when an inherited interface cannot declare the real failure and cannot be changed locally. The use remains technical debt and is always reported. Isolate it in the smallest adapter, preserve the cause, and keep it out of domain code.

Broad catches, swallowed failures, log-and-continue, log-and-rethrow, cause destruction, and success-shaped fallbacks erase the contract. Catch only what the boundary owns and translate it once.

## External input becomes a valid internal value

Validate and parse external input at the boundary. The boundary should return a type that represents valid input, not validate a raw DTO and then pass the same nullable or stringly representation through the application.

Repeated validation, repeated null checks, and deep precondition checks are evidence that the boundary did not finish its job. Internal domain code should trust project-owned invariants.

## Effects stay at explicit edges

I/O, RPC, database access, filesystem access, time, randomness, environment reads, and framework callbacks are effects. Put them at explicit boundaries with explicit failure contracts. Keep transformation and decision logic pure where practical.

A function name and signature must disclose its effects. A query does not secretly mutate state, start a session, emit analytics, or write a record. An operation that must perform several effects should coordinate them in one visible effect boundary rather than scatter them through otherwise pure helpers.

## Framework and vendor concepts stop at adapters

HTTP statuses, servlet types, request or response DTOs, ORM exceptions, database rows, serializer nodes, vendor clients, and vendor exceptions belong at adapters. Module-owned interfaces use module-owned vocabulary.

Third-party APIs should be wrapped where their types, nulls, errors, or lifecycle rules would otherwise leak inward. The wrapper translates once and exposes the semantic contract the caller needs.

## Module contracts are complete

A boundary signature states success, absence, expected failure, and effect semantics. Callers should not need implementation knowledge, message matching, sentinel checks, or defensive broad catches to discover possible outcomes.

Use semantic types instead of boolean flags, selector strings, loosely related primitive arguments, and maps whose keys carry an undocumented schema. Judge the contract by whether each value has one clear meaning.

## New seams improve legacy code

Existing bad architecture is context, not permission. New APIs, versioned routes, replacement modules, and newly owned adapters are strangler-fig seams. Put checked failures, explicit absence, valid domain values, and project-owned types inside the new seam.

The review remains scoped to the PR. Report legacy code when the change copies it, deepens it, exposes it through a new contract, or can avoid it with a small local adapter or enabling refactor. Do not demand an unrelated migration.

## Cohesion is semantic, not numerical

A function or class should express one coherent operation at one useful level of abstraction. There is no target line count, helper count, argument count, or nesting depth.

Do not extract until functions are tiny. A single readable end-to-end flow is often clearer than several single-use helpers. Extract when the new unit names a real invariant, effect boundary, reusable operation, or independently meaningful concept.

Use guard clauses and early returns at external boundaries and for terminal cases. In an internal transformation, prefer a pipeline or explicit result when early exits fragment the operation.

## Names, types, and comments carry meaning

Names should be spelled out, searchable, pronounceable, and stated in accurate domain vocabulary. Consistency is useful only when the established term is semantically correct. A repeated misleading name is a defect to contain, not a convention to copy. A name should not lie about collection semantics, mutability, units, effects, or abstraction level.

Types should make invalid combinations difficult to express. Avoid raw types, unsafe casts, warning suppression, stringly typed states, untyped maps, and booleans whose meaning changes by position.

Comments describe the present code in positive terms. Keep comments that explain a non-obvious invariant, external constraint, domain formula, effect, or compatibility requirement. Remove obsolete, redundant, historical, negative, banner, journal, and commented-out code.

## Duplicate knowledge is the defect

Flag two places that independently encode one rule and can drift. Do not flag syntax that happens to look alike when each copy belongs to a different boundary or improves local readability.

Wire types, domain types, storage types, and test fixtures may intentionally duplicate shape to preserve encapsulation. Shared helpers and common-types modules are not automatically cleaner. Consolidate only when there is one owner for the duplicated knowledge.

## Test code follows the same cleanliness rules

Changed test code should use clear names, explicit data, honest helpers, local readability, typed failure assertions, and deterministic effects. The linter may flag cleanliness violations in tests.

Whether tests exist, which cases are needed, where they belong, and whether their oracles can fail are separate questions for `testing-review`.
