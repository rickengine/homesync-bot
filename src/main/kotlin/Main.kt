import br.com.homesync.WhatsAppClient
import br.com.homesync.repository.GoogleSheetsTaskRepository
import br.com.homesync.NotificationService
import io.github.cdimascio.dotenv.dotenv

fun main() {
    println("--- Iniciando sincronização HomeSync ---")

    val env = dotenv { ignoreIfMissing = true }

    val spreadsheetId = env["GOOGLE_SPREADSHEET_ID"] ?: "1BnjiTotAxgmDBpmda21q45cyd0f_Zp8v7RPR1xveaGU"

    val taskRepository = GoogleSheetsTaskRepository(spreadsheetId)
    val messageClient = WhatsAppClient()
    val notificationService = NotificationService(taskRepository, messageClient)

    try {
        notificationService.notifyDailyTasks()
        println("--- Processo finalizado com sucesso ---")
    } catch (e: Exception) {
        println("🚨 Erro crítico: ${e.message}")
    }
}
