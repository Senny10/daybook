variable "app_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "alb_dns_name" {
  type        = string
  description = "ALB DNS name for API forwarding"
}