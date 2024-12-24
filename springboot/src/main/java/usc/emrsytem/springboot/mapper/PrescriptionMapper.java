package usc.emrsytem.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import usc.emrsytem.springboot.controller.request.PrescriptionRequest;
import usc.emrsytem.springboot.entity.Prescription;

import java.util.List;

@Mapper
public interface PrescriptionMapper {
    // 添加处方
    int addPrescription(Prescription prescription);

    List<Prescription> listPrescription(PrescriptionRequest request);

    int deletePrescription(Integer prescriptionId);
}
