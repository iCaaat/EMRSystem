package usc.emrsytem.springboot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import usc.emrsytem.springboot.mapper.DoctorPatientMapper;
import usc.emrsytem.springboot.mapper.UserMapper;
import usc.emrsytem.springboot.service.IDoctorPatientService;
import usc.emrsytem.springboot.service.impl.UserService;

@SpringBootTest
class SpringbootApplicationTests {

	@Autowired
	private UserMapper userMapper;
    @Autowired
    private UserService userService;
	@Autowired
	private DoctorPatientMapper doctorPatientMapper;
	@Autowired
	private IDoctorPatientService doctorPatientService;

//	@Test
//	public void deleteByUserId() {
//		DocAndPatRequest docAndPatRequest = new DocAndPatRequest();
//		docAndPatRequest.setDoctorUserId(2030);
//		docAndPatRequest.setPatientUserId(2022);
//		doctorPatientService.deleteByUserId(docAndPatRequest);
//	}
}
