# Deployment posture

`RateLimiter` is a new module in this change. Nothing outside its own tests constructs it yet, and
the feature that will use it ships behind the config flag `limiter.enabled`, which defaults to
false. The module's documented contract is its class javadoc.
