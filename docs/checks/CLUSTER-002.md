ID: CLUSTER-002

Name:
Excessive auto-termination period

Severity:
WARNING

Why:
Auto-termination is enabled but set so high that it provides little practical protection
against idle-cost accumulation. This is dbdoctor's own opinion, not a documented Databricks
best practice — the threshold is configurable.

Detection:
`autoTerminationMinutes > 120` (default threshold, configurable via constructor).

Recommendation:
Consider lowering the auto-termination timeout to reduce idle cost exposure.

Can auto-fix:
No
