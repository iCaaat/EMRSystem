package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.request.DoctorPatientRequest;
import usc.emrsytem.springboot.entity.DoctorPatient;

import java.util.List;

public interface IDoctorPatientService {
    // 根据医生ID和病人ID添加关系到表doctor_patient
    int addDoctorPatient(DoctorPatientRequest doctorPatientRequest);

    List<DoctorPatient> getByUserId(Integer userId);

    int deleteByUserId(Integer doctorUserId, Integer patientUserId);

    int deleteByPatientUserId(Integer patientUserId);
}
