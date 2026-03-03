package org.delcom.pam_p4_ifs23038.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.delcom.pam_p4_ifs23038.network.motor.service.IMotorAppContainer
import org.delcom.pam_p4_ifs23038.network.motor.service.IMotorRepository
import org.delcom.pam_p4_ifs23038.network.motor.service.MotorAppContainer
import org.delcom.pam_p4_ifs23038.network.motor.service.MotorRepository

@Module
@InstallIn(SingletonComponent::class)
object MotorModule {
    @Provides
    fun provideMotorContainer(): IMotorAppContainer {
        return MotorAppContainer()
    }

    @Provides
    fun provideMotorRepository(container: IMotorAppContainer): IMotorRepository {
        return container.motorRepository
    }
}
