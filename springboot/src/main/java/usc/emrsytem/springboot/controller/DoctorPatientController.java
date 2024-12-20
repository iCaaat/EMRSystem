package usc.emrsytem.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import usc.emrsytem.springboot.common.Result;
import usc.emrsytem.springboot.controller.request.DoctorPatientRequest;
import usc.emrsytem.springboot.service.IDoctorPatientService;

@CrossOrigin
@RestController
@RequestMapping("/DoctorPatient")
public class DoctorPatientController {
    @Autowired
    IDoctorPatientService doctorPatientService;

    @PutMapping("/add")
    public Result addDoctorPatient(@RequestBody DoctorPatientRequest doctorPatientRequest){
        doctorPatientService.addDoctorPatient(doctorPatientRequest);
        return Result.success();
    }
}
