data "aws_ami" "ubuntu" {
  most_recent = true
  owners      = ["099720109477"]

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }
}

resource "aws_security_group" "homesync_sg" {
  name        = "${var.project_name}-sg"
  description = "Permite acesso a Evolution API e SSH"

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "evolution_api" {
  ami           = data.aws_ami.ubuntu.id
  instance_type = var.instance_type

  vpc_security_group_ids = [aws_security_group.homesync_sg.id]

  user_data = file("userdata.sh")

  tags = {
    Name = "${var.project_name}-server"
  }
}

resource "aws_eip" "evolution_api_eip" {
  domain   = "vpc"
  instance = aws_instance.evolution_api.id

  tags = {
    Name = "${var.project_name}-eip"
  }
}
