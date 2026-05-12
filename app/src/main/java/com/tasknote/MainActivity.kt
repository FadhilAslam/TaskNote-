package com.tasknote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.rememberNavController
import com.tasknote.navigation.AppNavGraph
import com.tasknote.ui.theme.TaskNoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )

        val app = application as TaskNoteApplication
        val themePreferences = app.themePreferences

        setContent {
            val isDarkMode by themePreferences.isDarkMode.collectAsState(initial = false)
            TaskNoteTheme(darkTheme = isDarkMode) {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}