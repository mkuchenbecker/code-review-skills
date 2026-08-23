# ID format for operation records

We will use UUIDv7 for operation record IDs. It is time-ordered, which the
scheduler's claim queries need, and it is globally unique without coordination,
which multi-instance schedulers need. The alternatives lose on exactly one
requirement each; the comparison is below.

## Requirements

| Tier | Requirement |
|---|---|
| Must | IDs sort by creation time, so claim queries can scan a time range |
| Must | IDs are globally unique with no coordination between instances |
| Should | No new infrastructure or allocation services to operate |
| Won't | IDs will not be human-readable; operators look records up by table name |
| Out of scope | Migrating existing records; this format applies to new records only |

## Options

The table compares the options against the requirements; UUIDv7 is the
recommendation because it is the only option that meets both musts and the
should.

| Option | Sorts by time | Unique without coordination | New infrastructure |
|---|---|---|---|
| UUIDv4 | No | Yes | None |
| **UUIDv7 (recommended)** | Yes | Yes | None |
| Snowflake IDs | Yes | Yes | A worker-ID allocator |

## Sketch

The record constructor calls the UUIDv7 generator in the existing ids module.
No schema change: the column is already a 128-bit UUID type. Claim queries drop
their separate created-at ordering clause and order by ID.

## Appendix: alternatives considered

UUIDv4 was the incumbent default; it fails time-ordering, which would keep the
separate created-at index forever. Snowflake IDs meet both musts but require
allocating worker IDs to scheduler instances, and the allocator is exactly the
kind of coordination the second must exists to avoid.
