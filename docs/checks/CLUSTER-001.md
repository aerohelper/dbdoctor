ID: CLUSTER-001

Name:
Auto termination disabled

Severity:
CRITICAL

Why:
Running idle compute can increase costs. A cluster with no auto-termination timeout will
keep running (and billing) indefinitely once started, even if nobody is using it.

Detection:
`autoTerminationMinutes == 0` or unset (`null`).

Recommendation:
Configure automatic termination on the cluster.

Can auto-fix:
No
