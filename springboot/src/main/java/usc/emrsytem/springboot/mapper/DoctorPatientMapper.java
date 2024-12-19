package usc.emrsytem.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import usc.emrsytem.springboot.entity.DoctorPatient;

@Mapper
public interface DoctorPatientMapper {
    // ============添加=================
    // 添加医生与患者对应关系到表doctor_patient
    int addDoctorPatient(@Param("doctorId") Integer doctorId, @Param("patientId") Integer patientId);

    // ============删除=================
    // ==============查询=================
    DoctorPatient getByDocAndPatId(@Param("doctorId") Integer doctorId, @Param("patientId") Integer patientId);
}
