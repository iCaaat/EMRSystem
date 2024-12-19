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
import usc.emrsytem.springboot.controller.request.PasswordRequest;
import usc.emrsytem.springboot.controller.request.UserPageRequest;
import usc.emrsytem.springboot.entity.Admin;
import usc.emrsytem.springboot.entity.Doctor;
import usc.emrsytem.springboot.entity.Patient;
import usc.emrsytem.springboot.entity.User;
import usc.emrsytem.springboot.exception.ServiceException;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.IUserService;
import usc.emrsytem.springboot.utils.PasswordUtil;
import usc.emrsytem.springboot.utils.TokenUtils;

import java.sql.Timestamp;
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
    // 根据id查询用户
    @Override
    public User getById(Integer integer) {
        return userMapper.getUserId(integer);
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

        // 加密密码
        String encodedPassword = PasswordUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 存入数据库
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

        // 加密密码
        String encodedPassword = PasswordUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);

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

        // 加密密码
        String encodedPassword = PasswordUtil.encode(user.getPassword());
        user.setPassword(encodedPassword);

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

        // 加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = PasswordUtil.encode(user.getPassword());
            user.setPassword(encodedPassword);
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
        if(Objects.equals(doctor.getGender(), "男") || Objects.equals(doctor.getGender(), "male")) {
            doctor.setGender("male");
        } else {
            doctor.setGender("female");
        }

        // 加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = PasswordUtil.encode(user.getPassword());
            user.setPassword(encodedPassword);
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

        // 加密密码
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = PasswordUtil.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }

        userMapper.updateUser(user);
        userMapper.updateAdmin(admin);
        return 0;
    }

    // 登录
    @Override
    @Transactional
    public LoginDTO login(LoginRequest loginRequest) {
        User user = userMapper.getByPhone(loginRequest);
        if (user == null) {
            throw new ServiceException("用户不存在");
        }

        // 验证密码是否匹配
        boolean isPasswordMatch = PasswordUtil.matches(loginRequest.getPassword(), user.getPassword());
        if (!isPasswordMatch) {
            throw new ServiceException("用户名或密码错误");
        }

        // 修改最后登录时间
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        User tempUser = new User();
        tempUser.setUserId(user.getUserId());
        tempUser.setLastLoginAt(currentTime);
        userMapper.updateLastLoginTime(tempUser);


        LoginDTO loginDTO = new LoginDTO();
        BeanUtils.copyProperties(user, loginDTO);

        String token = TokenUtils.genToken(String.valueOf(user.getUserId()), user.getPassword());
        loginDTO.setToken(token);
        return loginDTO;
    }

    // 修改密码
    @Override
    public void changePassword(PasswordRequest request) {
        // 新密码加密
        request.setNewPassword(PasswordUtil.encode(request.getNewPassword()));

        int count = userMapper.updatePassword(request);
        if (count <= 0) {
            throw new ServiceException("修改密码失败");
        }
    }

}
