output "alb_dns_name" {
  description = "ALB DNS name — the public URL of the application"
  value       = aws_lb.main.dns_name
}

output "target_group_arn" {
  description = "Target group ARN for ECS service"
  value       = aws_lb_target_group.backend.arn
}

output "listener_arn" {
  description = "ALB listener ARN"
  value       = aws_lb_listener.http.arn
}
