resource "aws_ecs_cluster" "cluster" {
  name = "${var.project}-cluster"
}

resource "aws_iam_role" "task_execution_role" {
  name = "${var.project}-task-exec"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" },
      Action    = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "task_exec_attach" {
  role       = aws_iam_role.task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_ecs_task_definition" "task" {
  family                   = var.project
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]

  cpu    = "512"
  memory = "1024"

  execution_role_arn = aws_iam_role.task_execution_role.arn
  task_role_arn      = aws_iam_role.task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "fraud-engine"
      image     = "${aws_ecr_repository.repo.repository_url}:latest"
      essential = true

      environment = [
        { name = "SPRING_PROFILES_ACTIVE", value = "prod" },
        { name = "SPRING_DATASOURCE_URL", value = "jdbc:postgresql://${aws_db_instance.postgres.address}:5432/frauddb" },
        { name = "SPRING_DATASOURCE_USERNAME", value = "frauduser" },
        { name = "SPRING_DATASOURCE_PASSWORD", value = var.db_password },
        {
        name  = "FRAUD_ALERTS_TOPIC_ARN"
        value = aws_sns_topic.fraud_alerts.arn
      },
        {
    name  = "KAFKA_BOOTSTRAP_SERVERS"
    value = aws_msk_cluster.msk.bootstrap_brokers
  }
            ]
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
      logConfiguration = {
        logDriver = "awslogs"
        options = {
          awslogs-group         = "/ecs/${var.project}"
          awslogs-region        = var.region
          awslogs-stream-prefix = "ecs"
        }
      }
    }
  ])
}

resource "aws_ecs_service" "service" {
  name            = "${var.project}-service"
  cluster         = aws_ecs_cluster.cluster.id
  task_definition = aws_ecs_task_definition.task.arn
  desired_count = var.ecs_desired_count
  enable_execute_command = true
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.private_a.id]
    security_groups = [aws_security_group.ecs_sg.id]
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.tg.arn
    container_name   = "fraud-engine"
    container_port   = 8080
  }

  depends_on = [
    aws_lb_listener.listener
  ]
}