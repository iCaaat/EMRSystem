package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.request.DoctorPatientRequest;

public interface IDoctorPatientService {
    // 根据医生ID和病人ID添加关系到表doctor_patient
    int addDoctorPatient(DoctorPatientRequest doctorPatientRequest);
}
