ID: CLUSTER-007

Name:
Cluster not using an instance pool

Severity:
INFO

Why:
Instance pools reduce cluster start/autoscaling latency and can reduce cost via idle
instance reuse. This is an opinionated suggestion, not a hard requirement — many
workspaces run fine without pools, hence INFO rather than WARNING.

Detection:
`instancePoolId` is `null` or blank.

Recommendation:
Consider using an instance pool to reduce cluster start time and idle instance cost.

Can auto-fix:
No
