package usc.emrsytem.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import usc.emrsytem.springboot.controller.request.RecordRequest;
import usc.emrsytem.springboot.entity.MedicalRecord;

import java.util.List;

@Mapper
public interface MedicalRecordMapper {
    // 添加
    int addMedicalRecord(MedicalRecord medicalRecord);

    List<MedicalRecord> listPrescription(RecordRequest request);

    int updateMedicalRecord(MedicalRecord medicalRecord);

    int deleteMedicalRecord(Integer recordId);
}
