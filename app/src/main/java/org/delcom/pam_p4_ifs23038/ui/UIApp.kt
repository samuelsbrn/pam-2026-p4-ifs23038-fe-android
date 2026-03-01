package org.delcom.pam_p4_ifs23038.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.delcom.pam_p4_ifs23038.helper.ConstHelper
import org.delcom.pam_p4_ifs23038.ui.components.CustomSnackbar
import org.delcom.pam_p4_ifs23038.ui.screens.HomeScreen
import org.delcom.pam_p4_ifs23038.ui.screens.PlantsAddScreen
import org.delcom.pam_p4_ifs23038.ui.screens.PlantsDetailScreen
import org.delcom.pam_p4_ifs23038.ui.screens.PlantsEditScreen
import org.delcom.pam_p4_ifs23038.ui.screens.PlantsScreen
import org.delcom.pam_p4_ifs23038.ui.screens.ProfileScreen
import org.delcom.pam_p4_ifs23038.ui.viewmodels.PlantViewModel

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun UIApp(
    navController: NavHostController = rememberNavController(),
    plantViewModel: PlantViewModel
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { 
            SnackbarHost(snackbarHostState){ snackbarData ->
                CustomSnackbar(snackbarData, onDismiss = { snackbarHostState.currentSnackbarData?.dismiss() })
            } 
        },
    ) { _ ->
        NavHost(
            navController = navController,
            startDestination = ConstHelper.RouteNames.Home.path,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
        ) {
            // Home
            composable(route = ConstHelper.RouteNames.Home.path) {
                HomeScreen(navController = navController)
            }

            // Profile
            composable(route = ConstHelper.RouteNames.Profile.path) {
                ProfileScreen(navController = navController, plantViewModel = plantViewModel)
            }

            // --- SECTION PLANTS ---
            composable(route = ConstHelper.RouteNames.Plants.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }
            composable(route = ConstHelper.RouteNames.PlantsAdd.path) {
                PlantsAddScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel)
            }
            composable(
                route = ConstHelper.RouteNames.PlantsDetail.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsDetailScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = plantId)
            }
            composable(
                route = ConstHelper.RouteNames.PlantsEdit.path,
                arguments = listOf(navArgument("plantId") { type = NavType.StringType })
            ) { backStackEntry ->
                val plantId = backStackEntry.arguments?.getString("plantId") ?: ""
                PlantsEditScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = plantId)
            }

            // --- SECTION MOTORS ---
            composable(route = ConstHelper.RouteNames.Motors.path) {
                PlantsScreen(navController = navController, plantViewModel = plantViewModel)
            }
            composable(route = ConstHelper.RouteNames.MotorsAdd.path) {
                PlantsAddScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel)
            }
            composable(
                route = ConstHelper.RouteNames.MotorsDetail.path,
                arguments = listOf(navArgument("motorId") { type = NavType.StringType })
            ) { backStackEntry ->
                val motorId = backStackEntry.arguments?.getString("motorId") ?: ""
                PlantsDetailScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = motorId)
            }
            composable(
                route = ConstHelper.RouteNames.MotorsEdit.path,
                arguments = listOf(navArgument("motorId") { type = NavType.StringType })
            ) { backStackEntry ->
                val motorId = backStackEntry.arguments?.getString("motorId") ?: ""
                PlantsEditScreen(navController = navController, snackbarHost = snackbarHostState, plantViewModel = plantViewModel, plantId = motorId)
            }
        }
    }
}
