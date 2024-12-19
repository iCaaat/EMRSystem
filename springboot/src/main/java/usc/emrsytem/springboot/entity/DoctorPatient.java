package usc.emrsytem.springboot.entity;

import lombok.Data;

@Data
public class DoctorPatient {
    private Integer doctorId;
    private Integer patientId;
    private Integer DoctorPatientId;
}
