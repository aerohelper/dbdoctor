ID: CLUSTER-004

Name:
Missing cluster policy

Severity:
WARNING

Why:
Clusters not governed by a cluster policy are free to be configured however the creator
likes, which makes it harder to standardize configuration and control costs across a
workspace.

Detection:
`policyId` is `null` or blank.

Recommendation:
Attach a cluster policy to standardize configuration and control costs.

Can auto-fix:
No
