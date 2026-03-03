package org.delcom.pam_p4_ifs23038.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23038.R
import org.delcom.pam_p4_ifs23038.helper.*
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23038.network.plants.data.ResponsePlantData
import org.delcom.pam_p4_ifs23038.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23038.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23038.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23038.ui.viewmodels.PlantActionUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.PlantViewModel

@Composable
fun PlantAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    plantViewModel: PlantViewModel
) {
    val uiStatePlant by plantViewModel.uiState.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var tmpPlant by remember { mutableStateOf<ResponsePlantData?>(null) }

    LaunchedEffect(Unit) {
        uiStatePlant.plantAction = PlantActionUIState.Loading
    }

    fun onSave(context: Context, nama: String, deskripsi: String, manfaat: String, efekSamping: String, file: Uri) {
        isLoading = true
        tmpPlant = ResponsePlantData(nama = nama, deskripsi = deskripsi, manfaat = manfaat, efekSamping = efekSamping, id = "", createdAt = "", updatedAt = "")
        val namaBody = nama.toRequestBodyText()
        val deskripsiBody = deskripsi.toRequestBodyText()
        val manfaatBody = manfaat.toRequestBodyText()
        val efekBody = efekSamping.toRequestBodyText()
        val filePart = uriToMultipart(context, file, "file")
        plantViewModel.postPlant(nama = namaBody, deskripsi = deskripsiBody, manfaat = manfaatBody, efekSamping = efekBody, file = filePart)
    }

    LaunchedEffect(uiStatePlant.plantAction) {
        when (val state = uiStatePlant.plantAction) {
            is PlantActionUIState.Success -> {
                SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.SUCCESS, message = state.message)
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

    if (isLoading) {
        LoadingUI()
        return
    }

    Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
        TopAppBarComponent(navController = navController, title = "Tambah Tumbuhan", showBackButton = true)
        Box(modifier = Modifier.weight(1f)) {
            PlantAddUI(tmpPlant = tmpPlant, onSave = ::onSave)
        }
        BottomNavComponent(navController = navController)
    }
}

@Composable
fun PlantAddUI(tmpPlant: ResponsePlantData?, onSave: (Context, String, String, String, String, Uri) -> Unit) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(tmpPlant?.nama ?: "") }
    var dataDeskripsi by remember { mutableStateOf(tmpPlant?.deskripsi ?: "") }
    var dataManfaat by remember { mutableStateOf(tmpPlant?.manfaat ?: "") }
    var dataEfekSamping by remember { mutableStateOf(tmpPlant?.efekSamping ?: "") }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val deskripsiFocus = remember { FocusRequester() }
    val manfaatFocus = remember { FocusRequester() }
    val efekFocus = remember { FocusRequester() }

    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri -> dataFile = uri }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable {
                imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }, contentAlignment = Alignment.Center) {
                if (dataFile != null) {
                    AsyncImage(model = dataFile, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Text(text = "Pilih Gambar Tumbuhan", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        OutlinedTextField(value = dataNama, onValueChange = { dataNama = it }, label = { Text("Nama Tumbuhan") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(deskripsiFocus))
        OutlinedTextField(value = dataManfaat, onValueChange = { dataManfaat = it }, label = { Text("Manfaat") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(manfaatFocus))
        OutlinedTextField(value = dataEfekSamping, onValueChange = { dataEfekSamping = it }, label = { Text("Efek Samping") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(efekFocus))
        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(onClick = {
            if (dataFile == null) { AlertHelper.show(alertState, AlertType.ERROR, "Gambar harus dipilih!"); return@FloatingActionButton }
            if (dataNama.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Nama tidak boleh kosong!"); return@FloatingActionButton }
            onSave(context, dataNama, dataDeskripsi, dataManfaat, dataEfekSamping, dataFile!!)
        }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(onDismissRequest = { AlertHelper.dismiss(alertState) }, title = { Text(alertState.value.type.title) }, text = { Text(alertState.value.message) }, confirmButton = { TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") } })
    }
}
