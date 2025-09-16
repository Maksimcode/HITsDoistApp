package com.example.hitsdoist.logicSystem

import kotlinx.serialization.json.Json

private val jsonFormatter = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
}

fun saveTasks (tasks: List<Task>): String {
    return jsonFormatter.encodeToString(tasks)
}
fun loadTasks(jsonText: String): List<Task> =
    try {
        jsonFormatter.decodeFromString(jsonText)
    } catch (e: Exception) {
        emptyList()
    }