package org.delcom.pam_p4_ifs23038.network.motor.service

import org.delcom.pam_p4_ifs23038.network.motor.service.IMotorRepository

interface IMotorAppContainer {
    val motorRepository: IMotorRepository
}