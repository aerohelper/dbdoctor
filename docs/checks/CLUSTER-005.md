ID: CLUSTER-005

Name:
Oversized cluster

Severity:
WARNING

Why:
Fixed-size clusters with a large worker count may be provisioned larger than the workload
needs. This is deliberately a simple size cap, not an attempt to compute "optimal" sizing.

Detection:
`numWorkers > 10` (default threshold, configurable). Autoscaling clusters, which report no
fixed worker count, are not flagged.

Recommendation:
Review whether these clusters need this many workers, or consider autoscaling.

Can auto-fix:
No
