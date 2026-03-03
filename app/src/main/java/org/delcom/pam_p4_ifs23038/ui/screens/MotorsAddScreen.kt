package org.delcom.pam_p4_ifs23038.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import org.delcom.pam_p4_ifs23038.helper.*
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper.SnackBarType
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.toRequestBodyText
import org.delcom.pam_p4_ifs23038.helper.ToolsHelper.uriToMultipart
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorData
import org.delcom.pam_p4_ifs23038.ui.components.BottomNavComponent
import org.delcom.pam_p4_ifs23038.ui.components.LoadingUI
import org.delcom.pam_p4_ifs23038.ui.components.TopAppBarComponent
import org.delcom.pam_p4_ifs23038.ui.theme.DelcomTheme
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorActionUIState
import org.delcom.pam_p4_ifs23038.ui.viewmodels.MotorViewModel

@Composable
fun MotorsAddScreen(
    navController: NavHostController,
    snackbarHost: SnackbarHostState,
    motorViewModel: MotorViewModel
) {
    DelcomTheme(isMotorTheme = true) {
        val uiState by motorViewModel.uiState.collectAsState()
        var isLoading by remember { mutableStateOf(false) }
        var tmpMotor by remember { mutableStateOf<ResponseMotorData?>(null) }

        LaunchedEffect(Unit) {
            motorViewModel.clearMotorAction()
        }

        fun onSave(context: Context, nama: String, deskripsi: String, spesifikasi: String, harga: String, file: Uri) {
            isLoading = true
            val namaBody = nama.toRequestBodyText()
            val deskripsiBody = deskripsi.toRequestBodyText()
            val spesifikasiBody = spesifikasi.toRequestBodyText()
            val hargaBody = harga.toRequestBodyText()
            val filePart = uriToMultipart(context, file, "file")
            motorViewModel.postMotor(nama = namaBody, deskripsi = deskripsiBody, spesifikasi = spesifikasiBody, harga = hargaBody, file = filePart)
        }

        LaunchedEffect(uiState.motorAction) {
            when (val state = uiState.motorAction) {
                is MotorActionUIState.Success -> {
                    SuspendHelper.showSnackBar(snackbarHost = snackbarHost, type = SnackBarType.SUCCESS, message = "Motor berhasil ditambahkan")
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

        if (isLoading) {
            LoadingUI()
        } else {
            Column(modifier = Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.background)) {
                TopAppBarComponent(navController = navController, title = "Tambah Motor", showBackButton = true)
                Box(modifier = Modifier.weight(1f)) {
                    MotorsAddUI(tmpMotor = tmpMotor, onSave = ::onSave)
                }
                BottomNavComponent(navController = navController)
            }
        }
    }
}

@Composable
fun MotorsAddUI(tmpMotor: ResponseMotorData?, onSave: (Context, String, String, String, String, Uri) -> Unit) {
    val alertState = remember { mutableStateOf(AlertState()) }
    var dataFile by remember { mutableStateOf<Uri?>(null) }

    var dataNama by rememberSaveable { mutableStateOf(tmpMotor?.nama ?: "") }
    var dataDeskripsi by rememberSaveable { mutableStateOf(tmpMotor?.deskripsi ?: "") }
    var dataSpesifikasi by rememberSaveable { mutableStateOf(tmpMotor?.spesifikasi ?: "") }
    var dataHarga by rememberSaveable { mutableStateOf(tmpMotor?.harga ?: "") }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val deskripsiFocus = remember { FocusRequester() }
    val spesifikasiFocus = remember { FocusRequester() }
    val hargaFocus = remember { FocusRequester() }

    val imagePicker = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri -> dataFile = uri }

    Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(150.dp).clip(RoundedCornerShape(12.dp)).background(MaterialTheme.colorScheme.primaryContainer).clickable {
                imagePicker.launch("image/*")
            }, contentAlignment = Alignment.Center) {
                if (dataFile != null) {
                    AsyncImage(model = dataFile, contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
                } else {
                    Text(text = "Pilih Gambar Motor", color = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
        }

        OutlinedTextField(value = dataNama, onValueChange = { dataNama = it }, label = { Text("Nama Motor") }, modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { deskripsiFocus.requestFocus() }))
        OutlinedTextField(value = dataDeskripsi, onValueChange = { dataDeskripsi = it }, label = { Text("Deskripsi") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(deskripsiFocus), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { spesifikasiFocus.requestFocus() }))
        OutlinedTextField(value = dataSpesifikasi, onValueChange = { dataSpesifikasi = it }, label = { Text("Spesifikasi") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(spesifikasiFocus), keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next), keyboardActions = KeyboardActions(onNext = { hargaFocus.requestFocus() }))
        OutlinedTextField(value = dataHarga, onValueChange = { dataHarga = it }, label = { Text("Harga/Info Lain") }, modifier = Modifier.fillMaxWidth().height(100.dp).focusRequester(hargaFocus), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done), keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }))
        Spacer(modifier = Modifier.height(64.dp))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        FloatingActionButton(onClick = {
            if (dataFile == null) { AlertHelper.show(alertState, AlertType.ERROR, "Gambar harus dipilih!"); return@FloatingActionButton }
            if (dataNama.isEmpty()) { AlertHelper.show(alertState, AlertType.ERROR, "Nama motor tidak boleh kosong!"); return@FloatingActionButton }
            onSave(context, dataNama, dataDeskripsi, dataSpesifikasi, dataHarga, dataFile!!)
        }, modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
            Icon(Icons.Default.Save, contentDescription = "Simpan")
        }
    }

    if (alertState.value.isVisible) {
        AlertDialog(onDismissRequest = { AlertHelper.dismiss(alertState) }, title = { Text(alertState.value.type.title) }, text = { Text(alertState.value.message) }, confirmButton = { TextButton(onClick = { AlertHelper.dismiss(alertState) }) { Text("OK") } })
    }
}