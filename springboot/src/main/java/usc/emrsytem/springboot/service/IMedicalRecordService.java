package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;
import usc.emrsytem.springboot.controller.request.RecordRequest;
import usc.emrsytem.springboot.entity.MedicalRecord;

public interface IMedicalRecordService {
    // 添加
    int addMedicalRecord(AddMedicalRecordRequest request);

    Object listMedicalRecord(RecordRequest request);

    int updateMedicalRecord(MedicalRecord medicalRecord);

    int deleteMedicalRecord(Integer recordId);
}
