package usc.emrsytem.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import usc.emrsytem.springboot.entity.MedicalRecord;

@Mapper
public interface MedicalRecordMapper {
    // 添加
    int addMedicalRecord(MedicalRecord medicalRecord);
}
