package usc.emrsytem.springboot.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import usc.emrsytem.springboot.controller.request.LoginRequest;
import usc.emrsytem.springboot.controller.request.UserPageRequest;
import usc.emrsytem.springboot.entity.Admin;
import usc.emrsytem.springboot.entity.Doctor;
import usc.emrsytem.springboot.entity.Patient;
import usc.emrsytem.springboot.entity.User;

import java.util.List;

// 操作数据库表user的类
@Mapper
public interface UserMapper {
    // 注解方式
/*    @Select("select * from users")
    List<User> listUsers();*/

    // 查询所有用户
    List<User> selectAllUsers();
    // 根据条件查询用户
    List<User> listByCondition(UserPageRequest userPageRequest);
    // 查询所有角色为患者的用户
    List<Patient> listPatientUsers();
    // 查询所有角色为医生的用户
    List<Doctor> listDoctorUsers();
    // 查询所有角色为管理员的用户
    List<Admin> listAdminUsers();

    // 添加用户
    int addUser(User user);
    // 添加病人
    int addPatient(Patient patient);
    // 添加医生
    int addDoctor(Doctor doctor);
    // 添加管理员
    int addAdmin(Admin admin);

//    void deleteByIds(int[] ids);

    // 删除用户
    int deleteUserByRole(User user);
    int deleteFromUser(User user);

    // 修改用户
    int updateUser(User user);
    int updatePatient(Patient patient);
    int updateDoctor(Doctor doctor);
    int updateAdmin(Admin admin);

    // 登录
    User getByPhoneAndPassword(LoginRequest loginRequest);

    @Select("select * from users where username=#{username}")
    public List<User> selectByUsername(String username);


}
