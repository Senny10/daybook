variable "aws_region" {
  description = "AWS region to deploy to"
  type        = string
  default     = "eu-west-2"
}

variable "app_name" {
  description = "Application name used for resource naming"
  type        = string
  default     = "daybook"
}

variable "environment" {
  description = "Deployment environment"
  type        = string
  default     = "production"
}

variable "db_name" {
  description = "PostgreSQL database name"
  type        = string
  default     = "daybook"
}

variable "db_username" {
  description = "PostgreSQL master username"
  type        = string
  sensitive   = true
}

variable "db_password" {
  description = "PostgreSQL master password"
  type        = string
  sensitive   = true
}

variable "jwt_secret" {
  description = "JWT signing secret — minimum 32 characters"
  type        = string
  sensitive   = true
}

variable "backend_image" {
  description = "Docker image URI for the backend API"
  type        = string
}

variable "container_port" {
  description = "Port the backend container listens on"
  type        = number
  default     = 8080
}

variable "task_cpu" {
  description = "ECS task CPU units (1024 = 1 vCPU)"
  type        = number
  default     = 256
}

variable "task_memory" {
  description = "ECS task memory in MB"
  type        = number
  default     = 512
}