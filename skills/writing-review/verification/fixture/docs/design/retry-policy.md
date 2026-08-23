# Retry policy for the jobs client

This document explores the considerations around retry behavior in the jobs
client and aims to provide a comprehensive overview of the possible approaches
and their tradeoffs.

## Options

There are three possible approaches to handling failed job submissions.

- **Retry everything:** The client retries any failed submission up to five
  times with exponential backoff. This is simple to implement and handles
  transient network failures well.
- **Typed retry policy:** The client classifies failures and retries only those
  classified as transient. Timeouts after the request was sent are treated as
  state-unknown and are not retried.
- **No retries:** The client reports every failure to the caller, which decides.

The retry-everything approach deserves a closer look. Implementation is a small
loop around the existing submit call, and backoff parameters can live in the
existing client config block. Most failures in production are connection resets,
which it handles perfectly. The main structure would be a wrapper class holding
the retry counter, roughly forty lines including tests. It could be shipped in a
day.

## Requirements

The client should probably be resilient to transient failures, though it is
worth noting that resilience means different things in different contexts. It
might also be the case that duplicate submissions could potentially be
problematic, at least in some scenarios, so that may be something to keep in
mind as well.

## Analysis

It should be noted that each approach has strengths and weaknesses. Generally
speaking, retrying everything is arguably simpler, while a typed policy is
perhaps more correct in some sense. The no-retry option pushes complexity to
callers, which could be seen as either good or bad depending on one's
perspective.

After weighing the considerations above, the typed retry policy seems like it
could be the right direction, and we would likely recommend adopting it at some
point.
