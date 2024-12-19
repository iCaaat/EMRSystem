package usc.emrsytem.springboot.service;

import usc.emrsytem.springboot.controller.dto.LoginDTO;
import usc.emrsytem.springboot.controller.request.LoginRequest;
import usc.emrsytem.springboot.controller.request.UserPageRequest;
import usc.emrsytem.springboot.entity.Admin;
import usc.emrsytem.springboot.entity.Doctor;
import usc.emrsytem.springboot.entity.Patient;
import usc.emrsytem.springboot.entity.User;

import java.util.List;


public interface IUserService {
    // 查询
    List<User> userList();// 查询所有用户
    Object page(UserPageRequest userPageRequest);// 分页条件查询
    List<Patient> patientList();// 查询所有患者
    List<Doctor> doctorList();// 查询所有医生
    List<Admin> adminList();// 查询所有管理员

    // 添加
    int addPatient(Patient patient);
    int addDoctor(Doctor doctor);
    int addAdmin(Admin admin);

    // 删除
    int deleteUserById(User user);

    // 修改
    int updatePatient(Patient patient);
    int updateDoctor(Doctor doctor);
    int updateAdmin(Admin admin);

    LoginDTO login(LoginRequest loginRequest);

    User getById(Integer integer);
}
