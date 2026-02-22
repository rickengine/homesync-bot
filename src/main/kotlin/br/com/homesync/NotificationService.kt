package br.com.homesync

import br.com.homesync.client.MessageClient
import br.com.homesync.error.exceptions.ColumnNotFoundException
import br.com.homesync.error.exceptions.MessagingException
import br.com.homesync.repository.TaskRepository
import org.slf4j.LoggerFactory
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

class NotificationService(
    private val taskRepository: TaskRepository,
    private val messageClient: MessageClient
) {
    private val logger = LoggerFactory.getLogger(NotificationService::class.java)

    fun notifyDailyTasks() {
        val today = LocalDate.now()
        val dayOfWeek = today.dayOfWeek

        val columnSearch = when (dayOfWeek.value) {
            6, 7 -> "FimDeSemana"
            else -> {
                val locale = Locale.Builder().setLanguage("pt").setRegion("BR").build()
                dayOfWeek.getDisplayName(TextStyle.FULL, locale)
                    .replaceFirstChar { it.uppercase() }
            }
        }

        logger.info("🔍 Buscando tarefas para a coluna: {}", columnSearch)

        val tasks = taskRepository.getTasksByDay(columnSearch)
            ?: throw ColumnNotFoundException()

        if (tasks.isEmpty()) {
            logger.info("Nenhuma tarefa encontrada para a coluna: {}", columnSearch)
            return
        }

        val groupedTasks = tasks.groupBy { it.phoneNumber }

        groupedTasks.forEach { (phoneNumber, personTasks) ->
            if (phoneNumber != null) {
                try {
                    val name = personTasks.first().responsible
                    val tasksList = personTasks.joinToString("\n") { "• ${it.title}" }

                    val finalMessage = "Olá, *${name}*! 👋\n\n" +
                            "Aqui estão suas tarefas de hoje:\n" +
                            "${tasksList}\n\n" +
                            "Vamos nessa? 🚀"

                    messageClient.sendMessage(phoneNumber, finalMessage)
                    logger.info("✅ Mensagem enviada com sucesso para: {}", name)

                } catch (e: Exception) {
                    logger.error("❌ Falha ao processar envio para o número: {}", phoneNumber)
                    throw MessagingException(e)
                }
            }
        }
    }
}
