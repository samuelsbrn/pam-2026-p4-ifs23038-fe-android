package org.delcom.pam_p4_ifs23038.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23038.R
import org.delcom.pam_p4_ifs23038.helper.ConstHelper
import org.delcom.pam_p4_ifs23038.helper.RouteHelper
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorData
import org.delcom.pam_p4_ifs23038.ui.components.*
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorActionUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorViewModel

@Composable
fun MotorsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    motorViewModel: MotorViewModel, // FIXED: Changed from plantViewModel
    motorId: String
) {
    DelcomTheme(isMotorTheme = true) {
        val uiState by motorViewModel.uiState.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
        var isConfirmDelete by remember { mutableStateOf(false) }
        var motor by remember { mutableStateOf<ResponseMotorData?>(null) }

        LaunchedEffect(Unit) {
            isLoading = true
            motorViewModel.getMotorById(motorId)
        }

        LaunchedEffect(uiState.motor) {
            if (uiState.motor !is MotorUIState.Loading) {
                if (uiState.motor is MotorUIState.Success) {
                    motor = (uiState.motor as MotorUIState.Success).data
                    isLoading = false
                } else {
                    RouteHelper.back(navController)
                }
            }
        }

        fun onDelete() {
            isLoading = true
            motorViewModel.deleteMotor(motorId)
        }

        LaunchedEffect(uiState.motorAction) {
            when (val state = uiState.motorAction) {
                is MotorActionUIState.Success -> {
                    SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.SUCCESS, message = "Data motor berhasil dihapus")
                    RouteHelper.to(navController, ConstHelper.RouteNames.Motors.path, true)
                    isLoading = false
                }
                is MotorActionUIState.Error -> {
                    SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.ERROR, message = state.message)
                    isLoading = false
                }
                else -> {}
            }
        }

        if (isLoading || motor == null) {
            LoadingUI()
        } else {
            val detailMenuItems = listOf(
                TopAppBarMenuItem(
                    text = "Ubah Data",
                    icon = Icons.Filled.Edit,
                    route = null,
                    onClick = {
                        RouteHelper.to(navController, ConstHelper.RouteNames.MotorsEdit.path.replace("{motorId}", motor!!.id))
                    }
                ),
                TopAppBarMenuItem(
                    text = "Hapus Data",
                    icon = Icons.Filled.Delete,
                    route = null,
                    onClick = { isConfirmDelete = true }
                ),
            )

            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                TopAppBarComponent(navController = navController, title = motor!!.nama, showBackButton = true, customMenuItems = detailMenuItems)
                Box(modifier = Modifier.weight(1f)) {
                    MotorsDetailUI(motor = motor!!)
                    BottomDialog(
                        type = BottomDialogType.ERROR,
                        show = isConfirmDelete,
                        onDismiss = { isConfirmDelete = false },
                        title = "Konfirmasi Hapus",
                        message = "Yakin ingin menghapus data motor ini?",
                        confirmText = "Ya, Hapus",
                        onConfirm = { onDelete() },
                        cancelText = "Batal",
                        destructiveAction = true
                    )
                }
                BottomNavComponent(navController = navController)
            }
        }
    }
}

@Composable
fun MotorsDetailUI(motor: ResponseMotorData) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = ToolsHelper.getPlantImageUrl(motor.id),
            contentDescription = motor.nama,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Fit
        )
        Text(text = motor.nama, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
        
        MotorInfoCard(title = "Deskripsi", content = motor.deskripsi)
        MotorInfoCard(title = "Spesifikasi", content = motor.spesifikasi)
        MotorInfoCard(title = "Harga/Info Lain", content = motor.harga)
    }
}

@Composable
fun MotorInfoCard(title: String, content: String) {
    Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
