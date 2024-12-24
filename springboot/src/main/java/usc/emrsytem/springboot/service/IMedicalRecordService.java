package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;

public interface IMedicalRecordService {
    // 添加
    int addMedicalRecord(AddMedicalRecordRequest request);
}
