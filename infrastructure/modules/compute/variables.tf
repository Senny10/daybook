variable "app_name" {
  type = string
}

variable "environment" {
  type = string
}

variable "aws_region" {
  type = string
}

variable "backend_image" {
  type = string
}

variable "container_port" {
  type = number
}

variable "task_cpu" {
  type = number
}

variable "task_memory" {
  type = number
}

variable "private_subnet_ids" {
  type = list(string)
}

variable "ecs_security_group_id" {
  type = string
}

variable "target_group_arn" {
  type = string
}

variable "alb_listener_arn" {
  type = string
}

variable "db_name" {
  type = string
}

variable "db_username_secret_arn" {
  type = string
}

variable "db_password_secret_arn" {
  type = string
}

variable "jwt_secret_arn" {
  type = string
}

variable "db_host_secret_arn" {
  type = string
}
