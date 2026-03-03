package org.delcom.pam_p4_ifs23038.network.motor.data

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMotors (
    val motors: List<ResponseMotorData>
)

@Serializable
data class ResponseMotor (
    val motor: ResponseMotorData
)

@Serializable
data class ResponseMotorAdd (
    val motorId: String
)

@Serializable
data class ResponseMotorData(
    val id: String,
    val nama: String,
    val deskripsi: String,
    val spesifikasi: String,
    val harga: String,
    val createdAt: String,
    val updatedAt: String
)
