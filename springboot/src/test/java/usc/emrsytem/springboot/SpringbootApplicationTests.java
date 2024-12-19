package usc.emrsytem.springboot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.impl.UserService;

@SpringBootTest
class SpringbootApplicationTests {

	@Autowired
	private UserMapper userMapper;
    @Autowired
    private UserService userService;

//	@Test
//	public void addUser() {
//		//测试添加用户
//		User user = new User();
//		user.setUsername("李四");
//		user.setPassword("123456");
//		user.setRole("patient");
//		user.setEmail("zhangsan@usc.edu");
//		user.setPhoneNumber("1234567891");
//		userMapper.addUser(user);
//	}

//	@Test
//	public void addPatient() {
//		//测试添加患者
//		Patient patient = new Patient();
//		Date dateOfBirth = new Date(1990, 1, 1);
//		patient.setUserId(2003);
//		patient.setAddress("123 Main St");
//		patient.setAllergies("None");
//		patient.setContactPhone("1234567892");
//		patient.setGender("male");
//		patient.setDateOfBirth(dateOfBirth);
//		patient.setEmergencyContact("John Doe");
//		patient.setMedicalHistory("None");
//		userMapper.addPatient(patient);
//	}

//	@Test
//	public void listDoctors() {
//		//测试查询医生
//		List<Doctor> doctors = userMapper.listDoctorUsers();
//		for (Doctor doctor : doctors) {
//			System.out.println(doctor);
//		}
//	}
//	@Test
//	public void listAdmins() {
//		//测试查询医生
//		List<Admin> admins = userMapper.listAdminUsers();
//		for (Admin admin : admins) {
//			System.out.println(admin);
//		}
//	}

	@Test
	public void getById() {
		Integer id = 2026;
		System.out.println(userService.getById(id));
		System.out.println(userMapper.getUserId(id));
	}

}
