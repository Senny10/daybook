# Daybook — AWS Cost Analysis

## Overview

Daybook uses a deploy-on-demand strategy to minimise AWS costs
during development. Infrastructure is provisioned via Terraform
before demos and destroyed immediately after, keeping total spend
at $0.00 across the project lifetime.

## Infrastructure Cost Estimate (When Running)

| Service | Configuration | Estimated Cost |
|---|---|---|
| RDS PostgreSQL | db.t3.micro, 20GB gp2, eu-west-2 | ~£15/month |
| ECS Fargate | 256 CPU, 512MB memory, 1 task | ~£8/month |
| Application Load Balancer | 1 ALB, eu-west-2 | ~£15/month |
| AWS Secrets Manager | 4 secrets | ~£1.60/month |
| ECR | ~180MB image storage | ~£0.02/month |
| CloudWatch Logs | 7-day retention | ~£0.50/month |
| **Total** | | **~£40/month** |

## Actual Spend

- **Total charged to date:** $0.00
- **AWS Free Tier:** RDS db.t3.micro and ECS are eligible for
  free tier in the first 12 months
- **Deploy-on-demand:** Infrastructure destroyed after each session
  using `terraform destroy` — charges stop immediately

## Cost Monitoring

- **AWS Cost Explorer:** Configured and monitored throughout project
- **Zero spend budget:** Alert configured via AWS Budgets —
  email notification triggers if any charge appears
- **Screenshot evidence:** Cost Explorer showing $0.00 saved

## Cost Optimisation Decisions

### Deploy-on-demand (Primary Strategy)
For a portfolio project, running infrastructure 24/7 is unnecessary.
`terraform apply` provisions everything in ~8 minutes.
`terraform destroy` tears it down completely.
This keeps costs at zero while maintaining full deployment capability.

### Instance Sizing
- **RDS db.t3.micro** — smallest available, free tier eligible,
  sufficient for a demo workload
- **ECS 256 CPU / 512MB** — minimum Fargate configuration,
  adequate for Spring Boot demo traffic
- **Single AZ** — `multi_az=false` halves RDS cost. Acceptable
  for a portfolio project; production would use multi-AZ.

### Data Retention
- **CloudWatch Logs: 7-day retention** — prevents log storage
  costs accumulating over time
- **RDS: skip_final_snapshot=true** — no snapshot costs on destroy

## Production Cost Considerations

In a real production deployment, additional costs would include:
- **NAT Gateway** (~£30/month) — for private subnet internet access.
  Currently using public subnets as a trade-off.
- **Multi-AZ RDS** — doubles database cost but provides high
  availability
- **HTTPS/TLS** — AWS Certificate Manager (free) + Route 53
  (~£0.50/month for hosted zone)
- **S3 + CloudFront** — for frontend static file hosting (~£1/month)

## What This Demonstrates

Understanding cloud costs is part of delivering value to the
business. Every architectural decision has a cost implication:

- Public vs private subnets → NAT Gateway cost trade-off
- Single vs multi-AZ → availability vs cost trade-off
- Secrets Manager vs env vars → security vs VPC endpoint cost
- Log retention period → observability vs storage cost

Engineers who understand these trade-offs make better decisions
for their organisations.