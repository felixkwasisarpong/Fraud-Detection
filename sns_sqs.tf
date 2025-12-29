############################################################
# SNS Topic — Fraud Alerts
############################################################
resource "aws_sns_topic" "fraud_alerts" {
  name = "${var.project}-fraud-alerts"
  tags = {
    Project = var.project
  }
}

############################################################
# SQS Dead-Letter Queue
############################################################
resource "aws_sqs_queue" "fraud_alerts_dlq" {
  name                      = "${var.project}-fraud-alerts-dlq"
  message_retention_seconds = 1209600 # 14 days
}

############################################################
# Main SQS Queue (Subscribed to SNS)
############################################################
resource "aws_sqs_queue" "fraud_alerts_queue" {
  name = "${var.project}-fraud-alerts-queue"

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.fraud_alerts_dlq.arn
    maxReceiveCount     = 5
  })

  visibility_timeout_seconds = 60
  message_retention_seconds  = 345600 # 4 days
}

############################################################
# Allow SNS → SQS subscription delivery
############################################################
resource "aws_sqs_queue_policy" "fraud_alerts_queue_policy" {
  queue_url = aws_sqs_queue.fraud_alerts_queue.id

  policy = jsonencode({
    Version = "2012-10-17",
    Statement = [
      {
        Sid       = "Allow-SNS-SendMessage"
        Effect    = "Allow"
        Principal = "*"
        Action    = "sqs:SendMessage"
        Resource  = aws_sqs_queue.fraud_alerts_queue.arn

        Condition = {
          ArnEquals = {
            "aws:SourceArn" = aws_sns_topic.fraud_alerts.arn
          }
        }
      }
    ]
  })
}

############################################################
# SNS → SQS Subscription
############################################################
resource "aws_sns_topic_subscription" "fraud_alerts_subscription" {
  topic_arn = aws_sns_topic.fraud_alerts.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.fraud_alerts_queue.arn
}
