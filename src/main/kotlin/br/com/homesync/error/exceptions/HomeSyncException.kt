package br.com.homesync.error.exceptions

import br.com.homesync.error.ErrorMessage

sealed class HomeSyncException(val error: ErrorMessage, cause: Throwable? = null)
    : RuntimeException("${error.code}: ${error.message}", cause)

class ColumnNotFoundException : HomeSyncException(ErrorMessage.COLUMN_NOT_FOUND)

class MessagingException(cause: Throwable) : HomeSyncException(ErrorMessage.WHATSAPP_API_ERROR, cause)

class SheetReadException(cause: Throwable? = null) : HomeSyncException(ErrorMessage.SHEET_READ_ERROR, cause)
