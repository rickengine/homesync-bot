package br.com.homesync.client

import org.slf4j.LoggerFactory

class WhatsAppSimulador : MessageClient {
    private val logger = LoggerFactory.getLogger(WhatsAppSimulador::class.java)

    override fun sendMessage(phoneNumber: String, message: String) {
        logger.info("🧪 [SIMULADOR] Iniciando processo de envio de mensagem...")
        Thread.sleep(300)

        val formatado = """
            
            --------------------------------------------------
            📱 DESTINATÁRIO: $phoneNumber
            💬 MENSAGEM:
            $message
            --------------------------------------------------
        """.trimIndent()

        println(formatado)
        logger.info("✅ [SIMULADOR] Mensagem processada com sucesso.")
    }
}
