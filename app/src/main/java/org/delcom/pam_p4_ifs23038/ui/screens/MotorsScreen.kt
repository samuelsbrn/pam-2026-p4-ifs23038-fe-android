package org.delcom.pam_p4_ifs23038.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23038.R
import org.delcom.pam_p4_ifs23038.helper.ConstHelper
import org.delcom.pam_p4_ifs23038.helper.RouteHelper
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorData
import org.delcom.pam_p4_ifs23038.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23038.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23038.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorViewModel
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorsUIState

@Composable
fun MotorsScreen(
    navController: NavHostController,
    motorViewModel: MotorViewModel // Diubah dari plantViewModel
) {
    DelcomTheme(isMotorTheme = true) {
        val uiState by motorViewModel.uiState.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
        var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
        var motors by remember { mutableStateOf<List<ResponseMotorData>>(emptyList()) }

        fun fetchMotorsData() {
            isLoading = true
            motorViewModel.getAllMotors(searchQuery.text)
        }

        LaunchedEffect(Unit) {
            fetchMotorsData()
        }

        LaunchedEffect(uiState.motors) {
            if (uiState.motors !is MotorsUIState.Loading) {
                isLoading = false
                motors = if (uiState.motors is MotorsUIState.Success) {
                    (uiState.motors as MotorsUIState.Success).data
                } else {
                    emptyList()
                }
            }
        }

        if (isLoading) {
            LoadingUI()
        } else {
            fun onOpen(motorId: String) {
                RouteHelper.to(
                    navController = navController,
                    destination = ConstHelper.RouteNames.MotorsDetail.path.replace("{motorId}", motorId)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopAppBarComponent(
                    navController = navController,
                    title = "Motors",
                    showBackButton = false,
                    withSearch = true,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { query -> searchQuery = query },
                    onSearchAction = { fetchMotorsData() }
                )
                Box(modifier = Modifier.weight(1f)) {
                    MotorsUI(motors = motors, onOpen = ::onOpen)
                    Box(modifier = Modifier.fillMaxSize()) {
                        FloatingActionButton(
                            onClick = { RouteHelper.to(navController, ConstHelper.RouteNames.MotorsAdd.path) },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(16.dp),
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = "Tambah Motor")
                        }
                    }
                }
                BottomNavComponent(navController = navController)
            }
        }
    }
}

@Composable
fun MotorsUI(motors: List<ResponseMotorData>, onOpen: (String) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        items(motors) { motor ->
            MotorItemUI(motor, onOpen)
        }
    }
    if(motors.isEmpty()){
        Card(modifier = Modifier.fillMaxWidth().padding(8.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Text(text = "Tidak ada data motor!", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun MotorItemUI(motor: ResponseMotorData, onOpen: (String) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onOpen(motor.id) }, elevation = CardDefaults.cardElevation(4.dp)) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            AsyncImage(
                model = ToolsHelper.getMotorImageUrl(motor.id),
                contentDescription = motor.nama,
                placeholder = painterResource(R.drawable.img_placeholder),
                error = painterResource(R.drawable.img_placeholder),
                modifier = Modifier.size(70.dp).clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = motor.nama, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = motor.deskripsi, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
