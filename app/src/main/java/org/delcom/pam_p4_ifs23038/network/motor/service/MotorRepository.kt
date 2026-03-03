package org.delcom.pam_p4_ifs23038.network.motor.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23038.helper.SuspendHelper
import org.delcom.pam_p4_ifs23038.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotor
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorAdd
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotors

class MotorRepository(private val motorApiService: MotorApiService) : IMotorRepository {
    override suspend fun getAllMotors(search: String?): ResponseMessage<ResponseMotors?> {
        return SuspendHelper.safeApiCall {
            motorApiService.getAllMotors(search)
        }
    }

    override suspend fun postMotor(
        nama: RequestBody,
        deskripsi: RequestBody,
        spesifikasi: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part
    ): ResponseMessage<ResponseMotorAdd?> {
        return SuspendHelper.safeApiCall {
            motorApiService.postMotor(nama, deskripsi, spesifikasi, harga, file)
        }
    }

    override suspend fun getMotorById(motorId: String): ResponseMessage<ResponseMotor?> {
        return SuspendHelper.safeApiCall {
            motorApiService.getMotorById(motorId)
        }
    }

    override suspend fun putMotor(
        motorId: String,
        nama: RequestBody,
        deskripsi: RequestBody,
        spesifikasi: RequestBody,
        harga: RequestBody,
        file: MultipartBody.Part?
    ): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            motorApiService.putMotor(motorId, nama, deskripsi, spesifikasi, harga, file)
        }
    }

    override suspend fun deleteMotor(motorId: String): ResponseMessage<String?> {
        return SuspendHelper.safeApiCall {
            motorApiService.deleteMotor(motorId)
        }
    }
}