resource "aws_msk_cluster" "msk" {
  cluster_name  = "${var.project}-msk"
  kafka_version = "3.6.0"

  number_of_broker_nodes = 2 # small dev cluster

  broker_node_group_info {
    instance_type = "kafka.m5.large"
    storage_info {
      ebs_storage_info {
        volume_size = 100
      }
    }

    client_subnets = [
      aws_subnet.private_a.id,
      aws_subnet.private_b.id
    ]

    security_groups = [
      aws_security_group.msk_sg.id
    ]
  }

  encryption_info {
    encryption_in_transit {
      client_broker = "PLAINTEXT" # dev mode ONLY
      in_cluster    = true
    }
  }

  tags = {
    Name = "${var.project}-msk"
  }
}

resource "aws_subnet" "private_b" {
  vpc_id                  = aws_vpc.main.id
  cidr_block              = "10.0.12.0/24"
  availability_zone       = "${var.region}b"
  map_public_ip_on_launch = false

  tags = {
    Name = "${var.project}-private-b"
  }
}