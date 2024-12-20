<script>
import request from "@/utils/request";

export default {
  name: "BindPatientAndDoctor",
  data() {
    return {
      confirmDoctor: false,// 确认选中医生
      patientData: [],
      doctorData: [],
      patientTableData: [],
      doctorTableData: [],
      userData: [],
      selectedDoctor: [],
      selectedPatients: [],
      total: 0,
      params: {
        username: '',
        phoneNumber: '',
        email: '',
        pageSize: 13,
        pageNum: 1
      },
      doctorOptions: [{
        value: '医生 Id: xx',
        label: 'Id'
      }],
      doctorValue: '',
      patientOptions: [{
        value: '病人 Id: xx',
        label: 'Id'
      }],
      patientValue: '',
    }
  },
  created() {
    this.load();
    this.loadPatientsTable();
    this.loadDoctorsTable();
  },
  methods: {
    // 读取患者信息
    loadPatients() {
      this.patientData = this.userData.filter(user => user.role === 'patient');
    },
    // 读取医生信息
    loadDoctors() {
      this.doctorData = this.userData.filter(user => user.role === 'doctor');
    },
    load() {
      request.get('/user/page', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.userData = res.data.list
          this.total = res.data.total
          // console.log(JSON.stringify(this.userData))
          this.loadPatients()
          this.loadDoctors()
          this.generateOptions(this.doctorData, 'doctor')
          this.generateOptions(this.patientData, 'patient')
        }
      })
    },
    // 获取患者信息
    loadPatientsTable() {
      request.get('/user/patient', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.patientTableData = res.data.list
          // console.log(JSON.stringify(this.patientTableData))
        } else {
          this.$message.error(res.msg)
        }
      })
    },
    // 获取医生信息
    loadDoctorsTable() {
      request.get('/user/doctor', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorTableData = res.data
          // console.log(JSON.stringify(this.doctorTableData))
        }
      })
    },

    // 处理选项
    generateOptions(data, type) {
      const options = data.map( item => {
        return {
          value: item.userId.toString(),
          label: `${item.username} Id: ${item.userId}` // 拼接username和userId
        }
      })

      if (type === 'doctor') {
        this.doctorOptions = options
      } else if (type === 'patient') {
        this.patientOptions = options
      }
    },
    confirmDoc() {
      if (this.doctorValue === null || this.doctorValue === '') {
        this.$message.error('请选择医生')
        return
      }
      // console.log(this.doctorValue)
      // console.log(JSON.stringify(this.doctorTableData))
      try {
        let num = Number(this.doctorValue)
        for (let i = 0; i < this.doctorTableData.length; i++) {
          if (this.doctorTableData[i].userId === num) {
            this.selectedDoctor.push(this.doctorTableData[i])
            // console.log(JSON.stringify(this.selectedDoctor))
          }
        }
      } catch (e) {
        console.log(e)
      }
      this.confirmDoctor = true;
      this.patientValue = '';
    },
    confirmAdd() {
      // 将 patientOptions 的 value 转换成数字型数组
      console.log(this.patientValue)
      const patientValues = this.patientValue.map(item => parseInt(item, 10));
      console.log(patientValues)
      // 发送医生的userId和病人的id数组给后端
      request({
        url: '/DoctorPatient/add',
        method: 'put',
        data: {
          doctorValue: this.doctorValue,
          patientValues: patientValues
        }
      }).then(res => {

        if (res.code === '200') {
          this.$message.success('绑定成功')
        } else {
          this.$message.error(res.msg)
        }
        this.load()
      })
    },
    selectPatient() {
      const numericArray = this.patientValue.map(item => parseInt(item, 10));
      // console.log(numericArray)
      this.selectedPatients = this.patientTableData.filter(item =>
          numericArray.includes(item.userId)
      );
      const currentDate = new Date(); // 当前日期
      this.selectedPatients = this.selectedPatients.map(patient => {
        if (patient.dateOfBirth) {
          const birthDate = new Date(patient.dateOfBirth); // 解析出生日期
          const age = currentDate.getFullYear() - birthDate.getFullYear();
          const isBirthdayPassed =
              currentDate.getMonth() > birthDate.getMonth() ||
              (currentDate.getMonth() === birthDate.getMonth() &&
                  currentDate.getDate() >= birthDate.getDate());
          // 如果生日还没过，则年龄减一
          patient.age = isBirthdayPassed ? age : age - 1;
        } else {
          patient.age = null; // 如果没有 dateOfBirth，则设置为 null 或其他默认值
        }
        return patient;
      });
    }
  }

}
</script>

<template>
  <div class="page-container">
    <!-- 医生选择 -->
    <div class="section">
      <el-form>
        <el-form-item label="选择医生：">
          <el-select
              @change="confirmDoctor = false"
              v-model="doctorValue"
              filterable
              placeholder="请选择医生"
              class="select-box"
          >
            <el-option
                v-for="item in doctorOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            ></el-option>
          </el-select>
          <el-button type="primary" @click="confirmDoc">确定医生</el-button>
        </el-form-item>
      </el-form>

    </div>

    <!-- 医生表格 -->
    <div class="section">
      <el-table
          v-if="confirmDoctor"
          :data="selectedDoctor"
          class="table"
      >
        <el-table-column prop="userId" label="用户ID"></el-table-column>
        <el-table-column prop="user.username" label="医生姓名"></el-table-column>
        <el-table-column prop="department" label="部门"></el-table-column>
        <el-table-column prop="specialty" label="专业领域"></el-table-column>
        <el-table-column prop="experienceYears" label="从业年限（年）"></el-table-column>
      </el-table>
    </div>

    <!-- 患者选择 -->
    <div v-if="confirmDoctor" class="section">
      <el-form>
        <el-form-item label="选择患者：">
          <el-select
              v-model="patientValue"
              multiple
              filterable
              @change="selectPatient"
              placeholder="请选择患者"
              class="select-box"
          >
            <el-option
                v-for="item in patientOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            ></el-option>
          </el-select>
          <el-button type="primary" @click="confirmAdd">添加患者</el-button>
        </el-form-item>
      </el-form>

    </div>

    <!-- 患者表格 -->
    <div v-if="confirmDoctor" class="section">
      <el-table
          :data="selectedPatients"
          class="table"
      >
        <el-table-column prop="userId" label="用户ID"></el-table-column>
        <el-table-column prop="user.username" label="患者姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="age" label="患者年龄（岁）"></el-table-column>
        <el-table-column prop="medicalHistory" label="患者用药历史"></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<style scoped>
.page-container {
  display: flex;
  flex-direction: column;
  align-items: center; /* 水平居中 */
  background-color: rgba(255, 255, 255, 0.8); /* 半透明背景 */
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.section {
  width: 80%;
  margin-bottom: 20px;
}

.action-group {
  display: flex;
  justify-content: center; /* 水平居中 */
  align-items: center;
  gap: 10px; /* 控件之间的间距 */
}

.select-box {
  width: 300px;
}

.table {
  width: 100%;
  border-radius: 10px;
  background-color: rgba(255, 255, 255, 0.9); /* 表格背景更浅 */
  overflow: hidden;
}
</style>
