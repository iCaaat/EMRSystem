package usc.emrsytem.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usc.emrsytem.springboot.common.Result;
import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;
import usc.emrsytem.springboot.controller.request.RecordRequest;
import usc.emrsytem.springboot.entity.MedicalRecord;
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

    @GetMapping("/list")
    public Result listMedicalRecord(RecordRequest params) {
        return Result.success(medicalRecordService.listMedicalRecord(params));
    }

    @PostMapping("/update")
    public Result updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        medicalRecordService.updateMedicalRecord(medicalRecord);
        return Result.success();
    }

    @DeleteMapping("/delete/{recordId}")
    public Result deleteMedicalRecord(@PathVariable Integer recordId) {
        medicalRecordService.deleteMedicalRecord(recordId);
        return Result.success();
    }
}
