package usc.emrsytem.springboot.controller.request;

import lombok.Data;

@Data
public class PrescriptionRequest extends BaseRequest{
    private Integer doctorId;
    private String medicationName;
    private String remarks;
}
