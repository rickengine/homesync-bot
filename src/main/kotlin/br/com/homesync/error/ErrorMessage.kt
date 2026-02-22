package br.com.homesync.error

enum class ErrorMessage (val code: String, val message: String) {
    COLUMN_NOT_FOUND("HS-001", "A coluna do dia da semana não foi encontrada na planilha."),
    WHATSAPP_API_ERROR("HS-002", "Falha na comunicação com a Evolution API."),
    INVALID_TASK_DATA("HS-003", "Dados da tarefa incompletos ou inválidos."),
    SHEET_READ_ERROR("HS-004", "Erro ao tentar ler os dados da planilha Google.");
}
