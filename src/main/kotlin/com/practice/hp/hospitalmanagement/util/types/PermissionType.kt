package com.practice.hp.hospitalmanagement.util.types

enum class PermissionType(val value: String) {
    PATIENT_READ("patient-read"),
    PATIENT_WRITE("patient-write"),
    APPOINTMENT_READ("appointment-read"),
    APPOINTMENT_WRITE("appointment-write"),
    APPOINTMENT_DELETE("appointment-delete"),
    USER_MANAGEMENT("user-management"),
    REPORT_VIEW_READ("report-view-read"),
}