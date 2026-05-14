# ECR repository — stores our Docker images
resource "aws_ecr_repository" "backend" {
  name                 = "${var.app_name}-backend"
  image_tag_mutability = "MUTABLE"
  force_delete         = true

  image_scanning_configuration {
    scan_on_push = true
  }

  tags = {
    Name        = "${var.app_name}-backend"
    Environment = var.environment
  }
}

# ECS Cluster — the logical grouping of our containers
resource "aws_ecs_cluster" "main" {
  name = "${var.app_name}-cluster"

  tags = {
    Name        = "${var.app_name}-cluster"
    Environment = var.environment
  }
}

# IAM role for ECS task execution
resource "aws_iam_role" "ecs_execution" {
  name = "${var.app_name}-ecs-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "ecs-tasks.amazonaws.com"
      }
    }]
  })

  tags = {
    Name        = "${var.app_name}-ecs-execution-role"
    Environment = var.environment
  }
}

# Attach AWS managed policy for ECS task execution
resource "aws_iam_role_policy_attachment" "ecs_execution" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

# Allow ECS to read secrets from Secrets Manager
resource "aws_iam_role_policy" "ecs_secrets" {
  name = "${var.app_name}-ecs-secrets-policy"
  role = aws_iam_role.ecs_execution.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Action = [
          "secretsmanager:GetSecretValue"
        ]
        Resource = "arn:aws:secretsmanager:eu-west-2:*:secret:${var.app_name}/*"
      }
    ]
  })
}

# Allow ECS to pull images from ECR
resource "aws_iam_role_policy_attachment" "ecs_ecr" {
  role       = aws_iam_role.ecs_execution.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}

# ECS Task Definition — describes our container
resource "aws_ecs_task_definition" "backend" {
  family                   = "${var.app_name}-backend"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.task_cpu
  memory                   = var.task_memory
  execution_role_arn       = aws_iam_role.ecs_execution.arn

  container_definitions = jsonencode([{
    name  = "${var.app_name}-backend"
    image = var.backend_image

    portMappings = [{
      containerPort = var.container_port
      protocol      = "tcp"
    }]

    environment = [
      { name = "DB_NAME",                value = var.db_name },
      { name = "DB_PORT",                value = "5432" },
      { name = "DB_HOST",                value = var.db_host },
      { name = "DB_USER",                value = var.db_username },
      { name = "DB_PASSWORD",            value = var.db_password },
      { name = "JWT_SECRET",             value = var.jwt_secret },
      { name = "REGISTRATION_PUBLIC",    value = "true" },
      { name = "JWT_EXPIRATION",         value = "86400000" },
      { name = "SPRING_PROFILES_ACTIVE", value = "seed" }
    ]

    logConfiguration = {
      logDriver = "awslogs"
      options = {
        "awslogs-group"         = "/ecs/${var.app_name}"
        "awslogs-region"        = var.aws_region
        "awslogs-stream-prefix" = "ecs"
      }
    }
  }])

  tags = {
    Name        = "${var.app_name}-backend"
    Environment = var.environment
  }
}

# CloudWatch log group for container logs
resource "aws_cloudwatch_log_group" "ecs" {
  name              = "/ecs/${var.app_name}"
  retention_in_days = 7

  tags = {
    Name        = "${var.app_name}-logs"
    Environment = var.environment
  }
}

# ECS Service — keeps our task running
resource "aws_ecs_service" "backend" {
  name            = "${var.app_name}-backend"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.backend.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  health_check_grace_period_seconds = 120

  network_configuration {
    subnets          = var.public_subnet_ids
    security_groups  = [var.ecs_security_group_id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = var.target_group_arn
    container_name   = "${var.app_name}-backend"
    container_port   = var.container_port
  }

  depends_on = [var.alb_listener_arn]

  tags = {
    Name        = "${var.app_name}-backend"
    Environment = var.environment
  }
}