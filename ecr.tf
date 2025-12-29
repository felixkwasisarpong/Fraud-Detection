resource "aws_ecr_repository" "repo" {
  name = var.project
  force_delete = true
}