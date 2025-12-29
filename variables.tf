#########################################################
# Base Variables
#########################################################

variable "region" {
  type        = string
  description = "AWS region to deploy infrastructure"
  default     = "us-east-2"
}

variable "project" {
  type        = string
  description = "Project prefix for resource naming"
  default     = "fraud-engine"
}

#########################################################
# Networking Variables
#########################################################

variable "vpc_cidr" {
  type    = string
  default = "10.0.0.0/16"
}

variable "public_subnet_a_cidr" {
  type    = string
  default = "10.0.1.0/24"
}

variable "private_subnet_a_cidr" {
  type    = string
  default = "10.0.11.0/24"
}

variable "private_subnet_b_cidr" {
  type    = string
  default = "10.0.12.0/24"
}

#########################################################
# ECS Variables
#########################################################

variable "ecs_cpu" {
  type    = string
  default = "512"
}

variable "ecs_memory" {
  type    = string
  default = "1024"
}

#########################################################
# MSK Variables
#########################################################

variable "msk_instance_type" {
  type    = string
  default = "kafka.m5.large"
}

variable "msk_broker_count" {
  type    = number
  default = 2
}

#########################################################
# Redis Variables (optional)
#########################################################

variable "redis_node_type" {
  type    = string
  default = "cache.t3.micro"
}

variable "redis_num_nodes" {
  type    = number
  default = 1
}

#########################################################
# Tags
#########################################################

variable "tags" {
  type = map(string)
  default = {
    Environment = "dev"
    Owner       = "fraud-engine"
  }
}

variable "db_password" {
  description = "PostgreSQL database password"
  type        = string
  sensitive   = true
}

variable "ecs_desired_count" {
  type    = number
  default = 1
}