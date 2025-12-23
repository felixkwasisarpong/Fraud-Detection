output "sns_topic_arn" {
  value = aws_sns_topic.fraud_alerts.arn
}

output "sqs_queue_url" {
  value = aws_sqs_queue.fraud_alert_queue.id
}

output "sqs_queue_arn" {
  value = aws_sqs_queue.fraud_alert_queue.arn
}

output "dlq_arn" {
  value = aws_sqs_queue.fraud_alert_dlq.arn
}