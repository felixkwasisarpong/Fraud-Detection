resource "aws_db_instance" "postgres" {
  identifier = "fraud-engine-db"

  engine         = "postgres"
  instance_class = "db.t3.micro"

  allocated_storage = 20

  db_name  = "frauddb"
  username = "frauduser"
  password = var.db_password

  publicly_accessible = true
  apply_immediately   = true

  db_subnet_group_name = aws_db_subnet_group.subnets.name

  vpc_security_group_ids = [
    aws_security_group.rds_sg.id
  ]

  skip_final_snapshot = true
}

resource "aws_db_subnet_group" "subnets" {
  name = "${var.project}-db-subnet-group"

  subnet_ids = [
    aws_subnet.private_a.id,
    aws_subnet.private_b.id
  ]

  tags = {
    Name = "${var.project}-db-subnet-group"
  }
}


