package usc.emrsytem.springboot.entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Prescription {
    private Integer prescriptionId;
    private Integer doctorId;
    private String medicationName;
    private String dosage;
    private String frequency;
    private String duration;
    private String instructions;
    private String remarks;
    private Timestamp createdAt;
}
