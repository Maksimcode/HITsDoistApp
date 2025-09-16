package com.example.hitsdoist.logicSystem

import android.content.Context
import android.os.Environment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun readTasksJsonFromFile(context: Context): String? = withContext(Dispatchers.IO){
    val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: return@withContext null
    val file = File(dir, "tasks.json")
    return@withContext try {
        if(file.exists()) file.readText(Charsets.UTF_8) else null
    } catch (e: Exception){
        null
    }
}

class FileTasksStorage(private val context: Context) {
    fun writeJson(json: String) {
        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: return
        val file = File(dir, "tasks.json")
        file.parentFile?.mkdirs()
        file.writeText(json, Charsets.UTF_8)
    }

    suspend fun readJson(): String? = readTasksJsonFromFile(context)
}
