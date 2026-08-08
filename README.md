# Databricks Doctor

## Overview

Databricks Doctor is an open-source health assessment and diagnostics tool for Databricks workspaces.

Its goal is to help data engineers, platform engineers, and administrators identify performance, configuration, governance, reliability, and cost optimization issues through automated health checks and actionable recommendations.

The long-term vision is to become the "SonarQube for Databricks".

---

# Vision

Modern Databricks environments contain hundreds of jobs, clusters, SQL Warehouses, Delta tables, pipelines, and governance policies.

As these environments grow, identifying configuration issues, performance bottlenecks, security gaps, and unnecessary cloud costs becomes increasingly difficult.

Databricks Doctor aims to provide a single command that scans an entire workspace and generates a comprehensive health report.

Example:

```bash
dbdoctor scan
```

Example output:

```
Workspace Health Score: 87/100

Critical
---------
• Auto termination disabled on 2 clusters

Warnings
---------
• 3 jobs have no retry policy
• SQL Warehouse is oversized

Recommendations
----------------
• Enable auto termination
• Configure retries
• Reduce warehouse size
```

---

# Objectives

* Improve platform reliability
* Reduce cloud costs
* Detect configuration problems
* Enforce Databricks best practices
* Simplify platform administration
* Generate actionable recommendations
* Provide enterprise-ready health reports

---

# Initial MVP Scope

The first release focuses on workspace-level health checks.

Supported resources:

* Clusters
* Jobs
* SQL Warehouses

Health checks include:

* Auto termination disabled
* Oversized clusters
* Outdated Databricks Runtime
* Missing retry policies
* Long-running jobs
* SQL Warehouse auto-stop disabled
* SQL Warehouse sizing recommendations

Reports:

* Console
* JSON
* HTML

---

# Future Roadmap

## v0.2

* Delta Lake Health
* Small File Detection
* Optimize Recommendations
* Vacuum Recommendations

---

# High-Level Architecture

```
CLI
 │
 ▼
Authentication
 │
 ▼
Databricks API Client
 │
 ▼
Health Check Engine
 │
 ├── Cluster Checks
 ├── Job Checks
 ├── SQL Warehouse Checks
 ├── Delta Checks
 ├── Unity Catalog Checks
 └── Cost Checks
 │
 ▼
Recommendation Engine
 │
 ▼
Report Generator
 │
 ├── Console
 ├── JSON
 ├── HTML
 └── Markdown
```

---

# Project Structure

```
dbdoctor/

doctor-cli/
doctor-core/
doctor-api/
doctor-checks/
doctor-report/

docs/

examples/

.github/
```

---

# Technology Stack

Language

* Java 21

Build

* Maven

CLI

* Picocli

REST Client

* Java HttpClient

Serialization

* Jackson

Logging

* SLF4J
* Logback

Testing

* JUnit 5
* Mockito

Reporting

* Thymeleaf (planned)

---

# Design Principles

* Modular architecture
* Simple CLI
* Plugin-friendly
* Open-source first
* Testable
* Extensible
* Minimal dependencies
* Rule-based diagnostics
* Clear recommendations

---

# Safety Model

For v0.1, dbdoctor is entirely read-only.

```
READ Databricks
      ↓
ANALYZE
      ↓
REPORT
```

It should not, and does not:

* EDIT CLUSTER
* DELETE RESOURCE
* CHANGE POLICY
* STOP COMPUTE

Every Databricks SDK call dbdoctor makes is a `list`/`listRuns`/`me` read — there is no
create, update, or delete call anywhere in the codebase. This is what makes it safe for
engineers to point at a production workspace and try without asking anyone's permission
first.

A future `dbdoctor fix` command that makes changes is on the long-term roadmap, but is
explicitly out of scope for the MVP.

---

# Sample Commands

```
dbdoctor version

dbdoctor auth test

dbdoctor clusters

dbdoctor jobs

dbdoctor warehouses

dbdoctor scan
```

---

# Long-Term Goal

Databricks Doctor should become the standard open-source diagnostics toolkit for Databricks environments by helping organizations improve reliability, governance, performance, security, and cloud cost through automated health assessments and best-practice recommendations.

---

# License

Licensed under the [Apache License, Version 2.0](LICENSE).

---

# Contributing

Contributions are welcome.

Areas where contributors can help:

* New health checks
* Additional Databricks API integrations
* Report formats
* Unit testing
* Documentation
* Performance improvements
* Plugin development

Every health check should be independent, testable, and provide clear, actionable recommendations.

---

# Project Status

🚧 Early MVP (v0.1)

Current focus:

* Project setup
* Databricks authentication
* REST API integration
* Cluster health checks
* Job health checks
* SQL Warehouse health checks
* HTML and JSON reporting
