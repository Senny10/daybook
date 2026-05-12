resource "aws_secretsmanager_secret" "db_username" {
  name                    = "${var.app_name}/db-username"
  recovery_window_in_days = 0

  tags = {
    Name        = "${var.app_name}-db-username"
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "db_username" {
  secret_id     = aws_secretsmanager_secret.db_username.id
  secret_string = var.db_username
}

resource "aws_secretsmanager_secret" "db_password" {
  name                    = "${var.app_name}/db-password"
  recovery_window_in_days = 0

  tags = {
    Name        = "${var.app_name}-db-password"
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "db_password" {
  secret_id     = aws_secretsmanager_secret.db_password.id
  secret_string = var.db_password
}

resource "aws_secretsmanager_secret" "jwt_secret" {
  name                    = "${var.app_name}/jwt-secret"
  recovery_window_in_days = 0

  tags = {
    Name        = "${var.app_name}-jwt-secret"
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "jwt_secret" {
  secret_id     = aws_secretsmanager_secret.jwt_secret.id
  secret_string = var.jwt_secret
}

resource "aws_secretsmanager_secret" "db_host" {
  name                    = "${var.app_name}/db-host"
  recovery_window_in_days = 0

  tags = {
    Name        = "${var.app_name}-db-host"
    Environment = var.environment
  }
}

resource "aws_secretsmanager_secret_version" "db_host" {
  secret_id     = aws_secretsmanager_secret.db_host.id
  secret_string = var.db_host
}
