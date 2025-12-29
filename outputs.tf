output "sns_fraud_alerts_topic_arn" {
  value = aws_sns_topic.fraud_alerts.arn
}

output "sqs_fraud_alerts_queue_url" {
  value = aws_sqs_queue.fraud_alerts_queue.id
}

output "sqs_fraud_alerts_queue_arn" {
  value = aws_sqs_queue.fraud_alerts_queue.arn
}

output "sqs_fraud_alerts_dlq_arn" {
  value = aws_sqs_queue.fraud_alerts_dlq.arn
}
output "alb_dns" {
  value = aws_lb.alb.dns_name
}


output "msk_bootstrap_servers" {
  value = aws_msk_cluster.msk.bootstrap_brokers
}

output "alb_dns_name" {
  description = "Public DNS name of the Application Load Balancer"
  value       = aws_lb.alb.dns_name
}