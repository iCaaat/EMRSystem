package usc.emrsytem.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usc.emrsytem.springboot.controller.request.AddPrescriptionRequest;
import usc.emrsytem.springboot.controller.request.PrescriptionRequest;
import usc.emrsytem.springboot.entity.Prescription;
import usc.emrsytem.springboot.exception.ServiceException;
import usc.emrsytem.springboot.mapper.PrescriptionMapper;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.IPrescriptionService;

import java.util.List;

@Slf4j
@Service
public class PrescriptionService implements IPrescriptionService {

    @Autowired
    PrescriptionMapper prescriptionMapper;
    @Autowired
    UserMapper userMapper;

    // 添加处方
    @Override
    public int addPrescription(AddPrescriptionRequest addPrescriptionRequest) {
        Integer doctorUserId = addPrescriptionRequest.getDoctorUserId();
        if(userMapper.getDoctorById(doctorUserId) == null){
            throw new ServiceException("医生不存在");
        }
        Integer doctorId = userMapper.getDoctorById(doctorUserId).getDoctorId();
        Prescription prescription = new Prescription();
        prescription.setDoctorId(doctorId);
        prescription.setMedicationName(addPrescriptionRequest.getMedicationName());
        prescription.setDosage(addPrescriptionRequest.getDosage());
        prescription.setFrequency(addPrescriptionRequest.getFrequency());
        prescription.setDuration(addPrescriptionRequest.getDuration());
        prescription.setInstructions(addPrescriptionRequest.getInstructions());
        prescription.setRemarks(addPrescriptionRequest.getRemarks());
        prescriptionMapper.addPrescription(prescription);
        return 0;
    }

    @Override
    public Object listPrescription(PrescriptionRequest request) {
        try {
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<Prescription> prescriptions = prescriptionMapper.listPrescription(request);
        return new PageInfo<>(prescriptions);
    }

    // 删除
    @Override
    public int deletePrescription(Integer prescriptionId) {
        return prescriptionMapper.deletePrescription(prescriptionId);
    }
}
