package com.example.hitsdoist.logicSystem

import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    private var description: String,
    var isDone: Boolean
){
    init{
        require(description.isNotBlank())
    }

    fun editTask(newText: String){
        require(newText.isNotBlank()) { "Описание не может быть пустым" }
        description = newText
    }

    fun deleteTask(){

    }

    fun toggleStatus(){
        isDone = !isDone
    }

}