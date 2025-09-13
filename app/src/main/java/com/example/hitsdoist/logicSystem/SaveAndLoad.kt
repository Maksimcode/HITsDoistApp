package com.example.hitsdoist.logicSystem

import kotlinx.serialization.json.Json

private val jsonFormatter = Json {
    prettyPrint = true
    isLenient = true
}

fun saveTasks (tasks: List<Task>): String {
    return jsonFormatter.encodeToString(tasks)
}
fun loadTasks(jsonText: String): List<Task> {
    return jsonFormatter.decodeFromString(jsonText)
}