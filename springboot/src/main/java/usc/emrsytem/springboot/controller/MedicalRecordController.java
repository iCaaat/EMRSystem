package usc.emrsytem.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import usc.emrsytem.springboot.common.Result;
import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;
import usc.emrsytem.springboot.service.IMedicalRecordService;

@RestController
@RequestMapping("/medicalRecord")
public class MedicalRecordController {

    @Autowired
    IMedicalRecordService medicalRecordService;

    @PutMapping("/add")
    public Result addMedicalRecord(@RequestBody AddMedicalRecordRequest request) {
        medicalRecordService.addMedicalRecord(request);
        return Result.success();
    }
}
