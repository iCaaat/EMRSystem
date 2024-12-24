package usc.emrsytem.springboot.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MedicalRecord {
    private Integer recordId;
    private Integer patientId;
    private String diagnosis;
    private String symptoms;
    private String chart;
    private Integer prescriptionId;
    private Integer doctorId;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private String prescription;
    private String remarks;
}
