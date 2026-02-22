package br.com.homesync.model

import java.time.LocalTime

data class Task(
    val title: String,
    val responsible: String,
    val dayOfWeek: String,
    val phoneNumber: String? = null,
    val scheduledTime: LocalTime = LocalTime.of(0,35)
)
