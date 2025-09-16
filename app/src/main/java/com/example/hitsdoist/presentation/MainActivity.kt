package com.example.hitsdoist.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.hitsdoist.presentation.theme.HITsDoistTheme

class MainActivity : ComponentActivity() {
    private val vm: TasksViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HITsDoistTheme {
                MainScreen(vm)
            }
        }
    }
}