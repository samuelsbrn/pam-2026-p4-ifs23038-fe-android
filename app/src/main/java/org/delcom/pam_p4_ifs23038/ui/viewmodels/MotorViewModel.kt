package org.delcom.pam_p4_ifs23038.ui.viewmodels

import androidx.annotation.Keep
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorData
import org.delcom.pam_p4_ifs23038.network.motor.service.IMotorRepository
import javax.inject.Inject

sealed interface MotorsUIState {
    data class Success(val data: List<ResponseMotorData>) : MotorsUIState
    data class Error(val message: String) : MotorsUIState
    object Loading : MotorsUIState
}

sealed interface MotorUIState {
    data class Success(val data: ResponseMotorData) : MotorUIState
    data class Error(val message: String) : MotorUIState
    object Loading : MotorUIState
}

sealed interface MotorActionUIState {
    data class Success(val message: String) : MotorActionUIState
    data class Error(val message: String) : MotorActionUIState
    object Loading : MotorActionUIState
}

data class UIStateMotor(
    val motors: MotorsUIState = MotorsUIState.Loading,
    var motor: MotorUIState = MotorUIState.Loading,
    var motorAction: MotorActionUIState = MotorActionUIState.Loading
)

@HiltViewModel
@Keep
class MotorViewModel @Inject constructor(
    private val repository: IMotorRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(UIStateMotor())
    val uiState = _uiState.asStateFlow()

    fun getAllMotors(search: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(motors = MotorsUIState.Loading) }
            val result = runCatching { repository.getAllMotors(search) }.getOrNull()
            _uiState.update { 
                if (result?.status == "success") it.copy(motors = MotorsUIState.Success(result.data!!.motors))
                else it.copy(motors = MotorsUIState.Error(result?.message ?: "Gagal mengambil data"))
            }
        }
    }

    fun getMotorById(motorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(motor = MotorUIState.Loading) }
            val result = runCatching { repository.getMotorById(motorId) }.getOrNull()
            _uiState.update {
                if (result?.status == "success") it.copy(motor = MotorUIState.Success(result.data!!.motor))
                else it.copy(motor = MotorUIState.Error(result?.message ?: "Gagal mengambil data"))
            }
        }
    }

    fun postMotor(nama: RequestBody, deskripsi: RequestBody, spesifikasi: RequestBody, harga: RequestBody, file: MultipartBody.Part) {
        viewModelScope.launch {
            _uiState.update { it.copy(motorAction = MotorActionUIState.Loading) }
            val result = runCatching { repository.postMotor(nama, deskripsi, spesifikasi, harga, file) }.getOrNull()
            _uiState.update {
                if (result?.status == "success") it.copy(motorAction = MotorActionUIState.Success("Berhasil"))
                else it.copy(motorAction = MotorActionUIState.Error(result?.message ?: "Gagal simpan"))
            }
        }
    }

    fun putMotor(motorId: String, nama: RequestBody, deskripsi: RequestBody, spesifikasi: RequestBody, harga: RequestBody, file: MultipartBody.Part?) {
        viewModelScope.launch {
            _uiState.update { it.copy(motorAction = MotorActionUIState.Loading) }
            val result = runCatching { repository.putMotor(motorId, nama, deskripsi, spesifikasi, harga, file) }.getOrNull()
            _uiState.update {
                if (result?.status == "success") it.copy(motorAction = MotorActionUIState.Success("Berhasil diperbarui"))
                else it.copy(motorAction = MotorActionUIState.Error(result?.message ?: "Gagal perbarui"))
            }
        }
    }

    fun deleteMotor(motorId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(motorAction = MotorActionUIState.Loading) }
            val result = runCatching { repository.deleteMotor(motorId) }.getOrNull()
            _uiState.update {
                if (result?.status == "success") it.copy(motorAction = MotorActionUIState.Success("Berhasil dihapus"))
                else it.copy(motorAction = MotorActionUIState.Error(result?.message ?: "Gagal hapus"))
            }
        }
    }
}
