package com.tasknote.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tasknote.ui.screen.addedit.AddEditScreen
import com.tasknote.ui.screen.detail.DetailScreen
import com.tasknote.ui.screen.home.HomeScreen
import com.tasknote.ui.screen.recyclebin.RecycleBinScreen
import com.tasknote.ui.screen.settings.SettingsScreen

object Routes {
    const val HOME = "home"
    const val ADD = "add"
    const val EDIT = "edit/{noteId}"
    const val DETAIL = "detail/{noteId}"
    const val RECYCLE_BIN = "recycle_bin"
    const val SETTINGS = "settings"

    fun editRoute(noteId: Int) = "edit/$noteId"
    fun detailRoute(noteId: Int) = "detail/$noteId"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onAddNote = { navController.navigate(Routes.ADD) },
                    onNoteClick = { noteId -> navController.navigate(Routes.detailRoute(noteId)) },
                    onRecycleBinClick = { navController.navigate(Routes.RECYCLE_BIN) },
                    onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                    onEditNote = { noteId -> navController.navigate(Routes.editRoute(noteId)) }
                )
            }

            composable(Routes.ADD) {
                AddEditScreen(
                    noteId = null,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.EDIT,
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId")
                AddEditScreen(
                    noteId = noteId,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(
                route = Routes.DETAIL,
                arguments = listOf(navArgument("noteId") { type = NavType.IntType })
            ) { backStackEntry ->
                val noteId = backStackEntry.arguments?.getInt("noteId") ?: return@composable
                DetailScreen(
                    noteId = noteId,
                    onNavigateBack = { navController.popBackStack() },
                    onEditClick = { navController.navigate(Routes.editRoute(noteId)) },
                    onNoteDeleted = { navController.popBackStack() }
                )
            }

            composable(Routes.RECYCLE_BIN) {
                RecycleBinScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                    onNavigateBack = { navController.popBackStack() }
                )
            }
        }

        // Overlay hitam di area status bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
                .background(Color.Black)
        )
    }
}