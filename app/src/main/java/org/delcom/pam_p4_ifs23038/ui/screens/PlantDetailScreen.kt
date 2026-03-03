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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23038.R
import org.delcom.pam_p4_ifs23038.helper.*
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23038.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23038.ui.components.*
import org.delcom.pam_p4_ifs23038.ui.viewmodels.*

@Composable
fun PlantsDetailScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    plantViewModel: PlantViewModel,
    plantId: String
) {
    val uiStatePlant by plantViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isConfirmDelete by remember { mutableStateOf(false) }
    var plant by remember { mutableStateOf<ResponsePlantData?>(null) }

    LaunchedEffect(Unit) {
        isLoading = true
        uiStatePlant.plantAction = PlantActionUIState.Loading
        uiStatePlant.plant = PlantUIState.Loading
        plantViewModel.getPlantById(plantId)
    }

    LaunchedEffect(uiStatePlant.plant) {
        if(uiStatePlant.plant !is PlantUIState.Loading){
            if(uiStatePlant.plant is PlantUIState.Success){
                plant = (uiStatePlant.plant as PlantUIState.Success).data
                isLoading = false
            } else {
                RouteHelper.back(navController)
            }
        }
    }

    fun onDelete(){
        isLoading = true
        plantViewModel.deletePlant(plantId)
    }

    LaunchedEffect(uiStatePlant.plantAction) {
        when (val state = uiStatePlant.plantAction) {
            is PlantActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.SUCCESS, message = "Data berhasil dihapus")
                RouteHelper.to(navController, ConstHelper.RouteNames.Plants.path, true)
                isLoading = false
            }
            is PlantActionUIState.Error -> {
                SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.ERROR, message = state.message)
                isLoading = false
            }
            else -> {}
        }
    }

    if(isLoading || plant == null){
        LoadingUI()
        return
    }

    val detailMenuItems = listOf(
        TopAppBarMenuItem(
            text = "Ubah Data",
            icon = Icons.Filled.Edit,
            route = null,
            onClick = {
                RouteHelper.to(navController, ConstHelper.RouteNames.PlantsEdit.path.replace("{plantId}", plant!!.id))
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
        TopAppBarComponent(navController = navController, title = plant!!.nama, showBackButton = true, customMenuItems = detailMenuItems)
        Box(modifier = Modifier.weight(1f)) {
            PlantsDetailUI(plant = plant!!)
            BottomDialog(
                type = BottomDialogType.ERROR,
                show = isConfirmDelete,
                onDismiss = { isConfirmDelete = false },
                title = "Konfirmasi Hapus",
                message = "Apakah Anda yakin ingin menghapus data ini?",
                confirmText = "Ya, Hapus",
                onConfirm = { onDelete() },
                cancelText = "Batal",
                destructiveAction = true
            )
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlantsDetailUI(plant: ResponsePlantData) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        AsyncImage(
            model = ToolsHelper.getPlantImageUrl(plant.id),
            contentDescription = plant.nama,
            placeholder = painterResource(R.drawable.img_placeholder),
            error = painterResource(R.drawable.img_placeholder),
            modifier = Modifier.fillMaxWidth().height(250.dp),
            contentScale = ContentScale.Fit
        )
        Text(text = plant.nama, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
        
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Deskripsi", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = plant.deskripsi, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Manfaat", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = plant.manfaat, style = MaterialTheme.typography.bodyMedium)
            }
        }
        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Efek Samping", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                Text(text = plant.efekSamping, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPlantsDetailUI() {
    // Preview logic
}
