############################################################
# ECS Task Execution Role — pulls images, writes logs
############################################################

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "${var.project}-ecs-task-exec-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" },
      Action    = "sts:AssumeRole"
    }]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_exec_policy" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

############################################################
# ECS Task Role — your app uses this at runtime
############################################################

resource "aws_iam_role" "ecs_task_role" {
  name = "${var.project}-ecs-task-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect    = "Allow",
      Principal = { Service = "ecs-tasks.amazonaws.com" },
      Action    = "sts:AssumeRole"
    }]
  })
}

# App needs permission to Publish to SNS
resource "aws_iam_policy" "sns_publish_policy" {
  name        = "${var.project}-sns-publish-policy"
  description = "Allow ECS fraud engine to publish alerts"

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Effect   = "Allow",
        Action   = "sns:Publish",
        Resource = aws_sns_topic.fraud_alerts.arn
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_role_attach_sns" {
  role       = aws_iam_role.ecs_task_role.name
  policy_arn = aws_iam_policy.sns_publish_policy.arn
}

Resource = aws_sqs_queue.fraud_alerts_queue.arn