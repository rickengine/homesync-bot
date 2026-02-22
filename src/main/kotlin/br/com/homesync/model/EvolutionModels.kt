package br.com.homesync.model

data class WhatsAppMessage(
    val number: String,
    val textMessage: TextContent,
    val options: MessageOptions = MessageOptions()
)

data class TextContent(
    val text: String
)

data class MessageOptions(
    val delay: Int = 1200,
    val presence: String = "composing"
)
