output "evolution_api_public_ip" {
  description = "IP publico estatico (Elastic IP) da instancia da Evolution API"
  value       = aws_eip.evolution_api_eip.public_ip
}

output "api_url" {
  description = "A URL base para colocar no seu .env do Spring Boot"
  value       = "http://${aws_eip.evolution_api_eip.public_ip}:8080"
}
