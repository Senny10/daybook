output "alb_dns_name" {
  description = "Application URL — paste this in your browser"
  value       = module.loadbalancer.alb_dns_name
}

output "ecr_repository_url" {
  description = "ECR URL for pushing Docker images"
  value       = module.compute.ecr_repository_url
}

output "ecs_cluster_name" {
  description = "ECS cluster name"
  value       = module.compute.ecs_cluster_name
}