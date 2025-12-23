data "archive_file" "lambda_zip" {
  type        = "zip"
  source_dir  = "${path.module}/lambda"
  output_path = "${path.module}/lambda.zip"
}

resource "aws_lambda_function" "fraud_alert_handler" {
  function_name = "fraud-alert-handler"
  runtime       = "python3.12"
  handler       = "fraud_alert_handler.lambda_handler"

  role          = aws_iam_role.lambda_role.arn
  filename      = data.archive_file.lambda_zip.output_path
  source_code_hash = data.archive_file.lambda_zip.output_base64sha256

  timeout = 10
}

resource "aws_lambda_event_source_mapping" "sqs_trigger" {
  event_source_arn = aws_sqs_queue.fraud_alert_queue.arn
  function_name    = aws_lambda_function.fraud_alert_handler.arn
  batch_size       = 5
  enabled          = true
}