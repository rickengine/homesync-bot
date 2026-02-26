variable "aws_region" {
  description = "A região da AWS onde a infraestrutura será criada"
  type        = string
  default     = "us-east-1"
}

variable "instance_type" {
  description = "O tamanho da máquina virtual (EC2)"
  type        = string
  default     = "t3.micro"
}

variable "project_name" {
  description = "Nome do projeto para identificar os recursos"
  type        = string
  default     = "homesync-bot"
}