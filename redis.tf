resource "aws_elasticache_cluster" "redis" {
  cluster_id         = "${var.project}-redis"
  engine             = "redis"
  node_type          = "cache.t3.micro"
  num_cache_nodes    = 1
  port               = 6379
  subnet_group_name  = aws_elasticache_subnet_group.redis_subnets.name
  security_group_ids = [aws_security_group.redis_sg.id]
}
resource "aws_elasticache_subnet_group" "redis_subnets" {
  name = "${var.project}-redis-subnet-group"
  subnet_ids = [
    aws_subnet.private_a.id,
    aws_subnet.private_b.id
  ]

  tags = {
    Name = "${var.project}-redis-subnet-group"
  }
}