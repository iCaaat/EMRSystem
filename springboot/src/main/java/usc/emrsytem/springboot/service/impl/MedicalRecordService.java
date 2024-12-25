package usc.emrsytem.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usc.emrsytem.springboot.controller.request.AddMedicalRecordRequest;
import usc.emrsytem.springboot.controller.request.RecordRequest;
import usc.emrsytem.springboot.entity.MedicalRecord;
import usc.emrsytem.springboot.mapper.MedicalRecordMapper;
import usc.emrsytem.springboot.service.IMedicalRecordService;

import java.util.List;

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

    @Override
    public Object listMedicalRecord(RecordRequest request) {
        try {
            PageHelper.startPage(request.getPageNum(), request.getPageSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<MedicalRecord> records = medicalRecordMapper.listPrescription(request);
        return new PageInfo<>(records);
    }

    @Override
    public int updateMedicalRecord(MedicalRecord medicalRecord) {
        return medicalRecordMapper.updateMedicalRecord(medicalRecord);
    }

    @Override
    public int deleteMedicalRecord(Integer recordId) {
        return medicalRecordMapper.deleteMedicalRecord(recordId);
    }
}
