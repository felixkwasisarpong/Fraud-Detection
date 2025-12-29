#########################################################
# Main entrypoint — keeps Terraform root clean
#########################################################

terraform {
  required_version = ">= 1.4"
}

# (Optional) Global tags reusable across all resources
locals {
  common_tags = {
    Project     = var.project
    Environment = "dev"
  }
}

# Nothing else should be declared here.
# All resources live in their respective *.tf files:
#
#  - vpc.tf
#  - security-groups.tf
#  - ecs.tf
#  - alb.tf
#  - msk.tf
#  - redis.tf
#  - sns_sqs.tf
#  - iam/*.tf
#
# main.tf is intentionally empty to avoid duplicates.