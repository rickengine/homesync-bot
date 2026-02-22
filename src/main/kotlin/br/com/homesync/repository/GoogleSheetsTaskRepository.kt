package br.com.homesync.repository

import br.com.homesync.error.exceptions.ColumnNotFoundException
import br.com.homesync.error.exceptions.SheetReadException
import br.com.homesync.model.Task
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import io.github.cdimascio.dotenv.dotenv
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream

class GoogleSheetsTaskRepository(private val spreadsheetId: String) : TaskRepository {

    private val logger = LoggerFactory.getLogger(GoogleSheetsTaskRepository::class.java)
    private val jsonFactory = GsonFactory.getDefaultInstance()
    private val transport = GoogleNetHttpTransport.newTrustedTransport()
    private val env = dotenv { ignoreIfMissing = true }

    private val service: Sheets by lazy {
        val rawCredentials = env["GOOGLE_CREDENTIALS_JSON"] ?: System.getenv("GOOGLE_CREDENTIALS_JSON")
        ?: throw IllegalStateException("Variável GOOGLE_CREDENTIALS_JSON não configurada")

        val cleanCredentialsJson = rawCredentials.trim('\'', '"')

        val credentials = ServiceAccountCredentials.fromStream(ByteArrayInputStream(cleanCredentialsJson.toByteArray()))
            .createScoped(listOf(SheetsScopes.SPREADSHEETS_READONLY))

        Sheets.Builder(transport, jsonFactory, HttpCredentialsAdapter(credentials))
            .setApplicationName("HomeSync-Bot")
            .build()
    }

    override fun getTasksByDay(dayOfWeek: String): List<Task> {
        try {
            val contacts = getContactsFromSheet()
            val range = "Agenda!A:G"

            val response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute()

            val rows = response.getValues() ?: return emptyList()
            val header = rows[0].map { it.toString() }

            val columnIndex = header.indexOfFirst { it.trim().equals(dayOfWeek, ignoreCase = true) }

            if (columnIndex == -1) {
                logger.error("Coluna '{}' não encontrada na planilha Agenda. Colunas: {}", dayOfWeek, header)
                throw ColumnNotFoundException()
            }

            return rows.drop(1).mapNotNull { row ->
                val taskTitle = row.getOrNull(0)?.toString() ?: ""
                val responsible = row.getOrNull(columnIndex)?.toString() ?: ""

                if (taskTitle.isNotEmpty() && responsible.isNotEmpty()) {
                    Task(
                        title = taskTitle,
                        responsible = responsible,
                        dayOfWeek = dayOfWeek,
                        phoneNumber = contacts[responsible]
                    )
                } else null
            }
        } catch (e: Exception) {
            if (e is ColumnNotFoundException) throw e
            logger.error("Erro ao ler planilha do Google: {}", e.message)
            throw SheetReadException(e)
        }
    }

    private fun getContactsFromSheet(): Map<String, String> {
        val range = "Contatos!A:B"
        val response = service.spreadsheets().values()
            .get(spreadsheetId, range)
            .execute()

        val rows = response.getValues() ?: return emptyMap()
        return rows.drop(1).associate {
            it[0].toString() to (it.getOrNull(1)?.toString() ?: "")
        }
    }
}
