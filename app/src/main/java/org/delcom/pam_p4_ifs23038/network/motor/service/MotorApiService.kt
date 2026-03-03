package org.delcom.pam_p4_ifs23038.network.motor.service

import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.delcom.pam_p4_ifs23038.network.data.ResponseMessage
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotor
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotorAdd
import org.delcom.pam_p4_ifs23038.network.motor.data.ResponseMotors
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface MotorApiService {
    // Ambil semua data motor
    @GET("motors")
    suspend fun getAllMotors(
        @Query("search") search: String? = null
    ): ResponseMessage<ResponseMotors?>

    // Tambah data motor
    @Multipart
    @POST("motors")
    suspend fun postMotor(
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("spesifikasi") spesifikasi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part file: MultipartBody.Part
    ): ResponseMessage<ResponseMotorAdd?>

    // Ambil data motor berdasarkan ID
    @GET("motors/{motorId}")
    suspend fun getMotorById(
        @Path("motorId") motorId: String
    ): ResponseMessage<ResponseMotor?>

    // Ubah data motor
    @Multipart
    @PUT("motors/{motorId}")
    suspend fun putMotor(
        @Path("motorId") motorId: String,
        @Part("nama") nama: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody,
        @Part("spesifikasi") spesifikasi: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part file: MultipartBody.Part? = null
    ): ResponseMessage<String?>

    // Hapus data motor
    @DELETE("motors/{motorId}")
    suspend fun deleteMotor(
        @Path("motorId") motorId: String
    ): ResponseMessage<String?>
}