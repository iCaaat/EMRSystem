package usc.emrsytem.springboot.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;
import usc.emrsytem.springboot.entity.MedicalRecord;
import usc.emrsytem.springboot.mapper.MedicalRecordMapper;
import usc.emrsytem.springboot.service.IMedicalRecordService;

@Service
public class MedicalRecordService implements IMedicalRecordService {
    @Autowired
    MedicalRecordMapper medicalRecordMapper;

    @Override
    @Transactional
    public int addMedicalRecord(AddMedicalRecordRequest request) {
        MedicalRecord medicalRecord = new MedicalRecord();
        BeanUtils.copyProperties(request, medicalRecord);

        return medicalRecordMapper.addMedicalRecord(medicalRecord);
    }
}
