package com.example.hitsdoist.logicSystem

import kotlinx.serialization.Serializable
import java.util.UUID


@Serializable
data class Task(
    val id: String = UUID.randomUUID().toString(),
    var description: String,
    var isDone: Boolean = false
)