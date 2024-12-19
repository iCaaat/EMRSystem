package usc.emrsytem.springboot.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import usc.emrsytem.springboot.controller.request.DoctorPatientRequest;
import usc.emrsytem.springboot.exception.ServiceException;
import usc.emrsytem.springboot.mapper.DoctorPatientMapper;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.IDoctorPatientService;

import java.util.List;

@Slf4j
@Service
public class DoctorPatientService implements IDoctorPatientService {

    @Autowired
    DoctorPatientMapper doctorPatientMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public int addDoctorPatient(DoctorPatientRequest doctorPatientRequest) {
        Integer doctorUserId = doctorPatientRequest.getDoctorValue();
        List<Integer> patientUserIds = doctorPatientRequest.getPatientValues();
        if (doctorUserId == null || patientUserIds == null || patientUserIds.size() == 0) {
            throw new ServiceException("参数不能为空");
        }
        for (Integer patientUserId : patientUserIds) {
            Integer patientId = userMapper.getPatientById(patientUserId).getPatientId();
            Integer doctorId = userMapper.getDoctorById(doctorUserId).getDoctorId();
            if (patientId == null || doctorId == null) {
                throw new ServiceException("用户不存在");
            }
            if (doctorPatientMapper.getByDocAndPatId(doctorId, patientId) != null) {
                throw new ServiceException("医生和患者关系已存在");
            }
            doctorPatientMapper.addDoctorPatient(doctorId, patientId);

        }

        return 0;
    }
}
