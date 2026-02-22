package br.com.homesync.repository

import br.com.homesync.model.Task

interface TaskRepository {
    fun getTasksByDay(dayOfWeek: String): List<Task>
}
