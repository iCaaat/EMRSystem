package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.request.AddPrescriptionRequest;
import usc.emrsytem.springboot.controller.request.PrescriptionRequest;

public interface IPrescriptionService {
    // 添加处方
    int addPrescription(AddPrescriptionRequest addPrescriptionRequest);

    // 查询处方（包括模糊查询）
    Object listPrescription(PrescriptionRequest request);

    int deletePrescription(Integer prescriptionId);
}
