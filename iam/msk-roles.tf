resource "aws_iam_role" "msk_connect_role" {
  name = "${var.project}-msk-connect-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17",
    Statement = [{
      Effect = "Allow",
      Principal = { Service = "kafkaconnect.amazonaws.com" },
      Action = "sts:AssumeRole"
    }]
  })
}