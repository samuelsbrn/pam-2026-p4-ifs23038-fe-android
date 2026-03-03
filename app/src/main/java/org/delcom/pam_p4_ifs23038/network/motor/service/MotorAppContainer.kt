package org.delcom.pam_p4_ifs23038.network.motor.service

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.delcom.pam_p4_ifs23038.BuildConfig
import org.delcom.pam_p4_ifs23038.network.motor.service.MotorRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class MotorAppContainer: IMotorAppContainer {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    val okHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }

        connectTimeout(2, TimeUnit.MINUTES)
        readTimeout(2, TimeUnit.MINUTES)
        writeTimeout(2, TimeUnit.MINUTES)
    }.build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL_PANTS_API)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    private val retrofitService: MotorApiService by lazy {
        retrofit.create(MotorApiService::class.java)
    }

    override val motorRepository: IMotorRepository by lazy {
        MotorRepository(retrofitService)
    }
}