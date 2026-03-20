package com.practice.hp.hospitalmanagement.security

import com.practice.hp.hospitalmanagement.util.types.PermissionType
import com.practice.hp.hospitalmanagement.util.types.RoleType

object RoleToPermission {

    val mapping: Map<RoleType, Set<PermissionType>> = mapOf(
        RoleType.ADMIN to setOf(
            PermissionType.USER_MANAGEMENT,
            PermissionType.REPORT_VIEW_READ,
            PermissionType.PATIENT_READ,
            PermissionType.PATIENT_WRITE,
            PermissionType.APPOINTMENT_READ,
            PermissionType.APPOINTMENT_WRITE,
            PermissionType.APPOINTMENT_DELETE
        ),
        RoleType.DOCTOR to setOf(
            PermissionType.PATIENT_READ,
            PermissionType.APPOINTMENT_READ,
//            PermissionType.APPOINTMENT_WRITE
        ),
        RoleType.PATIENT to setOf(
            PermissionType.PATIENT_READ,
            PermissionType.APPOINTMENT_READ,
            PermissionType.APPOINTMENT_WRITE
        )
    )
}