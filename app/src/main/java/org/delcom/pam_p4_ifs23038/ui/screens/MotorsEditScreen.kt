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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import okhttp3.MultipartBody
import org.delcom.pam_p4_ifs23038.R
import org.delcom.pam_p4_ifs23038.helper.*
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorData
import org.delcom.pam_p4_ifs23038.ui.components.*
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorActionUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorViewModel

@Composable
fun MotorsEditScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    motorViewModel: MotorViewModel, // FIXED: Use MotorViewModel
    motorId: String
) {
    DelcomTheme(isMotorTheme = true) {
        val uiState by motorViewModel.uiState.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
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
                    isLoading = false
                }
            }
        }

        fun onSave(context: Context, nama: String, deskripsi: String, spesifikasi: String, harga: String, file: Uri? = null) {
            isLoading = true
            val namaBody = nama.toRequestBodyText()
            val deskripsiBody = deskripsi.toRequestBodyText()
            val spesifikasiBody = spesifikasi.toRequestBodyText()
            val hargaBody = harga.toRequestBodyText()
            var filePart: MultipartBody.Part? = null
            if (file != null) filePart = uriToMultipart(context, file, "file")
            motorViewModel.putMotor(motorId = motorId, nama = namaBody, deskripsi = deskripsiBody, spesifikasi = spesifikasiBody, harga = hargaBody, file = filePart)
        }

        LaunchedEffect(uiState.motorAction) {
            when (val state = uiState.motorAction) {
                is MotorActionUIState.Success -> {
                    SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.SUCCESS, message = "Data motor berhasil diperbarui")
                    RouteHelper.to(navController, ConstHelper.RouteNames.MotorsDetail.path.replace("{motorId}", motorId), popUpTo = ConstHelper.RouteNames.MotorsDetail.path.replace("{motorId}", motorId), removeBackStack = true)
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
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                TopAppBarComponent(navController = navController, title = "Ubah Data Motor", showBackButton = true)
                Box(modifier = Modifier.weight(1f)) {
                    MotorsEditUI(motor = motor!!, onSave = ::onSave)
                }
                BottomNavComponent(navController = navController)
            }
        }
    }
}

@Composable
fun MotorsEditUI(motor: ResponseMotorData, onSave: (Context, String, String, String, String, Uri?) -> Unit) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }
    var dataNama by remember { mutableStateOf(motor.nama) }
    var dataDeskripsi by remember { mutableStateOf(motor.deskripsi) }
    var dataSpesifikasi by remember { mutableStateOf(motor.spesifikasi) }
    var dataHarga by remember { mutableStateOf(motor.harga) }
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri -> dataFile = uri }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.size(150.dp).align(Alignment.CenterHorizontally).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable {
            imagePicker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }, contentAlignment = Alignment.Center) {
            if (dataFile != null) {
                AsyncImage(model = dataFile, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            } else {
                AsyncImage(model = ToolsHelper.getPlantImageUrl(motor.id), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            }
        }
        OutlinedTextField(value = dataNama, onValueChange = { dataNama = it }, label = { Text("Nama Motor") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        OutlinedTextField(value = dataSpesifikasi, onValueChange = { dataSpesifikasi = it }, label = { Text("Spesifikasi") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        OutlinedTextField(value = dataHarga, onValueChange = { dataHarga = it }, label = { Text("Harga/Info Lain") }, modifier = Modifier.fillMaxWidth().height(100.dp))
        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(onClick = {
            if (dataNama.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Nama motor tidak boleh kosong!"); return@FloatingActionButton }
            onSave(context, dataNama, dataDeskripsi, dataSpesifikasi, dataHarga, dataFile)
        }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }
}
