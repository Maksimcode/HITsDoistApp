package com.example.hitsdoist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.hitsdoist.logicSystem.FileTasksStorage
import com.example.hitsdoist.logicSystem.Task
import com.example.hitsdoist.logicSystem.loadTasks
import com.example.hitsdoist.logicSystem.saveTasks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TasksViewModel(application: Application): AndroidViewModel(application) {
    val storage = FileTasksStorage(application)
    val tasks = MutableStateFlow<List<Task>>(emptyList())

    /*init { Для автоматической загрузки при старте приложения
        viewModelScope.launch {
            storage.readJson()?.let { json ->
                val loaded = loadTasks(json)
                if (loaded.isNotEmpty()) _tasks.value = loaded
            }
        }
    }
     */
    fun getTasksJson(): String = saveTasks(tasks.value)

    fun exportToFile() {
        val json = getTasksJson()
        viewModelScope.launch(Dispatchers.IO) { storage.writeJson(json) }
    }

    fun importFromFile() {
        viewModelScope.launch {
            val json = storage.readJson()
            json?.let {
                val list = loadTasks(it)
                tasks.value = list
            }
        }
    }

    fun toggleTask(id: String) {
        val updated = tasks.value.toMutableList()
        val idx = updated.indexOfFirst { it.id == id }
        if (idx >= 0) {
            updated[idx] = updated[idx].copy(isDone = !updated[idx].isDone)
            tasks.value = updated
        }
    }

    fun addTask(description: String) {
        if (description.isBlank()) return
        val new = Task(description = description, isDone = false)
        tasks.value = tasks.value + new
    }

    fun deleteTask(id: String) {
        tasks.value = tasks.value.filterNot { it.id == id }
    }

    fun editTask(id: String, newDescription: String) {
        if (newDescription.isBlank()) return
        val updated = tasks.value.toMutableList()
        val idx = updated.indexOfFirst { it.id == id }
        if (idx >= 0) {
            updated[idx] = updated[idx].copy(description = newDescription)
            tasks.value = updated
        }
    }
}