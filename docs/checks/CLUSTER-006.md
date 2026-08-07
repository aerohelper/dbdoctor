ID: CLUSTER-006

Name:
Local disk encryption disabled

Severity:
WARNING

Why:
Data spilled to local disk during processing (shuffle spill, temp files) is not encrypted
at rest unless local disk encryption is enabled on the cluster.

Detection:
`enableLocalDiskEncryption != true` (treats unknown/unreported as disabled).

Recommendation:
Enable local disk encryption on clusters that process sensitive data.

Can auto-fix:
No
