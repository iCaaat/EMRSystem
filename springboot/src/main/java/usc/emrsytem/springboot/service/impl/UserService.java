package usc.emrsytem.springboot.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import usc.emrsytem.springboot.controller.dto.LoginDTO;
import usc.emrsytem.springboot.controller.request.LoginRequest;
import usc.emrsytem.springboot.controller.request.UserPageRequest;
import usc.emrsytem.springboot.entity.Admin;
import usc.emrsytem.springboot.entity.Doctor;
import usc.emrsytem.springboot.entity.Patient;
import usc.emrsytem.springboot.entity.User;
import usc.emrsytem.springboot.exception.ServiceException;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.IUserService;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class UserService implements IUserService {
    @Autowired
    UserMapper userMapper;

    //查询所有用户
    @Override
    public List<User> userList() {
        return userMapper.selectAllUsers();
    }
    // 分页查询
    @Override
    public Object page(UserPageRequest userPageRequest){
        try {
            PageHelper.startPage(userPageRequest.getPageNum(), userPageRequest.getPageSize());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<User> users = userMapper.listByCondition(userPageRequest);
        return new PageInfo<>(users);
    }
    // 查询所有患者信息
    @Override
    public List<Patient> patientList() {
        return userMapper.listPatientUsers();
    }
    // 查询所有医生信息
    @Override
    public List<Doctor> doctorList() {
        return userMapper.listDoctorUsers();
    }
    // 查询所有管理员信息
    @Override
    public List<Admin> adminList() {
        return userMapper.listAdminUsers();
    }



//    @Override
//    public int addUser(User user) {
//        return userMapper.addUser(user);
//    }

    // 添加病人
    @Override
    @Transactional
    public int addPatient(Patient patient) {
        User user = patient.getUser();
        userMapper.addUser(user);
        patient.setUserId(user.getUserId());
        userMapper.addPatient(patient);
        return 0;
    }
    // 添加医生
    @Override
    @Transactional
    public int addDoctor(Doctor doctor) {
        User user = doctor.getUser();
        userMapper.addUser(user);
        doctor.setUserId(user.getUserId());
        userMapper.addDoctor(doctor);
        return 0;
    }
    // 添加管理员
    @Override
    @Transactional
    public int addAdmin(Admin admin) {
        User user = admin.getUser();
        userMapper.addUser(user);
        admin.setUserId(user.getUserId());
        userMapper.addAdmin(admin);
        return 0;
    }

    // 删除用户
    @Override
    @Transactional
    public int deleteUserById(User user) {
        switch (user.getRole()) {
            case "患者" -> user.setRole("patient");
            case "医生" -> user.setRole("doctor");
            case "管理员" -> user.setRole("admin");
        }

        userMapper.deleteUserByRole(user);
        userMapper.deleteFromUser(user);
        return 0;
    }

    // 更新用户信息
    @Override
    @Transactional
    public int updatePatient(Patient patient) {
        User user = patient.getUser();
        if(Objects.equals(user.getRole(), "患者")) {
            user.setRole("patient");
        }
        if(Objects.equals(patient.getGender(), "男")) {
            patient.setGender("male");
        } else {
            patient.setGender("female");
        }

        userMapper.updateUser(user);
        userMapper.updatePatient(patient);
        return 0;
    }
    // 更新医生信息
    @Override
    @Transactional
    public int updateDoctor(Doctor doctor) {
        User user = doctor.getUser();
        if(Objects.equals(user.getRole(), "医生")) {
            user.setRole("doctor");
        }
        if(Objects.equals(doctor.getGender(), "男")) {
            doctor.setGender("male");
        } else {
            doctor.setGender("female");
        }

        userMapper.updateUser(user);
        userMapper.updateDoctor(doctor);
        return 0;
    }
    // 更新管理员信息
    @Override
    @Transactional
    public int updateAdmin(Admin admin) {
        User user = admin.getUser();
        if(Objects.equals(user.getRole(), "管理员")) {
            user.setRole("admin");
        }
        if(Objects.equals(admin.getAdminLevel(),"超级管理员") || Objects.equals(admin.getAdminLevel(),"super")) {
            admin.setAdminLevel("super");
        } else {
            admin.setAdminLevel("normal");
        }
        userMapper.updateUser(user);
        userMapper.updateAdmin(admin);
        return 0;
    }

    // 登录
    @Override
    public LoginDTO login(LoginRequest loginRequest) {
        User user = userMapper.getByPhoneAndPassword(loginRequest);
        if (user == null) {
            throw new ServiceException("用户名或密码错误");
        }
        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(user, loginDTO);
        return loginDTO;
    }

}
