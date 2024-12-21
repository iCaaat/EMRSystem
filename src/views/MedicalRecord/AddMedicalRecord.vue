<script>
import request from "@/utils/request";

export default {
  name: 'AddMedicalRecord',
  data() {
    return {
      patientOptions: [{
        value: '病人 Id: xx',
        label: 'Id'
      }],
      patientValue: '',
      selectedPatient: [],
      total: 0,
      params: {
        username: '',
        phoneNumber: '',
        email: '',
        pageSize: 13,
        pageNum: 1
      },
      patientData: [],
      patientTableData: [],
      form: {

      },
      isSelectPatient: false,
    }
  },
  created() {
    this.load();
    this.loadPatientsTable();
  },
  methods: {
    // 读取患者信息
    loadPatients() {
      this.patientData = this.userData.filter(user => user.role === 'patient');
    },
    load() {
      request.get('/user/page', {params: this.params}).then(res => {
        // console.log("res:" + JSON.stringify(res, null, 2))
        if (res.code === '200') {
          this.userData = res.data.list
          this.total = res.data.total
          // console.log(JSON.stringify(this.userData))
          this.loadPatients()
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
      this.selectedPatient = [];
      let num = Number(this.patientValue)
      try {
        for(let i = 0; i < this.patientTableData.length; i++) {
          if(this.patientTableData[i].userId === num) {
            this.selectedPatient.push(this.patientTableData[i])
          }
        }
        // console.log(this.selectedPatient)
        this.selectedPatient.forEach(item => {
          // 处理患者性别，female->女，male->男
          if(item.gender === 'female') {
            item.gender = '女'
          } else if(item.gender === 'male') {
            item.gender = '男'
          }
          // 处理患者年龄，根据dateOfBirth计算年龄
          let dateOfBirth = new Date(item.dateOfBirth)
          let now = new Date()
          item.age = now.getFullYear() - dateOfBirth.getFullYear()
          this.isSelectPatient = true;
        })
      } catch (e) {
        this.$message.error(e)
      }
    }
  }
}
</script>

<template>
  <div class="container">
    <h2 class="tittle">新增病历</h2>
    <!-- 患者选择 -->
    <div class="section">
      <el-form class="form">
        <el-form-item label="选择患者：" class="form-item">
          <el-select
              v-model="patientValue"
              filterable
              @change="selectPatient"
              placeholder="请选择患者"
              class="select-box">
            <el-option
                v-for="item in patientOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            ></el-option>
          </el-select>
        </el-form-item>
      </el-form>
    </div>
    <!-- 表格展示选择的患者信息以便确认 -->
    <div class="section">
      <el-table
          :data="selectedPatient"
          class="table"
      >
        <el-table-column prop="userId" label="用户ID"></el-table-column>
        <el-table-column prop="user.username" label="患者姓名"></el-table-column>
        <el-table-column prop="gender" label="性别"></el-table-column>
        <el-table-column prop="age" label="患者年龄（岁）"></el-table-column>
      </el-table>
    </div>
    <!--填写新增病例表单-->
    <el-form ref="form" v-if="isSelectPatient" :model="form" label-width="80px" class="form">
      <el-form-item label="选择医生" class="form-item">
        <el-select v-model="form.region" placeholder="请选择医生">
          <el-option label="区域一" value="shanghai"></el-option>
          <el-option label="区域二" value="beijing"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="诊断" class="form-item">
        <el-input type="textarea" v-model="form.desc"></el-input>
      </el-form-item>
      <el-form-item label="症状" class="form-item">
        <el-input type="textarea" v-model="form.desc"></el-input>
      </el-form-item>
      <el-form-item label="处方" class="form-item">
        <el-input type="textarea" v-model="form.desc"></el-input>
      </el-form-item>
      <el-form-item class="form-item">
        <el-button type="primary" @click="onSubmit">立即创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped>
/* 容器样式 */
.container {
  width: 90%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* 标题样式 */
.title {
  text-align: center;
  font-size: 2rem;
  color: #409eff;
  margin-bottom: 20px;
  font-weight: bold;
}

/* 每个部分的样式 */
.section {
  margin-bottom: 30px;
}

/* 表单项样式 */
.form-item {
  margin-bottom: 20px;
}

/* 输入框样式 */
.el-input,
.el-select {
  width: 100%;
  border-radius: 4px;
  box-sizing: border-box;
}

/* 按钮样式 */
.el-button {
  margin-right: 10px;
  font-size: 1rem;
}

/* 表格样式 */
.table {
  width: 100%;
  margin-top: 20px;
  border-radius: 8px;
}

.el-table {
  background-color: #fff;
  border-radius: 8px;
}

.el-table-column {
  text-align: center;
}

/* 选择框样式 */
.select-box {
  width: 100%;
}
</style>
