import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

@Component
class HomeSyncBotRunner : CommandLineRunner {

    private val log = LoggerFactory.getLogger(HomeSyncBotRunner::class.java)

    override fun run(vararg args: String?) {
        log.info("🚀 Iniciando a rotina do HomeSync Bot via Spring Boot...")

        try {
            val env = dotenv { ignoreIfMissing = true }

            val spreadsheetId = env["GOOGLE_SPREADSHEET_ID"]
                ?: throw IllegalArgumentException("❌ ERRO: GOOGLE_SPREADSHEET_ID não encontrado no .env!")

            val taskRepository = br.com.homesync.repository.GoogleSheetsTaskRepository(spreadsheetId)
            val messageClient = br.com.homesync.WhatsAppClient()
            val notificationService = br.com.homesync.NotificationService(taskRepository, messageClient)

            notificationService.notifyDailyTasks()

            log.info("✅ Processo finalizado com sucesso! Desligando...")

        } catch (e: Exception) {
            log.error("🚨 Falha crítica na execução do bot: {}", e.message, e)
            throw e
        }
    }
}
