ID: CLUSTER-003

Name:
Outdated Databricks Runtime

Severity:
WARNING

Why:
Older Databricks Runtime versions miss performance improvements, bug fixes, and (eventually)
security patches, and may fall out of Databricks' support window.

Detection:
Parses the leading major version number out of the runtime version string (e.g. `13` from
`"13.3.x-scala2.12"`) and flags it if below a configurable minimum (default: 13). Runtime
strings that can't be parsed (custom images, etc.) are not flagged — dbdoctor doesn't guess.

Recommendation:
Upgrade to a supported, current Databricks Runtime version.

Can auto-fix:
No
