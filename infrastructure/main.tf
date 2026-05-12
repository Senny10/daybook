terraform {
  required_version = ">= 1.0"

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = var.aws_region
}

# Networking
module "networking" {
  source = "./modules/networking"

  app_name       = var.app_name
  environment    = var.environment
  container_port = var.container_port
}

# Database
module "database" {
  source = "./modules/database"

  app_name              = var.app_name
  environment           = var.environment
  db_name               = var.db_name
  db_username           = var.db_username
  db_password           = var.db_password
  private_subnet_ids    = module.networking.private_subnet_ids
  rds_security_group_id = module.networking.rds_security_group_id
}

# Secrets Manager
module "secrets" {
  source = "./modules/secrets"

  app_name    = var.app_name
  environment = var.environment
  db_username = var.db_username
  db_password = var.db_password
  jwt_secret  = var.jwt_secret
  db_host     = module.database.db_endpoint
}

# Load Balancer
module "loadbalancer" {
  source = "./modules/loadbalancer"

  app_name              = var.app_name
  environment           = var.environment
  vpc_id                = module.networking.vpc_id
  public_subnet_ids     = module.networking.public_subnet_ids
  alb_security_group_id = module.networking.alb_security_group_id
  container_port        = var.container_port
}

# Compute (ECS)
module "compute" {
  source = "./modules/compute"

  app_name               = var.app_name
  environment            = var.environment
  aws_region             = var.aws_region
  backend_image          = var.backend_image
  container_port         = var.container_port
  task_cpu               = var.task_cpu
  task_memory            = var.task_memory
  private_subnet_ids     = module.networking.private_subnet_ids
  ecs_security_group_id  = module.networking.ecs_security_group_id
  target_group_arn       = module.loadbalancer.target_group_arn
  alb_listener_arn       = module.loadbalancer.listener_arn
  db_name                = var.db_name
  db_username_secret_arn = module.secrets.db_username_secret_arn
  db_password_secret_arn = module.secrets.db_password_secret_arn
  jwt_secret_arn         = module.secrets.jwt_secret_arn
  db_host_secret_arn     = module.secrets.db_host_secret_arn
}