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
      total: 0,
      params: {
        username: '',
        phoneNumber: '',
        role: '',
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
          this.patientTableData = res.data
          // console.log(JSON.stringify(this.patientData))
        }
      })
    },
    // 获取医生信息
    loadDoctorsTable() {
      request.get('/user/doctor', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.doctorTableData = res.data
          // console.log(JSON.stringify(this.doctorData))
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
      console.log(this.doctorValue)
      if (this.doctorValue === null || this.doctorValue === '') {
        this.$message.error('请选择医生')
        return
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
        url: '/DoctorPatient',
        method: 'put',
        data: {
          doctorValue: this.doctorValue,
          patientValues: patientValues
        }
      }).then(res => {

        if (res.code === '200') {
          this.$message.success('绑定成功')
        } else {
          this.$message.error('绑定失败')
        }
        this.load()
      })
    }
  }

}
</script>

<template>
  <div>
    <div>
      <el-select
          @change="confirmDoctor = false"
          v-model="doctorValue"
          filterable
          placeholder="请选择医生"
          style="width: 300px; margin: 10px">
        <el-option
            v-for="item in doctorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button @click="confirmDoc">确定医生</el-button>
    </div>
    <div v-if="confirmDoctor">
      <el-select
          v-model="patientValue"
          multiple
          filterable
          placeholder="请选择患者"
          style="width: 300px; margin: 10px">
        <el-option
            v-for="item in patientOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value">
        </el-option>
      </el-select>
      <el-button type="primary" @click="confirmAdd">添加</el-button>
    </div>

  </div>
</template>

<style scoped>

</style>
