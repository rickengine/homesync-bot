package br.com.homesync

import br.com.homesync.client.MessageClient
import br.com.homesync.model.TextContent
import br.com.homesync.model.WhatsAppMessage
import io.github.cdimascio.dotenv.dotenv
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class WhatsAppClient : MessageClient {

    private val client = OkHttpClient()
    private val mapper = com.fasterxml.jackson.module.kotlin.jacksonObjectMapper()

    private val env = dotenv {
        ignoreIfMissing = true
    }

    private val baseUrl = env["WHATSAPP_BASE_URL"] ?: System.getenv("WHATSAPP_BASE_URL") ?: "http://localhost:8080"
    private val apiKey = (env["WHATSAPP_API_TOKEN"] ?: System.getenv("WHATSAPP_API_TOKEN") ?: "chave_default").trim('\'', '"', ' ')
    private val instanceName = env["WHATSAPP_INSTANCE_NAME"] ?: System.getenv("WHATSAPP_INSTANCE_NAME") ?: "HomeSync"

    override fun sendMessage(phoneNumber: String, message: String) {
        val payload = WhatsAppMessage(
            number = phoneNumber,
            textMessage = TextContent(text = message)
        )

        val jsonString = mapper.writeValueAsString(payload)
        val mediaType = "application/json; charset=utf-8".toMediaType()
        val body = jsonString.toRequestBody(mediaType)

        val request = Request.Builder()
            .url("$baseUrl/message/sendText/$instanceName")
            .addHeader("apikey", apiKey)
            .post(body)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    println("✅ Mensagem enviada com sucesso para $phoneNumber!")
                } else {
                    val errorBody = response.body?.string()
                    println("❌ Falha no envio para $phoneNumber: Código ${response.code} - $errorBody")
                    throw RuntimeException("Falha na API do WhatsApp: ${response.code}")
                }
            }
        } catch (e: Exception) {
            println("🚨 Erro de conexão ao tentar falar com a Evolution API: ${e.message}")
            throw e
        }
    }
}