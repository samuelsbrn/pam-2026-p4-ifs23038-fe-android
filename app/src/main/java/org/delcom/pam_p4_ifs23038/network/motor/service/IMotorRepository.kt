package org.delcom.pam_p4_ifs23038.network.motor.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23038.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotor
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorAdd
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotors

interface IMotorRepository {
    // Ambil semua data motor
    suspend fun getAllMotors(
        search: String? = null
    ): ResponseMessage<ResponseMotors?>

    // Tambah data motor
    suspend fun postMotor(
        nama: RequestBody,
        deskripsi: RequestBody,
        spesifikasi: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseMotorAdd?>

    // Ambil data motor berdasarkan ID
    suspend fun getMotorById(
        motorId: String
    ): ResponseMessage<ResponseMotor?>

    // Ubah data motor
    suspend fun putMotor(
        motorId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        spesifikasi: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data motor
    suspend fun deleteMotor(
        motorId: String
    ): ResponseMessage<String?>
}