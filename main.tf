# SNS Topic
resource "aws_sns_topic" "fraud_alerts" {
  name = "fraud-alerts"
}

# Dead Letter Queue
resource "aws_sqs_queue" "fraud_alert_dlq" {
  name = "fraud-alert-dlq"
}

# Main Alert Queue
resource "aws_sqs_queue" "fraud_alert_queue" {
  name = "fraud-alert-queue"

  redrive_policy = jsonencode({
    deadLetterTargetArn = aws_sqs_queue.fraud_alert_dlq.arn
    maxReceiveCount     = 3
  })
}

# Allow SNS to publish to SQS
resource "aws_sqs_queue_policy" "allow_sns" {
  queue_url = aws_sqs_queue.fraud_alert_queue.id

  policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "sns.amazonaws.com"
        }
        Action   = "sqs:SendMessage"
        Resource = aws_sqs_queue.fraud_alert_queue.arn
        Condition = {
          ArnEquals = {
            "aws:SourceArn" = aws_sns_topic.fraud_alerts.arn
          }
        }
      }
    ]
  })
}

# SNS → SQS Subscription
resource "aws_sns_topic_subscription" "sns_to_sqs" {
  topic_arn = aws_sns_topic.fraud_alerts.arn
  protocol  = "sqs"
  endpoint  = aws_sqs_queue.fraud_alert_queue.arn

  raw_message_delivery = true
}