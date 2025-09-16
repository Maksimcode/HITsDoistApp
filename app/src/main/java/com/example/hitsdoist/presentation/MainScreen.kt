package com.example.hitsdoist.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.hitsdoist.logicSystem.Task

@Composable
fun MainScreen(vm: TasksViewModel) {
    val tasks by vm.tasks.collectAsState(initial = emptyList())

    var newText by rememberSaveable { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(48.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newText,
                onValueChange = { newText = it },
                label = { Text("Новая задача") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                val t = newText.trim()
                if (t.isNotBlank()) {
                    vm.addTask(t)
                    newText = ""
                }
            }) {
                Text("Добавить")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
            Button(onClick = { vm.exportToFile() }) { Text("Сохранить") }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { vm.importFromFile() }) { Text("Загрузить") }
        }

        Spacer(modifier = Modifier.height(16.dp))

        val pending = tasks.filter { !it.isDone }
        val done = tasks.filter { it.isDone }

        Text(text = "Активные")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            items(items = pending, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    onToggle = { vm.toggleTask(task.id) },
                    onDelete = { vm.deleteTask(task.id) },
                    onEdit = { newTitle -> vm.editTask(task.id, newTitle) }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Выполненные")
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
        ) {
            items(items = done, key = { it.id }) { task ->
                TaskRow(
                    task = task,
                    onToggle = { vm.toggleTask(task.id) },
                    onDelete = { vm.deleteTask(task.id) },
                    onEdit = { newTitle -> vm.editTask(task.id, newTitle) }
                )
            }
        }
    }
}

@Composable
fun TaskRow(
    task: Task,
    onToggle: () -> Unit,
    onDelete: () -> Unit,
    onEdit: (String) -> Unit
) {
    var editing by remember { mutableStateOf(false) }
    var editText by remember { mutableStateOf(task.description) }

    LaunchedEffect(task.id, task.description) {
        editText = task.description
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.isDone, onCheckedChange = { onToggle() })
        Spacer(modifier = Modifier.width(8.dp))

        if (editing) {
            OutlinedTextField(
                value = editText,
                onValueChange = { editText = it },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = {
                val t = editText.trim()
                if (t.isNotBlank()) onEdit(t)
                editing = false
            }) {
                Icon(Icons.Default.Check, contentDescription = "Save")
            }
            IconButton(onClick = { editing = false }) {
                Icon(Icons.Default.Close, contentDescription = "Cancel")
            }
        } else {
            Text(text = task.description)
            IconButton(onClick = { editing = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Edit")
            }
            IconButton(onClick = { onDelete() }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}
